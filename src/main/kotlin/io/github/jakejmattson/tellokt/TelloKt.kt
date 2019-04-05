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
    fun disconnect() = socket.close()

    @Throws(IOException::class)
    fun takeOff() = sendCommand("takeoff")

    @Throws(IOException::class)
    fun land() = sendCommand("land")

    /**
     * Range: 20-500 cm
     */
    @Throws(IOException::class)
    fun moveUp(z: Int) = move("up", z)

    /**
     * Range: 20-500 cm
     */
    @Throws(IOException::class)
    fun moveDown(z: Int) = move("down", z)

    /**
     * Range: 20-500 cm
     */
    @Throws(IOException::class)
    fun moveLeft(x: Int) = move("left", x)

    /**
     * Range: 20-500 cm
     */
    @Throws(IOException::class)
    fun moveRight(x: Int) = move("right", x)

    /**
     * Range: 20-500 cm
     */
    @Throws(IOException::class)
    fun moveForward(y: Int) = move("forward", y)

    /**
     * Range: 20-500 cm
     */
    @Throws(IOException::class)
    fun moveBack(y: Int) = move("back", y)

    /**
     * Rotate clockwise. Limit: 1-3600°
     */
    @Throws(IOException::class)
    fun rotateClockwise(x: Int) = rotate("cw", x)

    /**
     * Rotate counter-clockwise. Limit: 1-3600°
     */
    @Throws(IOException::class)
    fun rotateCounterClockwise(x: Int) = rotate("ccw", x)

    /**
     * Flip x l = (left) r = (right) f = (forward) b = (back) bl = (back/left) rb = (back/right) fl = (front/left) fr = (front/right)
     */
    @Throws(IOException::class)
    fun flip(direction: String) = sendCommand("flip $direction")

    /**
     * Limit: 1-100 cm/s
     */
    @Throws(IOException::class)
    fun setSpeed(speed: Int) = sendCommand("speed $speed")

    private fun getDistance(distance: Int) = if (!isImperial) distance else Math.round((distance * 2.54).toFloat())

    private fun move(command: String, distance: Int) =
        if (distance in 20..500)
            sendCommand("$command ${getDistance(distance)}")
        else
            "Command argument not in range!"

    private fun rotate(command: String, distance: Int) =
        if (distance in 1..3600)
            sendCommand("$command $distance")
        else
            "Command argument not in range!"

    @Throws(IOException::class)
    fun sendCommand(strCommand: String): String {
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
}