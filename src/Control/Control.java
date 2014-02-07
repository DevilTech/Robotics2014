/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;

/**
 *
 * @author robotics
 */
public class Control {
    
    public int rotationAxis, forwardAxis, rightAxis;
    public int FCSwitch, PIDSwitch;
    public int invertY = 1, invertX = 1, invertR = 1;
    public int hatUp, hatDown, hatRight, hatLeft;
    public int hatVertical, hatHorizontal;
    public int SpOSwitch, SpISwitch, rexSwitch;

    private Control (){};
        
    public static Control getXbox(){
        Control xbox = new Control();
        xbox.rotationAxis = 3;
        xbox.forwardAxis = 1;
        xbox.rightAxis = 2;
        xbox.FCSwitch = 3;
        xbox.PIDSwitch = 2;
        xbox.hatUp = 1;
        xbox.hatDown = -1;
        xbox.hatLeft = -1;
        xbox.hatRight = 1;
        xbox.hatHorizontal = 5;
        xbox.hatVertical = 6;
        xbox.SpISwitch = 6;
        xbox.SpOSwitch = 5;
        xbox.rexSwitch = 1;
        return xbox;
    }
    
    public static Control getPilot(){
        Control pilot = new Control();
        pilot.rotationAxis = 3;
        pilot.forwardAxis = 1;
        pilot.rightAxis = 2;
        pilot.FCSwitch = 2;
        pilot.PIDSwitch = 1;
        pilot.invertY = -1;
        pilot.hatUp = 1;
        pilot.hatDown = -1;
        pilot.hatLeft = -1;
        pilot.hatRight = 1;
        pilot.hatHorizontal = 5;
        pilot.hatVertical = 6;
        return pilot;
    }
}
