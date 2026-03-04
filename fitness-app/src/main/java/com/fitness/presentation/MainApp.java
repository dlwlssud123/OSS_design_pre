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
        Tab logTab = new Tab("기록 확인", createLogView());

        dashTab.setClosable(false);
        routineTab.setClosable(false);
        logTab.setClosable(false);

        mainTabPane.getTabs().addAll(dashTab, routineTab, logTab);

        Scene scene = new Scene(mainTabPane, 600, 800);
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

        Label stats = new Label(String.format("목표: %s | 현재 체중: %.1fkg (목표까지 %.1fkg)", 
                currentUser.getGoal(), currentUser.getWeight(), currentUser.getWeight() - currentUser.getTargetWeight()));
        stats.getStyleClass().add("info-label");

        Button recommendBtn = new Button("목적별 루틴 추천받기");
        recommendBtn.getStyleClass().add("primary-button");
        recommendBtn.setMaxWidth(Double.MAX_VALUE);

        TextArea routineArea = new TextArea();
        routineArea.setEditable(false);
        routineArea.setPrefHeight(300);
        routineArea.setPromptText("루틴을 추천받거나 관리 탭에서 루틴을 선택하세요.");

        workoutManager.setUser(currentUser);
        recommendBtn.setOnAction(e -> {
            applyAutoStrategy();
            Routine r = workoutManager.recommendRoutine();
            displayRoutine(r, routineArea);
        });

        Button startBtn = new Button("현재 루틴으로 운동 시작");
        startBtn.getStyleClass().add("primary-button");
        startBtn.setStyle("-fx-background-color: #6c5ce7;");
        startBtn.setMaxWidth(Double.MAX_VALUE);

        startBtn.setOnAction(e -> {
            if (routineArea.getText().isEmpty()) {
                showAlert("운동할 루틴을 먼저 결정해주세요!");
                return;
            }
            String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            String log = String.format("[%s] 운동 완료! (목표: %s)", now, currentUser.getGoal());
            currentUser.addWorkoutLog(log);
            workoutManager.saveUser();
            showAlert("운동이 기록되었습니다!");
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

    private VBox createLogView() {
        VBox container = new VBox(15);
        container.setPadding(new Insets(25));
        container.getStyleClass().add("main-container");

        Label title = new Label("운동 히스토리");
        title.getStyleClass().add("title-label");

        ListView<String> logList = new ListView<>();
        logList.getItems().addAll(currentUser.getWorkoutLogs());
        logList.setPrefHeight(500);

        Button refreshBtn = new Button("새로고침");
        refreshBtn.setOnAction(e -> {
            logList.getItems().clear();
            logList.getItems().addAll(currentUser.getWorkoutLogs());
        });

        container.getChildren().addAll(title, logList, refreshBtn);
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