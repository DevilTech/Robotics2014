/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import Competition.Wiring;
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

    public Shooter() {
        preTension = new Piston(Wiring.SOLENOID_SHOOTER_PRETENSION_OUT, Wiring.SOLENOID_SHOOTER_PRETENSION_IN);
        shoot = new Piston(Wiring.SOLENOID_SHOOTER_SHOOT_OUT, Wiring.SOLENOID_SHOOTER_SHOOT_IN);
        tension = new Piston(Wiring.SOLENOID_SHOOTER_TENSION_OUT, Wiring.SOLENOID_SHOOTER_TENSION_IN);
        tensioned = new DigitalInput(Wiring.LIMIT_SHOOTER_TENSIONED);
        up = new DigitalInput(Wiring.LIMIT_SHOOTER_UP);
        down = new DigitalInput(Wiring.LIMIT_SHOOTER_DOWN);
        deTensioned = new DigitalInput(Wiring.LIMIT_SHOOTER_DETENSIONED);
        tension.retract();
        shoot.retract();
        preTension.extend();

    }
    


    public void keepCocked() {
        switch (state) {

            case 0:
                System.out.println("checking position...");
                if (!up.get() && !deTensioned.get()) {
                    state = 1;
                    switchToCounters();
                }
                if (!down.get() && !deTensioned.get()) {
                    state = 2;
                    switchToCounters();
                }
                if (!down.get() && !tensioned.get()) {
                    state = 3;
                    switchToCounters();
                }
                if (!up.get() && !tensioned.get()) {
                    state = 4;
                    switchToCounters();
                }
                break;
            case 1:
                tension.relax();
                if (deTensionedCounter.get() >= 1) {
                    preTension.extend();
                    resetAllCounters();
                }
                System.out.println("pretensioning and waiting for arm");
                if (downCounter.get() >= 1) {
                    state = 2;
                    resetAllCounters();
                }
                break;
            case 2:
                tension.extend();
                preTension.retract();
                System.out.println("tensioning");
                if (tensionedCounter.get() >= 1) {
                    state = 3;
                    resetAllCounters();
                }
                break;
            case 3:
                 System.out.println("ready to shoot");
                break;
            case 4:
                System.out.println("releasing shooter piston");
                if (upCounter.get() >= 1)
                {
                    shoot.retract();
                    state = 1;
                    resetAllCounters();
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
        upCounter.reset();
        downCounter.reset();
        tensionedCounter.reset();
        deTensionedCounter.reset();
    }
    public void switchToCounters() {
        up.free();
        down.free();
        tensioned.free();
        deTensioned.free();
        upCounter = new Counter(Wiring.LIMIT_SHOOTER_UP);
        downCounter = new Counter(Wiring.LIMIT_SHOOTER_DOWN);
        tensionedCounter = new Counter(Wiring.LIMIT_SHOOTER_TENSIONED);
        deTensionedCounter = new Counter(Wiring.LIMIT_SHOOTER_DETENSIONED);
    }
}
