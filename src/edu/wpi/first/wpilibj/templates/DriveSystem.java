package edu.wpi.first.wpilibj.templates;
////REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE
//import edu.wpi.first.wpilibj.Talon;
//import edu.wpi.first.wpilibj.Encoder;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import java.util.TimerTask;


import Test.Wiring;
import edu.wpi.first.wpilibj.Timer;

/**
 *
 * @author jeremy
 */
public class DriveSystem {

    Talon fr, fl, br, bl;
    GY85_I2C sen;
    Happystick control;
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
    double prevTime = 0;

    public DriveSystem(GY85_I2C sensor, Happystick control, Encoder encoderY, Encoder encoderX, int driveType) {
        fr = new Talon(Wiring.MOTOR_RF);
        fl = new Talon(Wiring.MOTOR_LF);
        br = new Talon(Wiring.MOTOR_RB);
        bl = new Talon(Wiring.MOTOR_LB);

        sen = sensor;

        this.control = control;

        enY = encoderY;
        enX = encoderX;

        this.driveType = driveType;
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
        timeTest();
        switch (driveType) {
            case 0:
                PID_Drive();
                break;
            case 1:
                openLoop();
                break;
            case 2:
                halfOpen();
                break;
        }
    }

    public void calculateInput() {
        theta = sen.getCompassRadAngle(initialHeading);
        if (control.getFCSwitch()) {
            FCMode = (FCMode && driveType == Wiring.OPEN_C) ? false : true;
        }
        if (control.getLoopSwitch()) {
            driveType = (driveType == Wiring.PID_C) ? Wiring.OPEN_C : Wiring.PID_C;
        }

        if (FCMode) {
            if (Math.abs(speedZ) > .01 && !getHat()) {
                heading = theta;
                errorInHeading = 0;
            } else {
                errorInHeading = DTlib.radianWrap(heading - theta) * Wiring.KiR;
                speedZ = 0;
            }

            //takes values from joysticks and changes the values to the correct
            //vector based on compass input
            double temp = speedY * Math.cos(theta) + speedX * Math.sin(theta);
            speedX = -speedY * Math.sin(theta) + speedX * Math.cos(theta);
            speedY = temp;
        } else {
            errorInHeading = 0;
        }
    }

    public void getJoy() {
        speedY = control.getForward() * Math.abs(control.getForward());
        speedX = control.getRight() * Math.abs(control.getRight());
        speedZ = control.getRotation() * Math.abs(control.getRotation());
    }

    public void setSpeed(double x, double y, double z, double r) {
        speedY = y;
        speedX = x;
        speedZ = z;
        heading = r;
    }

    public boolean getHat() {

        if (control.getHATLeft()) {
            heading = Math.PI / 2;
            return true;
        } else if (control.getHATRight()) {
            heading = -Math.PI / 2;
            return true;
        } else if (control.getHATUp()) {
            heading = 0;
            return true;
        } else if (control.getHATDown()) {
            heading = Math.PI;
            return true;
        } else {
            return false;
        }
    }
    
    public boolean getHooks(){
        if(control.getLeftHook()){
            calculateMotorSpeed(Wiring.CF_HOOK, Wiring.FF_HOOK, Wiring.CB_HOOK, Wiring.FB_HOOK);
            return true;
        }else if(control.getRightHook()){
            calculateMotorSpeed(Wiring.FF_HOOK, Wiring.CF_HOOK, Wiring.FB_HOOK, Wiring.CB_HOOK);
            return true;
        }else{
            return false;
        }
        
        //lf, rf, lb, rb
    }

    //stuff that does stuff (ha ha)
    public void PID_Drive() {

        GZ = sen.getGyroZ() * Wiring.G_SCALE;

        double VY = enY.getRate();
        double VX = enX.getRate();

        //checking for NaN
        //chief delphi said it can return NaN, Verifed(ish) by us
        if (Double.isNaN(VX) || Double.isNaN(VY)) {
            System.out.println("get a freaking bucket");
            return;
        }

        double AY = sen.getAccelY() / Wiring.A_SCALE;// expected V range +/- maxXY
        double AX = sen.getAccelX() / Wiring.A_SCALE;// expected A range +/- 28.6

        //adds to forwardY the amount in which we want to move in y direction in in/s
        //max velocity times amount requested (-1, 1), minus current speed
        //then, the derivative of the speed (acceleration) is added to to the value of forwardY
        forwardY = DTlib.clamp(forwardY);
        forwardY += Wiring.KpY * (Wiring.MAX_XY * speedY - VY);//PD expected range +/- 1.0

        rightX = DTlib.clamp(rightX);
        rightX += Wiring.KpX * (Wiring.MAX_XY * speedX - VX);	//PD expected range +/- 0.577

        clockwiseZ = (Wiring.KfR * speedZ) + (Wiring.KpR * (Wiring.MAX_R * speedZ + GZ));

        double tempCZ = clockwiseZ + errorInHeading;
        double tempFY = (Wiring.KfY * speedY) + forwardY - (Wiring.KdY * AY);
        double tempRX = (Wiring.KfX * speedX) + rightX - (Wiring.KdX * AX);

        double lf, rf, lb, rb;

        lf = tempFY + tempCZ * Wiring.CENTER_OF_ROTATION + tempRX;
        rf = tempFY - tempCZ * Wiring.CENTER_OF_ROTATION - tempRX;
        lb = tempFY + tempCZ - tempRX;
        rb = tempFY - tempCZ + tempRX;

        calculateMotorSpeed(lf, rf, lb, rb);
    }

    public void halfOpen() {

        clockwiseZ = Wiring.KpR * (Wiring.MAX_R * speedZ + GZ);

        double tempCZ = clockwiseZ + errorInHeading;
        double tempFY = speedY;
        double tempRX = speedX * .577;

        double lf, rf, lb, rb;

        lf = tempFY + tempCZ + tempRX;
        rf = tempFY - tempCZ - tempRX;
        lb = tempFY + tempCZ - tempRX;
        rb = tempFY - tempCZ + tempRX;

        calculateMotorSpeed(lf, rf, lb, rb);
    }

    public void openLoop() {

        double tempCZ = speedZ;
        double tempFY = speedY;
        double tempRX = speedX;

        double lf, rf, lb, rb;

        lf = tempFY + tempCZ + tempRX;
        rf = tempFY - tempCZ - tempRX;
        lb = tempFY + tempCZ - tempRX;
        rb = tempFY - tempCZ + tempRX;

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
    }
    
    public void timeTest(){
        double time = Timer.getFPGATimestamp();
        System.out.print(time-prevTime + "     ");
        prevTime=time;
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
