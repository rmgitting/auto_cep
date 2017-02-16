/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.autocep.esper;

import java.awt.Color;
import me.autocep.global.Global;
import me.autocep.gui.CleanUp;

/**
 *
 * @author Raef M
 */
public class Evaluation {

    public static int tp;
    public static int fp;
    public static int count;
    public static double earliness;

    public static void handleStats(Esper esper) {
        Global.writeLog(esper.getMain().getFilePane(), Color.BLACK, "Stats For File: " + esper.getFileName() + ":");
        if (esper.getPredictedClass() == null) {
            esper.setPredictedClass("unclassified");
        }

        String result = "";
        if (!esper.getPredictedClass().equalsIgnoreCase("unclassified")) {
            result = testTrueOrFalse(esper.getTrueClass(), esper.getPredictedClass());
        }
        Global.writeLog(esper.getMain().getFilePane(), Color.BLACK, "True Class: " + esper.getTrueClass());
        Global.writeLog(esper.getMain().getFilePane(), Color.BLACK, "Classified as: " + esper.getPredictedClass());
        Global.writeLog(esper.getMain().getFilePane(), Color.BLACK, result);

        Global.writeLog(esper.getMain().getFilePane(), Color.BLACK, "Total length of this instance:" + esper.getTotalLength());
        double earl = 0;
        if (esper.getAvgLength() != 0) {
            earl = ((float) esper.getAvgLength() / esper.getTotalLength() * 100);
            count++;
            Global.writeLog(esper.getMain().getFilePane(), Color.BLACK, "Length of the Classifier Pattern:" + esper.getAvgLength());
            Global.writeLog(esper.getMain().getFilePane(), Color.BLACK, "Earliness:" + (earl * 100) + "%");
        }
        Global.drawLine(esper.getMain().getFilePane(), Color.BLUE);

        //***************
        //Handle Global
        CleanUp.erasePane(esper.getMain().getGlobalPane());
        int all = (fp + tp);
        Global.writeLog(esper.getMain().getGlobalPane(), Color.BLACK, "Total Number of Tested Instances: " + all);
        double acc = (double) tp / (tp + fp);
        acc *= 100;
        Global.writeLog(esper.getMain().getGlobalPane(), Color.BLACK, "Accuracy: " + acc + "%");
        earliness += earl;
        Global.writeLog(esper.getMain().getGlobalPane(), Color.black, "Earliness: " + earliness / count);
    }

    private static String testTrueOrFalse(String trueClass, String predictedClass) {
        if (trueClass.equalsIgnoreCase(predictedClass) || trueClass.equalsIgnoreCase("\"" + predictedClass + "\"")) {
            tp++;
            return "True Positive";
        }
        fp++;
        return "False Positive";
    }

    public static void reset() {
        tp = 0;
        fp = 0;
        count = 0;
        earliness = 0;
    }

}
