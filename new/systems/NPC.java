package com.dungeondelicacy.rpg.systems;

import com.dungeondelicacy.rpg.characters.Player;
import com.dungeondelicacy.rpg.map.Interactable;

import java.util.ArrayList;
import java.util.List;

public class NPC implements Interactable {
    private String name; // Tên NPC
    private int x, y; // Vị trí NPC
    private String dialog; // Câu thoại
    private List<Item> shopItems; // Danh sách vật phẩm bán
    private Quest quest; // Nhiệm vụ mà NPC giao

    public NPC(String name, int x, int y, String dialog) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.dialog = dialog;
        this.shopItems = new ArrayList<>();
        this.quest = null;
    }

    @Override
    public void interact(Player player) {
        System.out.println(name + ": " + dialog);
    }

    // Phương thức để mua đồ
    public void buyItem(Player player, String itemName) {
        for (Item item : shopItems) {
            if (item.getName().equals(itemName)) {
                Item copy = new Item(item.getName(), item.getDescription(), 1) {
                    @Override
                    public void use(Player player) {
                        System.out.println(player.getName() + " used " + getName());
                    }
                };
                if (player.getInventory().addItem(copy)) {
                    System.out.println(player.getName() + " bought " + itemName);
                }
                return;
            }
        }
        System.out.println("Item " + itemName + " not found in shop.");
    }

    // Phương thức để giao nhiệm vụ
    public void giveQuest(Player player) {
        if (quest != null && !player.getActiveQuests().contains(quest)) {
            player.addQuest(quest);
        }
    }

    // Getter và Setter
    public String getName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getDialog() {
        return dialog;
    }

    public List<Item> getShopItems() {
        return new ArrayList<>(shopItems);
    }

    public void addShopItem(Item item) {
        shopItems.add(item);
    }

    public Quest getQuest() {
        return quest;
    }

    public void setQuest(Quest quest) {
        this.quest = quest;
    }
}