/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends IterativeRobot {
   GY85_I2C g = new GY85_I2C();
   CANJaguar c = new CANJaguar();
   Encoder e = new Encoder();
   DriveSystem d;
   Joystick joy;
    public void robotInit() {
        joy = new Joystick(1);
        d = new DriveSystem(c,c,c,c,g,joy,e,e,1);
    }

   
    public void autonomousPeriodic() {

    }

    /**
     * This function is called periodically during operator control
     */
    
    public void teleopInit(){
        d.driveSystemInit();
    }
    
    public void teleopPeriodic() {
        
    }
    
   
    public void testPeriodic() {
    
    }
    
}
