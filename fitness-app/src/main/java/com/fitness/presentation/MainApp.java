package com.fitness.presentation;

import com.fitness.domain.*;
import com.fitness.infrastructure.FileRepository;
import com.fitness.service.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MainApp extends Application {

    private static final String USERS_DIR = "users/";
    private WorkoutManager workoutManager;
    private User currentUser;
    private TabPane mainTabPane;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("OSS Fitness Tracker Pro");
        showLoginScreen();
    }

    private void showLoginScreen() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(50));
        root.setAlignment(Pos.CENTER);
        root.getStyleClass().add("root");

        VBox container = new VBox(15);
        container.setPadding(new Insets(30));
        container.getStyleClass().add("main-container");
        container.setAlignment(Pos.CENTER);

        Label title = new Label("Fitness Tracker");
        title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #2d3436;");

        TextField idField = new TextField();
        idField.setPromptText("사용자 아이디 (예: gemini)");
        idField.setMaxWidth(250);

        Button loginBtn = new Button("로그인 / 시작하기");
        loginBtn.getStyleClass().add("primary-button");
        loginBtn.setMaxWidth(250);

        loginBtn.setOnAction(e -> {
            String userId = idField.getText().trim();
            if (userId.isEmpty()) {
                showAlert("아이디를 입력해주세요.");
                return;
            }
            handleLogin(userId);
        });

        container.getChildren().addAll(title, new Label("로그인하여 데이터를 안전하게 보관하세요."), idField, loginBtn);
        root.getChildren().add(container);

        Scene scene = new Scene(root, 500, 400);
        loadCSS(scene);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleLogin(String userId) {
        String filePath = USERS_DIR + userId + ".dat";
        FileRepository repo = new FileRepository(filePath);
        workoutManager = new WorkoutManager(repo);
        
        Object data = repo.load();
        if (data instanceof User) {
            currentUser = (User) data;
            showMainUI();
        } else {
            showOnboarding(userId);
        }
    }

    private void showOnboarding(String userId) {
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.getStyleClass().add("root");

        VBox container = new VBox(15);
        container.setPadding(new Insets(25));
        container.getStyleClass().add("main-container");

        Label title = new Label("첫 방문을 환영합니다!");
        title.getStyleClass().add("title-label");

        TextField nameField = new TextField(); nameField.setPromptText("이름");
        TextField weightField = new TextField(); weightField.setPromptText("현재 체중 (kg)");
        TextField targetField = new TextField(); targetWeightField.setPromptText("목표 체중 (kg)");
        ComboBox<String> goalCombo = new ComboBox<>();
        goalCombo.getItems().addAll("근비대", "다이어트", "체력 증진");
        goalCombo.setPromptText("운동 목적");
        goalCombo.setMaxWidth(Double.MAX_VALUE);

        Button startBtn = new Button("프로필 생성");
        startBtn.getStyleClass().add("primary-button");
        startBtn.setMaxWidth(Double.MAX_VALUE);

        startBtn.setOnAction(e -> {
            try {
                currentUser = new User(nameField.getText(), Double.parseDouble(weightField.getText()), 
                                       Double.parseDouble(targetField.getText()), goalCombo.getValue());
                workoutManager.setUser(currentUser);
                workoutManager.saveUser();
                showMainUI();
            } catch (Exception ex) { showAlert("모든 정보를 올바르게 입력해주세요."); }
        });

        container.getChildren().addAll(title, nameField, weightField, targetField, goalCombo, startBtn);
        root.getChildren().add(container);
        Scene scene = new Scene(root, 450, 600);
        loadCSS(scene);
        primaryStage.setScene(scene);
    }

    private void showMainUI() {
        mainTabPane = new TabPane();
        mainTabPane.getStyleClass().add("root");

        Tab routineTab = new Tab("루틴 관리", createRoutineManagementView());
        Tab recommendTab = new Tab("추천 루틴", createRecommendView());
        Tab reportTab = new Tab("리포트", createReportView());
        Tab myPageTab = new Tab("My Page", createMyPageView());

        routineTab.setClosable(false);
        recommendTab.setClosable(false);
        reportTab.setClosable(false);
        myPageTab.setClosable(false);

        mainTabPane.getTabs().addAll(routineTab, recommendTab, reportTab, myPageTab);

        Scene scene = new Scene(mainTabPane, 800, 900);
        loadCSS(scene);
        primaryStage.setScene(scene);
    }

    // --- Tab Views (Placeholders for next turn) ---
    private VBox createRoutineManagementView() {
        VBox container = new VBox(15);
        container.setPadding(new Insets(25));
        container.getStyleClass().add("main-container");

        Label title = new Label("루틴 보관함");
        title.getStyleClass().add("title-label");

        ListView<Routine> routineListView = new ListView<>();
        routineListView.getItems().addAll(currentUser.getSavedRoutines());
        routineListView.setPrefHeight(300);

        Button createBtn = new Button("+ 새 루틴 만들기");
        createBtn.getStyleClass().add("primary-button");
        createBtn.setMaxWidth(Double.MAX_VALUE);
        createBtn.setOnAction(e -> showCustomRoutineDesigner());

        Button startBtn = new Button("선택한 루틴으로 운동 시작");
        startBtn.getStyleClass().add("primary-button");
        startBtn.setStyle("-fx-background-color: #6c5ce7;");
        startBtn.setMaxWidth(Double.MAX_VALUE);
        startBtn.setOnAction(e -> {
            Routine selected = routineListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                showWorkoutRecordingUI(selected);
            }
        });

        container.getChildren().addAll(title, new Label("내가 만든 루틴:"), routineListView, startBtn, createBtn);
        return container;
    }

    private void showCustomRoutineDesigner() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(25));
        root.getStyleClass().add("root");

        VBox container = new VBox(10);
        container.setPadding(new Insets(20));
        container.getStyleClass().add("main-container");

        Label title = new Label("나만의 루틴 만들기");
        title.getStyleClass().add("title-label");

        TextField routineNameField = new TextField();
        routineNameField.setPromptText("루틴 이름 (예: 퇴근 후 가슴)");

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

        Button saveBtn = new Button("루틴 저장");
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
            showMainUI();
        });

        Button backBtn = new Button("취소");
        backBtn.setOnAction(e -> showMainUI());

        container.getChildren().addAll(title, new Label("루틴 이름:"), routineNameField, new Label("부위 필터:"), categoryCombo, exerciseList, saveBtn, backBtn);
        root.getChildren().add(container);

        Scene scene = new Scene(root, 500, 750);
        loadCSS(scene);
        primaryStage.setScene(scene);
    }

    private void showWorkoutRecordingUI(Routine routine) {
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.getStyleClass().add("root");

        VBox container = new VBox(15);
        container.setPadding(new Insets(25));
        container.getStyleClass().add("main-container");

        Label title = new Label(routine.getRoutineName() + " 진행 중");
        title.getStyleClass().add("title-label");

        // One-tap 기록 로직
        com.fitness.domain.WorkoutSession lastSession = currentUser.getWorkoutSessions().stream()
                .filter(s -> s.getRoutineName().equals(routine.getRoutineName()))
                .reduce((first, second) -> second).orElse(null);

        com.fitness.domain.WorkoutSession currentSession = new com.fitness.domain.WorkoutSession(routine.getRoutineName());
        
        TextField weightInput = new TextField(); 
        TextField repsInput = new TextField(); 
        if (lastSession != null && !lastSession.getSetRecords().isEmpty()) {
            var lastSet = lastSession.getSetRecords().get(0);
            weightInput.setText(String.valueOf(lastSet.getWeight()));
            repsInput.setText(String.valueOf(lastSet.getReps()));
        } else {
            weightInput.setPromptText("무게 (kg)");
            repsInput.setPromptText("횟수 (reps)");
        }
        
        Label timerLabel = new Label("휴식 시간: - ");
        timerLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #e67e22; -fx-font-weight: bold;");
        
        Button addSetBtn = new Button("세트 기록 & 휴식 시작");
        addSetBtn.getStyleClass().add("primary-button");
        addSetBtn.setOnAction(e -> {
            try {
                double w = Double.parseDouble(weightInput.getText());
                int r = Integer.parseInt(repsInput.getText());
                currentSession.addSetRecord(new com.fitness.domain.SetRecord("운동", currentSession.getSetRecords().size() + 1, w, r));
                startRestTimer(timerLabel);
            } catch (Exception ex) { showAlert("숫자를 입력하세요."); }
        });

        VBox guideBox = new VBox(5);
        for (Exercise ex : routine.getExercises()) {
            if (ex.getVideoUrl() != null && !ex.getVideoUrl().isEmpty()) {
                Hyperlink link = new Hyperlink(ex.getName() + " 가이드");
                link.setOnAction(ev -> getHostServices().showDocument(ex.getVideoUrl()));
                guideBox.getChildren().add(link);
            }
        }

        Button finishBtn = new Button("운동 종료 및 저장");
        finishBtn.getStyleClass().add("primary-button");
        finishBtn.setStyle("-fx-background-color: #27ae60;");
        finishBtn.setOnAction(e -> {
            if (currentSession.getSetRecords().isEmpty()) return;
            currentSession.endSession();
            currentUser.addWorkoutSession(currentSession);
            workoutManager.saveUser();
            showMainUI();
            showAlert("기록되었습니다! 총 볼륨: " + currentSession.getTotalVolume() + "kg");
        });

        container.getChildren().addAll(title, guideBox, weightInput, repsInput, addSetBtn, timerLabel, finishBtn);
        root.getChildren().add(container);
        Scene scene = new Scene(root, 500, 700);
        loadCSS(scene);
        primaryStage.setScene(scene);
    }

    private void startRestTimer(Label label) {
        final int[] time = {60};
        javafx.animation.Timeline timeline = new javafx.animation.Timeline(
            new javafx.animation.KeyFrame(javafx.util.Duration.seconds(1), e -> {
                time[0]--;
                label.setText("휴식: " + time[0] + "초 남음");
                if (time[0] <= 0) label.setText("휴식 종료! 다음 세트 Go!");
            })
        );
        timeline.setCycleCount(60);
        timeline.play();
    }
    private VBox createRecommendView() {
        VBox container = new VBox(15);
        container.setPadding(new Insets(25));
        container.getStyleClass().add("main-container");

        Label title = new Label(currentUser.getGoal() + " 추천 루틴");
        title.getStyleClass().add("title-label");

        TextArea routineArea = new TextArea();
        routineArea.setEditable(false);
        routineArea.setPrefHeight(300);

        Button refreshBtn = new Button("추천 루틴 새로고침");
        refreshBtn.getStyleClass().add("primary-button");
        refreshBtn.setOnAction(e -> {
            applyAutoStrategy();
            Routine r = workoutManager.recommendRoutine();
            if (r != null) {
                StringBuilder sb = new StringBuilder("== " + r.getRoutineName() + " ==\n");
                for (Exercise ex : r.getExercises()) sb.append("- ").append(ex.displayGuide()).append("\n");
                routineArea.setText(sb.toString());
            }
        });

        Button startBtn = new Button("이 루틴으로 바로 운동 시작");
        startBtn.getStyleClass().add("primary-button");
        startBtn.setStyle("-fx-background-color: #6c5ce7;");
        startBtn.setOnAction(e -> {
            applyAutoStrategy();
            Routine r = workoutManager.recommendRoutine();
            if (r != null) showWorkoutRecordingUI(r);
        });

        container.getChildren().addAll(title, new Label("회원님의 목적에 맞는 전문가 추천 루틴입니다."), routineArea, refreshBtn, startBtn);
        return container;
    }

    private VBox createReportView() {
        VBox container = new VBox(15);
        container.setPadding(new Insets(25));
        container.getStyleClass().add("main-container");

        Label title = new Label("활동 리포트");
        title.getStyleClass().add("title-label");

        TabPane reportTabs = new TabPane();
        
        VBox sessionBox = new VBox(10);
        sessionBox.setPadding(new Insets(10));
        ListView<String> sessionList = new ListView<>();
        for (WorkoutSession s : currentUser.getWorkoutSessions()) {
            sessionList.getItems().add(0, String.format("[%s] %s | %.1fkg", 
                s.getStartTime().format(DateTimeFormatter.ofPattern("MM/dd")), s.getRoutineName(), s.getTotalVolume()));
        }
        sessionBox.getChildren().add(sessionList);
        Tab sTab = new Tab("운동 히스토리", sessionBox);
        sTab.setClosable(false);

        VBox metricBox = new VBox(10);
        metricBox.setPadding(new Insets(10));
        ListView<String> metricList = new ListView<>();
        for (BodyMetric m : currentUser.getBodyMetrics()) {
            metricList.getItems().add(0, String.format("[%s] 체중: %.1fkg | 근육: %.1fkg", 
                m.getDate().format(DateTimeFormatter.ofPattern("MM/dd")), m.getWeight(), m.getMuscleMass()));
        }
        TextField wIn = new TextField(); wIn.setPromptText("체중(kg)");
        TextField mIn = new TextField(); mIn.setPromptText("골격근량(kg)");
        Button addM = new Button("오늘의 체성분 기록");
        addM.setOnAction(e -> {
            try {
                currentUser.addBodyMetric(new BodyMetric(Double.parseDouble(wIn.getText()), Double.parseDouble(mIn.getText()), 0));
                workoutManager.saveUser();
                showAlert("기록되었습니다!");
                showMainUI();
            } catch (Exception ex) { showAlert("숫자를 입력하세요."); }
        });
        metricBox.getChildren().addAll(metricList, wIn, mIn, addM);
        Tab mTab = new Tab("체성분 변화", metricBox);
        mTab.setClosable(false);

        reportTabs.getTabs().addAll(sTab, mTab);
        container.getChildren().addAll(title, reportTabs);
        return container;
    }

    private VBox createMyPageView() {
        VBox container = new VBox(20);
        container.setPadding(new Insets(25));
        container.getStyleClass().add("main-container");

        Label title = new Label("마이 페이지");
        title.getStyleClass().add("title-label");

        Label nameLabel = new Label("이름: " + currentUser.getName());
        Label goalLabel = new Label("현재 목적: " + currentUser.getGoal());

        ComboBox<String> changeGoal = new ComboBox<>();
        changeGoal.getItems().addAll("근비대", "다이어트", "체력 증진");
        changeGoal.setPromptText("목적 변경하기");
        
        Button saveGoalBtn = new Button("목적 업데이트");
        saveGoalBtn.setOnAction(e -> {
            if (changeGoal.getValue() != null) {
                currentUser.updateProfile(currentUser.getName(), currentUser.getWeight(), currentUser.getTargetWeight(), changeGoal.getValue());
                workoutManager.saveUser();
                showAlert("목적이 변경되었습니다. 추천 루틴 탭을 확인하세요!");
                showMainUI();
            }
        });

        Button logoutBtn = new Button("로그아웃");
        logoutBtn.getStyleClass().add("danger-button");
        logoutBtn.setOnAction(e -> showLoginScreen());

        container.getChildren().addAll(title, nameLabel, goalLabel, new Separator(), new Label("목적 변경 (기존 데이터는 유지됩니다):"), changeGoal, saveGoalBtn, new Separator(), logoutBtn);
        return container;
    }

    private void applyAutoStrategy() {
        String goal = currentUser.getGoal();
        if ("근비대".equals(goal)) workoutManager.setStrategy(new com.fitness.service.HypertrophyStrategy());
        else if ("다이어트".equals(goal)) workoutManager.setStrategy(new com.fitness.service.DietStrategy());
        else workoutManager.setStrategy(new com.fitness.service.BeginnerStrategy());
    }

    private void loadCSS(Scene scene) {
        if (getClass().getResource("/style.css") != null) {
            scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        }
    }

    private void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("알림");
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    public static void main(String[] args) { launch(args); }
}