/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

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
        motor = new Jaguar(Wiring.shooterMotor);
        tension = new Solenoid(Wiring.tension);
        tensioned = new Counter(Wiring.tensioned);
        deTensioned = new Counter(Wiring.deTensioned);
        tension.set(false);
        cocked = new Counter(Wiring.cocked);
        unCocked = new Counter(Wiring.unCocked);
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
            System.out.println("Checking Position...");
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
            System.out.println("Cocking...");
            motor.set(0.6);
            if (cocked.get() >= 1) 
            {
                state = 2;
                resetAllCounters();
            }
            break;
        case 2:
            System.out.println("Tensioning...");
            tension.set(true);
            motor.set(0);
            if(tensioned.get() >= 1) 
            {
                state = 3;
                resetAllCounters();
            }
            break;
        case 3:
            System.out.println("Ready To Shoot...");
            break;
        case 4:
            if (unCocked.get() >= 1)
            {
                motor.set(0);
                tension.set(false);
                System.out.println("Done Shooting...");
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
            System.out.println("Shooting...");
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
