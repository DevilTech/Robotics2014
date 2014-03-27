package edu.wpi.first.wpilibj.templates;

import Competition.Wiring;
import edu.wpi.first.wpilibj.AnalogChannel;

public class MaxSonar {
     AnalogChannel sensor;
     double conversionFactor;
     double dist;
     public MaxSonar(int channel)
     {
         sensor = new AnalogChannel(channel);
         conversionFactor = 84;
         sensor.setAverageBits(8);
         sensor.setOversampleBits(0);
         dist = 0;
     }
     public double getInches ()
     {
         return (sensor.getAverageVoltage()*conversionFactor-21);
     }
     public double getFeet ()
     {
         return ((sensor.getAverageVoltage()*.149) - 1.58);
     }
     public double getVoltage ()
     {
         return (sensor.getAverageVoltage());
     }
     public boolean canShoot() 
     {
         dist = getFeet();
         if((dist >= Wiring.MIN_DIST_R1) && (dist <= Wiring.MAX_DIST_R1)) {
             return true;
         } else if ((dist >= Wiring.MIN_DIST_R2) && (dist <= Wiring.MAX_DIST_R2)) {
             return true;
         } else {
             return false;
         }
     }
     
}
