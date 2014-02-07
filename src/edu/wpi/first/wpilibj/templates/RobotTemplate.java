/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Joystick;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends IterativeRobot 
{
    Compressor compressor;
    GATHERER gatherer;
    Joystick joystick;
    boolean flag = true;
    boolean flag1 = true;
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() 
    {
        compressor = new Compressor(1,1);
        compressor.start();
        gatherer = new GATHERER();
        joystick = new Joystick(WIRING.joy1);
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousInit()
    {

    }
    public void autonomousPeriodic() 
    {

    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopInit()
    {
        
    }
    public void teleopPeriodic() 
    {
       if(joystick.getRawButton(6))
       {
           gatherer.gathererStartForward();
       }
       if(joystick.getRawButton(5))
       {
           gatherer.gathererStartReverse();
       }
       if(!joystick.getRawButton(6) && !joystick.getRawButton(5))
       {
           gatherer.gathererStop();
       }
       if(joystick.getRawButton(1))
       {
           if(flag)
           {
               if(flag1)
               {
                   gatherer.gathererUp();
                   flag1 = false;
               }
               else
               {
                   gatherer.gathererDown();
                   flag1 = true;
               }
               flag = false;
           }
       }
       else
       {
           flag = true;
       }
    }
    
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() 
    {
    
    }
    public void disabledInit()
    {
        
    }
    
}
