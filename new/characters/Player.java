package com.dungeondelicacy.rpg.characters;

import com.dungeondelicacy.rpg.systems.Equipment;
import com.dungeondelicacy.rpg.systems.Inventory;
import com.dungeondelicacy.rpg.systems.Quest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player extends Character {
    private Inventory inventory; // Túi đồ của người chơi
    private List<Quest> activeQuests; // Danh sách nhiệm vụ đang làm
    private Map<String, Equipment> equippedItems; // Trang bị hiện tại (theo slot)
    private int attackBonus; // Tăng sát thương từ trang bị
    private int defenseBonus; // Tăng phòng thủ từ trang bị

    public Player(String name, int x, int y) {
        super(name, x, y);
        this.inventory = new Inventory(20); // Dung lượng túi đồ là 20
        this.activeQuests = new ArrayList<>();
        this.equippedItems = new HashMap<>();
        this.attackBonus = 0;
        this.defenseBonus = 0;
    }

    @Override
    public void performAction() {
        System.out.println(getName() + " performs a player-specific action.");
    }

    // Trang bị vật phẩm
    public boolean equipItem(Equipment equipment) {
        String slot = equipment.getEquipSlot();
        if (equippedItems.containsKey(slot)) {
            // Nếu slot đã có trang bị, thay thế và cập nhật chỉ số
            Equipment oldEquipment = equippedItems.get(slot);
            attackBonus -= oldEquipment.getAttackBonus();
            defenseBonus -= oldEquipment.getDefenseBonus();
        }
        equippedItems.put(slot, equipment);
        attackBonus += equipment.getAttackBonus();
        defenseBonus += equipment.getDefenseBonus();
        return true;
    }

    // Getter và Setter
    public Inventory getInventory() {
        return inventory;
    }

    public List<Quest> getActiveQuests() {
        return new ArrayList<>(activeQuests); // Trả về bản sao
    }

    public void addQuest(Quest quest) {
        activeQuests.add(quest);
        System.out.println(getName() + " accepted quest: " + quest.getTitle());
    }

    public void completeQuest(Quest quest) {
        activeQuests.remove(quest);
        System.out.println(getName() + " completed quest: " + quest.getTitle());
        // Thêm phần thưởng vào túi đồ
        if (quest.getReward() != null) {
            inventory.addItem(quest.getReward());
        }
    }

    public Map<String, Equipment> getEquippedItems() {
        return new HashMap<>(equippedItems);
    }

    public int getAttackBonus() {
        return attackBonus;
    }

    public int getDefenseBonus() {
        return defenseBonus;
    }
}