package com.dungeondelicacy.rpg.systems;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private List<Item> items; // Danh sách vật phẩm
    private int maxCapacity; // Dung lượng tối đa của túi đồ

    public Inventory(int maxCapacity) {
        this.items = new ArrayList<>();
        this.maxCapacity = maxCapacity;
    }

    // Thêm vật phẩm vào túi đồ
    public boolean addItem(Item item) {
        if (items.size() >= maxCapacity) {
            System.out.println("Inventory is full! Cannot add " + item.getName());
            return false;
        }
        // Kiểm tra xem vật phẩm đã có trong túi đồ chưa
        for (Item existingItem : items) {
            if (existingItem.getName().equals(item.getName()) && existingItem.getClass().equals(item.getClass())) {
                existingItem.addQuantity(item.getQuantity());
                return true;
            }
        }
        items.add(item);
        System.out.println("Added " + item.getName() + " (x" + item.getQuantity() + ") to inventory");
        return true;
    }

    // Xóa vật phẩm khỏi túi đồ
    public void removeItem(Item item) {
        items.remove(item);
        System.out.println("Removed " + item.getName() + " from inventory");
    }

    // Getter
    public List<Item> getItems() {
        return new ArrayList<>(items); // Trả về bản sao để bảo vệ dữ liệu
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public int getCurrentSize() {
        return items.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Inventory (").append(items.size()).append("/").append(maxCapacity).append("):\n");
        for (Item item : items) {
            sb.append("- ").append(item.toString()).append("\n");
        }
        return sb.toString();
    }
}