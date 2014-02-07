/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import Control.Control;
import edu.wpi.first.wpilibj.Joystick;

public class Happystick {
    Joystick joy;
    Control controller;
    Button FCButt;
    Button PIDButt;
    Button revButt;
    Button gathButt;
    Button leftButt;
    Button rightButt;
    boolean gathExtend = false;
    boolean gathSpinOut = false;
    boolean gathSpinIn = false;
    
    public Happystick(int joyNum, Control controller){
        joy = new Joystick(joyNum);
        this.controller = controller;
        FCButt = new Button(joy, this.controller.FCSwitch);
        PIDButt = new Button(joy, this.controller.PIDSwitch);
        gathButt = new Button(joy, this.controller.gathSwitch);
        revButt = new Button(joy, this.controller.revSwitch);
    }
    
    public double getRotation(){
        return joy.getRawAxis(controller.rotationAxis) * controller.invertR;
    }
    
    public double getForward(){
        return joy.getRawAxis(controller.forwardAxis) * controller.invertY;
    }
    
    public double getRight(){
        return joy.getRawAxis(controller.rightAxis) * controller.invertX;
    }
    
    public boolean getFCSwitch(){
        return FCButt.getReHit();
    }
    
    public boolean getLoopSwitch(){
        return PIDButt.getReHit();
    }
    
    public boolean getHATUp(){
        return (joy.getRawAxis(controller.hatVertical) == controller.hatUp);
    }
    
    public boolean getHATDown(){
        return (joy.getRawAxis(controller.hatVertical) == controller.hatDown);
    }
    
    public boolean getHATRight(){
        return (joy.getRawAxis(controller.hatHorizontal) == controller.hatRight);
    }
    
    public boolean getHATLeft(){
        return (joy.getRawAxis(controller.hatHorizontal) == controller.hatLeft);
    }
    
    public boolean getGather(){
       return gathButt.isPressed();
    }
    
    public boolean getReverseGather(){
        return revButt.isPressed();
    }
    
    public boolean getLeftHook(){
        return true;
    }
    
    public boolean getRightHook(){
        return true;
    }
    
    
            
}
