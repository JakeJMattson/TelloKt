import me.jakejmattson.tellokt.*

private fun main() {
    val tello = Tello()

    with(tello) {
        //Attempt to connect to Tello
        connect()
        if (!isConnected) return

        //Begin demo
        streamOn()
        takeOff()

        //Demonstrate movement commands
        val moveAmount = 20
        moveLeft(moveAmount)
        moveRight(moveAmount)
        moveForward(moveAmount)
        moveBack(moveAmount)
        moveUp(moveAmount)
        moveDown(moveAmount)

        //Demonstrate rotation commands
        val rotationAmount = 180
        rotateClockwise(rotationAmount)
        rotateCounterClockwise(rotationAmount)

        //Demonstrate flip commands
        moveDown(60)
        flip(FlipDirection.FORWARD)
        flip(FlipDirection.BACKWARD)

        //Demonstrate info commands
        read(Info.SPEED)
        read(Info.BATTERY)
        read(Info.TIME)
        read(Info.HEIGHT)
        read(Info.TEMP)
        read(Info.ATTITUDE)
        read(Info.BARO)
        read(Info.ACCELERATION)
        read(Info.TOF)
        read(Info.WIFI)

        //Conclude demo
        streamOff()
        land() //Use emergency() to stop immediately instead of landing
        disconnect()
    }
}