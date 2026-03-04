module com.fitness {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;

    exports com.fitness.presentation;
    exports com.fitness.domain;
    exports com.fitness.service;
}