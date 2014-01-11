package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DigitalModule;
import edu.wpi.first.wpilibj.I2C;

public class GY85_I2C {
    
    private I2C cread, cwrite;
    private I2C gread, gwrite;
    private I2C aread, awrite;
    
    private byte compassBuffer[], gyroBuffer[], accelBuffer[];
    private int compassByte, gyroByte, accelByte;
    
    
    
    public GY85_I2C() {
        compassByte = 6;
        gyroByte = 6;
        accelByte = 6;
        compassBuffer = new byte[compassByte];
        gyroBuffer = new byte[gyroByte];
        accelBuffer = new byte[accelByte];
    }

    public void setupCompass() {
        cwrite = new I2C(DigitalModule.getInstance(1), 0x3C);
        cread = new I2C(DigitalModule.getInstance(1), 0x3D);

        cwrite.write(0, 88);
        cwrite.write(1, 64);
        cwrite.write(2, 0);
    }
    
    public void setupGyro() {
        gwrite = new I2C(DigitalModule.getInstance(1), 0xD1);
        gread = new I2C(DigitalModule.getInstance(1), 0xD0);

        gwrite.write(21, Wiring.SAMPLE_RATE);
        gwrite.write(22, 0x1A);
    }
    
    public void setupAccel() {
        awrite = new I2C(DigitalModule.getInstance(1), 0xA6);
        aread = new I2C(DigitalModule.getInstance(1), 0xA7);

        awrite.write(44, 0x0A);
        awrite.write(45, 0x08);
        awrite.write(49, 0x08);
    }
    
    double getAccelX() { return byteCombo(accelBuffer[1], accelBuffer[0]); }
    double getAccelY() { return byteCombo(accelBuffer[3], accelBuffer[2]); }

    double getGyroX() { return byteCombo(gyroBuffer[0], gyroBuffer[1]); }
    double getGyroY() { return byteCombo(gyroBuffer[2], gyroBuffer[3]); }
    double getGyroZ() { return (byteCombo(gyroBuffer[4], gyroBuffer[5])) / Wiring.SENSOR_SCALE; }

    double getCompassX() { return byteCombo(compassBuffer[0], compassBuffer[1]); } // - 458
    double getCompassY() { return byteCombo(compassBuffer[4], compassBuffer[5]); } //  - 93

    double getGyroAngle() { return Math.toDegrees(atan2(getGyroY(), getGyroX())); }
    double getGyroRadAngle() { return atan2(getGyroY(), getGyroX()); }

    double getCompassAngle() { return Math.toDegrees(atan2(getCompassY(), getCompassX())); }
    double getCompassRadAngle() { return atan2(getCompassY(), getCompassX()); }
    double getCompassRadAngle(double initialHeading) { return radianWrap(atan2(getCompassY(), getCompassX()) - initialHeading); }
    
    public void readAll() {
        cread.read(3, compassByte, compassBuffer);
        gread.read(29, gyroByte, gyroBuffer);
        aread.read(50, accelByte, accelBuffer);
    }
    
    public int byteCombo(byte num1, byte num2) {
        return ((num1 << 8) | num2 & 0x000000ff); 
    }
    
    public double radianWrap(double d){ return (d> Math.PI) ? d-2*Math.PI : (d< -Math.PI) ? d+ 2*Math.PI : d; }
    
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
