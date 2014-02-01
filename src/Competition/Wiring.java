/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Competition;

/**
 *
 * @author robotics
 */
public class Wiring {
    public static final int SAMPLE_RATE = 9; //10ms = 1KHz/10
    public static final double SENSOR_SCALE = 1;
    public static double dt = 0.01;
    public static final double MAX_XY = 180;
    public static final double MAX_R = 2 * Math.PI;
 
    //K is for PID control
    //P is proportion, D is derivative
    public static double KpR = 12.86 * dt / (2 * Math.PI);
    public static double KiR = 29.19 * dt / (2 * Math.PI);
    public static double KpY = 3 / MAX_XY * dt;                 //3
    public static double KdY = 1.01 / MAX_XY;                   //1.01
    public static double KpX = 13.4 / MAX_XY * dt;              //13.4
    public static double KdX = .49 / MAX_XY; //0.01 * dt * dt   //.49

    //T is for THB control
    //P is proportion  
    public static double TpY = 1.37 * dt;
    public static double TpX = .31 * dt;
    
    public static final double A_SCALE = 32.174 * 12;
    public static final double G_SCALE = (Math.PI / 180);
    public static final long DRIVE_POLL_RATE = (long) (1000 * dt);
    public static final int PID_C = 0;
    public static final int THB_C = 1;
    public static final int OPEN_C = 2;
    
    public static final int MOTOR_LF = 1;
    public static final int MOTOR_RF = 2;
    public static final int MOTOR_LB = 3;
    public static final int MOTOR_RB = 4;
    
    public static final int[] ENCODER_X = {1,2};
    public static final int[] ENCODER_Y = {3,4};
    
    public static final int PILOT_JOY = 1;
    
    public static final double CENTER_OF_ROTATION = .608;
}
