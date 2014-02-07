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

public class RobotTemplate extends IterativeRobot {

    Encoder enX = new Encoder(Wiring.ENCODER_X[0], Wiring.ENCODER_X[1]);
    Encoder enY = new Encoder(Wiring.ENCODER_Y[0], Wiring.ENCODER_Y[1]);
    Talon talonlf, talonrf, talonlb, talonrb;
    GY85_I2C sensor = new GY85_I2C();
    DriveSystem d;
    Gatherer g;
    Joystick joy;
    double previousValue = 0;
    Happystick control;
    boolean hasReached2;
    boolean hasReached3;
    boolean hasReached6;
    Compressor compressor;

    public void robotInit() {   
        setupEncoders();
        compressor = new Compressor(1,1);
        compressor.start();
        talonlf = new Talon(Wiring.MOTOR_LF);
        talonrf = new Talon(Wiring.MOTOR_RF);
        talonlb = new Talon(Wiring.MOTOR_LB);
        talonrb = new Talon(Wiring.MOTOR_RB);
        control = new Happystick(1, Control.getXbox());
        d = new DriveSystem(talonrf, talonlf, talonrb, talonlb, sensor, control, enY, enX, Wiring.OPEN_C);
        g = new Gatherer();
    }
    
    public void autonomousInit(){
        enY.reset();
        d.driveSystemInit();
    }

    public void autonomousPeriodic() {
        if(enY.getDistance() < 24 && !hasReached2){
            d.setSpeed(0,((24 - enY.getDistance())/48), 0, (45 * Math.PI / 180));
        }else if (!hasReached2){
            hasReached2 = !hasReached2;
        }else if (enX.getDistance() < 36 && !hasReached3){
            d.setSpeed(((36-enX.getDistance())/72),0,0,(45 * Math.PI / 180));
        }else if (!hasReached3){
            hasReached3 = !hasReached3;
        }else if (enX.getDistance() < 72 && !hasReached6){
            d.setSpeed(((-72+enX.getDistance())/144),0,0,(45 * Math.PI / 180));
        }else if (!hasReached6){
            hasReached6 = !hasReached6;
        }
        d.calculateInput();
    }
    
    public void teleopInit() {
        d.driveSystemInit();
    }
   
    public void teleopPeriodic() {
        d.getJoy();
        d.calculateInput();
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
    
    public void gathererButtonCheck(){
        if(control.gathExtend){
            g.up();
        }else{
            g.down();
        }
        
        if (control.gathSpinIn){
            g.startForward();
        }else if (control.gathSpinOut){
            g.startReverse();
        }else{
            g.stop();
    }
    }
    
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
        SmartDashboard.putNumber("CW", sensor.getCompassRadAngle());
        SmartDashboard.putNumber("aX", sensor.getAccelX());
        SmartDashboard.putNumber("aY", sensor.getAccelY());
        SmartDashboard.putNumber("GZ", d.GZ);
        SmartDashboard.putNumber("enX", enX.getRate());
        SmartDashboard.putNumber("enY", enY.getDistance());
        SmartDashboard.putNumber("errorH" , d.errorInHeading);
        /*
        SmartDashboard.putNumber("C", d.clockwiseZ);
        SmartDashboard.putNumber("R", d.rightX);
        SmartDashboard.putNumber("F", d.forwardY); //here
         */
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


