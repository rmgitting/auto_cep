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
    public static int firstTp;
    public static int firstFp;
    public static int majorityTp;
    public static int majorityFp;
    public static int closestTp;
    public static int closestFp;
    public static int abnormalTp;
    public static int abnormalFp;
    public static int count;
    public static double earliness;
    public static double firstEarliness;
    public static double closestEarliness;
    public static double abnormalEarliness;
    public static int unclassified;

    public static void handleStats(Esper esper) {
        Global.writeLog(esper.getMain().getFilePane(), Color.BLACK, "Stats For File: " + esper.getFileName() + ":");
        if (esper.getPredictedClass() == null) {
            esper.setPredictedClass("unclassified");
        }

        boolean result = false;
        boolean resultFirst = false;
        boolean resultClosest = false;
        if (!esper.getPredictedClass().equalsIgnoreCase("unclassified")) {
            result = testTrueOrFalse(esper.getTrueClass(), esper.getPredictedClass());
            if (result) {
                tp++;
            } else {
                fp++;
            }
            resultFirst = testTrueOrFalse(esper.getTrueClass(), esper.getFirstPredictedClass());
            if (resultFirst) {
                firstTp++;
            } else {
                firstFp++;
            }
            resultClosest = testTrueOrFalse(esper.getTrueClass(), esper.getClosestPredictedClass());
            if (resultClosest) {
                closestTp++;
            } else {
                closestFp++;
            }
            if (esper.getMain().getAbnormalCheckBox().isSelected()) {
                if (esper.getTrueClass().equalsIgnoreCase("abnormal") && esper.isAbnormalDetected()) {
                    abnormalTp++;
                } else if (!esper.getTrueClass().equalsIgnoreCase("abnormal") && !esper.isAbnormalDetected()) {
                    abnormalTp++;
                } else {
                    abnormalFp++;
                }
            }
        } else {
            unclassified++;
        }
        Global.writeLog(esper.getMain().getFilePane(), Color.BLACK, "True Class: " + esper.getTrueClass());
        int trueClassification = 0;
        int falseClassification = 0;
        boolean majorityResult = false;
        Global.writeLog(esper.getMain().getFilePane(), Color.BLACK, "Predicted Classes Over Time:");
        for (String c : esper.getAllPredictedClasses()) {
            Global.writeLog(esper.getMain().getFilePane(), Color.BLACK, c);
            majorityResult = testTrueOrFalse(esper.getTrueClass(), c.split("@")[0]);
            if (majorityResult) {
                trueClassification++;
            } else {
                falseClassification++;
            }
        }
        if (trueClassification >= falseClassification) {
            majorityTp++;
        } else {
            majorityFp++;
        }
        if (esper.getMain().getAbnormalCheckBox().isSelected()) {
            Global.writeLog(esper.getMain().getFilePane(), Color.BLACK, "Abnormality Detected: " + esper.isAbnormalDetected());
        }
        Global.writeLog(esper.getMain().getFilePane(), Color.BLACK, "Majority Classification: " + majorityResult);
        Global.writeLog(esper.getMain().getFilePane(), Color.BLACK, "True Class Occured: " + result);
        Global.writeLog(esper.getMain().getFilePane(), Color.BLACK, "First Classification: " + resultFirst);
        Global.writeLog(esper.getMain().getFilePane(), Color.BLACK, "Closest Classification: " + resultClosest);

        Global.writeLog(esper.getMain().getFilePane(), Color.BLACK, "Total length of this instance:" + esper.getTotalLength());
        double earl = 0;
        double firstEarl = 0;
        double closestEarl = 0;
        double abnormalEarl = 0;
        if (esper.getAvgLength() != 0) {
            earl = (float) esper.getAvgLength() * 100 / esper.getTotalLength();
            firstEarl = (float) esper.getFirstAvgLength() * 100 / esper.getTotalLength();
            closestEarl = (float) esper.getClosestAvgLength() * 100 / esper.getTotalLength();
            count++;
            Global.writeLog(esper.getMain().getFilePane(), Color.BLACK, "Length of the Classifier Pattern:" + esper.getAvgLength());
            Global.writeLog(esper.getMain().getFilePane(), Color.BLACK, "Earliness:" + earl + "%");

            Global.writeLog(esper.getMain().getFilePane(), Color.BLACK, "Length of the First Classifier Pattern:" + esper.getFirstAvgLength());
            Global.writeLog(esper.getMain().getFilePane(), Color.BLACK, "Earliness:" + firstEarl + "%");

            Global.writeLog(esper.getMain().getFilePane(), Color.BLACK, "Length of the Closest Classifier Pattern:" + esper.getClosestAvgLength());
            Global.writeLog(esper.getMain().getFilePane(), Color.BLACK, "Earliness:" + closestEarl + "%");

            if (esper.getMain().getAbnormalCheckBox().isSelected()) {
                abnormalEarl = (float) esper.getAbnormalAvgLength() * 100 / esper.getTotalLength();
                Global.writeLog(esper.getMain().getFilePane(), Color.BLACK, "Length of the Abnormaility Classifier Pattern:" + esper.getAbnormalAvgLength());
                Global.writeLog(esper.getMain().getFilePane(), Color.BLACK, "Earliness:" + abnormalEarl + "%");
            }
        }
        Global.drawLine(esper.getMain().getFilePane(), Color.BLUE);

        //***************
        //Handle Global
        CleanUp.erasePane(esper.getMain().getGlobalPane());
        int all = (fp + tp);
        int firstAll = (firstFp + firstTp);
        int majorityAll = (majorityTp + majorityFp);
        int closestAll = (closestFp + closestTp);
        Global.writeLog(esper.getMain().getGlobalPane(), Color.BLACK, "Total Number of Tested Instances: " + (all + unclassified));
        double acc = (double) tp / all;
        acc *= 100;
        double firstAcc = (double) firstTp / firstAll;
        firstAcc *= 100;
        double closestAcc = (double) closestTp / closestAll;
        closestAcc *= 100;
        double majorityAcc = (double) majorityTp / majorityAll;
        majorityAcc *= 100;
        if (esper.getMain().getAbnormalCheckBox().isSelected()) {
            int abnormalAll = (abnormalTp + abnormalFp);
            double abnormalAcc = (double) abnormalTp / abnormalAll;
            abnormalAcc *= 100;
            Global.writeLog(esper.getMain().getGlobalPane(), Color.BLACK, "Abnormality Detection Accuracy: " + abnormalAcc + "%");
        }
        Global.writeLog(esper.getMain().getGlobalPane(), Color.BLACK, "Majority Accuracy: " + majorityAcc + "%");
        Global.writeLog(esper.getMain().getGlobalPane(), Color.BLACK, "Accuracy (true class exist): " + acc + "%");
        Global.writeLog(esper.getMain().getGlobalPane(), Color.BLACK, "Accuracy of the first classification: " + firstAcc + "%");
        Global.writeLog(esper.getMain().getGlobalPane(), Color.BLACK, "Accuracy of the closest classification: " + closestAcc + "%");
        earliness += earl;
        firstEarliness += firstEarl;
        closestEarliness += closestEarl;
        if (esper.getMain().getAbnormalCheckBox().isSelected()) {
            abnormalEarliness += abnormalEarl;
            Global.writeLog(esper.getMain().getGlobalPane(), Color.BLACK, "Abnormality Detection Earliness: " + abnormalEarliness / count + "%");
        }
        Global.writeLog(esper.getMain().getGlobalPane(), Color.black, "Earliness (true class exist): " + earliness / count);
        Global.writeLog(esper.getMain().getGlobalPane(), Color.black, "Earliness of the first classification: " + firstEarliness / count);
        Global.writeLog(esper.getMain().getGlobalPane(), Color.black, "Earliness of the closest classification: " + closestEarliness / count);
        double app = (double) all / (all + unclassified);
        Global.writeLog(esper.getMain().getGlobalPane(), Color.black, "Applicability: " + (app * 100) + "%");
    }

    private static boolean testTrueOrFalse(String trueClass, String predictedClass) {
        if (trueClass.equalsIgnoreCase(predictedClass) || trueClass.equalsIgnoreCase("\"" + predictedClass + "\"")) {
            return true;
        }
        return false;
    }

    public static void reset() {
        tp = 0;
        fp = 0;
        firstTp = 0;
        firstFp = 0;
        count = 0;
        earliness = 0;
        firstEarliness = 0;
        unclassified = 0;
        closestEarliness = 0;
        closestFp = 0;
        closestTp = 0;
        majorityTp = 0;
        majorityFp = 0;
        abnormalTp = 0;
        abnormalFp = 0;
        abnormalEarliness = 0;
    }

}
