/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Solenoid;


/**
 *
 * @author Nate Baker
 */
public class GATHERER 
{
    Solenoid armUp;
    Solenoid armDown;
    Jaguar armMotorL;
    Jaguar armMotorR;
    public GATHERER()
    {
        armMotorL = new Jaguar(WIRING.gathererMotorL);
        armMotorR = new Jaguar(WIRING.gathererMotorR);
        armUp = new Solenoid(WIRING.gathererUp);
        armDown = new Solenoid(WIRING.gathererDown);
        armUp.set(true);
        armDown.set(false);
    }
    public void gathererUp() //Lifts arms of gatherer up
    {
        armUp.set(true);
        armDown.set(false);
    }
    public void gathererDown() //Lowers arms of gatherer
    {
        armUp.set(false);
        armDown.set(true);
    }
    public void gathererStartForward() //Starts the motors of gatherer forward
    {
        armMotorL.set(WIRING.gathererSpeedForward);
        armMotorR.set(WIRING.gathererSpeedForward);
    }
    public void gathererStop() //Stops the motors of gatherer
    {
        armMotorL.set(0);
        armMotorR.set(0);
    }
    public void gathererStartReverse() //Starts the motors of gatherer in reverse
    {
        armMotorL.set(WIRING.gathererSpeedReverse);
        armMotorR.set(WIRING.gathererSpeedReverse);
        
    }
}
