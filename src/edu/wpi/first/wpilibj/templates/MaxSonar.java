package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.AnalogChannel;

public class MaxSonar {
     AnalogChannel sensor;
     double conversionFactor;
     public MaxSonar(int channel)
     {
         sensor = new AnalogChannel(channel);
         conversionFactor = 84;
         sensor.setAverageBits(8);
         sensor.setOversampleBits(0);
     }
     public double getInches ()
     {
         return (sensor.getAverageVoltage()*conversionFactor);
     }
     public double getFeet ()
     {
         return (sensor.getAverageVoltage()*conversionFactor/12);
     }
     public double getVoltage ()
     {
         return (sensor.getAverageVoltage());
     }
}
