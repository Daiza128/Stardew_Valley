package com.example.stardewvalley.model;

import com.example.stardewvalley.model.achievements.AchievementSystem;

public class GameState {
    private static AchievementSystem achievementSystem;
    private static Player player;

    public static AchievementSystem getAchievementSystem() {
        if (achievementSystem == null) {
            achievementSystem = new AchievementSystem();
        }
        return achievementSystem;
    }

    public static Player getPlayer() {
        if (player == null) {
            player = new Player(0, 0, 200, null);
        }
        return player;
    }

    public static void cleanGame() {
        player = null;
        achievementSystem = null;
    }
}