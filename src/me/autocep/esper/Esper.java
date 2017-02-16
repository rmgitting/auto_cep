/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.autocep.esper;

import me.autocep.esper.queries.TestQueries;
import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPException;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esperio.AdapterInputSource;
import com.espertech.esperio.csv.CSVInputAdapter;
import com.espertech.esperio.csv.CSVInputAdapterSpec;
import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.autocep.esper.models.DeleteEvent;
import me.autocep.esper.models.TestEvent;
import me.autocep.esper.models.WaferEvent;
import me.autocep.esper.queries.RuleQueries;
import me.autocep.esper.queries.ShapeletQueries;
import me.autocep.global.Global;
import me.autocep.gui.MainGUI;
import me.autocep.input.Input;
import me.autocep.models.Shapelet;
import me.autocep.models.TimeSequence;

/**
 *
 * @author Raef M
 */
public class Esper extends Thread {

    private EPServiceProvider service;
    private final MainGUI main;
    private final String stream;
    private final Class streamClass;
    private final String[] properties;
    public BlockingQueue bQ = new BlockingQueue();
    public List<TimeSequence> rules;

    private int totalLength;
    private String trueClass;
    private String fileName;

    private int avgLength;
    private String predictedClass;

    public Esper(MainGUI main, String dataset, List<TimeSequence> rules) {
        this.rules = rules;
        this.main = main;
        if (dataset.equalsIgnoreCase(Global.WAFER)) {
            this.stream = "WaferEvent";
            this.streamClass = WaferEvent.class;
            this.properties = new String[]{"time", "dim_11", "dim_12", "dim_15", "dim_6", "dim_7", "dim_8"};
        } else {
            this.stream = "TestEvent";
            this.streamClass = TestEvent.class;
            this.properties = null;
        }
    }

    private void configureEngine() {
        Configuration config = new Configuration();
        config.addEventTypeAutoName("me.autocep.esper.models");
        config.addImport("me.autocep.esper.functions.*");
        config.addEventType(stream, streamClass);
        config.getEngineDefaults().getThreading().setInternalTimerEnabled(false);

//        config = this.addTestVariables(config);
        config = this.addRuleVariables(config, rules);
        service = EPServiceProviderManager.getDefaultProvider(config);
    }

    @Override
    public void run() {
        this.configureEngine();

//        this.configureTestEPLs();
//        this.runTests();
        this.configureRuleEPLs(rules);

        while (true) {
            try {
                File eventSource = bQ.take();
                if (eventSource != null) {
                    if (eventSource.getName().equalsIgnoreCase("stop")) {
                        break;
                    }
                    dispatchEvents(eventSource);
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(Esper.class.getName()).log(Level.SEVERE, null, ex);
            } catch (EPException ex) {
                System.out.println("End of File Reached");
                Evaluation.handleStats(this);
                service.getEPRuntime().sendEvent(new DeleteEvent(true));
            }
        }
        System.out.println("Killing the Esper Engine");
        service.destroy();
    }

    private void configureRuleEPLs(List<TimeSequence> rules) {
        int index = 0;
        Global.writeLog(main.getRulePane(), Color.BLACK, "For each rule, the following CEP rules are created:");
        String query;
        for (TimeSequence ts : rules) {

            Global.writeLog(main.getRulePane(), Color.DARK_GRAY, "Rule " + index + ":");

            query = RuleQueries.queryCreateWindow(service, index);
            Global.writeLog(main.getRulePane(), Color.DARK_GRAY, "Named Window Creation: ");
            Global.writeLog(main.getRulePane(), Color.BLUE, query);

            query = RuleQueries.queryDeleteWindow(service, index);
            Global.writeLog(main.getRulePane(), Color.DARK_GRAY, "Named Window Deletion on EOF: ");
            Global.writeLog(main.getRulePane(), Color.BLUE, query);

            query = RuleQueries.query3(this, ts, index);
            Global.writeLog(main.getRulePane(), Color.DARK_GRAY, "Global CEP Rule: ");
            Global.writeLog(main.getRulePane(), Color.BLUE, query);

            Global.writeLog(main.getRulePane(), Color.BLACK, "For each shapelet inside rule" + index + ", the following CEP rules are created:");
            int i = 0;
            for (Shapelet s : ts.getSequence()) {
                query = ShapeletQueries.query1(service, stream, s, index);
                Global.writeLog(main.getRulePane(), Color.DARK_GRAY, "Shapelet " + i + ":");
                Global.writeLog(main.getRulePane(), Color.BLUE, query);
                i++;
            }
            Global.drawLine(main.getRulePane(), Color.BLACK);
            index++;
        }
    }

    private Configuration addRuleVariables(Configuration config, List<TimeSequence> rules) {
        Set<Long> ids = new HashSet<>();
        rules.stream().forEach((ts) -> {
            ts.getSequence().stream().forEach((s) -> {
                long id = s.getId();
                if (!(ids.contains(id))) {
                    ids.add(id);
                    config.addVariable("dim" + id, String.class, s.getDimension_name(), true);
                    config.addVariable("data" + id, List.class, s.getSubsequence(), true);
//                    config.addVariable("delta", Double.class, s.getDist_threshold(), true);
                }
            });
        });
        return config;
    }

//    private void removeRuleVariables() {
//            ids.stream().forEach((id) -> {
//                    service.getEPAdministrator().getConfiguration().removeVariable("dim" + id, true);
//                    service.getEPAdministrator().getConfiguration().removeVariable("data" + id, true);
////                    config.addVariable("delta", Double.class, s.getDist_threshold(), true);
//            });
//            ids.clear();
//    }
    private void dispatchEvents(File eventSource) throws EPException {
        String lastLine = Input.lastLineInFile(eventSource);
        String[] splitted = lastLine.split(",");
        trueClass = splitted[0];
        totalLength = Input.countLines(eventSource);
        fileName = eventSource.getName();

        predictedClass = null;
        avgLength = 0;

        AdapterInputSource adapterInputSource = new AdapterInputSource(eventSource);
        CSVInputAdapterSpec spec = new CSVInputAdapterSpec(adapterInputSource, stream);
        spec.setPropertyOrder(properties);
        (new CSVInputAdapter(service, spec)).start();

//        if (!classification.equals("")) {
//            Global.writeLog(esperFrame.getInfoTextPane(), Color.red, "Predicted at: " + violationTime);
//            Global.writeLog(esperFrame.getInfoTextPane(), Color.red, "Class: " + classification);
    }



    public EPServiceProvider getService() {
        return service;
    }

    public void setService(EPServiceProvider service) {
        this.service = service;
    }

    public String getTrueClass() {
        return trueClass;
    }

    public void setTrueClass(String trueClass) {
        this.trueClass = trueClass;
    }

    public MainGUI getMain() {
        return main;
    }

    public BlockingQueue getbQ() {
        return bQ;
    }

    public void setbQ(BlockingQueue bQ) {
        this.bQ = bQ;
    }

    public List<TimeSequence> getRules() {
        return rules;
    }

    public void setRules(List<TimeSequence> rules) {
        this.rules = rules;
    }

    public int getTotalLength() {
        return totalLength;
    }

    public void setTotalLength(int totalLength) {
        this.totalLength = totalLength;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPredictedClass() {
        return predictedClass;
    }

    public void setPredictedClass(String predictedClass) {
        this.predictedClass = predictedClass;
    }

    public int getAvgLength() {
        return avgLength;
    }

    public void setAvgLength(int avgLength) {
        this.avgLength = avgLength;
    }

    private void configureTestEPLs() {

        TestQueries.query2(service);
    }

    private void runTests() {
        for (int i = 0; i < 10; i++) {
            TestEvent t = new TestEvent(i, "hi", i / 2.0);
            service.getEPRuntime().sendEvent(t);

        }
    }

    private Configuration addTestVariables(Configuration config) {
        config.addVariable("dim", String.class, "1");
        List<Double> l = new ArrayList<>();
        l.add(2.);
        l.add(3.);
        l.add(4.);
        config.addVariable("data", Object.class, l);
        return config;
    }

}
