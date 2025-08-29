package com.example.stardewvalley.model.world;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

public class Chest {
    private double x;
    private double y;
    private List<CropField> storedCrops;
    private Image sprite = new Image(getClass().getResourceAsStream("/com/example/stardewvalley/sprites/tool/chest.png"));

    public Chest(double x, double y) {
        this.x = x;
        this.y = y;
        storedCrops = new ArrayList<>();
    }

    public void addCropField(List<CropField> cropField){
        storedCrops.addAll(cropField);
    }

    public void paint(GraphicsContext gc) {
        gc.drawImage(sprite, x, y);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public List<CropField> getStoredCrops() {
        return storedCrops;
    }
}
