package com.fitness.domain;

import java.io.Serializable;

public abstract class Exercise implements Serializable {
    protected String name;
    protected String targetMuscle;
    protected String description;

    public Exercise(String name, String targetMuscle, String description) {
        this.name = name;
        this.targetMuscle = targetMuscle;
        this.description = description;
    }

    public String getName() { return name; }
    public String getTargetMuscle() { return targetMuscle; }

    public abstract String displayGuide();
}