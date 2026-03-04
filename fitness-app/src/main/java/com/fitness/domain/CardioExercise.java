package com.fitness.domain;

public class CardioExercise extends Exercise {
    private int durationMinutes;
    private int intensity;

    public CardioExercise(String name, String targetMuscle, String description, int durationMinutes, int intensity) {
        super(name, targetMuscle, description, ""); // Default empty URL
        this.durationMinutes = durationMinutes;
        this.intensity = intensity;
    }

    public CardioExercise(String name, String targetMuscle, String description, String videoUrl, int durationMinutes, int intensity) {
        super(name, targetMuscle, description, videoUrl);
        this.durationMinutes = durationMinutes;
        this.intensity = intensity;
    }

    public int getDurationMinutes() { return durationMinutes; }
    public int getIntensity() { return intensity; }

    @Override
    public String displayGuide() {
        return String.format("[유산소] %s (%s): %s | %d분 강도 %d", name, targetMuscle, description, durationMinutes, intensity);
    }
}