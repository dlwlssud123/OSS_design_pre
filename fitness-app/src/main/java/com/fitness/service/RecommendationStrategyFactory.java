package com.fitness.service;

import java.util.HashMap;
import java.util.Map;

public class RecommendationStrategyFactory {
    private static final Map<String, RecommendationStrategy> strategies = new HashMap<>();

    static {
        strategies.put("근비대", new HypertrophyStrategy());
        strategies.put("다이어트", new DietStrategy());
        strategies.put("체력 증진", new BeginnerStrategy());
    }

    public static RecommendationStrategy getStrategy(String goal) {
        return strategies.getOrDefault(goal, new BeginnerStrategy());
    }
}
