package game4;

public class Monster {
    private String name;
    private int health;
    private int maxHealth;
    private int damage;
    private String specialAttack;
    private int experienceReward;

    public Monster(String name, int health, int damage, String specialAttack) {
        this.name = name;
        this.health = health;
        this.maxHealth = health;
        this.damage = damage;
        this.specialAttack = specialAttack;
        this.experienceReward = health / 2; // Exp dựa trên máu của quái vật
    }

    public void attack(Character target) {
        if (Math.random() > 0.7) {
            // Special attack
            System.out.println(name + " uses " + specialAttack + "!");
            target.takeDamage((int) (damage * 1.5));
        } else {
            // Normal attack
            target.takeDamage(damage);
        }
    }

    public void takeDamage(int damage) {
        health = Math.max(0, health - damage);
    }

    public boolean isAlive() {
        return health > 0;
    }

    // Getters
    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getDamage() {
        return damage;
    }

    public String getSpecialAttack() {
        return specialAttack;
    }

    public int getExperienceReward() {
        return experienceReward;
    }

    @Override
    public String toString() {
        return name + " - HP: " + health + "/" + maxHealth + " Damage: " + damage + " Special: " + specialAttack;
    }
}