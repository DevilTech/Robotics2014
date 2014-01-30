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
import edu.wpi.first.wpilibj.Joystick;

import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class RobotTemplate extends IterativeRobot {

    Encoder enX = new Encoder(1, 2);
    Encoder enY = new Encoder(3, 4);
    Talon talonlf, talonrf, talonlb, talonrb;
    GY85_I2C sensor = new GY85_I2C();
    DriveSystem d;
    Joystick joy;
    double previousValue = 0;

    public void robotInit() {   
        setupEncoders();
        talonlf = new Talon(Wiring.MOTOR_LF);
        talonrf = new Talon(Wiring.MOTOR_RF);
        talonlb = new Talon(Wiring.MOTOR_LB);
        talonrb = new Talon(Wiring.MOTOR_RB);
        joy = new Joystick(Wiring.PILOT_JOY);
        d = new DriveSystem(talonrf, talonlf, talonrb, talonlb, sensor, joy, enY, enX, Wiring.PID_C);
    }

    public void autonomousPeriodic() { }

    public void teleopInit() {
        d.driveSystemInit();
    }

    public void teleopPeriodic() {
        d.getInput();
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

    public void testPeriodic() { }
    
    public void smartInit() {
        smartPush();
        
        SmartDashboard.putNumber("dt", Wiring.dt);
        SmartDashboard.putNumber("kpR", Wiring.KpR);
        SmartDashboard.putNumber("kpR", Wiring.KpR);
        SmartDashboard.putNumber("kpX", Wiring.KpX*100);
        SmartDashboard.putNumber("kpY", Wiring.KpY*100);
        SmartDashboard.putNumber("kdX", Wiring.KdX);
        SmartDashboard.putNumber("kdY", Wiring.KdY);
        SmartDashboard.putNumber("KiR", Wiring.KiR);
    }

    public void smartPush() {
        SmartDashboard.putNumber("CW",  sensor.getCompassRadAngle());
        SmartDashboard.putNumber("aX", sensor.getAccelX());
        SmartDashboard.putNumber("aY", sensor.getAccelY());
        SmartDashboard.putNumber("GZ", d.GZ);
        SmartDashboard.putNumber("enX", enX.getRate());
        SmartDashboard.putNumber("enY", enY.getRate());
        SmartDashboard.putNumber("errorH" , d.errorInHeading);
        SmartDashboard.putNumber("C", d.clockwiseZ);
        SmartDashboard.putNumber("R", d.rightX);
        SmartDashboard.putNumber("F", d.forwardY); //here
        SmartDashboard.putNumber("heading", d.heading);
        SmartDashboard.putNumber("theta", d.theta);
    }

    public void smartPull() {
        Wiring.dt  = SmartDashboard.getNumber("dt");
        Wiring.KpR = SmartDashboard.getNumber("kpR");
        Wiring.KpX = SmartDashboard.getNumber("kpX")/100;
        Wiring.KpY = SmartDashboard.getNumber("kpY")/100;
        Wiring.KdX = SmartDashboard.getNumber("kdX");
        Wiring.KdY = SmartDashboard.getNumber("kdY");
        Wiring.KiR = SmartDashboard.getNumber("KiR");
    }
    
    public void setupEncoders(){
        enX.start();
        enY.start();
        enX.setDistancePerPulse(2.75 * Math.PI / 90);
        enY.setDistancePerPulse(2.75 * Math.PI / 90);
    }
}
