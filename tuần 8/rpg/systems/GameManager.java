package com.dungeondelicacy.rpg.systems;

import com.dungeondelicacy.rpg.characters.Player;
import com.dungeondelicacy.rpg.map.Map;

import java.util.ArrayList;
import java.util.List;

public class GameManager {
    private Player player; // Người chơi chính
    private Map currentMap; // Bản đồ hiện tại
    private List<Map> maps; // Danh sách tất cả bản đồ
    private BattleSystem battleSystem; // Hệ thống chiến đấu

    public GameManager() {
        maps = new ArrayList<>();
        battleSystem = new BattleSystem();
    }

    public void startGame() {
        player = new Player("Laios", 1, 1); // Vị trí ban đầu (1,1)
        initializeMaps();
        currentMap = maps.get(0);
        System.out.println("Game started! Player " + player.getName() + " is in " + currentMap.getMapId());
    }

    private void initializeMaps() {
        // Tầng 1: Khu chợ (5x5)
        int[][] mapData1 = {
                {1, 1, 1, 1, 1},
                {1, 0, 0, 0, 1},
                {1, 0, 0, 2, 1}, // Ô (2,3) là ô chuyển sang Tầng 2
                {1, 0, 0, 0, 1},
                {1, 1, 1, 1, 1}
        };
        maps.add(new Map("Tầng 1 - Khu chợ", mapData1));

        // Tầng 2: Rừng rậm (5x5)
        int[][] mapData2 = {
                {1, 1, 1, 1, 1},
                {1, 2, 0, 0, 1}, // Ô (1,1) là ô quay lại Tầng 1
                {1, 0, 0, 0, 1},
                {1, 0, 0, 2, 1}, // Ô (3,3) là ô chuyển sang Tầng 3
                {1, 1, 1, 1, 1}
        };
        maps.add(new Map("Tầng 2 - Rừng rậm", mapData2));

        // Tầng 3: Hang động tối (5x5)
        int[][] mapData3 = {
                {1, 1, 1, 1, 1},
                {1, 2, 0, 0, 1}, // Ô (1,1) là ô quay lại Tầng 2
                {1, 0, 0, 0, 1},
                {1, 0, 0, 2, 1}, // Ô (3,3) là ô chuyển sang Tầng cuối
                {1, 1, 1, 1, 1}
        };
        maps.add(new Map("Tầng 3 - Hang động tối", mapData3));

        // Tầng cuối: Hang rồng (5x5)
        int[][] mapData4 = {
                {1, 1, 1, 1, 1},
                {1, 2, 0, 0, 1}, // Ô (1,1) là ô quay lại Tầng 3
                {1, 0, 0, 0, 1},
                {1, 1, 1, 1, 1}
        };
        maps.add(new Map("Tầng cuối - Hang rồng", mapData4));
    }

    public void switchMap(String newMapId) {
        for (Map map : maps) {
            if (map.getMapId().equals(newMapId)) {
                currentMap = map;
                player.setX(1);
                player.setY(1);
                System.out.println("Switched to map: " + newMapId);
                return;
            }
        }
        System.out.println("Map not found: " + newMapId);
    }

    public void endGame() {
        System.out.println("Game ended! Player " + player.getName() + " reached the end.");
    }

    // Getter
    public Player getPlayer() { return player; }
    public Map getCurrentMap() { return currentMap; }
    public BattleSystem getBattleSystem() { return battleSystem; }
}