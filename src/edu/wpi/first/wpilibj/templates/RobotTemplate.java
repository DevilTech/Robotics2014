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
    Happystick driver;
    Happystick coPilot;
    boolean hasReached2;
    boolean hasReached3;
    boolean hasReached6;
    Compressor compressor;
    double prevTime = 0;
    Shooter shooter;
    boolean shootonce;
    DefensiveArm arm;

    public void robotInit() {
        setupEncoders();
        if (!Wiring.isTest) {
            compressor = new Compressor(Wiring.COMPRESSOR_PRESSURE_SWITCH, Wiring.COMPRESSOR_RELAY);
            compressor.start();
            g = new Gatherer();
           // shooter = new Shooter();
        }
        driver = new Happystick(1, Control.getXbox());
        coPilot = new Happystick(2, Control.getXbox());
        d = new DriveSystem(sensor, driver, enY, enX, Wiring.OPEN_C);
    }

    public void autonomousInit() {
        enY.reset();
        d.driveSystemInit();
    }

    public void autonomousPeriodic() {
    }

    public void autoOffense() {
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
    }

    public void teleopPeriodic() {
        d.getJoy();
        d.calculateInput();
        gathererButtonCheck();
        //shooterButtonCheck();
        smartPush();
        smartPull();
    }

    public void disabledInit() {
        d.driveSystemDenit();
        smartInit();
    }

    public void disabledPeriodic() {
        sensor.readAll();
        smartPush();
        smartPull();
    }

    public void testPeriodic() {
    }

    public void gathererButtonCheck() {
        if (driver.getGather()) {
            g.down();
            g.startIn();
        } else if (driver.getReverseGather()) {
            g.up();
            g.startOut();
        } else {
            g.up();
            g.stop();
        }
    }

    public void shooterButtonCheck() {
        if (driver.getShoot()) {
            shooter.shoot();
        }
        shooter.keepCocked();
    }

    
    public void defenseCheck() {
        if (coPilot.getArmRaise()) {
            if (arm.isUp) {
                arm.goDown();
            } else {
                arm.goUp();
            }
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
