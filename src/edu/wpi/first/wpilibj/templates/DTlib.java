/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testlibrary;

/**
 *
 * @author robotics
 */
public class DTlib {
    
    private static final double kGsPerLSB = 0.00390625;
    
    public static double radianWrap(double d) {
        return (d > Math.PI) ? d - 2 * Math.PI : (d <= -Math.PI) ? d + 2 * Math.PI : d;
    }
    
    public static double clamp(double value) {
        return (value > 1) ? 1 : (value < -1) ? -1 : value;
    }
    
    public static boolean isSameSign(double one, double two){
        return ((one<0)==(two<0));
    }
       public int byteCombo(byte num1, byte num2) {
        return ((num1 << 8) | (num2 & 0x000000ff)); 
    }

    public static double accelByteCombo(byte first, byte second) {
        short tempLow = (short) (first & 0xff);
        short tempHigh = (short) ((second << 8) & 0xff00);
        return (tempLow | tempHigh) * kGsPerLSB;
    }
    
    public static double f(double t) {
        /* This provides 1/10 of a degree accuracy: */
        return -0.001096995 + t * (1.041963708 + t * (-0.196333807 + t * (-0.060821409)));
    }
    
    public static double atan2(double y, double x) {
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
