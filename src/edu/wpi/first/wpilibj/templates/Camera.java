/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import Competition.Wiring;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Camera {

    Servo servo;
    double lowMotion;
    double highMotion;
    double motionPercent;
    double ballAngle;
    double cameraShoot;

    public Camera() {
        servo = new Servo(Wiring.CAMERA_SERVO);
        lowMotion = 50.0;
        highMotion = 75.0;
        motionPercent = 0.0;
    }

    
    public void shootAngle() {
        servo.setAngle(cameraShoot);
    }
    
    public void setAngle(double angle) {
        
        if((angle < 0.0) || (angle > 180)){
            return;
        } else {
        servo.setAngle(angle);
        }
    }

    public double getAngleToBall() {
        ballAngle = SmartDashboard.getNumber("ANGLE_TO_BALL");
        return ballAngle;
    }

    public void setResolution(String res) {
        if (res.equals("160x120")) {
            AxisCamera.getInstance().writeResolution(AxisCamera.ResolutionT.k160x120);
        } else if (res.equals("320x240")) {
            AxisCamera.getInstance().writeResolution(AxisCamera.ResolutionT.k320x240);
        } else if (res.equals("640x480")) {
            AxisCamera.getInstance().writeResolution(AxisCamera.ResolutionT.k640x480);
        } else {
            System.out.println("Unsupported Resolution: " + res);
        }
    }

    public boolean getHotGoal() {
        if ((motionPercent < highMotion) && (motionPercent < lowMotion)) {
            return true;
        } else if ((motionPercent > highMotion) || (motionPercent > lowMotion)) {
            System.out.println("Outside Thershold!!!!!!!!!");
            return false;
        } else {
            return false;
        }
    }
}