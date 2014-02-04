package edu.wpi.first.wpilibj.templates;
////REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE
//import edu.wpi.first.wpilibj.Talon;
//import edu.wpi.first.wpilibj.Encoder;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import java.util.TimerTask;


import Competition.Wiring;
import edu.wpi.first.wpilibj.Timer;

/**
 *
 * @author jeremy
 */
public class DriveSystem {

    Talon fr, fl, br, bl;
    GY85_I2C sen;
    Joystick joy;
    boolean FCMode = true;
    boolean hasBeenStarted = false;
    double theta;
    double speedY = 0;
    double speedX = 0;
    double speedZ = 0;
    double heading;
    double errorInHeading = 0;
    double initialHeading = 0;
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
    Button FCButt;
    Button openButt;

    public DriveSystem(Talon frontRight, Talon frontLeft, Talon backRight, Talon backLeft,
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
        
        FCButt = new Button(joy, 2);
        openButt = new Button(joy, 1);
    }

    public void driveSystemInit() {
        if (!hasBeenStarted) {
            hasBeenStarted = true;
            forwardY = 0;
            rightX = 0;
            clockwiseZ = 0;
            IX = 0;
            IY = 0;
            time = new java.util.Timer();
            time.schedule(new DriveLoop(this), 0L, Wiring.DRIVE_POLL_RATE);
            initialHeading = sen.getCompassRadAngle();
        } else {
            System.out.println("Drive system already init");
        }

    }

    public void driveSystemDenit() {
        if (hasBeenStarted) {
            hasBeenStarted = false;
            time.cancel();
        }
    }

    private void runDrive() {
        sen.readA();
        sen.readG();
        sen.readC();
        switch (driveType) {
            case 0:
                PID_Drive();
                break;
            case 1:
                openLoop();
                break;

        }
    }

    public void calculateInput() {
        theta = sen.getCompassRadAngle(initialHeading);
        System.out.println(sen.getCompassRadAngle(initialHeading));
        if (FCButt.getReHit()) {
            FCMode = (FCMode) ? false : true;
        }
        if (openButt.getReHit()) {
            driveType = (driveType == 0) ? 1 : 0;
        }

        if (FCMode) {
            if (Math.abs(speedZ) > .001) {
                heading = theta;
                errorInHeading = 0;
            } else {
                errorInHeading = DTlib.radianWrap(heading - theta) * Wiring.KiR;
            }
            getHat();
            //takes values from joysticks and changes the values to the correct
            //vector based on compass input
            double temp = speedY * Math.cos(theta) + speedX * Math.sin(theta);
            speedX = -speedY * Math.sin(theta) + speedX * Math.cos(theta);
            speedY = temp;
        } else {
            errorInHeading = 0;
        }
    }
    
    public void getJoy(){
        speedY = -joy.getY() * Math.abs(joy.getY());
        speedX = joy.getX() * Math.abs(joy.getX());
        speedZ = joy.getZ() * Math.abs(joy.getZ()) / 2;
    }
    
    public void setSpeed(double x, double y, double z){
        speedY = y;
        speedX = x;
        speedZ = z;
    }

    public void getHat() {
        //axis numbers are complete guesses
        if (joy.getRawAxis(5) == 1) {
            heading = Math.PI / 2;
        } else if (joy.getRawAxis(5) == -1) {
            heading = Math.PI / 2;
        } else if (joy.getRawAxis(6) == -1) {
            heading = 0;
        } else if (joy.getRawAxis(6) == 1) {
            heading = Math.PI;
        }
    }

    //stuff that does stuff (ha ha)
    public void PID_Drive() {

        GZ = sen.getGyroZ() * Wiring.G_SCALE;

        double VY = enY.getRate();
        double AY = sen.getAccelY() / Wiring.A_SCALE;// expected V range +/- maxXY
        double VX = enX.getRate();
        double AX = sen.getAccelX() / Wiring.A_SCALE;// expected A range +/- 28.6

        //adds to forwardY the amount in which we want to move in y direction in in/s
        //max velocity times amount requested (-1, 1), minus current speed
        //then, the derivative of the speed (acceleration) is added to to the value of forwardY
        forwardY = DTlib.clamp(forwardY);
        forwardY += Wiring.KpY * (Wiring.MAX_XY * speedY - VY);//PD expected range +/- 1.0
        rightX = DTlib.clamp(rightX);
        rightX += Wiring.KpX * (Wiring.MAX_XY * speedX - VX) * .577;	//PD expected range +/- 0.577
        clockwiseZ = DTlib.clamp(clockwiseZ);
        clockwiseZ += Wiring.KpR * (speedZ + GZ);

        double tempCZ = clockwiseZ+ errorInHeading;
        double tempFY = forwardY - Wiring.KdY * AY;
        double tempRX = rightX - Wiring.KdX * AX;

        double lf, rf, lb, rb;

        lf = tempFY + tempCZ * Wiring.CENTER_OF_ROTATION + tempRX;
        rf = tempFY - tempCZ * Wiring.CENTER_OF_ROTATION - tempRX;
        lb = tempFY + tempCZ - tempRX;
        rb = tempFY - tempCZ + tempRX;
        
        calculateMotorSpeed(lf, rf, lb, rb);

    }

    public void openLoop() {

        double lf, rf, lb, rb;

        lf = speedY + speedZ + speedX;
        rf = speedY - speedZ - speedX;
        lb = speedY + speedZ - speedX;
        rb = speedY - speedZ + speedX;

        calculateMotorSpeed(lf, rf, lb, rb);

    }

    public void calculateMotorSpeed(double lf, double rf, double lb, double rb) {

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
        
        if(Double.isNaN(max)){
            System.out.println("HOLY MOTHER OF GOD IT HURTS");
        }

        if (max > 1) {
            lf /= max;
            rf /= max;
            lb /= max;
            rb /= max;
        }

        fl.set(lf);
        fr.set(-rf);
        bl.set(lb);
        br.set(-rb);
        System.out.println(lf);
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
