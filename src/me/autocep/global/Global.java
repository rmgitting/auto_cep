package me.autocep.global;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.io.File;
import javax.swing.filechooser.FileNameExtensionFilter;
import me.autocep.gui.MainGUI;

/**
 * Created by Raef M on 2/7/2017.
 */
public class Global {

    public final static String WAFER = "wafer";
    public final static String ECG = "ecg";
    public final static String ROBOT = "robot";

    public static void writeLog(JTextPane textPane, Color color, String text) {
        Style style = textPane.addStyle("Style", null);
        StyledDocument doc = textPane.getStyledDocument();
        StyleConstants.setForeground(style, color);

        try {
            doc.insertString(doc.getLength(), text + "\n", style);

        } catch (BadLocationException e) {
        }
    }
    
    

    public static File[] selectFiles(MainGUI frame, String text, String filter) {
        File[] files = null;
        JFileChooser fc = new JFileChooser();
        FileNameExtensionFilter csvFilter = new FileNameExtensionFilter(text, filter);
        fc.setFileFilter(csvFilter);
        fc.setMultiSelectionEnabled(true);
        fc.setDialogTitle("Choose your Files");
        fc.setAcceptAllFileFilterUsed(false);
        fc.setCurrentDirectory(new File(System.getProperty("user.dir")));
        if (fc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            files = fc.getSelectedFiles();
            Global.writeLog(frame.getInfoPane(), Color.BLACK, "User chose: " + files.length + " files");
        } else {
            Global.writeLog(frame.getInfoPane(), Color.BLACK, "User Canceled");
        }
        return files;
    }

    public static void drawLine(JTextPane infoPane, Color BLUE) {
        writeLog(infoPane, BLUE, "******************************************");
    }

}
