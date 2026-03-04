package com.fitness.service;

import com.fitness.domain.Routine;
import com.fitness.domain.User;
import com.fitness.domain.WeightExercise;

public class HypertrophyStrategy implements RecommendationStrategy {
    @Override
    public Routine recommend(User user) {
        Routine routine = new Routine(user.getName() + "님을 위한 근비대 루틴");
        routine.addExercise(new WeightExercise("벤치 프레스", "가슴", "8~12회 반복 가능한 무게 설정", 4, 10, 60));
        routine.addExercise(new WeightExercise("데드리프트", "전신/등", "복압 유지에 유의", 3, 5, 80));
        routine.addExercise(new WeightExercise("바벨 숄더 프레스", "어깨", "팔꿈치가 너무 뒤로 빠지지 않게", 3, 12, 30));
        return routine;
    }
}
