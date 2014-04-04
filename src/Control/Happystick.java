
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;

import Control.Control;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.templates.Button;

public class Happystick {

    Joystick joy;
    Control controller;
    Button FCButt;
    Button PIDButt;
    Button revButt;
    Button gathButt;
    Button leftButt;
    Button rightButt;
    Button shootButt;
    Button armButt;
    Button popButt;
    Button resetDriveButt;
    Button resetShooterButt;
    boolean gathExtend = false;
    boolean gathSpinOut = false;
    boolean gathSpinIn = false;

    public Happystick(int joyNum, Control controller) {
        joy = new Joystick(joyNum);
        this.controller = controller;
        FCButt = new Button(joy, this.controller.FCSwitch);
        PIDButt = new Button(joy, this.controller.PIDSwitch);
        gathButt = new Button(joy, this.controller.gathSwitch);
        revButt = new Button(joy, this.controller.revSwitch);
        shootButt = new Button(joy, this.controller.shoot);
        armButt = new Button(joy, this.controller.defenseButton);
        popButt = new Button(joy, this.controller.popShot);
        resetDriveButt = new Button(joy, this.controller.resetDriveButton);
        resetShooterButt = new Button(joy, this.controller.resetShooterButton);
    }

    public double getAxis(int num) {
        if (num != 0) {
            return joy.getRawAxis(num);
        } else {
            return 0;
        }

    }

    public double getRotation() {
        return getAxis(controller.rotationAxis) * controller.invertR;
    }

    public double getForward() {
        return getAxis(controller.forwardAxis) * controller.invertY;
    }

    public double getRight() {
        return getAxis(controller.rightAxis) * controller.invertX;
    }

    public boolean getFCSwitch() {
        return FCButt.getReHit();
    }

    public boolean getLoopSwitch() {
        return PIDButt.getReHit();
    }

    public boolean getHATUp() {
        return (getAxis(controller.hatVertical) == controller.hatUp);
    }

    public boolean getHATDown() {
        return (getAxis(controller.hatVertical) == controller.hatDown);
    }

    public boolean getHATRight() {
        return (getAxis(controller.hatHorizontal) == controller.hatRight);
    }

    public boolean getHATLeft() {
        return (joy.getRawAxis(controller.hatHorizontal) == controller.hatLeft);
    }

    public boolean getGather() {
        return gathButt.isPressed();
    }

    public boolean getReverseGather() {
        return revButt.isPressed();
    }

    public boolean getLeftHook() {
        if (getAxis(controller.hookAxis) > controller.hookAxisSens) {
            return true;
        } else {
            return false;
        }
    }

    public boolean getRightHook() {
        if (getAxis(controller.hookAxis) < -controller.hookAxisSens) {
            return true;
        } else {
            return false;
        }
    }

    public boolean getShoot() {
        return shootButt.isPressed();
    }

    public boolean getArmRaise() {
        return armButt.isPressed();
    }
    
    public boolean getPop(){
        return popButt.isPressed();
    }
    
    public boolean getReset(){
        return resetDriveButt.getReHit();
    }
    
    public boolean getShooterReset(){
        return resetShooterButt.getReHit();
    }
}
