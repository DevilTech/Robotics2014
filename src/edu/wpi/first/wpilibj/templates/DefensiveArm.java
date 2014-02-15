/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import Competition.Wiring;
import edu.wpi.first.wpilibj.Solenoid;

/**
 *
 * @author robotics
 */
public class DefensiveArm {

    Piston pis;


    boolean isUp = false;

    public DefensiveArm() {
        pis = new Piston(Wiring.DEFENSIVE_ARM_UP, Wiring.DEFENSIVE_ARM_DOWN);
    }

    public void goUp() {
        pis.extend();
        isUp = true;
    }

    public void goDown() {
        pis.retract();
        isUp = false;
    }
}