package com.dungeondelicacy.rpg.map;

import com.dungeondelicacy.rpg.characters.Player;

public class Tile implements Interactable {
    private String type; // Loại địa hình (đất, cỏ, nước, v.v.)
    private boolean isPassable; // Có thể đi qua không

    public Tile(String type, boolean isPassable) {
        this.type = type;
        this.isPassable = isPassable;
    }

    public String getType() { return type; }
    public boolean isPassable() { return isPassable; }

    @Override
    public void interact(Player player) {
        System.out.println("Player interacted with tile: " + type);
    }
}