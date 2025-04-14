package characters;

import items.Item.Item;
import java.util.ArrayList;
import java.util.List;


public class Player extends Character {
    public Player(String name, int x, int y){
        super(name, x, y);
    }

    @Override
    public void performAction(){
        System.out.println(getName() + " performs a player-specific action.");
    }

    private List<Item> inventory = new ArrayList<>();

    public List<Item> getInventory() {
        return inventory;
    }

    public void addItem(Item item) {
        inventory.add(item);
    }

}
