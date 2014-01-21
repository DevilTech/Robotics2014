/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
    double initialHeading;
    double forwardY;
    double rightX;
    double clockwiseZ;
    double GZ;
    Encoder enY;
    Encoder enX;
    double IY;
    double IX;
    private int driveType;
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
        initialHeading = sen.getCompassRadAngle();
    }

    public void driveSystemDenit() {

        if (hasBeenStarted) {
            hasBeenStarted = false;
            time.cancel();
        }
    }

    private void runDrive() {
        switch (driveType) {
            case 0:
                PID_Drive();
                break;
            case 1:
                THB_Drive();
                break;
            case 2:
                openLoop();
                break;
        }
    }

    public void getInput() {

        theta = Wiring.radianWrap(sen.getCompassRadAngle() - initialHeading);
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

        clamp(IX);
        clamp(IY);

        IY += joyY - VY;
        IX += joyX - VX;

        forwardY = Wiring.TpY * (IY + Wiring.TdY * AY);
        rightX = Wiring.TpX * (IX + Wiring.TdX * AX);
        clockwiseZ = clockwiseZ + Wiring.KpR * (6.28 * joyZ - GZ) + errorInHeading;

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
        forwardY = forwardY + Wiring.KpY * (Wiring.MAX_XY * joyY - VY);//PD expected range +/- 1.0
        System.out.println((Wiring.MAX_XY * joyY) + ", " + (Wiring.MAX_XY * joyY - VY));
        rightX = clamp(rightX);
        rightX = rightX + Wiring.KpX * (Wiring.MAX_XY * joyX - VX);	//PD expected range +/- 0.577
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
