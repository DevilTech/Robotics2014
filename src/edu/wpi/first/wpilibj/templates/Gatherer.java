
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import Competition.Wiring;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;

/**
 *
 * @author Nate Baker
 */
public class Gatherer {



    Relay arm;

    Talon armMotorL;
    Talon armMotorR;

    public Gatherer() {
        armMotorL = new Talon(Wiring.MOTOR_GATHERER_LEFT);
        armMotorR = new Talon(Wiring.MOTOR_GATHERER_RIGHT);
        arm = new Relay(Wiring.RELAY_GATHERER);
        arm.set(Relay.Value.kOff);
    }

    public void up() //Lifts arms of gatherer up
    {
        arm.set(Relay.Value.kForward);
    }

    public void down() //Lowers arms of gatherer
    {
        arm.set(Relay.Value.kReverse);
    }

    public void startMotorsGather() //Starts the motors of gatherer in
    {
        armMotorL.set(-Wiring.GATHERER_SPEED_FORWARD);
        armMotorR.set(Wiring.GATHERER_SPEED_FORWARD);
    }

    public void stop() //Stops the motors of gatherer
    {
        armMotorL.set(0);
        armMotorR.set(0);
    }

    public void startMotorsReverse() //Starts the motors of gatherer in reverse
    {
        armMotorL.set(-Wiring.GATHERER_SPEED_REVERSE);
        armMotorR.set(Wiring.GATHERER_SPEED_REVERSE);

    }
    
    public void gather(){
        startMotorsGather();
        down();
    }
    
    public void pullUp(){
        startMotorsGather();
        up();   
    }
    
    public void reverse(){
        startMotorsReverse();
        down();
    }
    
    public void rest(){
        stop();
        up();
    }
}