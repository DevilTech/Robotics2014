/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;

/**
 *
 * @author robotics
 */
public class Gatherer {
    private Talon right;
    private Talon left;
    private Solenoid out;
    private Solenoid in;
    
    public Gatherer(int right, int left, int in, int out){
        this.right = new Talon(right);
        this.left = new Talon(left);
        this.in = new Solenoid(in);
        this.out = new Solenoid(out);
    }
    
    public void spinIn(){
        left.set(1);
        right.set(-1);
    }
    
    public void spinOut(){
        left.set(-1);
        right.set(1);
    }
    
    public void spinStop(){
        left.set(0);
        right.set(0);
    }
    
    public void extend(){
        in.set(true);
        out.set(false);
    }
    
    public void retract(){
        in.set(false);
        out.set(true);
    }
    
    
    
}
