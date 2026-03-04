package com.fitness.domain;

import java.io.Serializable;

public class User implements Serializable {
    private String name;
    private double weight;
    private double targetWeight;
    private String goal; // GoalType could be an Enum, using String for simplicity initially

    public User(String name, double weight, double targetWeight, String goal) {
        this.name = name;
        this.weight = weight;
        this.targetWeight = targetWeight;
        this.goal = goal;
    }

    public void updateProfile(String name, double weight, double targetWeight, String goal) {
        this.name = name;
        this.weight = weight;
        this.targetWeight = targetWeight;
        this.goal = goal;
    }

    public String getName() { return name; }
    public double getWeight() { return weight; }
    public double getTargetWeight() { return targetWeight; }
    public String getGoal() { return goal; }
}