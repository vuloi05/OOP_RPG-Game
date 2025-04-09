package com.dungeondelicacy.rpg.map;

import com.dungeondelicacy.rpg.characters.Enemy;

import java.util.ArrayList;
import java.util.List;

public class Map {
    private String mapId; // Mã định danh (Tầng 1, Tầng 2, v.v.)
    private int[][] mapData; // Ma trận bản đồ
    private Tile[][] tiles; // Ma trận các ô
    private List<Enemy> enemies; // Danh sách quái vật trên bản đồ

    public Map(String mapId, int[][] mapData) {
        this.mapId = mapId;
        this.mapData = mapData;
        this.tiles = new Tile[mapData.length][mapData[0].length];
        this.enemies = new ArrayList<>(); // Khởi tạo danh sách quái vật
        initializeTiles();
    }

    private void initializeTiles() {
        for (int i = 0; i < mapData.length; i++) {
            for (int j = 0; j < mapData[0].length; j++) {
                if (mapData[i][j] == 0) {
                    tiles[i][j] = new Tile("Ground", true); // Đất, có thể đi qua
                } else if (mapData[i][j] == 1) {
                    tiles[i][j] = new Tile("Wall", false); // Tường, không đi qua được
                } else if (mapData[i][j] == 2) {
                    tiles[i][j] = new Tile("Transition", true); // Ô chuyển bản đồ
                }
            }
        }
    }

    public String getMapId() { return mapId; }

    public Tile getTile(int x, int y) {
        if (x < 0 || x >= tiles.length || y < 0 || y >= tiles[0].length) {
            return new Tile("Wall", false);
        }
        return tiles[x][y];
    }

    public boolean canMove(int x, int y) {
        if (x < 0 || x >= tiles.length || y < 0 || y >= tiles[0].length) {
            return false;
        }
        return tiles[x][y].isPassable();
    }

    // Getter cho danh sách quái vật
    public List<Enemy> getEnemies() {
        return enemies;
    }
}