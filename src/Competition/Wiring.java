
package Competition;

import WiringMain.WiringParent;

/**
 *
 * @author robotics
 */
public class Wiring extends WiringParent {

    public static final boolean isTest = false;
    public static final double MAX_XY = 170;
    public static final int CAMERA_SERVO = 1;
    public static final int COMPRESSOR_RELAY = 7;
    public static final int COMPRESSOR_PRESSURE_SWITCH = 14;
    //------------------------------------------------------------try increasing I value for rotation
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
    
    //----------------------------------------------------------------MOTORS FOR DRIVE
    public static final int MOTOR_LF = 1;
    public static final int MOTOR_RF = 2;
    public static final int MOTOR_LB = 3;
    public static final int MOTOR_RB = 4;
    //---------------------------------------------------------------GATHERER STUFF
    public static final int MOTOR_GATHERER_LEFT = 5;
    public static final int MOTOR_GATHERER_RIGHT = 6;
    
    public static final int RELAY_GATHERER = 6;
    public static final double GATHERER_SPEED_FORWARD = 1;
    public static final double GATHERER_SPEED_REVERSE = -1;
    //---------------------------------------------------------------SHOOTER STUFF
    public static final int SOLENOID_SHOOTER_PRETENSION_IN = 7; // the middle cylinder
    public static final int SOLENOID_SHOOTER_PRETENSION_OUT = 8;
    public static final int SOLENOID_SHOOTER_SHOOT_IN = 5;  // the little one
    public static final int SOLENOID_SHOOTER_SHOOT_OUT = 6;
    public static final int SOLENOID_SHOOTER_TENSION_IN = 3; // the two cylinders on the side
    public static final int SOLENOID_SHOOTER_TENSION_OUT = 4;
    
    public static final int LIMIT_SHOOTER_DETENSIONED = 5;
    public static final int LIMIT_SHOOTER_TENSIONED = 6;
    public static final int LIMIT_SHOOTER_DOWN = 9;
    public static final int LIMIT_SHOOTER_MIDDLE_PISTON = 8;
    
    public static final int OPTICAL_SHOOTER_SENSOR = 1;
    public static final int OPTICAL_SHOOTER_BALL_SENSOR = 5;
    public static final double C_HAS_BALL = 2;
  
    public static final double CENTER_OF_ROTATION = .608;
    
    public static final int COCKED = 5;
    public static final int UNCOCKED = 8;
    public static final int TENSIONED = 7;
    public static final int DETENSIONED = 6;
    //----------------------------------------------------------------MISC 
    public static final int DEFENSIVE_ARM = 8;

    public static final double CF_HOOK = -.255;
    public static final double FF_HOOK = -.588;
    public static final double CB_HOOK = .187;
    public static final double FB_HOOK = -1;
    
    //Sonar things
    public static final int SONAR_CHANNEL = 7; //This will probably have to change...
    public static final double MAX_DIST_R1 = 14; //Needs an update...
    public static final double MIN_DIST_R1 = 13;
    public static final double MAX_DIST_R2 = 5;
    public static final double MIN_DIST_R2 = 0;
    //PRESSURE SENSOR
    public static final int PRESSURE_SENSOR = 6;
    //----------------------------------------------------------------KATE KRATE
    public static final int KATE_KRATE_SWITCH = 7;
    public static final int KATE_KRATE_ARM = 16;
    public static final int KATE_KRATE_DEFENSE_SWITCH = 9;
}
