
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import Competition.Wiring;
import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Shooter {

    Counter upCounter;
    Counter downCounter;
    Counter tensionedCounter;
    Counter deTensionedCounter;
    DigitalInput up;
    DigitalInput down;
    DigitalInput tensioned;
    DigitalInput deTensioned;
    int state = 0;
    Piston middlePiston;
    Piston shoot;
    Piston outerPistons;
    AnalogChannel ball;
    DigitalInput middlePistonLimit;
    DigitalInput limitDown;
    double distance;
    int counter = 0;
    boolean readyToShoot = false;
    boolean hasShot = false;
    boolean isDown = false;
    boolean shooting = false;
    boolean popTrig = false;

    public Shooter() {
        shoot = new Piston(Wiring.SOLENOID_SHOOTER_SHOOT_OUT, Wiring.SOLENOID_SHOOTER_SHOOT_IN);
        outerPistons = new Piston(Wiring.SOLENOID_SHOOTER_TENSION_IN, Wiring.SOLENOID_SHOOTER_TENSION_OUT);
        middlePiston = new Piston(Wiring.SOLENOID_SHOOTER_PRETENSION_OUT, Wiring.SOLENOID_SHOOTER_PRETENSION_IN);
        deTensioned = new DigitalInput(Wiring.LIMIT_SHOOTER_DETENSIONED);
        ball = new AnalogChannel(Wiring.OPTICAL_SHOOTER_BALL_SENSOR);
        middlePistonLimit = new DigitalInput(Wiring.LIMIT_SHOOTER_MIDDLE_PISTON);
        tensionedCounter = new Counter(Wiring.LIMIT_SHOOTER_TENSIONED);
        limitDown =  new DigitalInput(Wiring.LIMIT_SHOOTER_DOWN);
        tensionedCounter.start();
        tensionedCounter.reset();
        
    }

    public void initalize() {
        readyToShoot = false;
        isDown = false;
        tensionedCounter.reset();
        SmartDashboard.putBoolean("TENSIONING", false);
        SmartDashboard.putBoolean("READY", false);
        SmartDashboard.putBoolean("DOWN", false);

    }
    public void override(){
        middlePiston.retract();
        shoot.extend();
        readyToShoot = false;
        isDown = false;
    }
    
    
    public void cock(){
        if(!readyToShoot){
            if(!limitDown.get()){
                middlePiston.extend();
                outerPistons.retract();
                shoot.retract();
                tensionedCounter.reset();
                SmartDashboard.putBoolean("DOWN", false);
                SmartDashboard.putBoolean("TENSIONING", false);
            }else{
                SmartDashboard.putBoolean("DOWN", true);
                SmartDashboard.putBoolean("TENSIONING", true);
                if (tensionedCounter.get() == 0) {
                    outerPistons.extend();
                    SmartDashboard.putBoolean("TENSIONING", true);
                }else{
                    readyToShoot = true;
                    SmartDashboard.putBoolean("TENSIONING", false);
                    SmartDashboard.putBoolean("READY", true);
                }
            }
        }
    }
    public void cockOld() {
        if (!readyToShoot) {
            if (!isDown) {
                middlePiston.extend();
                outerPistons.retract();
                shoot.retract();
                tensionedCounter.reset();
                if (!middlePistonLimit.get()) {
                    isDown = true;
                    SmartDashboard.putBoolean("DOWN", true);
                    //System.out.println(shooterPistonLimit.get());
                }
                
                SmartDashboard.putBoolean("TENSIONING", true);
                //System.out.println("readying " + shooterPistonLimit.get());
            } else {
                if (tensionedCounter.get() == 0) {
                    outerPistons.extend();
                    SmartDashboard.putBoolean("TENSIONING", true);
                    //System.out.println("tensionin");
                } else {
                    readyToShoot = true;
                    SmartDashboard.putBoolean("TENSIONING", false);
                    SmartDashboard.putBoolean("READY", true);
                    //System.out.println("ready");
                }
            }
        }
    }

    public void shoot() {

        //if (isDown ){&& (ball.getVoltage() > Wiring.C_HAS_BALL)) {
            middlePiston.retract();
            shoot.extend();
            SmartDashboard.putBoolean("TENSIONING", false);
            SmartDashboard.putBoolean("READY", false);
            SmartDashboard.putBoolean("DOWN", false);
            //System.out.println("shoot");
            readyToShoot = false;
            System.out.println("shot");
    }

    public void reset() {
        outerPistons.retract();
        middlePiston.retract();
        shoot.retract();
        isDown = false;
        readyToShoot = false;
        SmartDashboard.putBoolean("TENSIONING", false);
        SmartDashboard.putBoolean("READY", false);
        SmartDashboard.putBoolean("DOWN", false);
        //System.out.println("Making Shooter Safe");
    }

    public void popShot() {
        
            if (!deTensioned.get()) {
                shoot.extend();
                middlePiston.retract();
                readyToShoot = false;
                popTrig = true;
                isDown = false;
                SmartDashboard.putBoolean("TENSIONING", false);
                SmartDashboard.putBoolean("DOWN", false);
            } else if (deTensioned.get()) {
                outerPistons.retract();
                
                SmartDashboard.putBoolean("READY", false);
                //System.out.println("pop");
                readyToShoot = false;
            }
        
    }
    public void load(){
        middlePiston.extend();
        outerPistons.retract();
        shoot.retract();
        tensionedCounter.reset();
        if (!middlePistonLimit.get()) {
            isDown = true;
            SmartDashboard.putBoolean("DOWN", true);
        }
    }
    
    public void tension(){
        middlePiston.extend();
        outerPistons.extend();
        shoot.retract();
        if (tensionedCounter.get() == 1) {
            readyToShoot = true;
            SmartDashboard.putBoolean("TENSIONING", false);
            SmartDashboard.putBoolean("READY", true);  
        }
    }
}
