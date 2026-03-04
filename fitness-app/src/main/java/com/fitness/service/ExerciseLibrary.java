package com.fitness.service;

import com.fitness.domain.Exercise;
import com.fitness.domain.WeightExercise;
import com.fitness.domain.CardioExercise;
import java.util.ArrayList;
import java.util.List;

public class ExerciseLibrary {
    public static List<Exercise> getAllExercises() {
        List<Exercise> list = new ArrayList<>();
        
        // 1. 가슴 (Chest)
        list.add(new WeightExercise("벤치 프레스", "가슴", "바벨을 가슴 위로 밀어 올립니다.", 3, 10, 40));
        list.add(new WeightExercise("덤벨 플라이", "가슴", "가슴 근육을 확장하며 덤벨을 모읍니다.", 3, 12, 10));
        list.add(new WeightExercise("푸시업", "가슴", "맨몸으로 가슴과 팔 근육을 단련합니다.", 3, 15, 0));

        // 2. 등 (Back)
        list.add(new WeightExercise("데드리프트", "등/전신", "바닥에서 바벨을 들어 올립니다.", 3, 5, 60));
        list.add(new WeightExercise("렛 풀 다운", "등", "바를 가슴 쪽으로 당깁니다.", 3, 12, 35));
        list.add(new WeightExercise("바벨 로우", "등", "상체를 숙이고 바벨을 복부 쪽으로 당깁니다.", 3, 10, 30));

        // 3. 하체 (Legs)
        list.add(new WeightExercise("스쿼트", "하체", "등을 펴고 앉았다 일어납니다.", 3, 15, 40));
        list.add(new WeightExercise("레그 익스텐션", "하체", "다리를 펴며 허벅지 근육을 수축합니다.", 3, 15, 20));
        list.add(new WeightExercise("런지", "하체", "한쪽 다리를 앞으로 내디디며 무릎을 굽힙니다.", 3, 12, 0));

        // 4. 어깨 (Shoulders)
        list.add(new WeightExercise("오버헤드 프레스", "어깨", "바벨이나 덤벨을 머리 위로 밀어 올립니다.", 3, 10, 20));
        list.add(new WeightExercise("사이드 레터럴 레이즈", "어깨", "덤벨을 양옆으로 들어 올립니다.", 3, 15, 5));

        // 5. 이두 (Biceps)
        list.add(new WeightExercise("덤벨 컬", "이두", "덤벨을 들어 올리며 이두근을 수축합니다.", 3, 12, 8));
        list.add(new WeightExercise("바벨 컬", "이두", "바벨을 양손으로 잡고 들어 올립니다.", 3, 10, 15));

        // 6. 삼두 (Triceps)
        list.add(new WeightExercise("트라이셉스 익스텐션", "삼두", "팔꿈치를 머리 위에서 펴며 삼두근을 자극합니다.", 3, 12, 10));
        list.add(new WeightExercise("딥스", "삼두", "평행봉에서 몸을 내렸다가 팔 힘으로 올라옵니다.", 3, 10, 0));

        // 7. 유산소 (Cardio)
        list.add(new CardioExercise("러닝머신", "전신", "일정한 속도로 달립니다.", 20, 8));
        list.add(new CardioExercise("천국의 계단", "하체/심폐", "계단을 오르는 동작을 반복합니다.", 15, 6));
        list.add(new CardioExercise("사이클", "하체/심폐", "자전거 페달을 일정한 속도로 밟습니다.", 30, 5));
        
        return list;
    }
}
