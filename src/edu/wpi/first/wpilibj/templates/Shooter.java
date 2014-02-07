/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import Competition.Wiring;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Jaguar;

/**
 *
 *
 */
public class Shooter {
    Jaguar motor;
    Solenoid tension;
    Counter tensioned;
    Counter deTensioned;
    Counter cocked;
    Counter unCocked;
    int state = 0;
    
    public Shooter()
    {
        motor = new Jaguar(Wiring.MOTOR_SHOOTER);
        tension = new Solenoid(Wiring.TENSION_SOLENOID);
        tensioned = new Counter(Wiring.TENSIONED);
        deTensioned = new Counter(Wiring.DETENSIONED);
        tension.set(false);
        cocked = new Counter(Wiring.COCKED);
        unCocked = new Counter(Wiring.UNCOCKED);
        tensioned.start();
        deTensioned.start();
        cocked.start();
        unCocked.start();
    }
    
    public void Cock()
    {
        //System.out.println("Got This far.");
        switch (state)
        {
        case 0:   
            //checking position
            if (unCocked.get() >= 1 && deTensioned.get() >= 1 && !tension.get()) 
            {
                state = 1;
                resetAllCounters();
            }
            if (cocked.get() >= 1 && deTensioned.get() >= 1 && !tension.get()) 
            {
                state = 2;
                resetAllCounters();
            }
            if (cocked.get() >= 1 && tensioned.get() >= 1 && tension.get()) 
            {
                state = 3;
                resetAllCounters();
            }
            if (unCocked.get() >= 1 && tensioned.get() >= 1 && tension.get()) 
            {
                state = 4;
                resetAllCounters();
            }
            break;
        case 1:
            //cocking
            motor.set(0.6);
            if (cocked.get() >= 1) 
            {
                state = 2;
                resetAllCounters();
            }
            break;
        case 2:
             //tensioning
            tension.set(true);
            motor.set(0);
            if(tensioned.get() >= 1) 
            {
                state = 3;
                resetAllCounters();
            }
            break;
        case 3:
            //ready to shoot
            break;
        case 4:
            if (unCocked.get() >= 1)
            {
                motor.set(0);
                tension.set(false);
                //done shooting
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
            motor.set(-.6);
            //shooting
            state = 4;
        }
        else
        {
            System.out.println("Trying to Shoot While Not Ready...");
        }
    }
    public void resetAllCounters()
    {
        cocked.reset();
        unCocked.reset();
        tensioned.reset();
        deTensioned.reset();
    }
}
