package characters;

public class Enemy extends Character {
    private String type; // Loại quái vật (Skeleton, Vampire, Dragon, v.v.)
    private String state; // Trạng thái (Idle, Moving, Attacking)

    public Enemy(String type, int x, int y) {
        super(type, x, y); // Tên của quái vật sẽ là loại quái vật
        this.type = type;
        this.state = "Idle"; // Trạng thái mặc định là Idle
    }

    // Getter và Setter
    public String getType() {
        return type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public void performAction() {
        System.out.println("Enemy " + getName() + " (" + type + ") is " + state + " and performs an action.");
    }
}
