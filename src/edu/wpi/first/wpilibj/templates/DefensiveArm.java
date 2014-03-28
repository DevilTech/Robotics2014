/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import Competition.Wiring;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Solenoid;

/**
 *
 * @author robotics
 */
public class DefensiveArm {

    Relay piston;
    boolean isUp = false;

    public DefensiveArm() {
        piston = new Relay(Wiring.DEFENSIVE_ARM);
    }

    public void goUp() {
        piston.set(Relay.Value.kForward);
        isUp = true;
    }

    public void goDown() {
        piston.set(Relay.Value.kReverse);
        isUp = false;

    }
}