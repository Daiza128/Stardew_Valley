package com.example.stardewvalley.controller;

import com.example.stardewvalley.model.*;
import com.example.stardewvalley.model.achievements.AchievementSystem;
import com.example.stardewvalley.model.world.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Random;

public class PlayController {
    @FXML
    private Canvas board;

    @FXML
    private Button btnHome;

    @FXML
    private Button btnAchievement;

    private GraphicsContext gc;
    private Player player;
    private WorldController worldController;
    private boolean up, down, left, right;
    private AchievementSystem achievementSystem;

    private Thread gameLoopThread;
    private Random random;

    private final int fieldSize = 40;
    private final double width = 1040;
    private final double height = 760;

    private Image backgroundImage;

    public void initialize() {
        achievementSystem = GameState.getAchievementSystem();
        player = GameState.getPlayer();

        backgroundImage = new Image(getClass().getResource("/com/example/stardewvalley/images/achievement.png").toExternalForm());

        btnHome.setOnAction(e -> play());
        btnAchievement.setOnAction(e -> achievement());

        board.setFocusTraversable(true);
        board.requestFocus();

        gc = board.getGraphicsContext2D();
        random = new Random();

        double xChest = random.nextInt((int) (width / fieldSize)) * fieldSize;
        double yChest = random.nextInt((int) (height / fieldSize)) * fieldSize;

        Chest chest = new Chest(xChest, yChest);

        player.setChest(chest);
        player.setBoardSize(board.getWidth(), board.getHeight());

        worldController = new WorldController();

        startGameLoop();
    }

    private void startGameLoop() {
        gameLoopThread = new Thread(() -> {
            long lastTime = System.nanoTime();
            final double nsPerTick = 1_000_000_000.0 / 60.0; // 60 FPS
            double delta = 0;

            while (true) {
                long now = System.nanoTime();
                delta += (now - lastTime) / nsPerTick;
                lastTime = now;

                if (delta >= 1) {
                    updateGameState();
                    render();
                    delta--;
                }

                if (achievementSystem.areAllAchievementsCompleted()) {
                    showWinScreen();
                }

                try {
                    Thread.sleep(10);  // Sleep to maintain 60 FPS
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        gameLoopThread.setDaemon(true);
        gameLoopThread.start();
    }

    private void updateGameState() {
        double deltaTime = 1.0 / 60.0;

        if (up) player.move("up", deltaTime, worldController);
        if (down) player.move("down", deltaTime, worldController);
        if (left) player.move("left", deltaTime, worldController);
        if (right) player.move("right", deltaTime, worldController);

        player.update(System.nanoTime());
        worldController.update(deltaTime, worldController);
    }

    private void render() {
        gc.drawImage(backgroundImage, 0, 0, board.getWidth(), board.getHeight());

        gc.setFill(Color.rgb(255, 255, 255, 0.7));
        gc.fillRect(0, 0, board.getWidth(), board.getHeight());

        worldController.render(gc);
        player.paint(gc);
        worldController.renderPlayerInfo(gc);
    }

    @FXML
    private void onKeyPressed(KeyEvent event) {
        switch (event.getCode()) {
            case UP -> up = true;
            case DOWN -> down = true;
            case LEFT -> left = true;
            case RIGHT -> right = true;
            case SPACE -> {
                worldController.handleAnimalAttacks();
                worldController.handleObstacleRemoval();
            }
            case P -> {
                worldController.handleToolCollection();
                worldController.handleResourceCollection();
            }
            case C -> worldController.handleCultivate();
            case X -> worldController.handleHarvest();
            case Z -> worldController.handleSaveCollection();
            case R -> player.repairTool();
        }

        if (achievementSystem.areAllAchievementsCompleted()) {
            showWinScreen();
        }
    }

    @FXML
    private void onKeyReleased(KeyEvent event) {
        switch (event.getCode()) {
            case UP -> up = false;
            case DOWN -> down = false;
            case LEFT -> left = false;
            case RIGHT -> right = false;
        }
    }

    private void play() {
        loadScene("/com/example/stardewvalley/hello-view.fxml");
    }

    private void achievement() {
        loadScene("/com/example/stardewvalley/achievement.fxml");
    }

    private void loadScene(String fxml) {
        try {
            Stage stage = (Stage) btnHome.getScene().getWindow();
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource(fxml))));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showWinScreen() {
        Stage winStage = new Stage();
        winStage.setTitle("¡Has ganado!");

        Label titleLabel = new Label("¡Felicidades, has completado todos los logros!");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2e8b57;");

        Button btnNewGame = new Button("Iniciar Nuevo Juego");
        btnNewGame.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-color: #32cd32; -fx-text-fill: white; -fx-padding: 10px 20px; -fx-border-radius: 5px;");

        btnNewGame.setOnAction(e -> {
            winStage.close();
            startNewGame();
        });

        VBox vbox = new VBox(20, titleLabel, btnNewGame);
        vbox.setStyle("-fx-background-color: #f0f8ff; -fx-padding: 30px; -fx-alignment: center;");

        vbox.setAlignment(Pos.CENTER);

        Scene winScene = new Scene(vbox, 400, 250);

        winStage.setScene(winScene);
        winStage.initStyle(StageStyle.UTILITY); // Estilo de ventana de utilidad

        winStage.show();
    }

    private void startNewGame() {
        GameState.cleanGame();
        worldController.changeScene();

        loadScene("/com/example/stardewvalley/hello-view.fxml");
    }
}