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
public class DeleteEvent {
    boolean d;

    public DeleteEvent(boolean delete) {
        this.d = delete;
    }

    public boolean isD() {
        return d;
    }

    public void setD(boolean delete) {
        this.d = delete;
    }
    
    
}
