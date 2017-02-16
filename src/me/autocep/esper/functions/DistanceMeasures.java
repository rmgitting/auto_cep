/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.autocep.esper.functions;

import java.util.List;

/**
 *
 * @author Raef M
 */
public class DistanceMeasures {
    
    public static double euclidean(Object o1, Object o, String dim) {
        List<Double> d2 = (List<Double>) o;
        List<Double> d1 = Utils.extractData(o1, dim);
        double dist = Double.MAX_VALUE;
        double sum = 0;
        if (d1.size() != d2.size()) {
            return dist;
        }
        for (int i = 0; i < d1.size(); i++) {
            double x = Math.pow(d1.get(i) - d2.get(i), 2);
            sum += x;
        }
        dist = Math.sqrt(sum);
        return dist;
    }
    
}
