package com.example.stardewvalley.model;

import com.example.stardewvalley.model.achievements.AchievementSystem;
import com.example.stardewvalley.model.world.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorldController {
    private AchievementSystem achievements;

    private List<Animal> animals;
    private List<Obstacle> obstacles;
    private List<Tool> tools;
    private List<CropField> cropFields;
    private List<Resource> resources;
    private Door door;
    private Player player;

    private final Random random = new Random();
    private final int fieldSize = 40;
    private final double width = 1040;
    private final double height = 760;

    public WorldController(){
        animals = new ArrayList<>();
        obstacles = new ArrayList<>();
        tools = new ArrayList<>();
        cropFields = new ArrayList<>();
        resources = new ArrayList<>();
        this.achievements = GameState.getAchievementSystem();
        this.player = GameState.getPlayer();

        initializeWorld();
    }

    public void update(double deltaTime, WorldController worldController) {
        for (Animal animal : animals) {
            animal.update(deltaTime, worldController);
        }

        for (CropField crop : cropFields) {
            crop.update(deltaTime);
        }

        if (door.isPlayerOnDoor(player.getX(), player.getY(), 30, 40)) {
            achievements.updateAchievements("CHANGE SCENE");
            changeScene();
        }
    }

    public void renderPlayerInfo(GraphicsContext gc) {
        double boxWidth = 180;
        double boxHeight = 150;

        gc.setFill(Color.color(0, 0, 0, 0.5));
        gc.fillRect(0, 0, boxWidth, boxHeight);

        Font normalFont = Font.font("Arial", 12);
        Font boldFont = Font.font("Arial", FontWeight.BOLD, 12);

        int yOffset = 20;

        gc.setFont(boldFont);
        gc.setFill(Color.WHITE);
        gc.fillText("TOOLS:", 20, yOffset);

        yOffset += 20;

        gc.setFont(normalFont);
        for (Tool tool : player.getInventory()) {
            gc.fillText(tool.getType().name() + " - Durability: " + tool.getDurability(), 20, yOffset);
            yOffset += 20;
        }

        gc.setFont(boldFont);
        gc.fillText("RESOURCES:", 20, yOffset);

        yOffset += 20;

        gc.setFont(normalFont);
        gc.fillText("WOOD - " + player.getResourceSummary("WOOD"), 20, yOffset);
        yOffset += 20;

        gc.fillText("GEM - " + player.getResourceSummary("GEM"), 20, yOffset);
        yOffset += 20;

        gc.setFont(boldFont);
        gc.fillText("SCORE:", 20, yOffset);

        gc.setFont(normalFont);
        gc.fillText(String.valueOf(player.getScore()), 70, yOffset);
    }

    public void changeScene() {
        animals.clear();
        obstacles.clear();
        tools.clear();
        cropFields.clear();
        resources.clear();

        initializeWorld();
    }

    public void initializeWorld(){
        initializeCropFields();
        initializeObstaclesAndTools();
        initializeDoor();

        while (!isPositionFree(player.getX(), player.getY(), 40, 40, true)) {
            changeScene();
        }
    }

    private void initializeDoor() {
        double doorX = random.nextInt((int) (width / fieldSize)) * fieldSize;
        double doorY = random.nextInt((int) (height / fieldSize)) * fieldSize;

        while (!isPositionFree(doorX, doorY, 40, 40, false)) {
            doorX = random.nextInt((int) (width / fieldSize)) * fieldSize;
            doorY = random.nextInt((int) (height / fieldSize)) * fieldSize;
        }

        door = new Door(doorX, doorY);
    }

    public void render(GraphicsContext gc){
        for (Obstacle obstacle : obstacles) {
            obstacle.paint(gc);
        }

        for (Animal animal : animals) {
            animal.paint(gc);
        }

        for (Tool tool : tools) {
            gc.drawImage(tool.getSprite(), tool.getX(), tool.getY());
        }

        for (CropField field : cropFields) {
            field.paint(gc);
        }

        for (Resource resource : resources){
            resource.paint(gc);
        }

        if (door != null) {
            door.paint(gc);
        }
    }

    private void spawnObstacles(int count, String obstacle, ToolType requiredTool) {
        Image obstacleSprite = new Image(getClass().getResourceAsStream("/com/example/stardewvalley/sprites/obstacle/" + obstacle + ".png"));
        for (int i = 0; i < count; i++) {
            double x = random.nextInt((int) (width / fieldSize)) * fieldSize;
            double y = random.nextInt((int) (height / fieldSize)) * fieldSize;

            if (isPositionFree(x, y, 40, 40, false)) {
                obstacles.add(new Obstacle(x, y, obstacleSprite, true, requiredTool));
            }
        }
    }

    private void spawnTools(int count, ToolType tool) {
        Image toolSprite = new Image(getClass().getResourceAsStream("/com/example/stardewvalley/sprites/tool/" + tool.name().toLowerCase() + ".png"));
        for (int i = 0; i < count; i++) {
            double x = random.nextInt((int) (width / fieldSize)) * fieldSize;
            double y = random.nextInt((int) (height / fieldSize)) * fieldSize;

            while(x == 0 && y == 0){
                x = random.nextInt((int) (width / fieldSize)) * fieldSize;
                y = random.nextInt((int) (height / fieldSize)) * fieldSize;
            }

            if (isPositionFree(x, y, 40, 40, false)) {
                tools.add(new Tool(x, y, tool, 5, toolSprite));
            }
        }
    }

    private void spawnResources(int count, ToolType tool, String resource){
        Image sprite = new Image(getClass().getResourceAsStream("/com/example/stardewvalley/sprites/resources/" + resource + ".png"));
        for (int i = 0; i < count; i++) {
            double x = random.nextInt((int) (width / fieldSize)) * fieldSize;
            double y = random.nextInt((int) (height / fieldSize)) * fieldSize;

            while(x == 0 && y == 0){
                x = random.nextInt((int) (width / fieldSize)) * fieldSize;
                y = random.nextInt((int) (height / fieldSize)) * fieldSize;
            }

            if (isPositionFree(x, y, 40, 40, false)) {
                resources.add(new Resource(x, y, sprite, tool, resource.toUpperCase()));
            }
        }
    }

    private void spawnAnimals(int count) {
        for (int i = 0; i < count; i++) {
            double x = random.nextInt((int) (width / fieldSize)) * fieldSize;
            double y = random.nextInt((int) (height / fieldSize)) * fieldSize;

            while(x == 0 && y == 0){
                x = random.nextInt((int) (width / fieldSize)) * fieldSize;
                y = random.nextInt((int) (height / fieldSize)) * fieldSize;
            }

            if (isPositionFree(x, y, 40, 40, false)) {
                animals.add(new Animal(x, y, width, height));
            }
        }
    }

    private void initializeCropFields() {
        cropFields = new ArrayList<>();
        int maxFields = 5 + random.nextInt(5);
        double fieldSize = 40;

        for (int i = 0; i < maxFields; i++) {
            int rows = 2 + random.nextInt(3);
            int cols = 3 + random.nextInt(4);

            double startX = random.nextInt((int) (width / fieldSize - cols)) * fieldSize;
            double startY = random.nextInt((int) (height / fieldSize - rows)) * fieldSize;

            while(startX == 0 && startY == 0){
                startX = random.nextInt((int) (width / fieldSize - cols)) * fieldSize;
                startY = random.nextInt((int) (height / fieldSize - rows)) * fieldSize;
            }

            if (isAreaFree(startX, startY, cols * fieldSize, rows * fieldSize)) {
                for (int row = 0; row < rows; row++) {
                    for (int col = 0; col < cols; col++) {
                        double x = startX + col * fieldSize;
                        double y = startY + row * fieldSize;
                        cropFields.add(new CropField(x, y));
                    }
                }
            }
        }
    }

    private void initializeObstaclesAndTools() {
        obstacles = new ArrayList<>();
        tools = new ArrayList<>();
        animals = new ArrayList<>();

        // Generar obstáculos
        int obstacleCount = 30 + random.nextInt(10); // De 20 a 30 obstáculos
        spawnObstacles(obstacleCount / 2, "grass", ToolType.PICKAXE);
        spawnObstacles(obstacleCount / 2, "mushroom", ToolType.AXE);

        // Generar herramientas
        int toolsCount = 6 + random.nextInt(4);
        spawnTools(toolsCount / 2, ToolType.PICKAXE);
        spawnTools(toolsCount / 2, ToolType.AXE);

        // Generar recursos
        int resourcesCount = 20 + random.nextInt(10);
        spawnResources(resourcesCount / 2, ToolType.PICKAXE, "wood");
        spawnResources(resourcesCount / 2, ToolType.AXE, "gem");

        // Generar animales
        spawnAnimals(7);
    }

    private boolean isAreaFree(double startX, double startY, double width, double height) {
        for (CropField field : cropFields) {
            if (isOverlapping(startX, startY, width, height, field.getX(), field.getY(), 40, 40)) {
                return false;
            }
        }

        if (isOverlapping(startX, startY, width, height, player.getChest().getX(), player.getChest().getY(), 40, 40)) {
            return false;
        }

        if (door != null && isOverlapping(startX, startY, width, height, door.getX(), door.getY(), 40, 40)) {
            return false;
        }

        return true;
    }

    private boolean isOverlapping(double x1, double y1, double width1, double height1,
                                  double x2, double y2, double width2, double height2) {
        return x1 < x2 + width2 && x1 + width1 > x2 &&
                y1 < y2 + height2 && y1 + height1 > y2;
    }

    public boolean isPositionFree(double x, double y, double width, double height, boolean isPlayer) {
        for (CropField field : cropFields) {
            if (isOverlapping(x, y, width, height, field.getX(), field.getY(), 40, 40)) {
                if (!isPlayer) {
                    return false;
                }
            }
        }

        for (Obstacle obstacle : obstacles) {
            if (isOverlapping(x, y, width, height, obstacle.getX(), obstacle.getY(), 40, 40)) {
                return false;
            }
        }

        for (Tool tool : tools) {
            if (isOverlapping(x, y, width, height, tool.getX(), tool.getY(), 40, 40)) {
                return false;
            }
        }

        for (Animal otherAnimal : animals) {
            if (isOverlapping(x, y, width, height, otherAnimal.getX(), otherAnimal.getY(), 40, 40)) {
                return false;
            }
        }

        for (Resource resource : resources) {
            if(isOverlapping(x, y, width, height, resource.getX(), resource.getY(), 40, 40)) {
                return false;
            };
        }

        if (isOverlapping(x, y, width, height, player.getChest().getX(), player.getChest().getY(), 40, 40)) {
            return false;
        }

        if (door != null && isOverlapping(x, y, width, height, door.getX(), door.getY(), 40, 40)) {
            if (!isPlayer) {
                return false;
            }
        }
        
        return true;
    }

    public void handleToolCollection() {
        for (Tool tool : new ArrayList<>(tools)) {
            if (player.isNear(tool.getX(), tool.getY())) {
                for (Tool inventoryTool : player.getInventory()) {
                    if (inventoryTool.getType() == tool.getType()) {
                        inventoryTool.increaseDurability(tool.getDurability());
                        tools.remove(tool);
                        System.out.println("Combined tool durability: " + inventoryTool.getDurability());
                        return;
                    }
                }
                player.collectTool(tool);
                tools.remove(tool);
                System.out.println("Picked up tool: " + tool.getType().name());
                return;
            }
        }
    }

    public void handleResourceCollection() {
        for (Resource resource : resources) {
            if (player.isNear(resource.getX(), resource.getY())) {
                player.addResource(resource);
                resources.remove(resource);
                System.out.println("Resource collected to repair: "+ resource.getType().name());
                return;
            }
        }
    }

    public void handleObstacleRemoval() {
        for (Obstacle obstacle : new ArrayList<>(obstacles)) { // Evitar modificación concurrente
            if (player.isFacing(obstacle.getX(), obstacle.getY()) && player.isNear(obstacle.getX(), obstacle.getY())) {
                if (obstacle.isRemovable() && player.hasTool(obstacle.getRequiredTool())) {
                    obstacles.remove(obstacle);
                    System.out.println("Obstacle removed!");
                    player.addScore(5);
                    achievements.updateAchievements("REMOVE OBSTACLE");
                } else {
                    System.out.println("You need a " + obstacle.getRequiredTool() + " to remove this!");
                }
                break;
            }
        }
    }

    public void handleCultivate() {
        for (CropField field : cropFields) {
            if (field.contains(player.getX(), player.getY()) && field.isEmpty()) {
                field.plant();
                achievements.updateAchievements("CULTIVATE");
                player.addScore(5);
                System.out.println("Planted " + field.getCropType() + " en: " + field.getX() + ", " + field.getY());
                break;
            }
        }
    }

    public void handleHarvest() {
        for (CropField field : cropFields) {
            if (field.contains(player.getX(), player.getY()) && field.isMature()) {
                player.addCollect(field);
                field.harvest();
                achievements.updateAchievements("HARVEST");
                player.addScore(5);
                System.out.println("Crop harvested in: " + field.getX() + ", " + field.getY());
                break;
            }
        }
    }

    public void handleAnimalAttacks() {
        for (Animal animal : new ArrayList<>(animals)) {
            if (player.isFacing(animal.getX(), animal.getY()) && player.isNear(animal.getX(), animal.getY())) {
                boolean dead = animal.takeDamage();
                System.out.println("Hit animal! Remaining life: " + animal.getHealth());
                achievements.updateAchievements("HIT ANIMAL");
                player.addScore(5);
                if (dead) {
                    System.out.println("Animal removed!");
                    player.addScore(20);
                    animals.remove(animal);
                    achievements.updateAchievements("REMOVE ANIMAL");
                }
                break;
            }
        }
    }

    public void handleSaveCollection(){
        if(player.saveCollect()){
            achievements.updateAchievements("STORE");
        }
    }
}
