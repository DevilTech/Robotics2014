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
    
    private byte compassBuffer[], gyroBuffer[], accelBuffer[];
    private int compassByte, gyroByte, accelByte;
    
    private static final double kGsPerLSB = 0.00390625;
        
    public GY85_I2C() {    
        compassByte = 6;
        gyroByte = 2;
        accelByte = 4;
        
        compassBuffer = new byte[compassByte];
        gyroBuffer = new byte[gyroByte];
        accelBuffer = new byte[accelByte];
        
        setupCompass();
        setupGyro();
        setupAccel();
    }

    private void setupCompass() { //HMC5883L
        cwrite = new I2C(DigitalModule.getInstance(1), 0x3C);
        cread = new I2C(DigitalModule.getInstance(1), 0x3D);

        cwrite.write(0, 0x54); //75Hz
        cwrite.write(1, 0x58); //1.9 Ga
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
        return accelByteCombo(accelBuffer[1], accelBuffer[0]); 
    }

    double getAccelY() { 
        return accelByteCombo(accelBuffer[3], accelBuffer[2]); 
    }

    double getGyroZ() { 
        readG();
        return (byteCombo(gyroBuffer[0], gyroBuffer[1])); 
    }

    private double getCompassX() { 
        return byteCombo(compassBuffer[0], compassBuffer[1]); 
    }// - 458

    private double getCompassY() { 
        return byteCombo(compassBuffer[4], compassBuffer[5]); 
    } //  - 93

    double getCompassAngle() { 
        readC();
        return Math.toDegrees(atan2(getCompassY(), getCompassX())); 
    }
    //double getCompassRadAngle() { return atan2(getCompassY(), getCompassX()); }
    double getCompassRadAngle(double initialHeading) { 
        readC();
        return Wiring.radianWrap(atan2(getCompassY(), getCompassX()) - initialHeading); 
    }
    
    double getCompassRadAngle() { 
        readC();
        return Wiring.radianWrap(atan2(getCompassY(), getCompassX())); 
    }
    
    public void readAll() {
        cread.read(3, compassByte, compassBuffer);
        gread.read(29, gyroByte, gyroBuffer);
        aread.read(50, accelByte, accelBuffer);
    }
    
    public void readA() {
        aread.read(50, accelByte, accelBuffer);
    }
    
    public void readC() {
        cread.read(3, compassByte, compassBuffer);
    }
    
    public void readG() {
        gread.read(33, gyroByte, gyroBuffer);
    }
    
    public int byteCombo(byte num1, byte num2) {
        return ((num1 << 8) | num2 & 0x000000ff); 
    }

    public double accelByteCombo(byte first, byte second) {
        short tempLow = (short) (first & 0xff);
        short tempHigh = (short) ((second << 8) & 0xff00);
        return (tempLow | tempHigh) * kGsPerLSB;
    }
    
    double f(double t) {
        /* This provides 1/10 of a degree accuracy: */
        return -0.001096995 + t * (1.041963708 + t * (-0.196333807 + t * (-0.060821409)));
    }
    
    
    public double atan2(double y, double x) {
        double pi = Math.PI;
        double pi2 = Math.PI / 2;
        if (x >= 0) { /* right half-plane */
            if (y >= 0) { /* 1st quadrant */
                if (y <= x) {
                    if (x == 0) {
                        return 0;  /* x & y both zero */
                    } else {
                        return f(y / x);
                    }
                } else {
                    return pi2 - f(x / y);
                }
            } else {  /* 4th quadrant */
                if (-y <= x) {
                    return -f(-y / x);
                } else {
                    return -pi2 + f(-x / y);
                }
            }
        } else {  /* left half-plane */
            if (y >= 0) {  /* 2nd quadrant */
                if (y >= -x) {
                    return pi2 + f(-x / y);
                } else {
                    return pi - f(-y / x);
                }
            } else {  /* 3rd quadrant */
                if (y >= x) {
                    return -pi + f(y / x);
                } else {
                    return -pi2 - f(x / y);
                }
            }
        }
    }
}
