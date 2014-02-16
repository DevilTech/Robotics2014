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
    Piston preTension;
    Piston shoot;
    Piston tension;
    AnalogChannel optical;
    double distance;
    int counter = 0;

    public Shooter() {
        preTension = new Piston(Wiring.SOLENOID_SHOOTER_PRETENSION_OUT, Wiring.SOLENOID_SHOOTER_PRETENSION_IN);
        shoot = new Piston(Wiring.SOLENOID_SHOOTER_SHOOT_OUT, Wiring.SOLENOID_SHOOTER_SHOOT_IN);
        tension = new Piston(Wiring.SOLENOID_SHOOTER_TENSION_OUT, Wiring.SOLENOID_SHOOTER_TENSION_IN);
        tensioned = new DigitalInput(Wiring.LIMIT_SHOOTER_TENSIONED);
        up = new DigitalInput(Wiring.LIMIT_SHOOTER_UP);
        down = new DigitalInput(Wiring.LIMIT_SHOOTER_DOWN);
        deTensioned = new DigitalInput(Wiring.LIMIT_SHOOTER_DETENSIONED);
        optical = new AnalogChannel(Wiring.OPTICAL_SHOOTER_SENSOR);
        distance = optical.getVoltage();
        tension.retract();
        shoot.retract();
        preTension.extend();
    }
    


    public void keepCocked() {
        switch (state) {

            case 0:
                System.out.println("checking position...");
                if (!isDown() && !deTensioned.get()) {
                    state = 1;
                    //switchToCounters();
                }
                if (isDown() && !deTensioned.get()) {
                    state = 2;
                    //switchToCounters();
                }
                if (isDown() && !tensioned.get()) {
                    state = 3;
                    //switchToCounters();
                }
                if (!isDown() && !tensioned.get()) {
                    state = 4;
                    //switchToCounters();
                }
                break;
            case 1:
                counter = 0;
                tension.retract();
                if (!deTensioned.get()) {
                    preTension.extend();
                    //resetAllCounters();
                }
                System.out.println("pretensioning and waiting for arm");
                if (isDown()) {
                    state = 2;
                    //resetAllCounters();
                }
                break;
            case 2:
                tension.extend();
                preTension.retract();
                System.out.println("tensioning");
                if (!tensioned.get()) {
                    state = 3;
                    //resetAllCounters();
                }
                break;
            case 3:
                 System.out.println("ready to shoot");
                break;
            case 4:
                counter ++;
                System.out.println("releasing shooter piston");
                if (up.get() && counter == 5)
                {
                    shoot.retract();
                    state = 1;
                    //resetAllCounters();
                }
                break;
        }
    }

    public void shoot() {
        if (state == 3) {
            shoot.extend();
            state = 4;
        } else {
            System.out.println("Trying to Shoot While Not Ready...");
        }
    }

    public void resetAllCounters() {
        tensionedCounter.reset();
        deTensionedCounter.reset();
    }
    public void switchToCounters() {
        tensioned.free();
        deTensioned.free();
        tensionedCounter = new Counter(Wiring.LIMIT_SHOOTER_TENSIONED);
        deTensionedCounter = new Counter(Wiring.LIMIT_SHOOTER_DETENSIONED);
    }
    
    public boolean isDown(){
        if(optical.getVoltage() > 2){
            return true;
        }else{
            return false;
        }
    }
}
