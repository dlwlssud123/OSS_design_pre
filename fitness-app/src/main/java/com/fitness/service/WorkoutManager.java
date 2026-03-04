package com.fitness.service;

import com.fitness.domain.Routine;
import com.fitness.domain.User;
import com.fitness.infrastructure.Repository;

public class WorkoutManager {
    private Routine currentRoutine;
    private RecommendationStrategy recStrategy;
    private Repository repository;
    private User currentUser;

    public WorkoutManager(Repository repository) {
        this.repository = repository;
    }

    public void setStrategy(RecommendationStrategy s) {
        this.recStrategy = s;
    }

    public void setUser(User user) {
        this.currentUser = user;
    }

    public Routine recommendRoutine() {
        if (recStrategy != null && currentUser != null) {
            currentRoutine = recStrategy.recommend(currentUser);
            return currentRoutine;
        }
        return null;
    }

    public void logWorkout() {
        if (currentRoutine != null) {
            System.out.println("Workout logged: " + currentRoutine.getRoutineName());
            System.out.println("Total Volume: " + currentRoutine.getTotalVolume());
            // In a real app, this would save to a WorkoutSession entity
        }
    }

    public void saveUser() {
        if (currentUser != null && repository != null) {
            repository.save(currentUser);
        }
    }
}