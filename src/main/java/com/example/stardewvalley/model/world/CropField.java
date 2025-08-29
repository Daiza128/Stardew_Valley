package com.example.stardewvalley.model.world;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.Random;

public class CropField {
    private final double x, y;
    private final double size = 40;
    private boolean planted;
    private boolean harvested;
    private Image[] growthStages;
    private int currentStage;
    private double growthTimer;
    private CropType cropType;
    private final double growthInterval = 3.0;

    public CropField(double x, double y) {
        this.x = x;
        this.y = y;
        this.cropType = getRandomCrop();
        this.planted = false;
        this.harvested = false;
        this.growthStages = loadCropSprites();
        this.currentStage = 0;
        this.growthTimer = 0;
    }

    public static CropType getRandomCrop() {
        CropType[] crop = CropType.values();
        Random random = new Random();
        return crop[random.nextInt(crop.length)];
    }

    private Image[] loadCropSprites() {
        Image[] sprites = new Image[6];
        for (int i = 0; i < 6; i++) {
            String path = "/com/example/stardewvalley/sprites/crop/" + cropType.name().toLowerCase() + (i + 1) + ".png";
            sprites[i] = new Image(getClass().getResourceAsStream(path));
        }
        return sprites;
    }

    public void plant() {
        if (!planted) {
            this.planted = true;
            this.harvested = false;
            this.growthStages = loadCropSprites();
            this.currentStage = 0;
            this.growthTimer = 0;
        }
    }

    public void harvest() {
        if (isMature()) {
            this.harvested = true;
            this.planted = false;
            this.growthStages = null;
            this.currentStage = 0;
            this.growthTimer = 0;
        }
    }

    public boolean isEmpty() {
        return !planted && !harvested;
    }

    public boolean isPlanted() {
        return planted;
    }

    public boolean isMature() {
        return planted && currentStage == growthStages.length - 1;
    }

    public void update(double deltaTime) {
        if (planted && !isMature()) {
            growthTimer += deltaTime;
            if (growthTimer >= growthInterval) {
                growthTimer -= growthInterval;
                currentStage++;
            }
        }
    }

    public void paint(GraphicsContext gc) {
        gc.setStroke(Color.DARKGRAY);
        gc.strokeRect(x, y, size, size);

        if (planted && growthStages != null) {
            gc.drawImage(growthStages[currentStage], x + 5, y + 5, size - 10, size - 10);
        } else if (harvested) {
            gc.setFill(Color.rgb(139, 69, 19, 0.6));
            gc.fillRoundRect(x, y, size, size, 15, 15);
            gc.setFill(Color.rgb(0, 0, 0, 0.3));
            gc.fillRoundRect(x + 3, y + 3, size, size, 15, 15);
        } else {
            gc.setFill(Color.rgb(144, 238, 144, 0.7));
            gc.fillRoundRect(x, y, size, size, 15, 15);
            gc.setFill(Color.rgb(0, 0, 0, 0.3));
            gc.fillRoundRect(x + 3, y + 3, size, size, 15, 15);
        }
    }

    public boolean contains(double px, double py) {
        return px >= x && px <= x + size && py >= y && py <= y + size;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String getCropType() {
        return cropType.toString();
    }
}