/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

/**
 *
 * @author jeremy
 */
public class Piston {
    
    DoubleSolenoid sol;
    boolean isExtend;
    
    public Piston(int extendValve, int retractValve){
        sol = new DoubleSolenoid(extendValve, retractValve);
        retract();
    }
    
    public void extend(){
        sol.set(true);
        isExtend = true;
    }
    
    public void retract(){
        sol.set(false);
        isExtend = false;
    }
    
}
