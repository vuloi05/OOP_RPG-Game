package com.dungeondelicacy.rpg.map;

import com.dungeondelicacy.rpg.characters.Enemy;
import com.dungeondelicacy.rpg.systems.NPC;

import java.util.ArrayList;
import java.util.List;

public class Map {
    private String mapId;
    private int[][] mapData;
    private Tile[][] tiles;
    private List<Enemy> enemies;
    private List<NPC> npcs; // Danh sách NPC

    public Map(String mapId, int[][] mapData) {
        this.mapId = mapId;
        this.mapData = mapData;
        this.tiles = new Tile[mapData.length][mapData[0].length];
        this.enemies = new ArrayList<>();
        this.npcs = new ArrayList<>();
        initializeTiles();
    }

    private void initializeTiles() {
        for (int i = 0; i < mapData.length; i++) {
            for (int j = 0; j < mapData[0].length; j++) {
                if (mapData[i][j] == 0) {
                    tiles[i][j] = new Tile("Ground", true);
                } else if (mapData[i][j] == 1) {
                    tiles[i][j] = new Tile("Wall", false);
                } else if (mapData[i][j] == 2) {
                    tiles[i][j] = new Tile("Transition", true);
                }
            }
        }
    }

    public String getMapId() {
        return mapId;
    }

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

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public List<NPC> getNpcs() {
        return npcs;
    }
}