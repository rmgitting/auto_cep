/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.autocep.esper.queries;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;
import me.autocep.models.Shapelet;

/**
 *
 * @author Raef M
 */
public class ShapeletQueries {

    public static String query1(EPServiceProvider service, String stream, Shapelet s, int index) {
        long id = s.getId();
        int l = s.getSubsequence().size();
        String query = "INSERT INTO rule" + index + " "
                + "SELECT time, "
                + "prior(" + (l - 1) + ", time) AS t, "
                + "dim" + id + " AS dim "
                //                        + "window(*) AS w "
                + "FROM " + stream + ".win:length(" + l + ") "
                + "HAVING count(*) = " + l + " "
                + "and "
                + "DistanceMeasures.euclidean("
                + "window(*)"
                + ","
                + "data"
                + id
                + ","
                + "dim"
                + id
                + ") <= " + s.getDist_threshold();
        EPStatement statement = service.getEPAdministrator().createEPL(query);
//        statement.addListener((EventBean[] ebs, EventBean[] ebs1) -> {
//            int time = (Integer) ebs[0].get("t");
//            System.out.println("time:" + time);
//            String dim = (String) ebs[0].get("dim");
//            System.out.println("dim:" + dim);
//        });
        return query;

    }
}
