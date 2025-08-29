package com.example.stardewvalley.model.achievements;

import com.example.stardewvalley.model.GameState;
import com.example.stardewvalley.model.Player;

import java.util.List;

public class AchievementSystem {
    private BinarySearchTree<Achievement> achievementTree;
    private Player player;

    public AchievementSystem() {
        this.achievementTree = new BinarySearchTree<>();
        this.player = GameState.getPlayer();
        initializeAchievements();
    }

    private void initializeAchievements() {
        achievementTree.insert(new Achievement("CULTIVATE 1", "Cultiva tu primera planta", 40, 1));
        achievementTree.insert(new Achievement("HARVEST 1", "Cosecha tu primera planta", 50, 1));
        achievementTree.insert(new Achievement("STORE 1", "Guarda un objeto en el cofre", 60, 1));
        achievementTree.insert(new Achievement("HIT ANIMAL 1", "Golpea un animal por primera vez", 70, 1));
        achievementTree.insert(new Achievement("REMOVE ANIMAL 1", "Elimina un animal por primera vez", 80, 1));
        achievementTree.insert(new Achievement("REMOVE OBSTACLE 1", "Elimina un obstáculo por primera vez", 90, 1));
        achievementTree.insert(new Achievement("CHANGE SCENE 1", "Cambia de escenario por primera vez", 100, 1));

        achievementTree.insert(new Achievement("CULTIVATE 2", "Cultiva 10 plantas", 80, 10));
        achievementTree.insert(new Achievement("HARVEST 2", "Cosecha 10 plantas", 100, 10));
        achievementTree.insert(new Achievement("STORE 2", "Guarda objetos en el cofre 5 veces", 120, 5));
        achievementTree.insert(new Achievement("HIT ANIMAL 2", "Golpea animales 10 veces", 140, 10));
        achievementTree.insert(new Achievement("REMOVE ANIMAL 2", "Elimina animales 10 veces", 160, 10));
        achievementTree.insert(new Achievement("REMOVE OBSTACLE 2", "Elimina obstáculos 10 veces", 180, 10));
        achievementTree.insert(new Achievement("CHANGE SCENE 2", "Cambia de escenario por segunda vez", 200, 2));
    }

    public void incrementAchievement(String achievementName) {
        Achievement achievement = achievementTree.search(new Achievement(achievementName, "", 0, 0));
        if (achievement != null) {
            achievement.incrementProgress();
            if (achievement.isUnlocked()) {
                achievement.unlock();
                player.addScore(achievement.getScore());
            }
        }
    }

    public void updateAchievements(String baseAchievementName) {
        incrementAchievement(baseAchievementName + " 1");
        incrementAchievement(baseAchievementName + " 2");
        incrementAchievement(baseAchievementName + " 3");
    }

    public List<Achievement> getAllAchievements() {
        return achievementTree.getAllAchievements();
    }

    public void displayAllAchievements() {
        List<Achievement> achievements = getAllAchievements();
        achievements.forEach(System.out::println);
    }

    public boolean areAllAchievementsCompleted() {
        // Verificar si todos los logros están desbloqueados
        return achievementTree.getAllAchievements().stream().allMatch(Achievement::isUnlocked);
    }
}