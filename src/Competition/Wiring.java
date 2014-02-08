/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Competition;

import WiringMain.WiringParent;

/**
 *
 * @author robotics
 */
public class Wiring extends WiringParent{
    
    public static final boolean isTest = false;
    
    public static final double MAX_XY = 170;
    
    public static final int CAMERA_SERVO = 1;
 
    //K is for PID control
    //P is proportion, D is derivative
    public static double KpR = 2.5 * dt;/// Math.PI;
    public static double KiR = 5.39 * dt;///  Math.PI;
    public static double KfR = MAX_R / 11.18;
    public static double KpY = 13.5 / MAX_XY * dt;                 //3
    public static double KdY = .49 / MAX_XY;                   //1.01
    public static double KfY = MAX_XY / 182;
    public static double KpX = 3.1 / MAX_XY * dt;              //13.4
    public static double KdX = .98 / MAX_XY; //0.01 * dt * dt   //.49
    public static double KfX = MAX_XY / 308;

    public static final int MOTOR_LF = 1;
    public static final int MOTOR_RF = 2;
    public static final int MOTOR_LB = 3;
    public static final int MOTOR_RB = 4;
    
    public static final int MOTOR_GATHERER_LEFT = 5;
    public static final int MOTOR_GATHERER_RIGHT = 6;
    public static final int SOLENOID_GATHERER_LEFT_OUT = 1;
    public static final int SOLENOID_GATHERER_LEFT_IN = 2;
    public static final int SOLENOID_GATHERER_RIGHT_OUT = 3;
    public static final int SOLENOID_GATHERER_RIGHT_IN = 4;
    
    public static final double GATHERER_SPEED_FORWARD = 1;
    public static final double GATHERER_SPEED_REVERSE = 1;
    
    public static final double CENTER_OF_ROTATION = .608;
    
    
    public static final int MOTOR_SHOOTER = 3;
    
    public static final int COCKED = 1;
    public static final int UNCOCKED = 4;
    public static final int TENSIONED = 3;
    public static final int DETENSIONED = 2;
    
    public static final int TENSION_SOLENOID = 1;
    public static final int DEFENSIVE_ARM_UP = 2;
    public static final int DEFENSIVE_ARM_DOWN = 3;
    
    
    public static final double CF_HOOK = -.255;
    public static final double FF_HOOK = -.588;
    public static final double CB_HOOK = .187;
    public static final double FB_HOOK = -1;
}
