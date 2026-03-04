package com.fitness.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class WorkoutSession implements Serializable {
    private String routineName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<SetRecord> setRecords;

    public WorkoutSession(String routineName) {
        this.routineName = routineName;
        this.startTime = LocalDateTime.now();
        this.setRecords = new ArrayList<>();
    }

    public void endSession() {
        this.endTime = LocalDateTime.now();
    }

    public void addSetRecord(SetRecord record) {
        this.setRecords.add(record);
    }

    public double getTotalVolume() {
        return setRecords.stream().mapToDouble(SetRecord::getVolume).sum();
    }

    public String getRoutineName() { return routineName; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public List<SetRecord> getSetRecords() { return setRecords; }
}
