import io.github.jakejmattson.tellokt.*

private fun main() {
    val tello = TelloKt()

    with(tello) {
        //Attempt to connect to Tello
        connect()
        if (!isConnected) return

        //Begin demo
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
        battery
        speed
        time

        //Conclude demo
        land()
        disconnect()
    }
}