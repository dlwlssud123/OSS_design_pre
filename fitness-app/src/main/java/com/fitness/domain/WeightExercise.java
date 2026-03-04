package com.fitness.domain;

public class WeightExercise extends Exercise {
    private int sets;
    private int reps;
    private double weight;

    public WeightExercise(String name, String targetMuscle, String description, int sets, int reps, double weight) {
        super(name, targetMuscle, description);
        this.sets = sets;
        this.reps = reps;
        this.weight = weight;
    }

    public int getSets() { return sets; }
    public int getReps() { return reps; }
    public double getWeight() { return weight; }

    @Override
    public String displayGuide() {
        return String.format("[웨이트] %s (%s): %s | %d세트 %d회 %.1fkg", name, targetMuscle, description, sets, reps, weight);
    }
}