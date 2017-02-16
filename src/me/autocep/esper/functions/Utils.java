/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.autocep.esper.functions;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Raef M
 */
public class Utils {

    public static List<Double> extractData(Object events, String dim) {
        List<Double> data = new ArrayList<>();
        Object[] objects = (Object[]) events;

        int index = -1;
//        if (dataset.equalsIgnoreCase(Global.WAFER)) {
//        WaferEvent[] wafers = (WaferEvent[]) events;
        Class<?> clazz = objects[0].getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            String name = fields[i].getName();
            String[] splits = name.split("_");
            if (splits.length == 2) {
                if (splits[1].equalsIgnoreCase(dim)) {
                    index = i;
                    break;
                }
            }
        }

//        }
        if (index == -1) {
            return data;
        }
        try {
            for (Object o : objects) {
                clazz = o.getClass();
                fields = clazz.getDeclaredFields();
                data.add(fields[index].getDouble(o));
            }
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return data;
    }

}
