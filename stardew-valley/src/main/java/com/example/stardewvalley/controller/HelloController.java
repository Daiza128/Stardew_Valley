package com.example.stardewvalley.controller;

import com.example.stardewvalley.model.GameState;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class HelloController {
    @FXML
    private ImageView background;
    @FXML
    private Button btnPlay;

    @FXML
    private Button btnAchievements;

    public void initialize() {
        background.setImage(new Image(getClass().getResource("/com/example/stardewvalley/images/achievement.png").toExternalForm()));

        btnPlay.setOnAction(e -> play());
        btnAchievements.setOnAction(e -> achievement());
    }

    private void play() {
        loadScene("/com/example/stardewvalley/play.fxml");
    }

    private void achievement() {
        loadScene("/com/example/stardewvalley/achievement.fxml");
    }

    private void loadScene(String fxml) {
        try {
            Stage stage = (Stage) btnPlay.getScene().getWindow();
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource(fxml))));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}