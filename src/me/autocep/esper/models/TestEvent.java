/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.autocep.esper.models;

/**
 *
 * @author Raef M
 */
public class TestEvent {
    
    private int n;
    private String s;
    public double dim_1;

    public TestEvent(int n, String s) {
        this.n = n;
        this.s = s;
    }
    
    public TestEvent(int n, String s, double dim) {
        this.n = n;
        this.s = s;
        this.dim_1 = dim;
    }
    public double getDim_1() {
        return dim_1;
    }

    public void setDim_1(double dim_1) {
        this.dim_1 = dim_1;
    }

    
    

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }
    
    
}
