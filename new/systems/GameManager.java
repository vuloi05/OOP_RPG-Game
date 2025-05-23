package com.dungeondelicacy.rpg.systems;

import com.dungeondelicacy.rpg.characters.Enemy;
import com.dungeondelicacy.rpg.characters.Player;
import com.dungeondelicacy.rpg.map.Map;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class GameManager {
    private Player player;
    private Map currentMap;
    private List<Map> maps;
    private Stack<Map> mapHistory;
    private BattleSystem battleSystem;
    private SaveManager saveManager;

    public GameManager() {
        maps = new ArrayList<>();
        mapHistory = new Stack<>();
        battleSystem = new BattleSystem();
        saveManager = new SaveManager();
    }

    public void startGame() {
        player = new Player("Laios", 1, 1);
        initializeMaps();
        currentMap = maps.get(0);
        mapHistory.push(currentMap);
        initializePlayerInventory();
        System.out.println("Game started! Player " + player.getName() + " is in " + currentMap.getMapId());
    }

    private void initializeMaps() {
        // Tầng 1: Khu chợ
        int[][] mapData1 = new int[16][16];
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                mapData1[i][j] = 1;
            }
        }
        for (int i = 1; i < 15; i++) {
            for (int j = 1; j < 15; j++) {
                mapData1[i][j] = 0;
            }
        }
        mapData1[14][14] = 2;
        Map map1 = new Map("Tầng 1 - Khu chợ", mapData1);
        map1.getEnemies().add(new Enemy("Skeleton", 12, 12));
        NPC merchant = new NPC("Merchant", 5, 5, "Welcome to my shop! Buy something or take a quest.");
        Item potion = new Item("Health Potion", "Restores 20 HP", 1) {
            @Override
            public void use(Player player) {
                int newHp = Math.min(100, player.getHp() + 20);
                player.setHp(newHp);
                System.out.println(player.getName() + " used Health Potion, restored 20 HP");
            }
        };
        merchant.addShopItem(potion);
        Quest quest1 = new Quest("Gather Herbs", "Collect 3 herbs in the forest", potion);
        merchant.setQuest(quest1);
        map1.getNpcs().add(merchant);
        maps.add(map1);

        // Tầng 2: Rừng rậm
        int[][] mapData2 = new int[16][16];
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                mapData2[i][j] = 1;
            }
        }
        for (int i = 1; i < 15; i++) {
            for (int j = 1; j < 15; j++) {
                mapData2[i][j] = 0;
            }
        }
        mapData2[1][1] = 2;
        mapData2[14][14] = 2;
        Map map2 = new Map("Tầng 2 - Rừng rậm", mapData2);
        map2.getEnemies().add(new Enemy("Skeleton", 10, 10));
        map2.getEnemies().add(new Enemy("Skeleton", 11, 10));
        maps.add(map2);

        // Tầng 3: Hang động tối
        int[][] mapData3 = new int[16][16];
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                mapData3[i][j] = 1;
            }
        }
        for (int i = 1; i < 15; i++) {
            for (int j = 1; j < 15; j++) {
                mapData3[i][j] = 0;
            }
        }
        mapData3[1][1] = 2;
        mapData3[14][14] = 2;
        Map map3 = new Map("Tầng 3 - Hang động tối", mapData3);
        map3.getEnemies().add(new Enemy("Vampire", 12, 13));
        maps.add(map3);

        // Tầng cuối: Hang rồng
        int[][] mapData4 = new int[16][16];
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                mapData4[i][j] = 1;
            }
        }
        for (int i = 1; i < 15; i++) {
            for (int j = 1; j < 15; j++) {
                mapData4[i][j] = 0;
            }
        }
        mapData4[1][1] = 2;
        Map map4 = new Map("Tầng cuối - Hang rồng", mapData4);
        map4.getEnemies().add(new Enemy("Dragon", 14, 14));
        maps.add(map4);
    }

    private void initializePlayerInventory() {
        Item potion = new Item("Health Potion", "Restores 20 HP", 3) {
            @Override
            public void use(Player player) {
                int newHp = Math.min(100, player.getHp() + 20);
                player.setHp(newHp);
                System.out.println(player.getName() + " used Health Potion, restored 20 HP");
            }
        };
        Equipment sword = new Equipment("Iron Sword", "A sharp sword", 1, 10, 0, "Weapon");
        player.getInventory().addItem(potion);
        player.getInventory().addItem(sword);
    }

    public void switchMap(String newMapId) {
        for (Map map : maps) {
            if (map.getMapId().equals(newMapId)) {
                currentMap = map;
                mapHistory.push(currentMap);
                player.setX(1);
                player.setY(1);
                System.out.println("Switched to map: " + newMapId);
                return;
            }
        }
        System.out.println("Map not found: " + newMapId);
    }

    public void goBackToPreviousMap() {
        if (mapHistory.size() > 1) {
            mapHistory.pop();
            currentMap = mapHistory.peek();
            player.setX(1);
            player.setY(1);
            System.out.println("Returned to map: " + currentMap.getMapId());
        } else {
            System.out.println("No previous map to return to.");
        }
    }

    public void movePlayer(int newX, int newY) {
        if (currentMap.canMove(newX, newY)) {
            for (Enemy enemy : currentMap.getEnemies()) {
                if (enemy.getX() == newX && enemy.getY() == newY) {
                    battleSystem.startBattle(player, enemy.getType());
                    return;
                }
            }
            player.setX(newX);
            player.setY(newY);
        }
    }

    public void interactWithNPC(Player player, String action) {
        for (NPC npc : currentMap.getNpcs()) {
            if (npc.getX() == player.getX() && npc.getY() == player.getY()) {
                if (action.equals("Buy")) {
                    npc.buyItem(player, "Health Potion");
                } else if (action.equals("Quest")) {
                    npc.giveQuest(player);
                }
                return;
            }
        }
    }

    public void endGame() {
        System.out.println("Game ended! Player " + player.getName() + " reached the end.");
    }

    // Getter
    public Player getPlayer() {
        return player;
    }

    public Map getCurrentMap() {
        return currentMap;
    }

    public BattleSystem getBattleSystem() {
        return battleSystem;
    }

    public void saveGame() {
        saveManager.saveGame(this);
    }

    public void loadGame() {
        saveManager.loadGame(this);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}