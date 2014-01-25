package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.ADXL345_I2C;
import edu.wpi.first.wpilibj.DigitalModule;
import edu.wpi.first.wpilibj.I2C;

/**
 *
 * @author colin
 */
public class GY85_I2C {
    
    private I2C cread, cwrite;
    private I2C gread, gwrite;
    private I2C aread, awrite;
    private ADXL345_I2C accelerometer;
    
    private byte compassBuffer[], cStatusBuffer[], gyroBuffer[], accelBuffer[];
    private int compassByte, cStatusByte, gyroByte, accelByte;
    
    private static final double kGsPerLSB = 0.00390625;
        
    public GY85_I2C() {
        
        compassByte = 6;
        cStatusByte = 1;
        gyroByte = 2;
        accelByte = 4;
        
        compassBuffer = new byte[compassByte];
        cStatusBuffer = new byte[cStatusByte];
        gyroBuffer = new byte[gyroByte];
        accelBuffer = new byte[accelByte];
        
        setupCompass();
        setupGyro();
        setupAccel();
    }

    private void setupCompass() { //HMC5883L
        cwrite = new I2C(DigitalModule.getInstance(1), 0x3C);
        cread = new I2C(DigitalModule.getInstance(1), 0x3D);
        
        cwrite.write(0, 0x74); //75Hz
        cwrite.write(1, 0x40); //1.9 Ga
        cwrite.write(2, 0); //Continuous Mode
    }
    
    private void setupGyro() { //ITG3200
        gwrite = new I2C(DigitalModule.getInstance(1), 0xD1);
        gread = new I2C(DigitalModule.getInstance(1), 0xD0);

        gwrite.write(21, Wiring.SAMPLE_RATE);
        gwrite.write(22, 0x1b); //2000dps, 42Hz
        gwrite.write(62, 0x30); //sty x,y
    }
    
    private void setupAccel() { //ADXL345
        awrite = new I2C(DigitalModule.getInstance(1), 0xA6);
        aread = new I2C(DigitalModule.getInstance(1), 0xA7);

        awrite.write(0x2C, 0x0A); //100 Hz
        awrite.write(0x2D, 0x08); //Measure mode
        awrite.write(0x31, 0x00); //10bit, sign EXT, 2G
    }
    
   
    double getAccelX() { 
        return DTlib.accelByteCombo(accelBuffer[1], accelBuffer[0]); 
    }

    double getAccelY() { 
        return DTlib.accelByteCombo(accelBuffer[3], accelBuffer[2]); 
    }

    double getGyroZ() { 
        //readG();
        return (DTlib.byteCombo(gyroBuffer[0], gyroBuffer[1])); 
    }

     double getCompassX() {
        double tempX = DTlib.byteCombo(compassBuffer[0], compassBuffer[1]);
        if(tempX == -4096) { System.out.println("OVERFLOW!"); }
        return tempX;
    }// - 458

     double getCompassY() { 
        double tempY = DTlib.byteCombo(compassBuffer[4], compassBuffer[5]); 
        if(tempY == -4096) { System.out.println("OVERFLOW!"); }
        return tempY;
    } //  - 93

    double getCompassAngle() { 
        //readC();
        return Math.toDegrees(DTlib.atan2(getCompassY(), getCompassX())); 
    }
    //double getCompassRadAngle() { return atan2(getCompassY(), getCompassX()); }
    double getCompassRadAngle(double initialHeading) { 
        //readC();
        return DTlib.radianWrap(DTlib.atan2(getCompassY(), getCompassX()) - initialHeading); 
    }
    
    double getCompassRadAngle() { 
//        readC();
        return DTlib.atan2(getCompassY(), getCompassX()); 
    }
    
    String getCompassStatus() {
        switch((cStatusBuffer[0] & 0x3)) {
            case 0: return "Not Ready, No Lock";
            case 1: return "Ready, No Lock";
            case 2: return "Lock, Not Ready";
            case 3: return "LOCK, READY";
            default: return "what?";
        }
    }
    
    public void readAll() {
        readC();
        readA();
        readG();
    }
    
    public void readA() {
        aread.read(50, accelByte, accelBuffer);
    }
    
    public void readC() {
        cread.read(9, cStatusByte, cStatusBuffer);
        if((cStatusBuffer[0] & 0x1) == 1) {
            cread.read(3, compassByte, compassBuffer);
        }
    }
    
    public void readG() {
        gread.read(33, gyroByte, gyroBuffer);
    }
}
