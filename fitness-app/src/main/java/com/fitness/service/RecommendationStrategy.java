package com.fitness.service;

import com.fitness.domain.Routine;
import com.fitness.domain.User;

public interface RecommendationStrategy {
    Routine recommend(User user);
}