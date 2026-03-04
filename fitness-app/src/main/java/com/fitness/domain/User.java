package com.fitness.domain;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private String name;
    private double weight;
    private double targetWeight;
    private String goal; // GoalType could be an Enum, using String for simplicity initially
    private List<Routine> savedRoutines = new ArrayList<>();
    private List<String> workoutLogs = new ArrayList<>();

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
    
    public List<Routine> getSavedRoutines() { 
        if (savedRoutines == null) savedRoutines = new ArrayList<>();
        return savedRoutines; 
    }
    public void addRoutine(Routine routine) { getSavedRoutines().add(routine); }
    
    public List<String> getWorkoutLogs() { 
        if (workoutLogs == null) workoutLogs = new ArrayList<>();
        return workoutLogs; 
    }
    public void addWorkoutLog(String log) { getWorkoutLogs().add(log); }
}