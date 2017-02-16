package me.autocep.models;

import java.util.List;

/**
 * Created by Raef M on 2/6/2017.
 */
public class TimeSequence {

    private Double name;
    private String dimension_name;
    private String class_sequence;
    private List<Shapelet> sequence;
    private List<Integer> time_steps;

    public Double getName() {
        return name;
    }

    public void setName(Double name) {
        this.name = name;
    }

    public String getDimension_name() {
        return dimension_name;
    }

    public void setDimension_name(String dimension_name) {
        this.dimension_name = dimension_name;
    }

    public String getClass_sequence() {
        return class_sequence;
    }

    public void setClass_sequence(String class_sequence) {
        this.class_sequence = class_sequence;
    }

    public List<Shapelet> getSequence() {
        return sequence;
    }

    public void setSequence(List<Shapelet> sequence) {
        this.sequence = sequence;
    }

    public List<Integer> getTime_steps() {
        return time_steps;
    }

    public void setTime_steps(List<Integer> time_steps) {
        this.time_steps = time_steps;
    }

    public void fix() {
        if (this.sequence.size() != this.time_steps.size() + 1) {
            while (this.sequence.size() != this.time_steps.size() + 1) {
                this.time_steps.add(Integer.MAX_VALUE);
            }
        }
    }
}
