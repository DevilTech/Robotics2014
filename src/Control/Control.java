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
    public int gathSwitch, revSwitch;
    public int leftHook, rightHook;
    public int shoot;
    public boolean useZAxisHook = false;
    public int hookAxis;
    public double hookAxisSens;
    public int defenseButton;
    public int popShot;

    private Control() {
    }

    ;

    public static Control getXbox() {
        Control xbox = new Control();
        xbox.rotationAxis = 4;
        xbox.forwardAxis = 2;
        xbox.invertY = -1;
        xbox.rightAxis = 1;
        
        xbox.FCSwitch = XboxMap.X;
        xbox.PIDSwitch = XboxMap.B;
        
        xbox.hatUp = 1;
        xbox.hatDown = -1;
        xbox.hatLeft = -1;
        xbox.hatRight = 1;
        xbox.hatHorizontal = 5;
        xbox.hatVertical = 6;
        
        xbox.gathSwitch = XboxMap.RIGHT_BUMPER;
        xbox.revSwitch = XboxMap.LEFT_BUMPER;
        
        xbox.useZAxisHook = true;
        xbox.hookAxis = 3;
        
        xbox.shoot = XboxMap.A;
        xbox.hookAxisSens = .1;
        xbox.defenseButton = 1;
        xbox.popShot = XboxMap.Y;
        return xbox;
    }

    public static Control getPilot() {
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
