package edu.wpi.first.wpilibj.templates;
////REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE
//import edu.wpi.first.wpilibj.Talon;
//import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.TimerTask;
import java.util.Timer;

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
    double joyY;
    double joyX;
    double joyZ;
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
    }

    public void driveSystemInit() {
        if(!hasBeenStarted){
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
        else{
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
            case 0: PID_Drive(); break;
            case 1: THB_Drive(); break;
            case 2: openLoop(); break;
        }
    }

    public void getInput() {
        
        theta = sen.getCompassRadAngle(initialHeading);
        joyY = -joy.getY() * Math.abs(joy.getY());
        joyX = joy.getX() * Math.abs(joy.getX());
        joyZ = joy.getZ() * Math.abs(joy.getZ()) / 2;

        if (FCMode) {
            if (Math.abs(joy.getZ()) > .01) {
                heading = theta;
                errorInHeading = 0;
            } else {
                errorInHeading = DTlib.radianWrap(heading - theta) * Wiring.KiR;
            }
            //takes values from joysticks and changes the values to the correct
            //vector based on compass input
            double temp = joyY * Math.cos(theta) + joyX * Math.sin(theta);
            joyX = -joyY * Math.sin(theta) + joyX * Math.cos(theta);
            joyY = temp;
        } else {
            errorInHeading = 0;
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
        forwardY = 1;//Wiring.KpY * (Wiring.MAX_XY * joyY - VY);//PD expected range +/- 1.0
        rightX = DTlib.clamp(rightX);
        //rightX += Wiring.KpX * (Wiring.MAX_XY * joyX - VX);	//PD expected range +/- 0.577
        clockwiseZ = DTlib.clamp(clockwiseZ);
       // clockwiseZ += Wiring.KpR * (joyZ + GZ); //replace 0 with KpR
        
        double tempCZ = clockwiseZ + errorInHeading;
        double tempFY = forwardY - Wiring.KdY * AY;
        double tempRX = rightX - Wiring.KdX * AX;
        
        double lf, rf, lb, rb;

        lf = tempFY + tempCZ * .36 + tempRX;
        rf = tempFY - tempCZ * .36 - tempRX;
        lb = tempFY + tempCZ - tempRX;
        rb = tempFY - tempCZ + tempRX;

        calculateMotorSpeed(lf,rf,lb,rb);
        
    }

    public void openLoop() {

        double lf, rf, lb, rb;

        lf = joyY + joyZ + joyX;
        rf = joyY - joyZ - joyX;
        lb = joyY + joyZ - joyX;
        rb = joyY - joyZ + joyX;

        calculateMotorSpeed(lf,rf,lb,rb);
        
    }
    
    public void calculateMotorSpeed(double lf, double rf, double lb, double rb){
       
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
    
     //probably not needed
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
        
        forwardY = DTlib.clamp(forwardY);
        rightX = DTlib.clamp(rightX);
        
        if(!DTlib.isSameSign(previousErrorY, IY)){
            forwardY = 0.5 * (forwardY + TbY);
            TbY = forwardY;
            previousErrorY = IY;
        }
        if(!DTlib.isSameSign(previousErrorX, IX)){
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

        calculateMotorSpeed(lf,rf,lb,rb);
    }
}
