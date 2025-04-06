package com.dungeondelicacy.rpg.items;

public class Equipment extends Item {
    private int attackPower;
    private int defensePower;
    private String equipmentType; // vũ khí, giáp

    public Equipment(int itemId, String name, String description, int value, int attackPower, int defensePower, String equipmentType) {
        super(itemId, name, description, value);
        this.attackPower = attackPower;
        this.defensePower = defensePower;
        this.equipmentType = equipmentType;
    }

    @Override
    public void use() {
        System.out.println("Trang bị " + name + ": Loại " + equipmentType);
        System.out.println("Tăng " + attackPower + " attack.");
        System.out.println("Tăng " + defensePower + " defense.");
    }
}
