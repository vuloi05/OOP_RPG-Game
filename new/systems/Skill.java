package com.dungeondelicacy.rpg.systems;

import com.dungeondelicacy.rpg.characters.Character;

public class Skill {
    private String name; // Tên kỹ năng
    private int mpCost; // Chi phí MP
    private int damage; // Sát thương (nếu có)
    private int healing; // Hồi phục (nếu có)
    private Effect effect; // Hiệu ứng (nếu có)

    public Skill(String name, int mpCost, int damage, int healing, Effect effect) {
        this.name = name;
        this.mpCost = mpCost;
        this.damage = damage;
        this.healing = healing;
        this.effect = effect;
    }

    // Áp dụng kỹ năng lên mục tiêu
    public void apply(Character user, Character target) {
        if (user.getMp() < mpCost) {
            System.out.println(user.getName() + " does not have enough MP to use " + name);
            return;
        }
        user.setMp(user.getMp() - mpCost);
        if (damage > 0) {
            int newHp = Math.max(0, target.getHp() - damage);
            target.setHp(newHp);
            System.out.println(user.getName() + " uses " + name + ", deals " + damage + " damage to " + target.getName());
        }
        if (healing > 0) {
            int newHp = Math.min(100, user.getHp() + healing);
            user.setHp(newHp);
            System.out.println(user.getName() + " uses " + name + ", recovers " + healing + " HP");
        }
        if (effect != null) {
            effect.apply(target);
        }
    }

    // Getter
    public String getName() {
        return name;
    }

    public int getMpCost() {
        return mpCost;
    }
}