package com.example.stardewvalley.model;

import com.example.stardewvalley.model.world.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private double x, y;
    private double velocidad;
    private String direccion;
    private Image[] up;
    private Image[] down;
    private Image[] left;
    private Image[] right;
    private int spriteIndex = 0;
    private long lastSprite = 0;
    private int score;

    private double boardWidth, boardHeight;

    private List<Tool> inventory;
    private List<CropField> collect;
    private List<Resource> resources;
    private Chest chest;

    public Player(double x, double y, double velocidad, Chest chest) {
        this.x = x;
        this.y = y;
        this.velocidad = velocidad;
        this.direccion = "down";
        this.up = loadSprites("com/example/stardewvalley/sprites/player/up");
        this.down = loadSprites("com/example/stardewvalley/sprites/player/down");
        this.left = loadSprites("com/example/stardewvalley/sprites/player/left");
        this.right = loadSprites("com/example/stardewvalley/sprites/player/right");
        this.inventory = new ArrayList<>();
        this.chest = chest;
        this.collect = new ArrayList<>();
        this.resources = new ArrayList<>();
        this.score = 0;
    }

    private Image[] loadSprites(String basePath) {
        double targetHeight = 40;
        Image[] sprites = new Image[3];
        for (int i = 0; i < 3; i++) {
            String completePath = "/%s%d.png".formatted(basePath, i + 1);
            sprites[i] = new Image(getClass().getResourceAsStream(completePath), -1, targetHeight, true, true);
        }
        return sprites;
    }

    public void setBoardSize(double width, double height) {
        this.boardWidth = width;
        this.boardHeight = height;
    }

    public void move(String direccion, double deltaTiempo, WorldController worldController) {
        double newX = x;
        double newY = y;

        switch (direccion) {
            case "up" -> newY = Math.max(0, y - velocidad * deltaTiempo);
            case "down" -> newY = Math.min(boardHeight - 40, y + velocidad * deltaTiempo);
            case "left" -> newX = Math.max(0, x - velocidad * deltaTiempo);
            case "right" -> newX = Math.min(boardWidth - 30, x + velocidad * deltaTiempo);
        }

        this.direccion = direccion;
        if (worldController.isPositionFree(newX, newY, 30, 30, true)) {
            x = newX;
            y = newY;
        }
    }

    public void update(long tiempoActual) {
        if (tiempoActual - lastSprite > 200_000_000) {
            spriteIndex = (spriteIndex + 1) % 3;
            lastSprite = tiempoActual;
        }
    }

    public void paint(GraphicsContext gc) {
        Image currentSprite;
        switch (direccion) {
            case "up" -> currentSprite = up[spriteIndex];
            case "down" -> currentSprite = down[spriteIndex];
            case "left" -> currentSprite = left[spriteIndex];
            case "right" -> currentSprite = right[spriteIndex];
            default -> currentSprite = down[0];
        }
        gc.drawImage(currentSprite, x, y);

        chest.paint(gc);
    }

    public boolean isFacing(double objX, double objY) {
        double facingRange = 40;
        double offset = 10;

        return switch (direccion) {
            case "up" -> (objY + facingRange >= y - offset && objY < y) && Math.abs(objX - x) <= facingRange;
            case "down" -> (objY <= y + facingRange + offset && objY > y) && Math.abs(objX - x) <= facingRange;
            case "left" -> (objX + facingRange >= x - offset && objX < x) && Math.abs(objY - y) <= facingRange;
            case "right" -> (objX <= x + facingRange + offset && objX > x) && Math.abs(objY - y) <= facingRange;
            default -> false;
        };
    }

    public boolean isNear(double objX, double objY) {
        double range = 50;
        double distanceX = Math.abs(x - objX);
        double distanceY = Math.abs(y - objY);

        System.out.println("Checking isNear...");
        System.out.println("DistanceX: " + distanceX + ", DistanceY: " + distanceY);

        return distanceX <= range && distanceY <= range;
    }

    public void collectTool(Tool tool) {
        inventory.add(tool);
        System.out.println("Collected tool: " + tool.getType().name());
    }

    public boolean hasTool(ToolType tool) {
        for(Tool tools : inventory) {
            if(tools.getType() ==  tool) {
                tools.decreaseDurability();
                if(tools.getDurability() <= 0){
                    inventory.remove(tools);
                }
                return true;
            }
        }

        return false;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public List<Tool> getInventory() {
        return inventory;
    }

    public void addCollect(CropField crop){
        collect.add(crop);
    }

    public boolean saveCollect() {
        System.out.println("Player Position: x=" + x + ", y=" + y);
        System.out.println("Chest Position: x=" + chest.getX() + ", y=" + chest.getY());

        System.out.println("Facing: " + isFacing(chest.getX(), chest.getY()));
        System.out.println("Near: " + isNear(chest.getX(), chest.getY()));

        if (isFacing(chest.getX(), chest.getY()) && isNear(chest.getX(), chest.getY())) {
            chest.addCropField(collect);
            collect = new ArrayList<>();
            System.out.println("Crop saved in collection!");
            return true;
        } else {
            System.out.println("Error while saving collection!");
            return false;
        }
    }

    public Chest getChest() {
        return chest;
    }

    public List<CropField> getCollect() {
        return collect;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void addScore(int score) {
        this.score += score;
        System.out.println("Adding score: " + score + " to player. Total: " + this.score);
    }

    public void setChest(Chest chest) {
        this.chest = chest;
    }

    public List<Resource> getResources() {
        return resources;
    }

    public void addResource(Resource resource) {
        resources.add(resource);
    }

    public int getResourceSummary(String name) {
        int count = 0;

        for (Resource resource : resources) {
            if(resource.getName().equals(name)) {
                count++;
            }
        }

        return count;
    }

    public void repairTool() {
        if (resources.isEmpty()) {
            System.out.println("No resources available to repair tools!");
            return;
        }

        for (Tool tool : inventory) {
            ToolType toolType = tool.getType();
            int totalResources = 0;

            for (Resource resource : new ArrayList<>(resources)) {
                if (resource.getType() == toolType) {
                    totalResources++;
                    resources.remove(resource);
                }
            }

            if (totalResources > 0) {
                tool.increaseDurability(totalResources);
                System.out.println("Repaired " + toolType.name() + " with " + totalResources + " resources.");
            }
        }
    }

}