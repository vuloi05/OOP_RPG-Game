package com.dungeondelicacy.rpg;

import com.dungeondelicacy.rpg.systems.GameManager;

public class Main {
    public static void main(String[] args) {
        GameManager gameManager = new GameManager();
        gameManager.startGame();

        GameWindow gameWindow = new GameWindow(gameManager);
        gameWindow.run();
    }
}