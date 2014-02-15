/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import Competition.Wiring;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Solenoid;

public class Shooter {

    Counter up;
    Counter down;
    Counter tensioned;
    Counter deTensioned;
    int state = 0;
    Piston preTension;
    Piston shoot;
    Piston tension;

    public Shooter() {
        preTension = new Piston(Wiring.SOLENOID_SHOOTER_TENSION_IN, Wiring.SOLENOID_SHOOTER_TENSION_OUT);
        shoot = new Piston(Wiring.SOLENOID_SHOOTER_SHOOT_IN, Wiring.SOLENOID_SHOOTER_SHOOT_OUT);
        tension = new Piston(Wiring.SOLENOID_SHOOTER_TENSIONED_IN, Wiring.SOLENOID_SHOOTER_TENSIONED_OUT);
        tensioned = new Counter(Wiring.LIMIT_SHOOTER_TENSIONED);
        up = new Counter(Wiring.LIMIT_SHOOTER_UP);
        down = new Counter(Wiring.LIMIT_SHOOTER_DOWN);
        deTensioned = new Counter(Wiring.LIMIT_SHOOTER_DETENSIONED);
        tension.retract();
        shoot.extend();
        preTension.extend();
        tensioned.start();
        up.start();
        down.start();
        deTensioned.start();
    }

    public void keepCocked() {
        switch (state) {

            case 0:
                //System.out.println("checking position...");
                if (up.get() >= 1 && deTensioned.get() >= 1) {
                    state = 1;
                }
                if (down.get() >= 1 && deTensioned.get() >= 1) {
                    state = 2;
                }
                if (down.get() >= 1 && tensioned.get() >= 1) {
                    state = 3;
                }
                if (up.get() >= 1 && tensioned.get() >= 1) {
                    state = 4;
                }
                resetAllCounters();
                break;
            case 1:
                if (deTensioned.get() >= 1) {
                    preTension.retract();
                }
                //System.out.println("pretensioning and waiting for arm");
                if (down.get() >= 1) {
                    state = 2;
                    resetAllCounters();
                }
                break;
            case 2:
                tension.extend();
                preTension.extend();
                //System.out.println("tensioning");
                if (tensioned.get() >= 1) {
                    state = 3;
                    resetAllCounters();
                }
                break;
            case 3:
                // System.out.println("ready to shoot");
                break;
            case 4:
                if (up.get() >= 1) //System.out.println("detensioning and releasing shooter piston");
                {
                    shoot.extend();
                    state = 1;
                    resetAllCounters();
                }
                break;
            default:
                state = 0;
                break;

        }
    }

    public void shoot() {
        if (state == 3) {
            shoot.retract();
            state = 4;
        } else {
            System.out.println("Trying to Shoot While Not Ready...");
        }
    }

    public void resetAllCounters() {
        up.reset();
        down.reset();
        tensioned.reset();
        deTensioned.reset();
    }
}
