package Test;

import WiringMain.WiringParent;

/**
 *
 * @author jeremy
 */
public class Wiring extends WiringParent {

    public static final boolean isTest = true;
    public static final double MAX_XY = 132;
    //K is for PID control
    //P is proportion, D is derivative
    public static double KpR = 14.39 * dt / (2 * Math.PI);
    public static double KiR = 38.68 * dt / (2 * Math.PI);
    public static double KfR = MAX_R / 10.71;
    public static double KpY = 5 / MAX_XY * dt;
    public static double KdY = 0.31 / MAX_XY;
    public static double KfY = MAX_XY / 182;
    public static double KpX = 1.6 / MAX_XY * dt;
    public static double KdX = .44 / MAX_XY; //0.01 * dt * dt
    public static double KfX = MAX_XY / 308;
    public static final int MOTOR_LF = 1;
    public static final int MOTOR_RF = 2;
    public static final int MOTOR_LB = 3;
    public static final int MOTOR_RB = 4;
    public static final double CENTER_OF_ROTATION = .36;
    public static final double CF_HOOK = -.236;
    public static final double FF_HOOK = -.495;
    public static final double CB_HOOK = .268;
    public static final double FB_HOOK = -1;
}
