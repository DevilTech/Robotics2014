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
         conversionFactor = 8.4;
         dist = 0;
     }
     public double getInches()
     {
         return ((sensor.getVoltage()*conversionFactor/12)-21);
         //Distance to front of chassis is 21 in
     }
     public double getFeet()
     {
         return ((sensor.getVoltage()*conversionFactor)-1.75);
     }
     public double getVoltage()
     {
         return (sensor.getVoltage());
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

