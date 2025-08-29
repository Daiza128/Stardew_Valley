package com.example.stardewvalley.model.world;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Resource {
    private double x;
    private double y;
    private Image sprite;
    private ToolType toolType;
    private String name;

    public Resource(double x, double y, Image sprite, ToolType toolType, String name) {
        this.x = x;
        this.y = y;
        this.sprite = sprite;
        this.toolType = toolType;
        this.name = name;
    }

    public void paint(GraphicsContext gc) {
        gc.drawImage(sprite, x, y);
    }

    public double getY() {
        return y;
    }

    public double getX() {
        return x;
    }

    public ToolType getType() {
        return toolType;
    }

    public String getName() {
        return name;
    }
}
