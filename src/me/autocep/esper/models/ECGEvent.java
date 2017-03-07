/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.autocep.esper.models;

/**
 *
 * @author Raef
 */
public class ECGEvent {
    public int time;
    public double dim_0;
    public double dim_1;

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public double getDim_0() {
        return dim_0;
    }

    public void setDim_0(double dim_0) {
        this.dim_0 = dim_0;
    }

    public double getDim_1() {
        return dim_1;
    }

    public void setDim_1(double dim_1) {
        this.dim_1 = dim_1;
    }
    
    
}
