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

    public static final double MAX_XY = 170;
 
    //K is for PID control
    //P is proportion, D is derivative
    public static double KpR = 2.5 * dt;/// Math.PI;
    public static double KiR = 5.39 * dt;///  Math.PI;
    public static double KfR = MAX_R / 11.18;
    public static double KpY = 3.1 / MAX_XY * dt;                 //3
    public static double KdY = .98 / MAX_XY;                   //1.01
    public static double KfY = MAX_XY / 182;
    public static double KpX = 13.5 / MAX_XY * dt;              //13.4
    public static double KdX = .49 / MAX_XY; //0.01 * dt * dt   //.49
    public static double KfX = MAX_XY / 308;

    public static final int MOTOR_LF = 1;
    public static final int MOTOR_RF = 2;
    public static final int MOTOR_LB = 3;
    public static final int MOTOR_RB = 4;
    
    public static final double CENTER_OF_ROTATION = .608;
}
