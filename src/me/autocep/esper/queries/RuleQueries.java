/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.autocep.esper.queries;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import me.autocep.esper.Esper;
import me.autocep.models.TimeSequence;

/**
 *
 * @author Raef M
 */
public class RuleQueries {

    public static String queryCreateWindow(EPServiceProvider service, int index) {
        String query = "CREATE WINDOW rule" + index + ".win:keepall() "
                + "(time int, t int, dim String)";
        service.getEPAdministrator().createEPL(query);
        return query;

    }

    public static String queryDeleteWindow(EPServiceProvider service, int index) {
        String query = "ON DeleteEvent(d=true) DELETE FROM rule" + index;
        EPStatement statement = service.getEPAdministrator().createEPL(query);
        statement.addListener((EventBean[] ebs, EventBean[] ebs1) -> {
            System.out.println("Emptying the window rule" + index);
        });
        return query;

    }

    public static void query1(EPServiceProvider service, TimeSequence rule, int index) {
        String query = "SELECT t, dim FROM rule" + index;
        EPStatement statement = service.getEPAdministrator().createEPL(query);
        statement.addListener((EventBean[] ebs, EventBean[] ebs1) -> {
            System.out.println("IN RULEEESS");
            int time = (Integer) ebs[0].get("t");
            System.out.println("time:" + time);
            String dim = (String) ebs[0].get("dim");
            System.out.println("dim:" + dim);
        });
    }

    public static void query2(EPServiceProvider service, TimeSequence rule, int index) {
        String query = "SELECT a.t, a.dim FROM pattern ["
                + "every a=rule" + index + "(a.t>5)"
                + "]";
        EPStatement statement = service.getEPAdministrator().createEPL(query);
        statement.addListener((EventBean[] ebs, EventBean[] ebs1) -> {
            System.out.println("IN RULEEESS");
            int time = (Integer) ebs[0].get("a.t");
            System.out.println("time:" + time);
            String dim = (String) ebs[0].get("a.dim");
            System.out.println("dim:" + dim);
        });
    }

    public static String query3(Esper esper, TimeSequence rule, int index) {
        char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        String query = "SELECT ";
        for (int i = 0; i < rule.getSequence().size(); i++) {
            query += alphabet[i]+".time, " + alphabet[i] + ".t, " + alphabet[i] + ".dim ";
            if (i != rule.getSequence().size() - 1) {
                query += ", ";
            }
        }
        query += "FROM PATTERN [every ";
        for (int i = 0; i < rule.getSequence().size(); i++) {
            String dimension = rule.getSequence().get(i).getDimension_name();
            if (i == 0) {
                query += alphabet[i] + "=rule" + index + "(dim='" + dimension + "')";
            } else {
                query += " -> ";
                query += alphabet[i] + "=rule" + index + "(dim='" + dimension + "', t-" + alphabet[i - 1] + ".t <= " + rule.getTime_steps().get(i - 1) + ")";
            }
        }
        query += "]";
        System.out.println("Query:" + query);
        EPStatement statement = esper.getService().getEPAdministrator().createEPL(query);
        statement.addListener((EventBean[] ebs, EventBean[] ebs1) -> {
            System.out.println("Pattern Found");
            String ruleClass = rule.getClass_sequence();
            if(esper.getPredictedClass() == null){
                esper.setPredictedClass(ruleClass);
                int i = rule.getSequence().size() - 1;
                esper.setAvgLength((int)ebs[0].get(alphabet[i]+".time"));
            }
            else{
                if(!esper.getPredictedClass().equalsIgnoreCase(esper.getTrueClass()) && ruleClass.equalsIgnoreCase(esper.getTrueClass())){
                    esper.setPredictedClass(ruleClass);
                    int i = rule.getSequence().size() - 1;
                    esper.setAvgLength((int)ebs[0].get(alphabet[i]+".time"));
                }
            }
//            Global.writeLog(esper.getMain().getInfoPane(), Color.BLACK, "True Class: " + esper.getTrueClass());
//            Global.writeLog(esper.getMain().getInfoPane(), Color.BLACK, "Predicted Class: " + rule.getClass_sequence());
//            int time = (Integer) ebs[0].get("a.t");
//            System.out.println("time:" + time);
//            String dim = (String) ebs[0].get("a.dim");
//            System.out.println("dim:" + dim);
        });
        return query;
    }

}
