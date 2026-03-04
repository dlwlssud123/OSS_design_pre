package com.fitness.service;

import com.fitness.domain.Routine;
import com.fitness.domain.User;
import com.fitness.domain.WeightExercise;
import com.fitness.domain.CardioExercise;

public class BeginnerStrategy implements RecommendationStrategy {
    @Override
    public Routine recommend(User user) {
        Routine routine = new Routine("초보자 전신 추천 루틴 (" + user.getGoal() + ")");
        routine.addExercise(new WeightExercise("스쿼트", "하체", "맨몸 스쿼트로 기본 자세 연습", 3, 15, 0));
        routine.addExercise(new WeightExercise("푸시업", "가슴", "무릎 대고 푸시업", 3, 10, 0));
        routine.addExercise(new CardioExercise("가볍게 걷기", "전신", "러닝머신", 15, 3));
        return routine;
    }
}