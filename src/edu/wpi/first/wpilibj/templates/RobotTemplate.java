
/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import Competition.Wiring;
import Control.Control;
import edu.wpi.first.wpilibj.Timer;

public class RobotTemplate extends IterativeRobot {

    Encoder enX = new Encoder(Wiring.ENCODER_X[0], Wiring.ENCODER_X[1]);
    Encoder enY = new Encoder(Wiring.ENCODER_Y[0], Wiring.ENCODER_Y[1]);
    GY85_I2C sensor = new GY85_I2C();
    DriveSystem d;
    Gatherer g;
    Joystick joy;
    double previousValue = 0;
    static Happystick driver;
    static Happystick coPilot;
    boolean hasReached2;
    boolean hasReached3;
    boolean hasReached6;
    Compressor compressor;
    double prevTime = 0;
    Shooter shooter;
    boolean shootonce;
    DefensiveArm arm;
    static boolean gathererDown = false;
    static boolean gathererReversed = false;
    int gathCount = 0;
    boolean hasFired = false;
    int state = 0;

    public void robotInit() {
        setupEncoders();
        driver = new Happystick(1, Control.getXbox());
        coPilot = new Happystick(2, Control.getXbox());
        d = new DriveSystem(sensor, driver, enY, enX, Wiring.OPEN_C);
        d.FCMode = false;
        joy = new Joystick(1);
        if (!Wiring.isTest) {
            compressor = new Compressor(Wiring.COMPRESSOR_PRESSURE_SWITCH, Wiring.COMPRESSOR_RELAY);
            compressor.start();
            g = new Gatherer();
            shooter = new Shooter();
        }

    }

    public void autonomousInit() {
        enY.reset();
        d.driveSystemInit();
    }

    public void autonomousPeriodic() {
        autoOffense();
    }
    /*
     cam.setAngle(90.0);
     if(cam.getHotGoal()) {
     //shoot stuff
     //move forward
     } else { loopCounter++; }
     if(loopCounter >= 250) {
     System.out.println("Oh no! No hot goal detected.");
     //just move the robot forward
     }
     */

    public void autoOffense() {
        switch (state) {
            case 0:
                gathererButtonCheck(true, false);
                shooter.readyToShoot = true;
                d.setSpeed(0, ((24 - enY.getDistance()) / 48), 0, 0);
                if (enY.getDistance() > 24) {
                    state = 1;
                }
                break;
            case 1:

                shooter.shootThings(true);
                state = 2;
                break;
            case 2:
                shooter.shootThings(false);
                d.setSpeed(0, ((48 - enY.getDistance()) / 48), 0, 0);
                if (enY.getDistance() > 48) {
                    state = 3;
                }
                break;
            case 3:
                gathererButtonCheck(false, false);
                d.setSpeed(0, 0, 0, 0);
                shooter.shootThings(false);
                break;
        }
    }

    public void autoDefence() {
        if (enY.getDistance() < 24 && !hasReached2) {
            d.setSpeed(0, ((24 - enY.getDistance()) / 48), 0, (45 * Math.PI / 180));
        } else if (!hasReached2) {
            hasReached2 = !hasReached2;
        } else if (enX.getDistance() < 36 && !hasReached3) {
            d.setSpeed(((36 - enX.getDistance()) / 72), 0, 0, (45 * Math.PI / 180));
        } else if (!hasReached3) {
            hasReached3 = !hasReached3;
        } else if (enX.getDistance() < 72 && !hasReached6) {
            d.setSpeed(((-72 + enX.getDistance()) / 144), 0, 0, (45 * Math.PI / 180));
        } else if (!hasReached6) {
            hasReached6 = !hasReached6;
        }
        d.calculateInput();
    }

    public void teleopInit() {
        d.driveSystemInit();
        shootonce = true;
        shooter.initalize();
    }

    public void teleopPeriodic() {
        d.getJoy();
        d.calculateInput();
        gathererButtonCheck(driver.getGather(), driver.getReverseGather());
        //System.out.println("this makes it work");
        shooter.shootThings(driver.getShoot() && gathererDown);
        shooter.popShot(driver.getPop());
        smartPush();
        smartPull();
    }

    public void disabledInit() {
        d.driveSystemDenit();
        smartInit();
        state = 0;
    }

    public void disabledPeriodic() {
        sensor.readAll();
        smartPush();
        smartPull();
    }

    public void testPeriodic() {
        gathererButtonCheck(driver.getGather(), driver.getReverseGather());
        if (joy.getRawButton(1)) {
            shooter.middlePiston.extend();
        } else {
            shooter.middlePiston.retract();
        }
        if (joy.getRawButton(2)) {
            shooter.shoot.extend();
        } else {
            shooter.shoot.retract();
        }
        if (joy.getRawButton(3)) {
            shooter.outerPistons.extend();
        } else {
            shooter.outerPistons.retract();
        }
        System.out.println(shooter.ball.getVoltage());

    }

    public void gathererButtonCheck(boolean gath, boolean rev) {
        if (gath) {
            g.gather();
            gathererDown = true;
            gathererReversed = false;
            gathCount = 0;
        } else if (rev) {
            g.reverse();
            gathererDown = false;
            gathererReversed = true;
        } else {
            if (gathCount <= 100) {
                g.pullUp();
                gathCount++;
            } else {
                g.rest();
            }
            gathererDown = false;
            gathererReversed = false;
        }
    }

    public void defenseCheck() {
        if (coPilot.getArmRaise()) {
            arm.goUp();
        } else {
            arm.goDown();
        }

    }

    public void smartInit() {
        smartPush();

        SmartDashboard.putNumber("dt", Wiring.dt);
        SmartDashboard.putNumber("kpR", Wiring.KpR);
        SmartDashboard.putNumber("kpR", Wiring.KpR);
        SmartDashboard.putNumber("kpX", Wiring.KpX * 100);
        SmartDashboard.putNumber("kpY", Wiring.KpY * 100);
        SmartDashboard.putNumber("kdX", Wiring.KdX);
        SmartDashboard.putNumber("kdY", Wiring.KdY);
        SmartDashboard.putNumber("KiR", Wiring.KiR);
    }

    public void smartPush() {
        SmartDashboard.putNumber("CW", sensor.getCompassRadAngle());
        SmartDashboard.putNumber("aX", sensor.getAccelX());
        SmartDashboard.putNumber("aY", sensor.getAccelY());
        SmartDashboard.putNumber("GZ", d.GZ);
        SmartDashboard.putNumber("enX", enX.getDistance());
        SmartDashboard.putNumber("enY", enY.getDistance());
        SmartDashboard.putNumber("errorH", d.errorInHeading);
        /*
         SmartDashboard.putNumber("C", d.clockwiseZ);
         SmartDashboard.putNumber("R", d.rightX);
         SmartDashboard.putNumber("F", d.forwardY); //here
         */
        SmartDashboard.putNumber("heading", d.heading);
        SmartDashboard.putNumber("theta", d.theta);
    }

    public void smartPull() {
        Wiring.dt = SmartDashboard.getNumber("dt");
        Wiring.KpR = SmartDashboard.getNumber("kpR");
        Wiring.KpX = SmartDashboard.getNumber("kpX") / 100;
        Wiring.KpY = SmartDashboard.getNumber("kpY") / 100;
        Wiring.KdX = SmartDashboard.getNumber("kdX");
        Wiring.KdY = SmartDashboard.getNumber("kdY");
        Wiring.KiR = SmartDashboard.getNumber("KiR");
    }

    public void timeTest() {
        double time = Timer.getFPGATimestamp();
        System.out.println(time - prevTime);
        prevTime = time;
    }

    public void setupEncoders() {
        enX.start();
        enY.start();
        enX.setDistancePerPulse(2.75 * Math.PI / 90);
        enY.setDistancePerPulse(2.75 * Math.PI / 90);
    }
}
