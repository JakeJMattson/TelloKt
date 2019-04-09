package io.github.jakejmattson.tellokt.demo;

import io.github.jakejmattson.tellokt.*;
import java.io.*;

public class Main {
    public static void main(String[] args) {
        TelloKt tello = new TelloKt();

        try {
            //Attempt to connect to Tello
            tello.connect("192.168.10.1", 8889);
            if (!tello.isConnected()) return;

            //Begin demo
            tello.takeOff();

            //Demonstrate movement commands
            int moveAmount = 20;
            tello.moveLeft(moveAmount);
            tello.moveRight(moveAmount);
            tello.moveForward(moveAmount);
            tello.moveBack(moveAmount);
            tello.moveUp(moveAmount);
            tello.moveDown(moveAmount);

            //Demonstrate rotation commands
            int rotationAmount = 180;
            tello.rotateClockwise(rotationAmount);
            tello.rotateCounterClockwise(rotationAmount);

            //Demonstrate flip commands
            tello.moveDown(60);
            tello.flip(FlipDirection.FORWARD);
            tello.flip(FlipDirection.BACKWARD);

            //Demonstrate info commands
            tello.getBattery();
            tello.getSpeed();
            tello.getTime();

            //Conclude demo
            tello.land();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            tello.disconnect();
        }
    }
}
