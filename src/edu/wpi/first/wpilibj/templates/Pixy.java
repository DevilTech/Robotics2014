package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.visa.VisaException;

public class Pixy 
{
    SerialPort pixy; 
    public Pixy()
    {
        try 
        { 
            pixy = new SerialPort(19200, 8, SerialPort.Parity.kNone, SerialPort.StopBits.kOne);
        }
        catch (VisaException ex) 
        {
            ex.printStackTrace();
        }
    }
    
    public void returnData(int num)
    {
        try
        {
            System.out.println(pixy.read(num));
        }
        catch (VisaException ex)
        {
            ex.printStackTrace();
        }
    }
}
