package ru.croc.task13.pojo;

import java.util.HashSet;
import java.util.Set;

public class ComparingResults {

    private int similarityCoeff;
    private Set<String> newUserNotViewed;
    private double threshouldCheck;

    public ComparingResults(int similarityCoeff, Set<String> newUserNotViewed, double threshouldCheck){
        this.similarityCoeff = similarityCoeff;
        this.newUserNotViewed = newUserNotViewed;
        this.threshouldCheck = threshouldCheck;
    }

    public int getSimilarityCoeff() {
        return similarityCoeff;
    }

    public double getThreshouldCheck() {
        return threshouldCheck;
    }

    public Set<String> getNewUserNotViewed() {
        return new HashSet<>(newUserNotViewed);
    }
}
