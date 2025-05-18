package items.Item;

// trang bị gồm dame , giáp, loại trang bị (vũ khí giáp nhẫn vv)

public class Equipment extends Item {
    private int attackPower;    // dame
    private int defensePower;   // phòng thủ
    private String equipmentType;

    public Equipment(int itemId, String name, String description, int value, String imagePath, int attackPower, int defensePower, String equipmentType) {
        super(itemId, name, description, value, imagePath);
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
    public void useItem() {
        System.out.println("Trang bị " + getName() + ": Loại " + equipmentType);
        System.out.println("Tăng " + attackPower + " sức tấn công.");
        System.out.println("Tăng " + defensePower + " phòng thủ.");
    }
}
