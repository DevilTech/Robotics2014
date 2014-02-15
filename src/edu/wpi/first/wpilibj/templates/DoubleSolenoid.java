/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import Competition.Wiring;
import edu.wpi.first.wpilibj.Solenoid;

/**
 *
 * @author jeremy
 */
public class DoubleSolenoid {

    Solenoid one;
    Solenoid two;
    int onePasses = 0;
    int twoPasses = 0;
    boolean oneStatus = false;
    boolean twoStatus = false;

    public DoubleSolenoid(int valveOne, int valveTwo) {
        one = new Solenoid(valveOne);
        two = new Solenoid(valveTwo);

    }

    public void valveOne(boolean on) {
        if (on) {
            oneStatus = true;
            if (onePasses < Wiring.MAX_SOLENOID_PASSES) {
                one.set(true);
                onePasses++;
            }
        } else {
            oneStatus = false;
            one.set(false);
            onePasses = 0;
        }

    }

    public void valveTwo(boolean on) {
        if (on) {
            twoStatus = true;
            if (twoPasses < Wiring.MAX_SOLENOID_PASSES) {
                two.set(true);
                twoPasses++;
            }

        } else {
            twoStatus = false;
            two.set(false);
            twoPasses = 0;
        }

    }

    public void reverse() {
        valveOne(!oneStatus);
        valveTwo(!twoStatus);
    }

    public void set(boolean oneOn) {
        valveOne(oneOn);
        valveTwo(!oneOn);
    }
}
