package com.example.stardewvalley.controller;

import com.example.stardewvalley.model.GameState;
import com.example.stardewvalley.model.achievements.AchievementSystem;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;

public class AchievementController {

    @FXML
    private ListView<String> achievementList;

    @FXML
    private ImageView achievementBackground;

    private AchievementSystem achievementSystem;

    public void initialize() {
        achievementBackground.setImage(new Image(getClass().getResource("/com/example/stardewvalley/images/achievement.png").toExternalForm()));
        achievementSystem = GameState.getAchievementSystem();

        List<String> achievements = achievementSystem.getAllAchievements()
                .stream()
                .map(a -> a.toString())
                .collect(Collectors.toList());
        achievementList.getItems().setAll(achievements);
    }

    @FXML
    private void backToGame()  {
        loadScene("/com/example/stardewvalley/play.fxml");
    }

    private void loadScene(String fxml) {
        try {
            Stage stage = (Stage) achievementList.getScene().getWindow();
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource(fxml))));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}