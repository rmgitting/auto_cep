/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.autocep.gui;

import java.io.File;
import javax.swing.JTextPane;
import javax.swing.text.DefaultStyledDocument;
import me.autocep.esper.DispatchEvents;

/**
 *
 * @author Raef M
 */
public class CleanUp {
    
    public static void eraseAllPanes(MainGUI main){
        erasePane(main.getInfoPane());
        erasePane(main.getRulePane());
        erasePane(main.getFilePane());
        erasePane(main.getGlobalPane());
    }
    
    public static void erasePane(JTextPane textPane){
        textPane.setDocument(new DefaultStyledDocument());
    }
    
    public static void killEsperEngine(MainGUI main){
        File stopSignal = new File("stop");
        new DispatchEvents(new File[]{stopSignal}, main.getEsper()).start();
    }
    
}
