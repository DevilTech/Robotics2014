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
public class Gatherer {

    Solenoid armLeftUp;
    Solenoid armLeftDown;
    Solenoid armRightUp;
    Solenoid armRightDown;
    Talon armMotorL;
    Talon armMotorR;

    public Gatherer() 
    {
        armMotorL = new Talon(Wiring.MOTOR_GATHERER_LEFT);
        armMotorR = new Talon(Wiring.MOTOR_GATHERER_RIGHT);
        armLeftUp = new Solenoid(Wiring.SOLENOID_GATHERER_LEFT_IN);
        armLeftDown = new Solenoid(Wiring.SOLENOID_GATHERER_LEFT_OUT);
        armRightUp = new Solenoid(Wiring.SOLENOID_GATHERER_RIGHT_IN);
        armRightDown = new Solenoid(Wiring.SOLENOID_GATHERER_RIGHT_OUT);
        armLeftUp.set(true);
        armRightUp.set(true);
        armLeftDown.set(false);
        armRightDown.set(false);
    }

    public void up() //Lifts arms of gatherer up
    {
        armLeftUp.set(true);
        armRightUp.set(true);
        armLeftDown.set(false);
        armRightDown.set(false);
    }
    public void down() //Lowers arms of gatherer
    {
        armLeftUp.set(false);
        armRightUp.set(false);
        armLeftDown.set(true);
        armRightDown.set(true);
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