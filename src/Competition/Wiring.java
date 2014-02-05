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

    public static final double MAX_XY = 180;
 
    //K is for PID control
    //P is proportion, D is derivative
    public static double KpR = 12.86 * dt / Math.PI;
    public static double KiR = 1 / Math.PI; //29.19 * dt /  Math.PI;
    public static double KpY = 3 / MAX_XY * dt;                 //3
    public static double KdY = 1.01 / MAX_XY;                   //1.01
    public static double KpX = 13.4 / MAX_XY * dt;              //13.4
    public static double KdX = .49 / MAX_XY; //0.01 * dt * dt   //.49

    
    public static final int MOTOR_LF = 1;
    public static final int MOTOR_RF = 2;
    public static final int MOTOR_LB = 3;
    public static final int MOTOR_RB = 4;
    
    public static final double CENTER_OF_ROTATION = .608;
}
