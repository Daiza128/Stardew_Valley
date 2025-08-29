package com.example.stardewvalley.model.achievements;

import java.util.Objects;

public class Achievement implements Comparable<Achievement> {
    private String name;
    private String description;
    private int score;
    private boolean isUnlocked;
    private int progress;
    private int level;

    public Achievement(String name, String description, int score, int level) {
        this.name = name;
        this.description = description;
        this.score = score;
        this.isUnlocked = false;
        this.progress = 0;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public boolean isUnlocked() {
        return isUnlocked;
    }

    public void unlock() {
        if (!isUnlocked) {
            isUnlocked = true;
            System.out.println("¡Achievement unlocked!: " + name);
        }
    }

    private void checkUnlock() {
        if (progress == level &&  !isUnlocked) {
            unlock();
        }
    }

    public void incrementProgress() {
        this.progress++;
        checkUnlock();
    }

    public int getScore() {
        return score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Achievement that = (Achievement) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public int compareTo(Achievement o) {
        return this.name.compareTo(o.name);
    }
    @Override
    public String toString() {
        return name + " - " + (isUnlocked ? "Unlocked" : "Locked") + ": " + description;
    }
}
