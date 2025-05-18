package items.Item;

public abstract class Item {
    private int ItemId;
    private String Name;    // tên
    private String Description; // mô tả
    private int Value;  // giá bản vvv
    private String imagePath;  // ảnh

    public Item(int ItemId, String Name, String Description, int Value, String imagePath) {
        this.ItemId = ItemId;
        this.Name = Name;
        this.Description = Description;
        this.Value = Value;
        this.imagePath = imagePath;
    }

    public int getItemId() {
        return ItemId;
    }

    public String getName() {
        return Name;
    }

    public String getDescription() {
        return Description;
    }

    public int getValue() {
        return Value;
    }

    public String getImagePath() {
        return imagePath;
    }

    public abstract void useItem();

    public void displayItemInfo() {
        System.out.println("Tên: " + Name);
        System.out.println("Mô tả: " + Description);
        System.out.println("Giá trị: " + Value);
    }
}
