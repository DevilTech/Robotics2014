/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import Competition.Wiring;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;


/**
 *
 * @author Nate Baker
 */
public class Gatherer{
    Solenoid armUp;
    Solenoid armDown;
    Talon armMotorL;
    Talon armMotorR;
    public Gatherer()
    {
        armMotorL = new Talon(Wiring.MOTOR_GATHERER_LEFT);
        armMotorR = new Talon(Wiring.MOTOR_GATHERER_RIGHT);
        armUp = new Solenoid(Wiring.SOLENOID_GATHERER_IN);
        armDown = new Solenoid(Wiring.SOLENOID_GATHERER_OUT);
        armUp.set(true);
        armDown.set(false);
    }
    public void up() //Lifts arms of gatherer up
    {
        armUp.set(true);
        armDown.set(false);
    }
    public void down() //Lowers arms of gatherer
    {
        armUp.set(false);
        armDown.set(true);
    }
    public void startIn() //Starts the motors of gatherer forward
    {
        armMotorL.set(Wiring.GATHERER_SPEED_FORWARD);
        armMotorR.set(Wiring.GATHERER_SPEED_FORWARD);
    }
    public void stop() //Stops the motors of gatherer
    {
        armMotorL.set(0);
        armMotorR.set(0);
    }
    public void startOut() //Starts the motors of gatherer in reverse
    {
        armMotorL.set(Wiring.GATHERER_SPEED_REVERSE);
        armMotorR.set(Wiring.GATHERER_SPEED_REVERSE);
        
    }
}
