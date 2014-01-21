/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;

import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends IterativeRobot {

    Encoder enX = new Encoder(1, 2);
    Encoder enY = new Encoder(3, 4);
    CANJaguar jaglf, jagrf, jaglb, jagrb;
    GY85_I2C sensor = new GY85_I2C();
    DriveSystem d;
    Joystick joy;

    public void robotInit() {
        try {
            enX.start();
            enY.start();
            jaglf = new CANJaguar(2);
            jagrf = new CANJaguar(3);
            jaglb = new CANJaguar(12);
            jagrb = new CANJaguar(13);
            joy = new Joystick(1);
            d = new DriveSystem(jagrf, jaglf, jagrb, jaglb, sensor, joy, enY, enX, Wiring.THB_C);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }

    public void autonomousPeriodic() {
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopInit() {
        
        d.driveSystemInit();
    }

    public void teleopPeriodic() {
        d.getInput();
        smartPush();
        smartPull();   
        System.out.println(d.GZ);
    }

    public void disabledInit() {
        d.driveSystemDenit();
        smartInit();
    }
    public void disabled(){
        smartPush();
        smartPull();
    }

    public void testPeriodic() {
    }
    
     public void smartInit() {
        SmartDashboard.putNumber("CW", d.clockwiseZ);
        SmartDashboard.putNumber("GZ", d.GZ);
        SmartDashboard.putNumber("enX", enX.get());
        SmartDashboard.putNumber("enY", enY.get());
        SmartDashboard.putNumber("errorH" , d.errorInHeading);
        SmartDashboard.putNumber("C", d.clockwiseZ);
        SmartDashboard.putNumber("R", d.rightX);
        SmartDashboard.putNumber("F", d.forwardY); //here
        SmartDashboard.putNumber("heading", d.heading);
        SmartDashboard.putNumber("theta", d.theta);
        
        SmartDashboard.putNumber("kpR", Wiring.KpR);
        SmartDashboard.putNumber("kpR", Wiring.KpR);
        SmartDashboard.putNumber("kpX", Wiring.KpX);
        SmartDashboard.putNumber("kpY", Wiring.KpY);
        SmartDashboard.putNumber("kdX", Wiring.KdX);
        SmartDashboard.putNumber("kdY", Wiring.KdY);
        SmartDashboard.putNumber("KiR", Wiring.KiR);
    }
    public void smartPush(){
        SmartDashboard.putNumber("CW", d.clockwiseZ);
        SmartDashboard.putNumber("GZ", d.GZ);
        SmartDashboard.putNumber("enX", enX.get());
        SmartDashboard.putNumber("enY", enY.get());
        SmartDashboard.putNumber("errorH" , d.errorInHeading);
        SmartDashboard.putNumber("C", d.clockwiseZ);
        SmartDashboard.putNumber("R", d.rightX);
        SmartDashboard.putNumber("F", d.forwardY); //here
        SmartDashboard.putNumber("heading", d.heading);
        SmartDashboard.putNumber("theta", d.theta);
    }
    public void smartPull(){
        Wiring.KpR = SmartDashboard.getNumber("kpR");
        Wiring.KpX = SmartDashboard.getNumber("kpX");
        Wiring.KpY = SmartDashboard.getNumber("kpY");
        Wiring.KdX = SmartDashboard.getNumber("kdX");
        Wiring.KdY = SmartDashboard.getNumber("kdY");
        Wiring.KiR = SmartDashboard.getNumber("KiR");
    }
}
