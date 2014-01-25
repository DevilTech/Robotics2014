/* |-----------------------------------------------------|
 * | Written by Cody                                     |
 * | This class pulls circles from the smartdashboard    |
 * | that get sent through the network tables by RR      |
 * |                                                     |
 * |    RR = RoboRealm                                   |
 * |                                                     |
 * | ***IF YOU NEED TO ADJUST VALUES:                    |
 * |    Adjust the values in Roborealm - using the file: |
 * |        LED sensors.robo                             |
 * |                                                     |
 * | ***MAIN PARTS TO ROBO LED RECOGNITION               |
 * |    1. Axis Camera expected @ 10.15.59.11            |
 * |    2. Crop the image to look at                     |
 * |    3. Apply an intensity filter: 244-255            |
 * |    4. Movement Threshold level 42                   |
 * |    5. (DEBUGGING) Display movement_pixels on image  |
 * |    5. Network tables: Send value movement           |
 * |        Prefix: /SmartDashboard/                     |
 * |        Host: 10.15.59.2 (cRIO)                      |
 * |        Port: 1735                                   |
 * |        Variable name: MOVEMENT_PIXELS               |
 * |                                                     |
 * |    Isn't this a FANTASTIC multi-line comment?       |
 * |-----------------------------------------------------|
 */
package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class RobotTemplate extends IterativeRobot {
    
    int minC = 0;
    int maxC = 0;
    DigitalOutput led;
    
    int minM = 0;
    int maxM = 0;
    
    public void robotInit() {
        led = new DigitalOutput(7);
        led.set(true);
        System.out.println("Ready: Robot Init");
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousInit() {
        System.out.println("Ready: Autonomous");
    }
    
    public void autonomousPeriodic() {
        
    }

    public void teleopInit() {
        String success = "LED's have been detected!";
        String failure = "NO LED's detected.";
        //FIXED VALUES!
        
        
    }
    
    public void teleopPeriodic() {
        int numCircles = SmartDashboard.getInt("MOVEMENT_PIXELS");
        minM = 150;
        maxM = 300;
        
        if((numCircles > minM) && (numCircles < maxM)) {
            led.set(false);
            System.out.println("LED's On");
        } else if(numCircles > maxM) {
            led.set(true);
            System.out.println("Too much movement");
        } else if(numCircles < minM) {
            led.set(true);
            System.out.println("Too little movement!");
        } else {
            led.set(true);
            System.out.println("Logic gates failed -- Exception");
        }
        
    }
    
    public void disabled() {
        System.out.println("Ready: Disabled");
    }
    
    public void testPeriodic() {
        System.out.println("Ready: Test");
    }
    
}
