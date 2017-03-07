/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.autocep.esper;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Raef M
 */
public class DispatchEvents extends Thread {

    private File[] files;
    private final Esper esper;

    public DispatchEvents(Esper esper) {
        this.esper = esper;
    }

    public DispatchEvents(File[] chosenLogs, Esper esper) {
        this.files = chosenLogs;
        this.esper = esper;
    }

    public File[] getFiles() {
        return files;
    }

    public Esper getEsper() {
        return esper;
    }

    public void setFiles(File[] files) {
        this.files = files;
    }

    @Override
    public void run() {
        try {
//            for (File f : files) {
            esper.bQ.put(files);
//                Thread.sleep(2000);
//            }
        } catch (Exception ex) {
            Logger.getLogger(DispatchEvents.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
