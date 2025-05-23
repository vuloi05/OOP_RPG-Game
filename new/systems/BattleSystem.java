package com.dungeondelicacy.rpg.systems;

import com.dungeondelicacy.rpg.characters.Character;
import com.dungeondelicacy.rpg.characters.Player;

public class BattleSystem {
    private Skill healingSkill; // Kỹ năng hồi máu
    private Skill attackSkill; // Kỹ năng tấn công mạnh

    public BattleSystem() {
        // Khởi tạo các kỹ năng
        this.healingSkill = new Skill("Healing", 10, 0, 20, null);
        this.attackSkill = new Skill("Power Strike", 15, 25, 0, new Effect("Stun", 2, 0, -5));
    }

    public void startBattle(Player player, String enemy) {
        System.out.println("Battle started between " + player.getName() + " and " + enemy);
    }

    // Tấn công cơ bản
    public int attack(Character attacker, Character target, boolean isDefending) {
        int baseDamage = 10;
        int damage = baseDamage;
        if (attacker instanceof Player) {
            damage += ((Player) attacker).getAttackBonus();
        }
        if (isDefending) {
            int defense = 0;
            if (target instanceof Player) {
                defense = ((Player) target).getDefenseBonus();
            }
            damage = Math.max(0, damage - (damage / 2 + defense));
        }
        int newHp = Math.max(0, target.getHp() - damage);
        target.setHp(newHp);
        System.out.println(attacker.getName() + " deals " + damage + " damage to " + target.getName());
        if (target.getHp() <= 0) {
            System.out.println(target.getName() + " is defeated!");
        }
        return damage;
    }

    // Sử dụng kỹ năng
    public void useSkill(Character user, String skillName) {
        Skill skill = null;
        if (skillName.equals("Healing")) {
            skill = healingSkill;
        } else if (skillName.equals("Power Strike")) {
            skill = attackSkill;
        }
        if (skill != null) {
            skill.apply(user, user); // Mục tiêu là chính người dùng (cho Healing) hoặc đối thủ (cho Power Strike)
        }
    }
}