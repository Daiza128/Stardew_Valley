package com.example.stardewvalley.model.world;

import javafx.scene.image.Image;

public class Tool {
    private double x, y;
    private ToolType type;
    private int durability;
    private Image sprite;

    public Tool(double x, double y, ToolType type, int durability, Image sprite) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.durability = durability;
        this.sprite = sprite;
    }

    public ToolType getType() {
        return type;
    }

    public int getDurability() {
        return durability;
    }

    public void decreaseDurability() {
        if (durability > 0) {
            durability--;
        }
    }

    public boolean isBroken() {
        return durability <= 0;
    }

    public Image getSprite() {
        return sprite;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void increaseDurability(int amount) {
        this.durability += amount;
    }
}