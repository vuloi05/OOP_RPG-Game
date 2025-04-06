package com.dungeondelicacy.rpg.items;

public abstract class Item {
    private int ItemId;
    private String Name;
    private String Description;
    private int Value;

    public Item(int ItemId, String Name, String Description, int Value) {
        this.ItemId = ItemId;
        this.Name = Name;
        this.Description = Description;
        this.Value = Value;
    }

    public abstract void use();

    public void displayItemInfo() {
        System.out.println("ID: " + ItemId);
        System.out.println("Tên: " + Name);
        System.out.println("Mô tả: " + Description);
        System.out.println("Giá trị: " + Value);
    }
}
