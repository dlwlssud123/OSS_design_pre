package com.fitness.service;

import com.fitness.domain.Routine;
import com.fitness.domain.User;
import com.fitness.domain.CardioExercise;
import com.fitness.domain.WeightExercise;

public class DietStrategy implements RecommendationStrategy {
    @Override
    public Routine recommend(User user) {
        Routine routine = new Routine(user.getName() + "님을 위한 다이어트 루틴");
        routine.addExercise(new CardioExercise("러닝", "전신", "시속 8km 이상 유지", 30, 8));
        routine.addExercise(new WeightExercise("플랭크", "코어", "1분 유지", 3, 1, 0));
        routine.addExercise(new CardioExercise("사이클", "하체", "숨이 찰 정도의 강도", 20, 5));
        return routine;
    }
}
