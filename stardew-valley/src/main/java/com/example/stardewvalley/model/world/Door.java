package com.example.stardewvalley.model.world;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Door {
    public double x;
    public double y;
    private Image sprite = new Image(getClass().getResourceAsStream("/com/example/stardewvalley/sprites/tool/portal.png"), -1, 40, true, true);
    private boolean open;

    public Door(double x, double y) {
        this.x = x;
        this.y = y;
        this.open = false;
    }

    public void paint(GraphicsContext gc) {
        gc.drawImage(sprite, x, y);
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean isPlayerOnDoor(double playerX, double playerY, double playerWidth, double playerHeight) {
        return playerX < x + 40 && playerX + playerWidth > x &&
                playerY < y + 40 && playerY + playerHeight > y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
