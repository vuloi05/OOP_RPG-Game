package com.dungeondelicacy.rpg.items;

public class ConsumableItem extends Item {  // vật phẩm tiêu thụ
    private int restoreAmount;
    private String restoreType; // phục hồi hp mana ,vvv

    public ConsumableItem(int itemId, String name, String description, int value, int restoreAmount, String restoreType) {
        super(itemId, name, description, value);
        this.restoreAmount = restoreAmount;
        this.restoreType = restoreType;
    }

    @Override
    public void use() {
        System.out.println("Sử dụng " + name + ": Hồi phục " + restoreAmount + " " + restoreType);
    }
}
