package com.example.stardewvalley.model.world;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Obstacle {
    private double x, y;
    private Image sprite;
    private boolean removable;
    private ToolType requiredTool;

    public Obstacle(double x, double y, Image sprite, boolean removable, ToolType requiredTool) {
        this.x = x;
        this.y = y;
        this.sprite = sprite;
        this.removable = removable;
        this.requiredTool = requiredTool;
    }

    public void paint(GraphicsContext gc) {
        gc.drawImage(sprite, x, y);
    }

    public boolean isHit(double px, double py) {
        return px > x && px < x + sprite.getWidth() && py > y && py < y + sprite.getHeight();
    }

    public boolean isRemovable() {
        return removable;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public ToolType getRequiredTool() {
        return requiredTool;
    }
}