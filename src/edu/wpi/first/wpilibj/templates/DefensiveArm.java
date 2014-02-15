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

    DoubleSolenoid Left;
    DoubleSolenoid Right;
    boolean isUp = false;

    public DefensiveArm() {
        Left = new DoubleSolenoid(Wiring.DEFENSIVE_ARM_UP_LEFT, Wiring.DEFENSIVE_ARM_DOWN_LEFT);
        Right = new DoubleSolenoid(Wiring.DEFENSIVE_ARM_UP_RIGHT, Wiring.DEFENSIVE_ARM_DOWN_RIGHT);
    }

    public void goUp() {
        Left.set(true);
        Right.set(true);
        isUp = true;
    }

    public void goDown() {
        Left.set(false);
        Right.set(false);
        isUp = false;
    }
}