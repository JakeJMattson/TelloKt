package io.github.jakejmattson.tellokt

import java.io.IOException
import java.net.*

class TelloKt {
    companion object {
        val distanceRange = 20..500
        val rotationRange = 1..3600
        val speedRange = 1..100
        val rcRange = -100..100
        var invalidArgString = "Command argument not in range!"
    }

    private lateinit var socket: DatagramSocket
    var isImperial: Boolean = false

    val isConnected: Boolean
        get() = socket.isConnected

    @Throws(IOException::class)
    fun read(info: Info) = sendCommand(info.type)

    @Throws(IOException::class)
    fun connect(ip: String = "192.168.10.1", port: Int = 8889) {
        socket = DatagramSocket(port)
        socket.connect(InetAddress.getByName(ip), port)
        sendCommand("command")
    }

    fun disconnect() = socket.close()

    @Throws(IOException::class)
    fun takeOff() = sendCommand("takeoff")

    @Throws(IOException::class)
    fun land() = sendCommand("land")

    @Throws(IOException::class)
    fun emergency() = sendCommand("emergency")

    @Throws(IOException::class)
    fun streamOn() = sendCommand("streamon")

    @Throws(IOException::class)
    fun streamOff() = sendCommand("streamoff")

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
    fun rotateClockwise(degrees: Int) = rotate("cw", degrees)

    @Throws(IOException::class)
    fun rotateCounterClockwise(degrees: Int) = rotate("ccw", degrees)

    @Throws(IOException::class)
    fun flip(direction: FlipDirection) = sendCommand("flip ${direction.direction}")

    fun go(x: Int, y: Int, z: Int, speed: Int) =
        when {
            !x.isValidDistance() -> invalidArgString
            !y.isValidDistance() -> invalidArgString
            !z.isValidDistance() -> invalidArgString
            !speed.isValidSpeed() -> invalidArgString
            else -> sendCommand("go $x $y $z $speed")
        }

    fun curve(x1: Int, x2: Int, y1: Int, y2: Int, z1: Int, z2: Int, speed: Int) =
        when {
            !x1.isValidDistance() -> invalidArgString
            !x2.isValidDistance() -> invalidArgString
            !y1.isValidDistance() -> invalidArgString
            !y2.isValidDistance() -> invalidArgString
            !z1.isValidDistance() -> invalidArgString
            !z2.isValidDistance() -> invalidArgString
            speed !in 10..60 -> invalidArgString
            else -> sendCommand("curve $x1 $y1 $z1 $x2 $y2 $z2 $speed")
        }

    @Throws(IOException::class)
    fun setSpeed(speed: Int) =
        if (speed.isValidSpeed())
            sendCommand("speed $speed")
        else
            invalidArgString

    @Throws(IOException::class)
    fun setWifiSsidPass(ssid: String, pass: String) = sendCommand("wifi $ssid $pass")

    @Throws(IOException::class)
    fun sendRc(leftRight: Int, forwardBack: Int, upDown: Int, yaw: Int) =
        when {
            !leftRight.isValidRc() -> invalidArgString
            !forwardBack.isValidRc() -> invalidArgString
            !upDown.isValidRc() -> invalidArgString
            else -> sendCommand("rc $leftRight $forwardBack $upDown $yaw")
        }

    @Throws(IOException::class)
    private fun move(command: String, distance: Int) =
        if (distance.toMetric().isValidDistance())
            sendCommand("$command $distance")
        else
            invalidArgString

    @Throws(IOException::class)
    private fun rotate(command: String, degrees: Int) =
        if (degrees.isValidRotation())
            sendCommand("$command $degrees")
        else
            invalidArgString

    @Throws(IOException::class)
    fun sendCommand(command: String): String {
        if (command.isEmpty()) return "Empty command."
        if (!socket.isConnected) return "Socket Disconnected."

        val sendData = command.toByteArray()
        val sendPacket = DatagramPacket(sendData, sendData.size, socket.inetAddress, socket.port)
        socket.send(sendPacket)

        val receiveData = ByteArray(1024)
        val receivePacket = DatagramPacket(receiveData, receiveData.size)
        socket.receive(receivePacket)

        val response = String(receivePacket.data)
        println("$command: $response")
        return response
    }

    private fun Int.isValidDistance() = this in distanceRange
    private fun Int.isValidRotation() = this in rotationRange
    private fun Int.isValidSpeed() = this in speedRange
    private fun Int.isValidRc() = this in rcRange
    private fun Int.toMetric() = if (!isImperial) this else Math.round((this * 2.54).toFloat())
}

enum class Info(val type: String) {
    SPEED("speed?"),
    BATTERY("battery?"),
    TIME("time?"),
    HEIGHT("height?"),
    TEMP("temp?"),
    ATTITUDE("attitude?"),
    BARO("baro?"),
    ACCELERATION("acceleration?"),
    TOF("tof?"),
    WIFI("wifi?")
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