package edu.wpi.first.wpilibj.templates;
////REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE
//import edu.wpi.first.wpilibj.CANJaguar;
//import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.TimerTask;
import java.util.Timer;

/**
 *
 * @author jeremy
 */
public class DriveSystem {

    CANJaguar fr, fl, br, bl;
    GY85_I2C sen;
    Joystick joy;
    boolean FCMode;
    boolean hasBeenStarted = false;
    double theta;
    double heading;
    double joyY;
    double joyX;
    double joyZ;
    double errorInHeading;
    double forwardY = 0;
    double rightX = 0;
    double clockwiseZ = 0;
    double previousErrorX = 0;
    double previousErrorY = 0;
    double TbY = 0;
    double TbX = 0;
    double GZ;
    Encoder enY;
    Encoder enX;
    double IY;
    double IX;
    int driveType;
    java.util.Timer time;

    public DriveSystem(CANJaguar frontRight, CANJaguar frontLeft, CANJaguar backRight, CANJaguar backLeft,
            GY85_I2C sensor, Joystick joy, Encoder encoderY, Encoder encoderX, int driveType) {
        fr = frontRight;
        fl = frontLeft;
        br = backRight;
        bl = backLeft;

        sen = sensor;

        this.joy = joy;

        enY = encoderY;
        enX = encoderX;

        this.driveType = driveType;
    }

    public void driveSystemInit() {
        hasBeenStarted = true;
        forwardY = 0;
        rightX = 0;
        clockwiseZ = 0;
        IX = 0;
        IY = 0;
        time = new java.util.Timer();
        time.schedule(new DriveLoop(this), 0L, Wiring.DRIVE_POLL_RATE);
        
    }

    public void driveSystemDenit() {
        if (hasBeenStarted) {
            hasBeenStarted = false;
            time.cancel();
        }
    }

    private void runDrive() {
        switch (driveType) {
            case 0: PID_Drive(); break;
            case 1: THB_Drive(); break;
            case 2: openLoop(); break;
        }
    }

    public void getInput() {
        theta = sen.getCompassRadAngle();
        joyY = -joy.getY() * Math.abs(joy.getY());
        joyX = joy.getX() * Math.abs(joy.getX());
        joyZ = joy.getZ() * Math.abs(joy.getZ()) / 2;

        if (FCMode) {
            if (Math.abs(joy.getZ()) > .01) {
                heading = theta;
                errorInHeading = 0;
            } else {
                errorInHeading = Wiring.radianWrap(heading - theta) * Wiring.KiR;
            }
            //takes values from joysticks and changes the values to the correct
            //vector based on compass input
            double temp = forwardY * Math.cos(theta) + rightX * Math.sin(theta);
            rightX = -forwardY * Math.sin(theta) + rightX * Math.cos(theta);
            forwardY = temp;
        }
    }

    public void THB_Drive() {
        GZ = sen.getGyroZ() * Wiring.G_SCALE;

        double VY = enY.getRate();
        double AY = sen.getAccelY() / Wiring.A_SCALE;// expected V range +/- maxXY
        double VX = enX.getRate();
        double AX = sen.getAccelX() / Wiring.A_SCALE;	// expected A range +/- 28.6

        IY = joyY - VY;
        IX = joyX - VX;

        forwardY += Wiring.TpY * IY;
        rightX += Wiring.TpX * IX;
        
        clamp(forwardY);
        clamp(rightX);
        
        if(!isSameSign(previousErrorY, IY)){
            forwardY = 0.5 * (forwardY + TbY);
            TbY = forwardY;
            previousErrorY = IY;
        }
        if(!isSameSign(previousErrorX, IX)){
            rightX = 0.5 * (rightX + TbX);
            TbX = rightX;
            previousErrorX = IX;
        }
        
        clockwiseZ += clockwiseZ + Wiring.KpR * (6.28 * joyZ - GZ) + errorInHeading;

        double lf, rf, lb, rb;

        lf = forwardY + clockwiseZ + rightX;
        rf = forwardY - clockwiseZ - rightX;
        lb = forwardY + clockwiseZ - rightX;
        rb = forwardY - clockwiseZ + rightX;

        double max = Math.abs(lf);

        if (Math.abs(rf) > max) {
            max = Math.abs(rf);
        }
        if (Math.abs(lb) > max) {
            max = Math.abs(lb);
        }
        if (Math.abs(rb) > max) {
            max = Math.abs(rb);
        }

        if (max > 1) {
            lf /= max;
            rf /= max;
            lb /= max;
            rb /= max;
        }
        
        try {
            fl.setX(lf);
            fr.setX(-rf);
            bl.setX(lb);
            br.setX(-rb);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }
                                                                     //stuff that does stuff (ha ha)
    public void PID_Drive() {
        GZ = sen.getGyroZ() * Wiring.G_SCALE;

        double VY = enY.getRate();
        double AY = sen.getAccelY() / Wiring.A_SCALE;// expected V range +/- maxXY
        double VX = enX.getRate();
        double AX = sen.getAccelX() / Wiring.A_SCALE;	// expected A range +/- 28.6

        //adds to forwardY the amount in which we want to move in y direction in in/s
        //max velocity times amount requested (-1, 1), minus current speed
        //then, the derivative of the speed (acceleration) is added to to the value of forwardY
        forwardY = clamp(forwardY);
        //forwardY = forwardY + Wiring.KpY * (Wiring.MAX_XY * joyY - VY);//PD expected range +/- 1.0
        rightX = clamp(rightX);
        //rightX = rightX + Wiring.KpX * (Wiring.MAX_XY * joyX - VX);	//PD expected range +/- 0.577
        clockwiseZ = clamp(clockwiseZ);
        clockwiseZ = clockwiseZ + Wiring.KpR * (6.28 * joyZ + GZ) + errorInHeading; //replace 0 with KpR

        double lf, rf, lb, rb;

        lf = forwardY + clockwiseZ + rightX;
        rf = forwardY - clockwiseZ - rightX;
        lb = forwardY + clockwiseZ - rightX;
        rb = forwardY - clockwiseZ + rightX;

        double max = Math.abs(lf);

        if (Math.abs(rf) > max) {
            max = Math.abs(rf);
        }
        if (Math.abs(lb) > max) {
            max = Math.abs(lb);
        }
        if (Math.abs(rb) > max) {
            max = Math.abs(rb);
        }

        if (max > 1) {
            lf /= max;
            rf /= max;
            lb /= max;
            rb /= max;
        }

        try {
            fl.setX(lf);
            fr.setX(-rf);
            bl.setX(lb);
            br.setX(-rb);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }

    }

    public void openLoop() {
        FCMode = false;

        double lf, rf, lb, rb;

        lf = joyY + joyZ + joyX;
        rf = joyY - joyZ - joyX;
        lb = joyY + joyZ - joyX;
        rb = joyY - joyZ + joyX;

        double max = Math.abs(lf);

        if (Math.abs(rf) > max) {
            max = Math.abs(rf);
        }
        if (Math.abs(lb) > max) {
            max = Math.abs(lb);
        }
        if (Math.abs(rb) > max) {
            max = Math.abs(rb);
        }

        if (max > 1) {
            lf /= max;
            rf /= max;
            lb /= max;
            rb /= max;
        }

        try {
            fl.setX(lf);
            fr.setX(-rf);
            bl.setX(lb);
            br.setX(-rb);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }

    public double clamp(double value) {
        return (value > 1) ? 1 : (value < -1) ? -1 : value;
    }
    
    public boolean isSameSign(double one, double two){
        return ((one>0)&&(two>0)) ? true : ((one<0)&&(two<0)) ? true : false;
    }

    private class DriveLoop extends TimerTask {

        private DriveSystem d;

        public DriveLoop(DriveSystem d) {
            if (d == null) {
                System.out.println("Drive System not created - Something don't work");
            } else {
                this.d = d;
                d.runDrive();
            }
        }

        public void run() {
            hasBeenStarted = true;
            d.runDrive();
        }
    }
}
