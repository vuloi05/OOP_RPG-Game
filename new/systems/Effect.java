package com.dungeondelicacy.rpg.systems;

import com.dungeondelicacy.rpg.characters.Character;

public class Effect {
    private String name; // Tên hiệu ứng
    private int duration; // Thời gian hiệu ứng (số lượt)
    private int attackModifier; // Thay đổi sát thương
    private int defenseModifier; // Thay đổi phòng thủ

    public Effect(String name, int duration, int attackModifier, int defenseModifier) {
        this.name = name;
        this.duration = duration;
        this.attackModifier = attackModifier;
        this.defenseModifier = defenseModifier;
    }

    // Áp dụng hiệu ứng lên mục tiêu
    public void apply(Character target) {
        System.out.println(target.getName() + " is affected by " + name + " for " + duration + " turns");
        // Giả lập hiệu ứng (do không có hệ thống lượt)
        System.out.println("Attack modifier: " + attackModifier + ", Defense modifier: " + defenseModifier);
    }

    // Getter
    public String getName() {
        return name;
    }

    public int getDuration() {
        return duration;
    }
}