package com.fitness.presentation;

import com.fitness.domain.Exercise;
import com.fitness.domain.Routine;
import com.fitness.domain.User;
import com.fitness.infrastructure.FileRepository;
import com.fitness.service.BeginnerStrategy;
import com.fitness.service.WorkoutManager;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainApp extends Application {

    private static final String USER_DATA_FILE = "user_data.dat";
    private WorkoutManager workoutManager;
    private User currentUser;

    @Override
    public void init() throws Exception {
        super.init();
        FileRepository repo = new FileRepository(USER_DATA_FILE);
        workoutManager = new WorkoutManager(repo);
        
        Object loadedData = repo.load();
        if (loadedData instanceof User) {
            currentUser = (User) loadedData;
        }
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("OSS Fitness Tracker");

        if (currentUser == null) {
            showOnboarding(primaryStage);
        } else {
            showDashboard(primaryStage);
        }
    }

    private void showOnboarding(Stage stage) {
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.getStyleClass().add("root");

        VBox container = new VBox(15);
        container.setPadding(new Insets(25));
        container.getStyleClass().add("main-container");

        Label title = new Label("사용자 프로필 설정");
        title.getStyleClass().add("title-label");

        TextField nameField = new TextField();
        nameField.setPromptText("이름 입력");

        TextField weightField = new TextField();
        weightField.setPromptText("현재 체중 (kg)");

        TextField targetWeightField = new TextField();
        targetWeightField.setPromptText("목표 체중 (kg)");

        ComboBox<String> goalCombo = new ComboBox<>();
        goalCombo.getItems().addAll("근비대", "다이어트", "체력 증진", "커스텀");
        goalCombo.setPromptText("운동 목적 선택");
        goalCombo.setMaxWidth(Double.MAX_VALUE);

        Button submitBtn = new Button("시작하기");
        submitBtn.getStyleClass().add("primary-button");
        submitBtn.setMaxWidth(Double.MAX_VALUE);
        
        submitBtn.setOnAction(e -> {
            try {
                String name = nameField.getText();
                double weight = Double.parseDouble(weightField.getText());
                double targetWeight = Double.parseDouble(targetWeightField.getText());
                String goal = goalCombo.getValue();

                if (name.isEmpty() || goal == null) {
                    showAlert("모든 정보를 입력해주세요.");
                    return;
                }

                currentUser = new User(name, weight, targetWeight, goal);
                workoutManager.setUser(currentUser);
                workoutManager.saveUser();

                showDashboard(stage);
            } catch (NumberFormatException ex) {
                showAlert("체중은 숫자로 입력해주세요.");
            }
        });

        container.getChildren().addAll(title, 
                new Label("이름:"), nameField, 
                new Label("체중:"), weightField, 
                new Label("목표 체중:"), targetWeightField, 
                new Label("목표:"), goalCombo, submitBtn);
        
        root.getChildren().add(container);

        Scene scene = new Scene(root, 450, 580);
        if (getClass().getResource("/style.css") != null) {
            scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        }
        stage.setScene(scene);
        stage.show();
    }

    private void showDashboard(Stage stage) {
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.getStyleClass().add("root");

        VBox container = new VBox(15);
        container.setPadding(new Insets(25));
        container.getStyleClass().add("main-container");

        Label title = new Label(currentUser.getName() + "님의 대시보드");
        title.getStyleClass().add("title-label");
        
        Label info = new Label(String.format("목표: %s | 현재 체중: %.1fkg", currentUser.getGoal(), currentUser.getWeight()));
        info.getStyleClass().add("info-label");

        Button recommendBtn = new Button("오늘의 루틴 추천받기 (자동)");
        recommendBtn.getStyleClass().add("primary-button");
        recommendBtn.setMaxWidth(Double.MAX_VALUE);

        Button customBtn = new Button("나만의 루틴 만들기 (커스텀)");
        customBtn.getStyleClass().add("primary-button");
        customBtn.setStyle("-fx-background-color: linear-gradient(to right, #f093fb, #f5576c);");
        customBtn.setMaxWidth(Double.MAX_VALUE);

        TextArea routineArea = new TextArea();
        routineArea.setEditable(false);
        routineArea.setPrefHeight(200);

        workoutManager.setUser(currentUser);
        String goal = currentUser.getGoal();
        if ("근비대".equals(goal)) {
            workoutManager.setStrategy(new com.fitness.service.HypertrophyStrategy());
        } else if ("다이어트".equals(goal)) {
            workoutManager.setStrategy(new com.fitness.service.DietStrategy());
        } else {
            workoutManager.setStrategy(new com.fitness.service.BeginnerStrategy());
        }

        recommendBtn.setOnAction(e -> {
            Routine routine = workoutManager.recommendRoutine();
            if (routine != null) {
                displayRoutine(routine, routineArea);
            }
        });

        customBtn.setOnAction(e -> showCustomRoutineDesigner(stage));

        Button startWorkoutBtn = new Button("운동 시작");
        startWorkoutBtn.getStyleClass().add("primary-button");
        startWorkoutBtn.setStyle("-fx-background-color: linear-gradient(to right, #667eea, #764ba2);"); 
        startWorkoutBtn.setMaxWidth(Double.MAX_VALUE);

        startWorkoutBtn.setOnAction(e -> {
            if (routineArea.getText().isEmpty()) {
                showAlert("루틴을 먼저 추천받거나 생성해주세요!");
                return;
            }
            workoutManager.logWorkout();
            showAlert("운동 완료! 기록이 저장되었습니다.\n운동을 완료하여 건강에 한 걸음 더 다가갔습니다!");
        });

        Button resetBtn = new Button("초기화 (프로필 삭제)");
        resetBtn.getStyleClass().add("danger-button");
        resetBtn.setOnAction(e -> {
            java.io.File file = new java.io.File(USER_DATA_FILE);
            if (file.exists()) file.delete();
            currentUser = null;
            showOnboarding(stage);
        });

        container.getChildren().addAll(title, info, recommendBtn, customBtn, routineArea, startWorkoutBtn, resetBtn);
        root.getChildren().add(container);

        Scene scene = new Scene(root, 550, 750);
        if (getClass().getResource("/style.css") != null) {
            scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        }
        stage.setScene(scene);
        stage.show();
    }

    private void displayRoutine(Routine routine, TextArea area) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ").append(routine.getRoutineName()).append(" ===\n");
        for (Exercise ex : routine.getExercises()) {
            sb.append(ex.displayGuide()).append("\n");
        }
        area.setText(sb.toString());
    }

    private void showCustomRoutineDesigner(Stage stage) {
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.getStyleClass().add("root");

        VBox container = new VBox(15);
        container.setPadding(new Insets(25));
        container.getStyleClass().add("main-container");

        Label title = new Label("나만의 루틴 만들기");
        title.getStyleClass().add("title-label");

        Label categoryLabel = new Label("1. 운동 부위 선택:");
        categoryLabel.getStyleClass().add("info-label");

        ComboBox<String> categoryCombo = new ComboBox<>();
        categoryCombo.getItems().addAll("전체", "가슴", "등", "하체", "어깨", "이두", "삼두", "유산소");
        categoryCombo.setValue("전체");
        categoryCombo.setMaxWidth(Double.MAX_VALUE);

        Label exerciseLabel = new Label("2. 운동 종목 선택 (다중 선택 가능):");
        exerciseLabel.getStyleClass().add("info-label");

        ListView<Exercise> exerciseList = new ListView<>();
        java.util.List<Exercise> allExercises = com.fitness.service.ExerciseLibrary.getAllExercises();
        exerciseList.getItems().addAll(allExercises);
        exerciseList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
        // 필터링 로직 추가
        categoryCombo.setOnAction(e -> {
            String selectedCategory = categoryCombo.getValue();
            exerciseList.getItems().clear();
            if ("전체".equals(selectedCategory)) {
                exerciseList.getItems().addAll(allExercises);
            } else {
                for (Exercise ex : allExercises) {
                    if (ex.getTargetMuscle().contains(selectedCategory)) {
                        exerciseList.getItems().add(ex);
                    }
                }
            }
        });

        exerciseList.setCellFactory(lv -> new ListCell<Exercise>() {
            @Override
            protected void updateItem(Exercise item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName() + " [" + item.getTargetMuscle() + "]");
                }
            }
        });

        Button addBtn = new Button("선택한 운동으로 루틴 생성");
        addBtn.getStyleClass().add("primary-button");
        addBtn.setMaxWidth(Double.MAX_VALUE);

        addBtn.setOnAction(e -> {
            var selected = exerciseList.getSelectionModel().getSelectedItems();
            if (selected.isEmpty()) {
                showAlert("최소 하나 이상의 운동을 선택해주세요.");
                return;
            }

            Routine customRoutine = new Routine(currentUser.getName() + "님의 커스텀 루틴");
            for (Exercise ex : selected) {
                customRoutine.addExercise(ex);
            }
            
            workoutManager.setStrategy(user -> customRoutine);
            showDashboard(stage);
        });

        Button backBtn = new Button("돌아가기");
        backBtn.getStyleClass().add("danger-button");
        backBtn.setOnAction(e -> showDashboard(stage));

        container.getChildren().addAll(title, categoryLabel, categoryCombo, exerciseLabel, exerciseList, addBtn, backBtn);
        root.getChildren().add(container);

        Scene scene = new Scene(root, 500, 650);
        if (getClass().getResource("/style.css") != null) {
            scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        }
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("알림");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}