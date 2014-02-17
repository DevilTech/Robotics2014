/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Joystick;

/**
 *
 * @author robotics
 */
public class Button {

    Joystick joy;
    int button;
    private boolean isReal = false;
    private boolean hasBeenTriggered = false;

    public Button(Joystick joy, int button) {
        this.joy = joy;
        if (button != 0) {
            this.button = button;
            isReal = true;
        } else {
            isReal = false;
        }

    }

    public boolean isPressed() {
        return joy.getRawButton(button);
    }

    public boolean getReHit() {
        if (!isReal) {
            return false;
        }
        if (joy.getRawButton(button)) {
            if (hasBeenTriggered) {
                return false;
            } else {
                hasBeenTriggered = true;
                return true;
            }
        } else {
            hasBeenTriggered = false;
            return false;
        }
    }
}
