package com.fitness.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Routine implements Serializable {
    private String routineName;
    private List<Exercise> exercises;

    public Routine(String routineName) {
        this.routineName = routineName;
        this.exercises = new ArrayList<>();
    }

    public void addExercise(Exercise e) {
        exercises.add(e);
    }

    public String getRoutineName() {
        return routineName;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    public double getTotalVolume() {
        double volume = 0;
        for (Exercise e : exercises) {
            if (e instanceof WeightExercise) {
                WeightExercise we = (WeightExercise) e;
                volume += (we.getSets() * we.getReps() * we.getWeight());
            }
        }
        return volume;
    }
}