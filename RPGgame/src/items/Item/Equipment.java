package items.Item;

public class Equipment extends Item {
    private int attackPower;
    private int defensePower;
    private String equipmentType;

    public Equipment(int itemId, String name, String description, int value, int attackPower, int defensePower, String equipmentType) {
        super(itemId, name, description, value);
        this.attackPower = attackPower;
        this.defensePower = defensePower;
        this.equipmentType = equipmentType;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public int getDefensePower() {
        return defensePower;
    }

    public String getEquipmentType() {
        return equipmentType;
    }

    @Override
    public void use() {
        System.out.println("Trang bị " + getName() + ": Loại " + equipmentType);
        System.out.println("Tăng " + attackPower + " attack.");
        System.out.println("Tăng " + defensePower + " defense.");
    }
}
