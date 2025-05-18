package systems;

import characters.Enemy;
import characters.Player;
import items.Item.ConsumableItem;
import items.Item.Equipment;
import map.Map;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class GameManager {
    private Player player; // Người chơi chính
    private Map currentMap; // Bản đồ hiện tại
    private List<Map> maps; // Danh sách tất cả bản đồ
    private Stack<Map> mapHistory; // Lịch sử bản đồ
    private BattleSystem battleSystem; // Hệ thống chiến đấu

    public GameManager() {
        maps = new ArrayList<>();
        mapHistory = new Stack<>();
        battleSystem = new BattleSystem();
    }

    public void startGame() {
        player = new Player("Laios", 1, 1); // Vị trí ban đầu (1,1)
        player.addItem(new Equipment(1, "Kiếm Gỗ", "Vũ khí cơ bản", 10, "items/Item/Resource/image/normalsword.jpg",5, 0, "Vũ khí"));
        player.addItem(new ConsumableItem(2, "Bình Máu", "Hồi 50 HP", 5,"items/Item/Resource/image/hppotion.png", 50, "HP"));
        initializeMaps();
        currentMap = maps.get(0);
        mapHistory.push(currentMap);
        System.out.println("Game started! Player " + player.getName() + " is in " + currentMap.getMapId());
    }

    private void initializeMaps() {
        // Tầng 1: Khu chợ (16x16)
        int[][] mapData1 = new int[16][16];
        // Khởi tạo toàn bộ bản đồ là tường (1)
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                mapData1[i][j] = 1; // Tường
            }
        }
        // Tạo khu vực có thể đi được (Ground - 0)
        for (int i = 1; i < 15; i++) {
            for (int j = 1; j < 15; j++) {
                mapData1[i][j] = 0; // Đất
            }
        }
        // Đặt ô Transition tại (14,14) để chuyển sang Tầng 2
        mapData1[14][14] = 2; // Ô Transition
        Map map1 = new Map("Tầng 1 - Khu chợ", mapData1);
        map1.getEnemies().add(new Enemy("Skeleton", 12, 12)); // Thêm 1 Skeleton tại (12,12)
        maps.add(map1);

        // Tầng 2: Rừng rậm (16x16)
        int[][] mapData2 = new int[16][16];
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                mapData2[i][j] = 1; // Tường
            }
        }
        for (int i = 1; i < 15; i++) {
            for (int j = 1; j < 15; j++) {
                mapData2[i][j] = 0; // Đất
            }
        }
        mapData2[1][1] = 2; // Ô (1,1) là ô quay lại Tầng 1
        mapData2[14][14] = 2; // Ô (14,14) là ô chuyển sang Tầng 3
        Map map2 = new Map("Tầng 2 - Rừng rậm", mapData2);
        map2.getEnemies().add(new Enemy("Skeleton", 10, 10)); // Thêm Skeleton tại (10,10)
        map2.getEnemies().add(new Enemy("Skeleton", 11, 10)); // Thêm Skeleton tại (11,10)
        maps.add(map2);

        // Tầng 3: Hang động tối (16x16)
        int[][] mapData3 = new int[16][16];
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                mapData3[i][j] = 1; // Tường
            }
        }
        for (int i = 1; i < 15; i++) {
            for (int j = 1; j < 15; j++) {
                mapData3[i][j] = 0; // Đất
            }
        }
        mapData3[1][1] = 2; // Ô (1,1) là ô quay lại Tầng 2
        mapData3[14][14] = 2; // Ô (14,14) là ô chuyển sang Tầng cuối
        Map map3 = new Map("Tầng 3 - Hang động tối", mapData3);
        map3.getEnemies().add(new Enemy("Vampire", 12, 13)); // Thêm 1 Vampire tại (12,13)
        maps.add(map3);

        // Tầng cuối: Hang rồng (16x16)
        int[][] mapData4 = new int[16][16];
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                mapData4[i][j] = 1; // Tường
            }
        }
        for (int i = 1; i < 15; i++) {
            for (int j = 1; j < 15; j++) {
                mapData4[i][j] = 0; // Đất
            }
        }
        mapData4[1][1] = 2; // Ô (1,1) là ô quay lại Tầng 3
        Map map4 = new Map("Tầng cuối - Hang rồng", mapData4);
        map4.getEnemies().add(new Enemy("Dragon", 14, 14)); // Thêm 1 Dragon tại (14,14)
        maps.add(map4);
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
            mapHistory.pop(); // Bỏ bản đồ hiện tại
            currentMap = mapHistory.peek(); // Lấy bản đồ trước đó
            player.setX(1);
            player.setY(1);
            System.out.println("Returned to map: " + currentMap.getMapId());
        } else {
            System.out.println("No previous map to return to.");
        }
    }

    public void movePlayer(int newX, int newY) {
        // Kiểm tra xem ô mới có đi qua được không
        if (currentMap.canMove(newX, newY)) {
            // Kiểm tra va chạm với quái vật trước khi di chuyển
            for (Enemy enemy : currentMap.getEnemies()) {
                if (enemy.getX() == newX && enemy.getY() == newY) {
                    // Nếu có quái vật tại vị trí mới, bắt đầu trận đấu
                    battleSystem.startBattle(player, enemy.getType());
                    return; // Không di chuyển người chơi nếu có va chạm
                }
            }
            // Nếu không có va chạm, di chuyển người chơi
            player.setX(newX);
            player.setY(newY);
        }
    }

    public void endGame() {
        System.out.println("Game ended! Player " + player.getName() + " reached the end.");
    }

    // Getter
    public Player getPlayer() { return player; }
    public Map getCurrentMap() { return currentMap; }
    public BattleSystem getBattleSystem() { return battleSystem; }
}