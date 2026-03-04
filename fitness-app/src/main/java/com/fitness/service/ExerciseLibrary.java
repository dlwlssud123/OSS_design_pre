package com.fitness.service;

import com.fitness.domain.Exercise;
import com.fitness.domain.WeightExercise;
import com.fitness.domain.CardioExercise;
import java.util.ArrayList;
import java.util.List;

public class ExerciseLibrary {
    public static List<Exercise> getAllExercises() {
        List<Exercise> list = new ArrayList<>();
        // 가슴
        list.add(new WeightExercise("벤치 프레스", "가슴", "바벨을 가슴 위로 밀어 올립니다.", 3, 10, 40));
        list.add(new WeightExercise("덤벨 플라이", "가슴", "가슴 근육을 확장하며 덤벨을 모읍니다.", 3, 12, 10));
        // 등
        list.add(new WeightExercise("데드리프트", "등/전신", "바닥에서 바벨을 들어 올립니다.", 3, 5, 60));
        list.add(new WeightExercise("렛 풀 다운", "등", "바를 가슴 쪽으로 당깁니다.", 3, 12, 35));
        // 하체
        list.add(new WeightExercise("스쿼트", "하체", "등을 펴고 앉았다 일어납니다.", 3, 15, 40));
        list.add(new WeightExercise("레그 익스텐션", "하체", "다리를 펴며 허벅지 근육을 수축합니다.", 3, 15, 20));
        // 유산소
        list.add(new CardioExercise("러닝머신", "전신", "일정한 속도로 달립니다.", 20, 8));
        list.add(new CardioExercise("천국의 계단", "하체/심폐", "계단을 오르는 동작을 반복합니다.", 15, 6));
        
        return list;
    }
}
