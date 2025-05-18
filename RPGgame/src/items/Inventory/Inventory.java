package items.Inventory;

import items.Item.Item;
import items.Item.Resource.ItemDB.*;

import java.util.ArrayList;
import java.util.List;

// túi đồ
public class Inventory {
    private final List<Item> items;

    public Inventory() {
        items = new ArrayList<>();
    }

    public void addItem(Item item) {
        items.add(item);
        System.out.println("Đã thêm vật phẩm: " + item.getName());
    }

    public void removeItem(Item item) {
        if (items.remove(item)) {
            System.out.println("Đã xóa vật phẩm: " + item.getName());
        } else {
            System.out.println("Không có vật phẩm để xóa.");
        }
    }

    public void showInventory() {
        if (items.isEmpty()) {
            System.out.println("Kho đồ trống.");
            return;
        }
        System.out.println("Danh sách vật phẩm trong kho:");
        for (Item item : items) {
            item.displayItemInfo();
            System.out.println("---------------");
        }
    }

    public void useItem(int itemId) {
        for (Item item : items) {
            if (item.getItemId() == itemId) {
                item.useItem();
                return;
            }
        }
        System.out.println("Không có vật phẩm.");
    }
}


