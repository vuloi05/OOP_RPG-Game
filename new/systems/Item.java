package com.dungeondelicacy.rpg.systems;

import com.dungeondelicacy.rpg.characters.Player;

public abstract class Item {
    private String name; // Tên vật phẩm
    private String description; // Mô tả vật phẩm
    private int quantity; // Số lượng vật phẩm

    public Item(String name, String description, int quantity) {
        this.name = name;
        this.description = description;
        this.quantity = Math.max(0, quantity);
    }

    // Phương thức trừu tượng để sử dụng vật phẩm
    public abstract void use(Player player);

    // Getter và Setter
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = Math.max(0, quantity);
    }

    // Tăng số lượng vật phẩm
    public void addQuantity(int amount) {
        this.quantity += amount;
        if (this.quantity < 0) {
            this.quantity = 0;
        }
    }

    @Override
    public String toString() {
        return name + " (x" + quantity + "): " + description;
    }
}