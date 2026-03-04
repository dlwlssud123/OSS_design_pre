package com.fitness.domain;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private String name;
    private double weight;
    private double targetWeight;
    private String goal;
    private List<Routine> savedRoutines = new ArrayList<>();
    private List<WorkoutSession> workoutSessions = new ArrayList<>();
    private List<BodyMetric> bodyMetrics = new ArrayList<>();

    public User(String name, double weight, double targetWeight, String goal) {
        this.name = name;
        this.weight = weight;
        this.targetWeight = targetWeight;
        this.goal = goal;
        // 초기 체성분 기록
        this.bodyMetrics.add(new BodyMetric(weight, 0, 0));
    }

    public void addWorkoutSession(WorkoutSession session) {
        if (workoutSessions == null) workoutSessions = new ArrayList<>();
        workoutSessions.add(session);
    }

    public void addBodyMetric(BodyMetric metric) {
        if (bodyMetrics == null) bodyMetrics = new ArrayList<>();
        bodyMetrics.add(metric);
        this.weight = metric.getWeight(); // 최신 체중 업데이트
    }

    public List<WorkoutSession> getWorkoutSessions() {
        if (workoutSessions == null) workoutSessions = new ArrayList<>();
        return workoutSessions;
    }

    public List<BodyMetric> getBodyMetrics() {
        if (bodyMetrics == null) bodyMetrics = new ArrayList<>();
        return bodyMetrics;
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
}