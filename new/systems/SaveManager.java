package com.dungeondelicacy.rpg.systems;

import com.dungeondelicacy.rpg.characters.Enemy;
import com.dungeondelicacy.rpg.characters.Player;
import com.dungeondelicacy.rpg.map.Map;

import java.io.*;

public class SaveManager {
    private static final String SAVE_FILE = "game_save.txt";

    public void saveGame(GameManager gameManager) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(SAVE_FILE))) {
            Player player = gameManager.getPlayer();
            Map currentMap = gameManager.getCurrentMap();

            // Lưu thông tin người chơi
            writer.println("Player:");
            writer.println("name:" + player.getName());
            writer.println("x:" + player.getX());
            writer.println("y:" + player.getY());
            writer.println("hp:" + player.getHp());
            writer.println("mp:" + player.getMp());

            // Lưu thông tin túi đồ
            writer.println("Inventory:");
            writer.println("size:" + player.getInventory().getCurrentSize());
            for (Item item : player.getInventory().getItems()) {
                writer.println("item:" + item.getName() + "," + item.getQuantity());
            }

            // Lưu thông tin nhiệm vụ
            writer.println("Quests:");
            writer.println("count:" + player.getActiveQuests().size());
            for (Quest quest : player.getActiveQuests()) {
                writer.println("quest:" + quest.getTitle());
            }

            // Lưu thông tin bản đồ
            writer.println("Map:");
            writer.println("mapId:" + currentMap.getMapId());

            // Lưu thông tin quái vật
            writer.println("Enemies:");
            writer.println("count:" + currentMap.getEnemies().size());
            for (Enemy enemy : currentMap.getEnemies()) {
                writer.println("enemy:" + enemy.getType() + "," + enemy.getX() + "," + enemy.getY() + "," + enemy.getHp());
            }

            System.out.println("Game saved successfully!");
        } catch (IOException e) {
            System.err.println("Error saving game: " + e.getMessage());
        }
    }

    public void loadGame(GameManager gameManager) {
        try (BufferedReader reader = new BufferedReader(new FileReader(SAVE_FILE))) {
            String line;
            String playerName = null;
            int x = 0, y = 0, hp = 0, mp = 0;
            String mapId = null;

            while ((line = reader.readLine()) != null) {
                if (line.equals("Player:")) {
                    playerName = reader.readLine().split(":")[1];
                    x = Integer.parseInt(reader.readLine().split(":")[1]);
                    y = Integer.parseInt(reader.readLine().split(":")[1]);
                    hp = Integer.parseInt(reader.readLine().split(":")[1]);
                    mp = Integer.parseInt(reader.readLine().split(":")[1]);
                } else if (line.equals("Map:")) {
                    mapId = reader.readLine().split(":")[1];
                }
                // Bỏ qua Inventory, Quests, Enemies vì khởi tạo lại từ đầu
            }

            // Cập nhật trạng thái game
            Player player = new Player(playerName, x, y);
            player.setHp(hp);
            player.setMp(mp);
            gameManager.setPlayer(player);
            gameManager.switchMap(mapId);

            System.out.println("Game loaded successfully!");
        } catch (IOException e) {
            System.err.println("Error loading game: " + e.getMessage());
        }
    }
}