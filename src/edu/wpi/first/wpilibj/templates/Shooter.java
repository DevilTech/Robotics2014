/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Solenoid;

/**
 *
 *
 */
public class Shooter {
    Relay tension;
    Counter up;
    Counter down;
    Counter tensioned;
    Counter deTensioned;
    int state = 0;
    Solenoid preTensionIn;
    Solenoid preTensionOut;
    Solenoid shootIn;
    Solenoid shootOut;
    
    public Shooter()
    {
        preTensionIn = new Solenoid(Wiring.preTensionIn);
        preTensionOut = new Solenoid(Wiring.preTensionOut);
        shootIn = new Solenoid(Wiring.shootIn);
        shootOut = new Solenoid(Wiring.shootOut);
        tension = new Relay(Wiring.tension);
        tensioned = new Counter(Wiring.tensioned);
        up = new Counter(Wiring.up);
        down = new Counter(Wiring.down);
        deTensioned = new Counter(Wiring.deTensioned);
        tension.set(Relay.Value.kOff);
        shootIn.set(true);
        shootOut.set(false);
        preTensionIn.set(true);
        preTensionOut.set(false);
        tensioned.start();
        up.start();
        down.start();
        deTensioned.start();
    }
    
    public void keepCocked()
    {
        switch (state)
        {
            case 0:
                System.out.println("checking position...");
                if (up.get() >= 1 && deTensioned.get() >= 1) {state = 1;}
                if (down.get() >= 1 && deTensioned.get() >= 1) {state = 2;}
                if (down.get() >= 1 && tensioned.get() >= 1) {state = 3;}
                if (up.get() >= 1 && tensioned.get() >= 1) {state = 4;}
                resetAllCounters();
                break;
            case 1:
                if (deTensioned.get() >= 1)
                {
                preTensionOut.set(true);
                preTensionIn.set(false);
                }
                System.out.println("pretensioning and waiting for arm");
                if (down.get() >= 1)
                {
                    state = 2;
                    resetAllCounters();
                }
                break;
            case 2:
                tension.set(Relay.Value.kForward);
                preTensionIn.set(true);
                preTensionOut.set(false);
                System.out.println("tensioning");
                if (tensioned.get() >= 1)
                {
                    state = 3;
                    resetAllCounters();
                }
                break;
            case 3:
                System.out.println("ready to shoot");
                break;
            case 4:
                if (up.get() >= 1)
                System.out.println("detensioning and releasing shooter piston");
                {
                    shootIn.set(true);
                    shootOut.set(false);
                    state = 1;
                    resetAllCounters();
                }
                break;
            default:
                state = 0;
                break;
        }
    }
    
    public void shoot()
    {
        if (state == 3)
        {
            shootOut.set(true);
            shootIn.set(false);
            state = 4;
        }
        else
        {
            System.out.println("Trying to Shoot While Not Ready...");
        }
    }
    public void resetAllCounters()
    {
        up.reset();
        down.reset();
        tensioned.reset();
        deTensioned.reset();
    }
}
