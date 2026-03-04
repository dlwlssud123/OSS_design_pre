package com.fitness.domain;

import java.io.Serializable;

public class SetRecord implements Serializable {
    private String exerciseName;
    private int setNumber;
    private double weight;
    private int reps;

    public SetRecord(String exerciseName, int setNumber, double weight, int reps) {
        this.exerciseName = exerciseName;
        this.setNumber = setNumber;
        this.weight = weight;
        this.reps = reps;
    }

    public double getVolume() { return weight * reps; }
    public String getExerciseName() { return exerciseName; }
    public int getSetNumber() { return setNumber; }
    public double getWeight() { return weight; }
    public int getReps() { return reps; }
}
