package com.dungeondelicacy.rpg.characters;

public class Player extends Character {
    public Player(String name, int x, int y){
        super(name, x, y);
    }

    @Override
    public void performAction(){
        System.out.println(getName() + " performs a player-specific action.");
    }
}
