package edu.wpi.first.wpilibj.templates;

/**
 *
 * @author jeremy
 */
public class Wiring {

    static final int SAMPLE_RATE = 9; //10ms = 1KHz/10
    static final double SENSOR_SCALE = 1;
    static double dt = 0.01;
    static final double MAX_XY = 132;
    static final double MAX_R = 2 * Math.PI;
 
    //K is for PID control
    //P is proportion, D is derivative
    static double KpR = 0;//14.39 * dt / Math.PI;
    static double KiR = 38.68 * dt / Math.PI;
    static double KpY = 13.7 / MAX_XY * dt;
    static double KdY = 0.41 / MAX_XY; 
    static double KpX = 3.1 / MAX_XY * dt;
    static double KdX = .79 / MAX_XY; //0.01 * dt * dt

    //T is for THB control
    //P is proportion  
    static double TpY = 1.37 * dt;
    static double TpX = .31 * dt;
    
    static final double A_SCALE = 32.174 * 12 * .004;
    static final double G_SCALE = (Math.PI / 180) / 14.375;
    static final long DRIVE_POLL_RATE = 10;
    static final int PID_C = 0;
    static final int THB_C = 1;
    static final int OPEN_C = 2;
    
    static final int MOTOR_LF = 1;
    static final int MOTOR_RF = 2;
    static final int MOTOR_LB = 3;
    static final int MOTOR_RB = 4;
    
    static final int PILOT_JOY = 1;
}
