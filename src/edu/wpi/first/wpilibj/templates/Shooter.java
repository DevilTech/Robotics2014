/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import Competition.Wiring;
import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.DigitalInput;

public class Shooter {

    Counter upCounter;
    Counter downCounter;
    Counter tensionedCounter;
    Counter deTensionedCounter;
    DigitalInput up;
    DigitalInput down;
    DigitalInput tensioned;
    DigitalInput deTensioned;
    int state = 0;
    Piston middlePiston;
    Piston shoot;
    Piston outerPistons;
    AnalogChannel optical;
    AnalogChannel ball;
    double distance;
    int counter = 0;
    boolean readyToShoot = false;
    boolean hasShot = false;
    boolean isDown = false;

    public Shooter() {
        middlePiston = new Piston(Wiring.SOLENOID_SHOOTER_PRETENSION_OUT, Wiring.SOLENOID_SHOOTER_PRETENSION_IN);
        shoot = new Piston(Wiring.SOLENOID_SHOOTER_SHOOT_OUT, Wiring.SOLENOID_SHOOTER_SHOOT_IN);
        outerPistons = new Piston(Wiring.SOLENOID_SHOOTER_TENSION_OUT, Wiring.SOLENOID_SHOOTER_TENSION_IN);
        tensioned = new DigitalInput(Wiring.LIMIT_SHOOTER_TENSIONED);
        up = new DigitalInput(Wiring.LIMIT_SHOOTER_UP);
        down = new DigitalInput(Wiring.LIMIT_SHOOTER_DOWN);
        deTensioned = new DigitalInput(Wiring.LIMIT_SHOOTER_DETENSIONED);
        optical = new AnalogChannel(Wiring.OPTICAL_SHOOTER_SENSOR);
        ball = new AnalogChannel(2);
        distance = optical.getVoltage();
        outerPistons.retract();
        shoot.retract();
        middlePiston.retract();

    }

    public void initalize() {
        outerPistons.retract();
        middlePiston.retract();
        shoot.retract();
    }

    public void cock() {
        if (!readyToShoot) {
            if (optical.getVoltage() <= 2 && !isDown) {
                middlePiston.extend();
            } else if (counter < 50) {
                counter++;
            } else {
                isDown = true;
                if (tensioned.get()) {
                    middlePiston.retract();
                    outerPistons.extend();
                    System.out.println("tensionin");
                } else {
                    readyToShoot = true;
                    hasShot = false;
                    System.out.println("ready");
                }
            }
        }
    }

    public void shoot() {

        if (readyToShoot && !hasShot) {
            shoot.extend();
            System.out.println("shoot");
        }
        if (optical.getVoltage() < 1.5) {
            hasShot = true;
            counter = 0;
            readyToShoot = false;
            isDown = false;
            shoot.retract();
            outerPistons.retract();
            System.out.println("has shot");
        }
    }

    public void shootThings(boolean joy) {
        if (joy) {
            shoot();
        } else {
            cock();
        }
    }

    public void sm(boolean fire) {
        System.out.println(state);
        switch (state) {
            case 0:
                if (optical.getVoltage() >= 2.2) {
                    middlePiston.retract();
                    outerPistons.extend();
                    state = 1;
                }
                break;

            case 1:
                if (!tensioned.get()) {
                    state = 2;
                }
                break;
            case 2:
                if (optical.getVoltage() >= 2.0) {
                    state = 3;
                }
                break;
            case 3:
                System.out.println(ball.getVoltage());
                if (fire) {
                    shoot.extend();
                    state = 4;
                }
                break;
            case 4:
                if (optical.getVoltage() <= 1) {
                    state = 5;
                    shoot.retract();
                    System.out.println(optical.getVoltage());
                }
                break;
            case 5:
                outerPistons.retract();
                middlePiston.extend();
                state = 6;
                break;
            case 6:
                if (!deTensioned.get()) {
                    state = 7;
                    counter = 0;
                }
                break;
            case 7:
                if (optical.getVoltage() >= 2.0) {
                    state = 0;
                }
        }
    }

    public void makesafe() {
        outerPistons.retract();
        middlePiston.retract();
        shoot.retract();
        System.out.println("Making Shooter Safe");
    }

    public void popShot(boolean but) {
        if (but) {
            shoot.extend();
        } else {
            shoot.retract();
        }
    }
}
