package com.example;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/LogInScene.fxml")));

            Scene scene = new Scene(root);

            primaryStage.setTitle("Github Profile Viewer");
            primaryStage.setScene(scene);

            primaryStage.show();

            DatabaseManager.createTables(DatabaseManager.getURL());
        } catch (IOException e) {
            Logger.logs(null, "Couldn't load the loginScene", Arrays.asList(e));
        }
    }
}
