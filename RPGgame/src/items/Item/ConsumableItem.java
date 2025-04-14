package items.Item;

public class ConsumableItem extends Item {
    private int restoreAmount;
    private String restoreType;

    public ConsumableItem(int itemId, String name, String description, int value, int restoreAmount, String restoreType) {
        super(itemId, name, description, value);
        this.restoreAmount = restoreAmount;
        this.restoreType = restoreType;
    }

    public int getRestoreAmount() {
        return restoreAmount;
    }

    public String getRestoreType() {
        return restoreType;
    }

    @Override
    public void use() {
        System.out.println("Sử dụng " + getName() + ": Hồi phục " + restoreAmount + " " + restoreType);
    }
}

