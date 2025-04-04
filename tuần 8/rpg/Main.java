package com.dungeondelicacy.rpg;

import com.dungeondelicacy.rpg.systems.GameManager;

public class Main {
    public static void main(String[] args) {
        GameManager gameManager = new GameManager();
        gameManager.startGame();

        // Mô phỏng chuyển bản đồ
        gameManager.switchMap("Tầng 2 - Rừng rậm");
        gameManager.switchMap("Tầng 3 - Hang động tối");
        gameManager.switchMap("Tầng cuối - Hang rồng");
        gameManager.endGame();
    }
}