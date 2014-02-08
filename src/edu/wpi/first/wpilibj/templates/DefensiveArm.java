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
    Solenoid up;
    Solenoid down;
    boolean isUp = false;
    
    public DefensiveArm() {
        up = new Solenoid(Wiring.DEFENSIVE_ARM_UP);
        down = new Solenoid(Wiring.DEFENSIVE_ARM_DOWN);
    }
    
    public void goUp() {
        up.set(true);
        down.set(false);
        isUp = true;
    }
    
    public void goDown() {
        up.set(false);
        down.set(true);
        isUp = false;
    }
    
}