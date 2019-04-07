import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class TelloKt {
    companion object {
        val movementRange = 20..500
        val rotationrange = 1..3600
    }

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

    @Throws(IOException::class)
    fun flip(direction: FlipDirection) = sendCommand("flip ${direction.direction}")

    /**
     * Limit: 1-100 cm/s
     */
    @Throws(IOException::class)
    fun setSpeed(speed: Int) = sendCommand("speed $speed")

    private fun Int.toMetric() = if (!isImperial) this else Math.round((this * 2.54).toFloat())

    private fun move(command: String, distance: Int) = validateAndSend(command, distance.toMetric(), movementRange)
    private fun rotate(command: String, distance: Int) = validateAndSend(command, distance, rotationrange)

    private fun validateAndSend(command: String, distance: Int, range: IntRange) =
        if (distance in range) sendCommand("$command $distance") else "Command argument not in range!"

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

enum class FlipDirection(val direction: String) {
    LEFT("l"),
    RIGHT("r"),
    FORWARD("f"),
    BACKWARD("b"),

    BACK_LEFT("bl"),
    BACK_RIGHT("rb"),
    FRONT_LEFT("fl"),
    FRONT_RIGHT("fr")
}