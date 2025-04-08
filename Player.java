package game4;

public class Player extends Character {
    private int experience;
    private int level;

    public Player(String name, String role, int maxHealth, int maxMana, String[] skills) {
        super(name, role, maxHealth, maxMana, skills);
        this.experience = 0;
        this.level = 1;
    }

    public void gainExperience(int exp) {
        this.experience += exp;
        checkLevelUp();
    }

    private void checkLevelUp() {
        int expNeeded = level * 100;
        if (experience >= expNeeded) {
            level++;
            experience -= expNeeded;
            System.out.println(getName() + " leveled up to level " + level + "!");

            // Tăng chỉ số khi lên cấp
            this.maxHealth += 10;
            this.health = maxHealth;
            this.maxMana += 5;
            this.mana = maxMana;
        }
    }

    // Getters
    public int getExperience() {
        return experience;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return super.toString() + " Lvl: " + level + " Exp: " + experience + "/" + (level * 100);
    }
}
