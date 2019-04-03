import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class TelloKt {
    private lateinit var ip: InetAddress
    private lateinit var socket: DatagramSocket
    var isImperial: Boolean = false

    val isConnected: Boolean
        get() = socket.isConnected

    @Throws(Exception::class)
    fun connect() {
        this.connect("192.168.10.1", 8889)
    }

    @Throws(Exception::class)
    fun connect(strIP: String, port: Int) {
        ip = InetAddress.getByName(strIP)
        socket = DatagramSocket(port)
        socket.connect(ip, port)
        sendCommand("command")
    }

    @Throws(IOException::class)
    fun takeOff(): Boolean {
        return isOK(sendCommand("takeoff"))
    }

    @Throws(IOException::class)
    fun land(): Boolean {
        return isOK(sendCommand("land"))
    }

    /**
     * Range: 20-500 cm
     */
    @Throws(IOException::class)
    fun up(z: Int): Boolean {
        return isOK(sendCommand("up " + getDistance(z)))
    }

    /**
     * Range: 20-500 cm
     */
    @Throws(IOException::class)
    fun down(z: Int): Boolean {
        return isOK(sendCommand("down " + getDistance(z)))
    }

    /**
     * Range: 20-500 cm
     */
    @Throws(IOException::class)
    fun left(x: Int): Boolean {
        return isOK(sendCommand("left " + getDistance(x)))
    }

    /**
     * Range: 20-500 cm
     */
    @Throws(IOException::class)
    fun right(x: Int): Boolean {
        return isOK(sendCommand("right " + getDistance(x)))
    }

    /**
     * Range: 20-500 cm
     */
    @Throws(IOException::class)
    fun forward(y: Int): Boolean {
        return isOK(sendCommand("forward " + getDistance(y)))
    }

    /**
     * Range: 20-500 cm
     */
    @Throws(IOException::class)
    fun back(y: Int): Boolean {
        return isOK(sendCommand("back " + getDistance(y)))
    }

    /**
     * Rotate clockwise. Limit: 1-3600°
     */
    @Throws(IOException::class)
    fun cw(x: Int): Boolean {
        return isOK(sendCommand("cw $x"))
    }

    /**
     * Rotate counter-clockwise. Limit: 1-3600°
     */
    @Throws(IOException::class)
    fun ccw(x: Int): Boolean {
        return isOK(sendCommand("ccw $x"))
    }

    /**
     * Flip x l = (left) r = (right) f = (forward) b = (back) bl = (back/left) rb = (back/right) fl = (front/left) fr = (front/right)
     */
    @Throws(IOException::class)
    fun flip(direction: String): Boolean {
        return isOK(sendCommand("flip $direction"))
    }

    /**
     * Limit: 1-100 cm/s
     */
    @Throws(IOException::class)
    fun setSpeed(speed: Int): Boolean {
        return isOK(sendCommand("speed $speed"))
    }

    private fun getDistance(distance: Int): Int {
        return if (!isImperial) distance else Math.round((distance.toFloat() * 2.54).toFloat())
    }

    private fun isOK(strResult: String?): Boolean {
        return strResult == "OK"
    }

    @Throws(IOException::class)
    private fun sendCommand(strCommand: String): String {
        if (strCommand.isEmpty())
            return "empty command"

        if (!socket.isConnected)
            return "disconnected"

        val sendData = strCommand.toByteArray()
        val sendPacket = DatagramPacket(sendData, sendData.size, ip, socket.port)
        socket.send(sendPacket)

        val receiveData = ByteArray(1024)
        val receivePacket = DatagramPacket(receiveData, receiveData.size)
        socket.receive(receivePacket)
        val ret = String(receivePacket.data)

        println("$strCommand: $ret")
        return ret
    }

    fun close() {
        socket.close()
    }
}