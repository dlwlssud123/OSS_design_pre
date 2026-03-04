package com.fitness.domain;

import java.io.Serializable;

public abstract class Exercise implements Serializable {
    protected String name;
    protected String targetMuscle;
    protected String description;
    protected String videoUrl; // 유튜브 링크 등

    public Exercise(String name, String targetMuscle, String description, String videoUrl) {
        this.name = name;
        this.targetMuscle = targetMuscle;
        this.description = description;
        this.videoUrl = videoUrl;
    }

    public String getName() { return name; }
    public String getTargetMuscle() { return targetMuscle; }
    public String getVideoUrl() { return videoUrl; }

    public abstract String displayGuide();
}