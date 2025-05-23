package com.dungeondelicacy.rpg.characters;

public abstract class Character {
    private String name;
    private int x, y;
    private int hp, mp;

    public Character(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.hp = 100;
        this.mp = 50;
    }

    public String getName() { return name; }
    public int getX() { return x; }
    public int getY() { return y; }
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public int getHp() { return hp; }
    public int getMp() { return mp; }
    public void setHp(int hp) { this.hp = Math.max(0, hp); }
    public void setMp(int mp) { this.mp = Math.max(0, mp); }

    public abstract void performAction();
}