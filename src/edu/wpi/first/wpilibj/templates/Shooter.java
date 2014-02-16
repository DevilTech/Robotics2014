/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import Competition.Wiring;
import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.DigitalInput;

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
    AnalogChannel optical;
    double distance;
    int counter = 0;

    public Shooter() {
        middlePiston = new Piston(Wiring.SOLENOID_SHOOTER_PRETENSION_OUT, Wiring.SOLENOID_SHOOTER_PRETENSION_IN);
        shoot = new Piston(Wiring.SOLENOID_SHOOTER_SHOOT_OUT, Wiring.SOLENOID_SHOOTER_SHOOT_IN);
        outerPistons = new Piston(Wiring.SOLENOID_SHOOTER_TENSION_OUT, Wiring.SOLENOID_SHOOTER_TENSION_IN);
        tensioned = new DigitalInput(Wiring.LIMIT_SHOOTER_TENSIONED);
        up = new DigitalInput(Wiring.LIMIT_SHOOTER_UP);
        down = new DigitalInput(Wiring.LIMIT_SHOOTER_DOWN);
        deTensioned = new DigitalInput(Wiring.LIMIT_SHOOTER_DETENSIONED);
        optical = new AnalogChannel(Wiring.OPTICAL_SHOOTER_SENSOR);
        distance = optical.getVoltage();
        outerPistons.retract();
        shoot.retract();
        middlePiston.retract();
    }
    public void initalize()
    {
        outerPistons.retract();
        middlePiston.extend();
        shoot.retract();
    }
    
}
