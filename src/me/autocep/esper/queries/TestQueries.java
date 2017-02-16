/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.autocep.esper.queries;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import java.util.List;
import me.autocep.esper.models.TestEvent;
import me.autocep.esper.models.WaferEvent;

/**
 *
 * @author Raef M
 */
public class TestQueries {
    
    public static void query1(EPServiceProvider service){
          EPStatement statement = service.getEPAdministrator().createEPL("select n, Utils.extractData(window(*), dim) as wn, window(*) as w from TestEvent.win:length(3) where n > 5 having count(*) = 3");
        statement.addListener((EventBean[] ebs, EventBean[] ebs1) -> {
            System.out.println(ebs.length);
            System.out.println(ebs[0].get("n"));
            TestEvent[] t = (TestEvent[]) ebs[0].get("w");
            System.out.println("length:" + t.length);
            for (TestEvent ts : t) {
                System.out.println("n:" + ts.getN());
            }
            List<Double> data = (List<Double>) ebs[0].get("wn");
            System.out.println("data:" + data);
        });
    }
    
    public static void query2(EPServiceProvider service){
              String query = "SELECT prior(2,n) as m, window(*) AS w, "
                + "DistanceMeasures.euclidean("
                + "window(*)"
                + ","
                + "data"
                + ","
                + "dim"
                + ") > 3"
                + " AS d "
                + "FROM TestEvent.win:length(3) "
                + "HAVING count(*) = 3 "
                + "and "
                + "DistanceMeasures.euclidean("
                + "window(*)"
                + ","
                + "data"
                + ","
                + "dim"
                + ") < 3"
                + "";
        EPStatement statement2 = service.getEPAdministrator().createEPL(query);
        statement2.addListener((EventBean[] ebs, EventBean[] ebs1) -> {
            Boolean data = (Boolean) ebs[0].get("d");
            System.out.println("data:" + data);
            TestEvent[] t = (TestEvent[]) ebs[0].get("w");
            System.out.println("length:" + t.length);
            for (TestEvent ts : t) {
                System.out.println("n:" + ts.getN());
            }
            System.out.println("time:" + ebs[0].get("m"));
        });
    }
    
    public static void query3(EPServiceProvider service){
         String query = "SELECT * FROM WaferEvent";
        EPStatement statement3 = service.getEPAdministrator().createEPL(query);
        statement3.addListener((EventBean[] ebs, EventBean[] ebs1) -> {
            WaferEvent w = (WaferEvent) ebs[0].getUnderlying();
            System.out.println(w.getDim_11());
        });
    }
    
    public static void query4(EPServiceProvider service){
              String query = "SELECT n "
                + "FROM TestEvent.win:length(3) "
                + "HAVING count(*) = 3 "
                + "and "
                + "DistanceMeasures.euclidean("
                + "window(*)"
                + ","
                + "data"
                + ","
                + "dim"
                + ") > 3"
                + " output first";
        EPStatement statement2 = service.getEPAdministrator().createEPL(query);
        statement2.addListener((EventBean[] ebs, EventBean[] ebs1) -> {
            System.out.println("time:" + ebs[0].get("n"));
        });
    }
    
       public static void query5(EPServiceProvider service){
              String query = "SELECT  prior(2,n) as m "
                + "FROM TestEvent.win:length(3) "
                + "HAVING count(*) = 3 "
                + "and "
                + "DistanceMeasures.euclidean("
                + "window(*)"
                + ","
                + "data"
                + ","
                + "dim"
                + ") > 3"
                + "";
        EPStatement statement2 = service.getEPAdministrator().createEPL(query);
        statement2.addListener((EventBean[] ebs, EventBean[] ebs1) -> {
            System.out.println("time:" + ebs[0].get("m"));
        });
    }
    
}
