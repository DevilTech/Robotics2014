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
            jaglf = new CANJaguar(2);
            jagrf = new CANJaguar(3);
            jaglb = new CANJaguar(12);
            jagrb = new CANJaguar(13);
            joy = new Joystick(1);
            d = new DriveSystem(jagrf, jaglf, jagrb, jaglb, sensor, joy, enY, enX, 1);
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
    }

    public void testPeriodic() {
    }
}
