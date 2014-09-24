/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.AnalogChannel;


public class PressureSensor {
    AnalogChannel pSensor;
    //approx 25 psi per volt
    public PressureSensor(int channel){
        pSensor = new AnalogChannel(channel);
    }
    
    public double getVoltage(){
        return pSensor.getVoltage();
    }
}
