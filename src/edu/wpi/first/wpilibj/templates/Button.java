/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

/**
 *
 * @author robotics
 */
public class Button {

    private boolean hasBeenTriggered = false;

    public boolean getReHit(boolean button) {
        if (button) {
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
