import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class TelloKt {
    private lateinit var socket: DatagramSocket
    var isImperial: Boolean = false

    val isConnected: Boolean
        get() = socket.isConnected

    @Throws(Exception::class)
    fun connect(strIP: String = "192.168.10.1", port: Int = 8889) {
        socket = DatagramSocket(port)
        socket.connect(InetAddress.getByName(strIP), port)
        sendCommand("command")
    }
    fun disconnect() = socket.close()

    @Throws(IOException::class)
    fun takeOff() = sendCommand("takeoff")

    @Throws(IOException::class)
    fun land() = sendCommand("land")

    @Throws(IOException::class)
    fun moveLeft(x: Int) = move("left", x)

    @Throws(IOException::class)
    fun moveRight(x: Int) = move("right", x)

    @Throws(IOException::class)
    fun moveForward(y: Int) = move("forward", y)

    @Throws(IOException::class)
    fun moveBack(y: Int) = move("back", y)

    @Throws(IOException::class)
    fun moveUp(z: Int) = move("up", z)

    @Throws(IOException::class)
    fun moveDown(z: Int) = move("down", z)

    @Throws(IOException::class)
    fun rotateClockwise(x: Int) = rotate("cw", x)

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
        val sendPacket = DatagramPacket(sendData, sendData.size, socket.inetAddress, socket.port)
        socket.send(sendPacket)

        val receiveData = ByteArray(1024)
        val receivePacket = DatagramPacket(receiveData, receiveData.size)
        socket.receive(receivePacket)
        val ret = String(receivePacket.data)

        println("$strCommand: $ret")
        return ret
    }
}