package systems;

import characters.Player;

public class BattleSystem {
    public void startBattle(Player player, String enemy) {
        System.out.println("Battle started between " + player.getName() + " and " + enemy);
    }
}