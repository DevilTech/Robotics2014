package WiringMain;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author robotics
 */
public class WiringParent {

    public static final int SAMPLE_RATE = 9; //10ms = 1KHz/10
    public static final double SENSOR_SCALE = 1;
    public static final int MAX_SOLENOID_PASSES = 1;
    public static double dt = 0.01;
    public static final double MAX_R = 2 * Math.PI;
    public static final double A_SCALE = 32.174 * 12;
    public static final double G_SCALE = (Math.PI / 180);
    public static final long DRIVE_POLL_RATE = (long) (1000 * dt);
    public static final int PID_C = 0;
    public static final int OPEN_C = 1;
    public static final int HALF_C = 2;
    public static final int[] ENCODER_X = {1, 2};
    public static final int[] ENCODER_Y = {3, 4};
    public static final int PILOT_JOY = 1;
}
