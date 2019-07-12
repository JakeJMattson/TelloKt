package io.github.jakejmattson.tellokt.demo;

import io.github.jakejmattson.tellokt.*;
import java.io.*;

public class Main {
    public static void main(String[] args) {
        Tello tello = new Tello();

        try {
            //Attempt to connect to Tello
            tello.connect("192.168.10.1", 8889);
            if (!tello.isConnected()) return;

            //Begin demo
            tello.streamOn();
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
            tello.read(Info.SPEED);
            tello.read(Info.BATTERY);
            tello.read(Info.TIME);
            tello.read(Info.HEIGHT);
            tello.read(Info.TEMP);
            tello.read(Info.ATTITUDE);
            tello.read(Info.BARO);
            tello.read(Info.ACCELERATION);
            tello.read(Info.TOF);
            tello.read(Info.WIFI);

            //Conclude demo
            tello.streamOff();
            tello.land(); //Use emergency() to stop immediately instead of landing
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            tello.disconnect();
        }
    }
}
