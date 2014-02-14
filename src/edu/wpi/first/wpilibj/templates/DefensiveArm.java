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
    Solenoid upLeft;
    Solenoid downLeft;
    Solenoid upRight;
    Solenoid downRight;
    boolean isUp = false;
    
    public DefensiveArm() {
        upLeft = new Solenoid(Wiring.DEFENSIVE_ARM_UP_LEFT);
        downLeft = new Solenoid(Wiring.DEFENSIVE_ARM_DOWN_LEFT);
        upRight = new Solenoid(Wiring.DEFENSIVE_ARM_UP_RIGHT);
        downRight = new Solenoid(Wiring.DEFENSIVE_ARM_DOWN_RIGHT);
    }
    
    public void goUp() {
        upLeft.set(true);
        downLeft.set(false);
        upRight.set(true);
        downRight.set(false);
        isUp = true;
    }
    
    public void goDown() {
        upLeft.set(false);
        downLeft.set(true);
        upRight.set(false);
        downRight.set(true);
        isUp = false;
    }
    
}