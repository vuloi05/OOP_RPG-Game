package com.dungeondelicacy.rpg.systems;

import com.dungeondelicacy.rpg.characters.Player;

public class BattleSystem {
    public void startBattle(Player player, String enemy) {
        System.out.println("Battle started betweem "+ player.getName() + " and " + enemy);
    }
}
