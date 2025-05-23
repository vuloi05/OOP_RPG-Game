package com.dungeondelicacy.rpg.systems;

import com.dungeondelicacy.rpg.characters.Player;

public class Equipment extends Item {
    private int attackBonus; // Tăng thêm sát thương
    private int defenseBonus; // Tăng thêm phòng thủ
    private String equipSlot; // Vị trí trang bị (Head, Body, Weapon, etc.)

    public Equipment(String name, String description, int quantity, int attackBonus, int defenseBonus, String equipSlot) {
        super(name, description, quantity);
        this.attackBonus = Math.max(0, attackBonus);
        this.defenseBonus = Math.max(0, defenseBonus);
        this.equipSlot = equipSlot;
    }

    @Override
    public void use(Player player) {
        // Trang bị vật phẩm lên người chơi
        if (player.equipItem(this)) {
            setQuantity(getQuantity() - 1); // Giảm số lượng nếu trang bị thành công
            System.out.println(player.getName() + " equipped " + getName() + " (" + equipSlot + ")");
        } else {
            System.out.println("Failed to equip " + getName() + ". Slot is occupied.");
        }
    }

    // Getter và Setter
    public int getAttackBonus() {
        return attackBonus;
    }

    public void setAttackBonus(int attackBonus) {
        this.attackBonus = Math.max(0, attackBonus);
    }

    public int getDefenseBonus() {
        return defenseBonus;
    }

    public void setDefenseBonus(int defenseBonus) {
        this.defenseBonus = Math.max(0, defenseBonus);
    }

    public String getEquipSlot() {
        return equipSlot;
    }

    public void setEquipSlot(String equipSlot) {
        this.equipSlot = equipSlot;
    }

    @Override
    public String toString() {
        return super.toString() + " | Attack: +" + attackBonus + ", Defense: +" + defenseBonus + " (Slot: " + equipSlot + ")";
    }
}