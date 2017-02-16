package me.autocep.models;

import java.util.List;
import java.util.Map;

/**
 * Created by Raef M on 2/6/2017.
 */
public class Shapelet {

    private long id;
    private String name;
    private List<Double> subsequence;
    private String class_shapelet;
    private double dist_threshold;
    private double gain;
    private String dimension_name;
    private Map<String, List<Double>> covering_dict;
    private Map<String, Double> min_distance;
    private Map<String, List<Integer>> matching_indices;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Double> getSubsequence() {
        return subsequence;
    }

    public void setSubsequence(List<Double> subsequence) {
        this.subsequence = subsequence;
    }

    public String getClass_shapelet() {
        return class_shapelet;
    }

    public void setClass_shapelet(String class_shapelet) {
        this.class_shapelet = class_shapelet;
    }

    public double getDist_threshold() {
        return dist_threshold;
    }

    public void setDist_threshold(double dist_threshold) {
        this.dist_threshold = dist_threshold;
    }

    public double getGain() {
        return gain;
    }

    public void setGain(double gain) {
        this.gain = gain;
    }

    public String getDimension_name() {
        return dimension_name;
    }

    public void setDimension_name(String dimensionN_name) {
        this.dimension_name = dimensionN_name;
    }

    public Map<String, List<Double>> getCovering_dict() {
        return covering_dict;
    }

    public void setCovering_dict(Map<String, List<Double>> coveringD_dict) {
        this.covering_dict = coveringD_dict;
    }

    public Map<String, Double> getMin_distance() {
        return min_distance;
    }

    public void setMin_distance(Map<String, Double> min_distance) {
        this.min_distance = min_distance;
    }

    public Map<String, List<Integer>> getMatching_indices() {
        return matching_indices;
    }

    public void setMatching_indices(Map<String, List<Integer>> matchingI_indices) {
        this.matching_indices = matchingI_indices;
    }
}
