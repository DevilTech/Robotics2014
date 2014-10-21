/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;

import java.lang.Object.*;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.image.BinaryImage;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends IterativeRobot {
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    AxisCamera camera;
    int redLow = 100; //h
    int redHigh = 255;
    int greenLow = 0; //s
    int greenHigh = 100;
    int blueLow = 215; //i
    int blueHigh = 255;
    double initArea;
    double autoArea;
    double firstTime;
    boolean flag = true;
    
    double fps;
    
    double firstTimeCycle;
    double lastTimeCycle;
    double timeTaken;
    
    DigitalOutput light2;
    
    ColorImage image;
    BinaryImage binImage;
    
    public void robotInit() {
        camera = AxisCamera.getInstance();
        light2 = new DigitalOutput(2);
        light2.set(true);
        firstTime = Timer.getFPGATimestamp();
    }

    /**
     * This function is called periodically during autonomous
     * Is this function truely periodically called? Perhaps we need to try calling Anonymous - Colin. :P
     */
    
    
    public void autonomousPeriodic() {
        firstTimeCycle = Timer.getFPGATimestamp(); 
        autoArea = capture();
           
        if(autoArea-initArea > 125)
        {
            light2.set(false);
        } else {
            light2.set(true);
        }
        lastTimeCycle = Timer.getFPGATimestamp();
        timeTaken = lastTimeCycle - firstTimeCycle;
        
        System.out.println("It took: " + timeTaken);
        
    }

    
    public void teleopInit()
    {        
        fps = camera.getMaxFPS();
    }
    public void teleopPeriodic() {
        System.out.println(fps);
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
    public void disabledPeriodic() {
        camera.writeMaxFPS(15);
            light2.set(true);
            if(Timer.getFPGATimestamp() - firstTime > 10  && flag)
            {
                initArea = capture();
                flag = false;
            }
        
        
        
    }
    
    public void disabledInit()
    {
       
    }

    public double capture(){
        double totalSize = 0;
        try {
            
            image = camera.getImage();
            binImage = image.thresholdHSL(redLow, redHigh, greenLow, greenHigh, blueLow, blueHigh);
            image.write("/initImage.jpg");
            
            ParticleAnalysisReport[] reports;
                   reports = binImage.getOrderedParticleAnalysisReports(150);
                   
                   
                   
                   for(int i = 0; i < reports.length; ++i)
                   {
                       if(reports[i].boundingRectTop > 30 && reports[i].boundingRectTop < 140){
                           totalSize += reports[i].particleArea;
                       }
                   }
                   
                   System.out.println("totalsize DURING INIT: " + totalSize + "       length: " + reports.length);
                   
                   image.free();
                   binImage.free();
                   
                   
        } catch (AxisCameraException ex) {
            ex.printStackTrace();
        } catch (NIVisionException ex) {
            ex.printStackTrace();
        }
    return totalSize;
    }
    }
    
    
    

