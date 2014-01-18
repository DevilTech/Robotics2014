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
<<<<<<< HEAD
import edu.wpi.first.wpilibj.can.CANTimeoutException;
=======
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
>>>>>>> f9c3cc28cf89c3f4f6b7a0b04d84defddb54d005

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends IterativeRobot {
<<<<<<< HEAD

    Encoder enX = new Encoder(1, 2);
    Encoder enY = new Encoder(3, 4);
    CANJaguar jaglf, jagrf, jaglb, jagrb;
    GY85_I2C sensor = new GY85_I2C();
    DriveSystem d;
    Joystick joy;

=======
   GY85_I2C g = new GY85_I2C();
   CANJaguar c = new CANJaguar();
   Encoder e = new Encoder();
   DriveSystem d;
   Joystick joy;
   Timer inputLoop = new Timer();
   double pastTime = 0;
>>>>>>> f9c3cc28cf89c3f4f6b7a0b04d84defddb54d005
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
<<<<<<< HEAD

=======
>>>>>>> f9c3cc28cf89c3f4f6b7a0b04d84defddb54d005
    public void autonomousPeriodic() {
    }

    /**
     * This function is called periodically during operator control
     */
<<<<<<< HEAD
    public void teleopInit() {
=======
    
    public void teleopInit(){
        inputLoop.start();
>>>>>>> f9c3cc28cf89c3f4f6b7a0b04d84defddb54d005
        d.driveSystemInit();
    }

    public void teleopPeriodic() {
        d.getInput();
<<<<<<< HEAD
=======
        SmartDashboard.putNumber("Input" ,((int) (1000 * (inputLoop.get()-pastTime))));
        pastTime = inputLoop.get();
    }
    
    public void disabledInit() {
        d.driveSystemDenit();
>>>>>>> f9c3cc28cf89c3f4f6b7a0b04d84defddb54d005
    }

    public void testPeriodic() {
    }
}
