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
    boolean shooting = false;
    boolean popTrig = false;


    public Shooter() {
        middlePiston = new Piston(Wiring.SOLENOID_SHOOTER_PRETENSION_OUT, Wiring.SOLENOID_SHOOTER_PRETENSION_IN);
        shoot = new Piston(Wiring.SOLENOID_SHOOTER_SHOOT_OUT, Wiring.SOLENOID_SHOOTER_SHOOT_IN);
        outerPistons = new Piston(Wiring.SOLENOID_SHOOTER_TENSION_OUT, Wiring.SOLENOID_SHOOTER_TENSION_IN);
        //tensioned = new DigitalInput(Wiring.LIMIT_SHOOTER_TENSIONED);
        up = new DigitalInput(Wiring.LIMIT_SHOOTER_UP);
        down = new DigitalInput(Wiring.LIMIT_SHOOTER_DOWN);
        deTensioned = new DigitalInput(Wiring.LIMIT_SHOOTER_DETENSIONED);
        optical = new AnalogChannel(Wiring.OPTICAL_SHOOTER_SENSOR);
        ball = new AnalogChannel(Wiring.OPTICAL_SHOOTER_BALL_SENSOR);
        distance = optical.getVoltage();
        tensionedCounter = new Counter(Wiring.LIMIT_SHOOTER_TENSIONED);
        tensionedCounter.start();
        tensionedCounter.reset();
    }

    public void initalize() {
        outerPistons.retract();
        middlePiston.retract();
        shoot.retract();
        readyToShoot = false;
        isDown = false;
        tensionedCounter.reset();
        System.out.println("init");
        
    }

    public void cock() {
        if (!readyToShoot) {
            if (optical.getVoltage() <= 2 && !isDown) {
                middlePiston.extend();
                outerPistons.retract();
                shoot.retract();
                tensionedCounter.reset();
                System.out.println("readying");
                isDown = false;
            } else if (counter < 50) {
                counter++;
            } else {
                isDown = true;
                if (tensionedCounter.get() == 0) {
                    middlePiston.retract();
                    outerPistons.extend();
                    System.out.println("tensionin");
                } else {
                    middlePiston.retract();
                    readyToShoot = true;
                    hasShot = false;
                    System.out.println("ready");
                }
            }
        }
    }

    public void shoot() {
        // add ball sensor

        if (readyToShoot && !hasShot && (ball.getVoltage() > Wiring.C_HAS_BALL)) {
            shoot.extend();
            System.out.println("shoot");
            shooting = true;
        }
        if (optical.getVoltage() < 1.5) {
            hasShot = true;
            counter = 0;
            readyToShoot = false;
            isDown = false;
            shooting = false;
            shoot.retract();
            System.out.println("has shot");
        }
    }

    public void shootThings(boolean joy) {
        if (joy) {
            shoot();
        } else if(shooting){
            shoot();
        }else{
            cock();
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
            if(!deTensioned.get() && RobotTemplate.gathererReversed){
                shoot.extend();
                readyToShoot = false;
                popTrig = true;
            }else if(deTensioned.get()){
                outerPistons.retract();
                System.out.println("pop");
                readyToShoot = false;
            }
        } else if(popTrig){
            isDown = false;
            readyToShoot = false;
            popTrig = false;
        }

    }
}
