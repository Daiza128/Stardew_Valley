package com.example.stardewvalley.model.world;

import com.example.stardewvalley.model.WorldController;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.Random;

public class Animal {
    private double x, y;
    private double targetX, targetY;
    private double speed;
    private String direction;
    private boolean isMoving;
    private double boardWidth, boardHeight;
    private Image sprite;
    private int fieldSize;
    private int health;
    private AnimalType type;
    private final Random random = new Random();

    public Animal(double x, double y, double boardWidth, double boardHeight) {
        this.x = x;
        this.y = y;
        this.targetX = x;
        this.targetY = y;
        this.fieldSize = 40;
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.isMoving = false;
        this.direction = "down";

        getRandomAnimal();
    }

    public void update(double deltaTime, WorldController worldController) {
        if (!isMoving && random.nextDouble() < 0.02) {
            direction = changeDirection();

            double proposedTargetX = x;
            double proposedTargetY = y;

            switch (direction) {
                case "up" -> proposedTargetY = Math.max(0, y - fieldSize);
                case "down" -> proposedTargetY = Math.min(boardHeight - fieldSize, y + fieldSize);
                case "left" -> proposedTargetX = Math.max(0, x - fieldSize);
                case "right" -> proposedTargetX = Math.min(boardWidth - fieldSize, x + fieldSize);
            }

            if (worldController.isPositionFree(proposedTargetX, proposedTargetY, 40, 40, false)) {
                targetX = proposedTargetX;
                targetY = proposedTargetY;
                isMoving = true;
            }
        }

        if (isMoving) {
            double distanceX = targetX - x;
            double distanceY = targetY - y;
            double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);

            if (distance > 0) {
                double step = speed * deltaTime;
                if (step >= distance) {
                    x = targetX;
                    y = targetY;
                    isMoving = false;
                } else {
                    x += (distanceX / distance) * step;
                    y += (distanceY / distance) * step;
                }
            }
        }
    }

    public String changeDirection() {
        return switch (random.nextInt(4)) {
            case 0 -> "up";
            case 1 -> "down";
            case 2 -> "left";
            case 3 -> "right";
            default -> direction;
        };
    }

    public void paint(GraphicsContext gc) {
        gc.drawImage(sprite, x, y);
    }

    public boolean takeDamage() {
        health--;
        return health <= 0; // Devuelve true si el animal muere
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getHealth() {
        return health;
    }

    public void getRandomAnimal() {
        AnimalType[] animal = AnimalType.values();
        Random random = new Random();
        type = animal[random.nextInt(animal.length)];

        switch (type) {
            case DINO -> {
                health = 5;
            }
            case HEN -> {
                health = 4;
            }
            case DUCK -> {
                health = 3;
            }
            case RABBIT -> {
                health = 2;
            }
        }

        speed = health * 100;

        String path = "/com/example/stardewvalley/sprites/animal/" + type.name().toLowerCase() + ".png";
        sprite = new Image(getClass().getResourceAsStream(path), -1, 40, true, true);
    }

    public void setBoardSize(double boardWidth, double boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getSpeed() {
        return speed;
    }
}