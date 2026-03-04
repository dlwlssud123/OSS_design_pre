package com.fitness.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

public class BodyMetric implements Serializable {
    private LocalDateTime date;
    private double weight;
    private double muscleMass;
    private double fatPercentage;

    public BodyMetric(double weight, double muscleMass, double fatPercentage) {
        this.date = LocalDateTime.now();
        this.weight = weight;
        this.muscleMass = muscleMass;
        this.fatPercentage = fatPercentage;
    }

    public LocalDateTime getDate() { return date; }
    public double getWeight() { return weight; }
    public double getMuscleMass() { return muscleMass; }
    public double getFatPercentage() { return fatPercentage; }
}
