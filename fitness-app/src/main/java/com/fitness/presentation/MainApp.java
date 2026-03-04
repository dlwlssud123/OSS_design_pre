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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MainApp extends Application {

    private static final String USER_DATA_FILE = "user_data.dat";
    private WorkoutManager workoutManager;
    private User currentUser;
    private TabPane mainTabPane;

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
        primaryStage.setTitle("OSS Fitness Tracker Pro");
        if (currentUser == null) {
            showOnboarding(primaryStage);
        } else {
            showMainUI(primaryStage);
        }
    }

    private void showOnboarding(Stage stage) {
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.getStyleClass().add("root");

        VBox container = new VBox(15);
        container.setPadding(new Insets(25));
        container.getStyleClass().add("main-container");

        Label title = new Label("프로필 설정");
        title.getStyleClass().add("title-label");

        TextField nameField = new TextField();
        nameField.setPromptText("이름 입력");

        TextField weightField = new TextField();
        weightField.setPromptText("현재 체중 (kg)");

        TextField targetWeightField = new TextField();
        targetWeightField.setPromptText("목표 체중 (kg)");

        ComboBox<String> goalCombo = new ComboBox<>();
        goalCombo.getItems().addAll("근비대", "다이어트", "체력 증진");
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
                showMainUI(stage);
            } catch (Exception ex) {
                showAlert("입력값을 확인해주세요.");
            }
        });

        container.getChildren().addAll(title, nameField, weightField, targetWeightField, goalCombo, submitBtn);
        root.getChildren().add(container);

        Scene scene = new Scene(root, 450, 600);
        loadCSS(scene);
        stage.setScene(scene);
        stage.show();
    }

    private void showMainUI(Stage stage) {
        mainTabPane = new TabPane();
        mainTabPane.getStyleClass().add("root");

        Tab dashTab = new Tab("대시보드", createDashboardView(stage));
        Tab routineTab = new Tab("루틴 관리", createRoutineManagementView(stage));
        Tab logTab = new Tab("운동 기록", createLogView());
        Tab metricTab = new Tab("체성분 관리", createBodyMetricView());

        dashTab.setClosable(false);
        routineTab.setClosable(false);
        logTab.setClosable(false);
        metricTab.setClosable(false);

        mainTabPane.getTabs().addAll(dashTab, routineTab, logTab, metricTab);

        Scene scene = new Scene(mainTabPane, 700, 850);
        loadCSS(scene);
        stage.setScene(scene);
        stage.show();
    }

    private VBox createDashboardView(Stage stage) {
        VBox container = new VBox(20);
        container.setPadding(new Insets(25));
        container.getStyleClass().add("main-container");

        Label title = new Label(currentUser.getName() + "님, 안녕하세요!");
        title.getStyleClass().add("title-label");

        Label stats = new Label(String.format("목표: %s | 현재 체중: %.1fkg", currentUser.getGoal(), currentUser.getWeight()));
        stats.getStyleClass().add("info-label");

        Button recommendBtn = new Button("목적별 루틴 추천받기");
        recommendBtn.getStyleClass().add("primary-button");
        recommendBtn.setMaxWidth(Double.MAX_VALUE);

        TextArea routineArea = new TextArea();
        routineArea.setEditable(false);
        routineArea.setPrefHeight(200);
        routineArea.setPromptText("루틴을 추천받거나 관리 탭에서 루틴을 선택하세요.");

        workoutManager.setUser(currentUser);
        recommendBtn.setOnAction(e -> {
            applyAutoStrategy();
            Routine r = workoutManager.recommendRoutine();
            displayRoutine(r, routineArea);
        });

        Button startBtn = new Button("상세 기록하며 운동 시작");
        startBtn.getStyleClass().add("primary-button");
        startBtn.setStyle("-fx-background-color: #6c5ce7;");
        startBtn.setMaxWidth(Double.MAX_VALUE);

        startBtn.setOnAction(e -> {
            Routine currentRoutine = workoutManager.recommendRoutine();
            if (currentRoutine == null || routineArea.getText().isEmpty()) {
                showAlert("운동할 루틴을 먼저 결정해주세요!");
                return;
            }
            showWorkoutRecordingUI(stage, currentRoutine);
        });

        Button resetBtn = new Button("설정 초기화");
        resetBtn.getStyleClass().add("danger-button");
        resetBtn.setOnAction(e -> {
            new java.io.File(USER_DATA_FILE).delete();
            currentUser = null;
            showOnboarding(stage);
        });

        container.getChildren().addAll(title, stats, recommendBtn, routineArea, startBtn, resetBtn);
        return container;
    }

    private VBox createRoutineManagementView(Stage stage) {
        VBox container = new VBox(15);
        container.setPadding(new Insets(25));
        container.getStyleClass().add("main-container");

        Label title = new Label("나만의 루틴 관리");
        title.getStyleClass().add("title-label");

        ListView<Routine> routineListView = new ListView<>();
        routineListView.getItems().addAll(currentUser.getSavedRoutines());
        routineListView.setPrefHeight(200);

        Button createBtn = new Button("+ 새 루틴 만들기");
        createBtn.getStyleClass().add("primary-button");
        createBtn.setMaxWidth(Double.MAX_VALUE);
        createBtn.setOnAction(e -> showCustomRoutineDesigner(stage));

        Button selectBtn = new Button("선택한 루틴 대시보드에 적용");
        selectBtn.getStyleClass().add("primary-button");
        selectBtn.setStyle("-fx-background-color: #00b894;");
        selectBtn.setMaxWidth(Double.MAX_VALUE);
        selectBtn.setOnAction(e -> {
            Routine selected = routineListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                workoutManager.setStrategy(user -> selected);
                mainTabPane.getSelectionModel().select(0); // 대시보드 탭으로 이동
                showAlert(selected.getRoutineName() + " 루틴이 적용되었습니다.");
            }
        });

        container.getChildren().addAll(title, new Label("저장된 루틴 목록:"), routineListView, selectBtn, createBtn);
        return container;
    }

    private void showWorkoutRecordingUI(Stage stage, Routine routine) {
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.getStyleClass().add("root");

        VBox container = new VBox(15);
        container.setPadding(new Insets(25));
        container.getStyleClass().add("main-container");

        Label title = new Label(routine.getRoutineName() + " 진행 중");
        title.getStyleClass().add("title-label");

        com.fitness.domain.WorkoutSession session = new com.fitness.domain.WorkoutSession(routine.getRoutineName());
        
        TextField weightInput = new TextField(); weightInput.setPromptText("무게 (kg)");
        TextField repsInput = new TextField(); repsInput.setPromptText("횟수 (reps)");
        
        ListView<String> currentSessionSets = new ListView<>();
        currentSessionSets.setPrefHeight(200);

        Button addSetBtn = new Button("세트 기록 추가");
        addSetBtn.setOnAction(e -> {
            try {
                double w = Double.parseDouble(weightInput.getText());
                int r = Integer.parseInt(repsInput.getText());
                com.fitness.domain.SetRecord record = new com.fitness.domain.SetRecord("운동", session.getSetRecords().size() + 1, w, r);
                session.addSetRecord(record);
                currentSessionSets.getItems().add(String.format("Set %d: %.1fkg x %d", record.getSetNumber(), w, r));
            } catch (Exception ex) { showAlert("숫자를 입력하세요."); }
        });

        Button finishBtn = new Button("운동 종료 및 저장");
        finishBtn.getStyleClass().add("primary-button");
        finishBtn.setMaxWidth(Double.MAX_VALUE);
        finishBtn.setOnAction(e -> {
            if (session.getSetRecords().isEmpty()) {
                showAlert("기록된 세트가 없습니다.");
                return;
            }
            session.endSession();
            currentUser.addWorkoutSession(session);
            workoutManager.saveUser();
            showMainUI(stage);
            showAlert("운동이 기록되었습니다! 총 볼륨: " + session.getTotalVolume() + "kg");
        });

        container.getChildren().addAll(title, new Label("현재 세트 기록:"), weightInput, repsInput, addSetBtn, currentSessionSets, finishBtn);
        root.getChildren().add(container);

        Scene scene = new Scene(root, 500, 700);
        loadCSS(scene);
        stage.setScene(scene);
    }

    private VBox createLogView() {
        VBox container = new VBox(15);
        container.setPadding(new Insets(25));
        container.getStyleClass().add("main-container");

        Label title = new Label("상세 운동 히스토리");
        title.getStyleClass().add("title-label");

        TextArea logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setPrefHeight(500);

        Button refreshBtn = new Button("내역 불러오기");
        refreshBtn.getStyleClass().add("primary-button");
        refreshBtn.setOnAction(e -> {
            StringBuilder sb = new StringBuilder();
            List<com.fitness.domain.WorkoutSession> sessions = currentUser.getWorkoutSessions();
            for (int i = sessions.size() - 1; i >= 0; i--) {
                com.fitness.domain.WorkoutSession s = sessions.get(i);
                sb.append(String.format("[%s] %s\n", s.getStartTime().format(DateTimeFormatter.ofPattern("MM/dd HH:mm")), s.getRoutineName()));
                sb.append(String.format("총 볼륨: %.1f kg\n", s.getTotalVolume()));
                sb.append("----------------------------\n");
            }
            logArea.setText(sb.toString());
        });

        container.getChildren().addAll(title, logArea, refreshBtn);
        return container;
    }

    private VBox createBodyMetricView() {
        VBox container = new VBox(15);
        container.setPadding(new Insets(25));
        container.getStyleClass().add("main-container");

        Label title = new Label("체성분 변화 추적");
        title.getStyleClass().add("title-label");

        TextField weightInput = new TextField(); weightInput.setPromptText("체중 (kg)");
        TextField muscleInput = new TextField(); muscleInput.setPromptText("골격근량 (kg)");
        TextField fatInput = new TextField(); fatInput.setPromptText("체지방률 (%)");

        Button addBtn = new Button("기록 추가");
        addBtn.getStyleClass().add("primary-button");
        
        ListView<String> metricList = new ListView<>();
        metricList.setPrefHeight(300);

        Runnable refreshMetrics = () -> {
            metricList.getItems().clear();
            for (com.fitness.domain.BodyMetric m : currentUser.getBodyMetrics()) {
                String date = m.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                metricList.getItems().add(String.format("[%s] 체중: %.1fkg | 근육: %.1fkg | 지방: %.1f%%", 
                        date, m.getWeight(), m.getMuscleMass(), m.getFatPercentage()));
            }
        };

        addBtn.setOnAction(e -> {
            try {
                double w = Double.parseDouble(weightInput.getText());
                double m = Double.parseDouble(muscleInput.getText());
                double f = Double.parseDouble(fatInput.getText());
                currentUser.addBodyMetric(new com.fitness.domain.BodyMetric(w, m, f));
                workoutManager.saveUser();
                refreshMetrics.run();
                showAlert("체성분 정보가 업데이트되었습니다.");
            } catch (Exception ex) { showAlert("숫자를 올바르게 입력해주세요."); }
        });

        refreshMetrics.run();
        container.getChildren().addAll(title, new Label("새 기록 입력:"), weightInput, muscleInput, fatInput, addBtn, new Label("이력:"), metricList);
        return container;
    }

    private void showCustomRoutineDesigner(Stage stage) {
        VBox root = new VBox(15);
        root.setPadding(new Insets(25));
        root.getStyleClass().add("root");

        VBox container = new VBox(10);
        container.setPadding(new Insets(20));
        container.getStyleClass().add("main-container");

        TextField routineNameField = new TextField();
        routineNameField.setPromptText("루틴 이름을 입력하세요 (예: 월요일 가슴운동)");

        ComboBox<String> categoryCombo = new ComboBox<>();
        categoryCombo.getItems().addAll("전체", "가슴", "등", "하체", "어깨", "이두", "삼두", "유산소");
        categoryCombo.setValue("전체");
        categoryCombo.setMaxWidth(Double.MAX_VALUE);

        ListView<Exercise> exerciseList = new ListView<>();
        List<Exercise> all = com.fitness.service.ExerciseLibrary.getAllExercises();
        exerciseList.getItems().addAll(all);
        exerciseList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        categoryCombo.setOnAction(e -> {
            String cat = categoryCombo.getValue();
            exerciseList.getItems().clear();
            if ("전체".equals(cat)) exerciseList.getItems().addAll(all);
            else {
                for(Exercise ex : all) if(ex.getTargetMuscle().contains(cat)) exerciseList.getItems().add(ex);
            }
        });

        Button saveBtn = new Button("루틴 저장 및 닫기");
        saveBtn.getStyleClass().add("primary-button");
        saveBtn.setMaxWidth(Double.MAX_VALUE);
        saveBtn.setOnAction(e -> {
            String rName = routineNameField.getText();
            var selected = exerciseList.getSelectionModel().getSelectedItems();
            if (rName.isEmpty() || selected.isEmpty()) {
                showAlert("이름과 운동을 선택해주세요.");
                return;
            }
            Routine nr = new Routine(rName);
            for(Exercise ex : selected) nr.addExercise(ex);
            currentUser.addRoutine(nr);
            workoutManager.saveUser();
            showMainUI(stage);
        });

        Button backBtn = new Button("취소");
        backBtn.setOnAction(e -> showMainUI(stage));

        container.getChildren().addAll(new Label("루틴 명:"), routineNameField, new Label("부위 필터:"), categoryCombo, exerciseList, saveBtn, backBtn);
        root.getChildren().add(container);

        Scene scene = new Scene(root, 500, 700);
        loadCSS(scene);
        stage.setScene(scene);
    }

    private void applyAutoStrategy() {
        String goal = currentUser.getGoal();
        if ("근비대".equals(goal)) workoutManager.setStrategy(new com.fitness.service.HypertrophyStrategy());
        else if ("다이어트".equals(goal)) workoutManager.setStrategy(new com.fitness.service.DietStrategy());
        else workoutManager.setStrategy(new com.fitness.service.BeginnerStrategy());
    }

    private void displayRoutine(Routine routine, TextArea area) {
        if (routine == null) return;
        StringBuilder sb = new StringBuilder();
        sb.append("== ").append(routine.getRoutineName()).append(" ==\n");
        for (Exercise ex : routine.getExercises()) sb.append("- ").append(ex.displayGuide()).append("\n");
        area.setText(sb.toString());
    }

    private void loadCSS(Scene scene) {
        if (getClass().getResource("/style.css") != null) {
            scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        }
    }

    private void showAlert(String message) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("알림");
        a.setHeaderText(null);
        a.setContentText(message);
        a.showAndWait();
    }

    public static void main(String[] args) { launch(args); }
}