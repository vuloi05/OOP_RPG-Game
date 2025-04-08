package game4;

public class Character {
	 protected String name;
	    protected String role;
	    protected int health;
	    protected int maxHealth;
	    protected int mana;
	    protected int maxMana;
	    protected String[] skills;

	    public Character(String name, String role, int maxHealth, int maxMana, String[] skills) {
	        this.name = name;
	        this.role = role;
	        this.maxHealth = maxHealth;
	        this.health = maxHealth;
	        this.maxMana = maxMana;
	        this.mana = maxMana;
	        this.skills = skills;
	    }

	    public void basicAttack(Monster target) {
	        int damage = 10 + (int) (Math.random() * 10);
	        target.takeDamage(damage);
	        System.out.println(name + " attacks " + target.getName() + " for " + damage + " damage!");
	    }

	    public void useSkill(int skillIndex, Monster target) {
	        if (skillIndex >= 0 && skillIndex < skills.length) {
	            String skill = skills[skillIndex];
	            int manaCost = 15;

	            if (mana >= manaCost) {
	                mana -= manaCost;
	                int damage = 0;

	                switch (skill) {
	                    case "Sword Slash":
	                    case "Axe Throw":
	                        damage = 25 + (int) (Math.random() * 10);
	                        break;
	                    case "Shield Bash":
	                        damage = 15;
	                        target.takeDamage(damage);
	                        System.out.println(name + " uses " + skill + " on " + target.getName() +
	                                " for " + damage + " damage and stuns them!");
	                        return;
	                    case "Revitalize":
	                        heal(30);
	                        System.out.println(name + " uses " + skill + " and recovers 30 HP!");
	                        return;
	                    case "Fireball":
	                    case "Ice Shard":
	                    case "Lightning Bolt":
	                        damage = 30 + (int) (Math.random() * 15);
	                        break;
	                    case "Crossbow Shot":
	                    case "Sneak Attack":
	                        damage = 20 + (int) (Math.random() * 15);
	                        break;
	                    case "Trap Setup":
	                        System.out.println(name + " sets up a trap! The next enemy attack might fail!");
	                        return;
	                    case "Defensive Stance":
	                        System.out.println(name + " takes a defensive stance, reducing damage from next attack!");
	                        return;
	                    case "Hearty Meal":
	                        heal(40);
	                        System.out.println(name + " prepares a hearty meal and recovers 40 HP!");
	                        return;
	                }

	                target.takeDamage(damage);
	                System.out.println(name + " uses " + skill + " on " + target.getName() +
	                        " for " + damage + " damage!");
	            } else {
	                System.out.println("Not enough mana! Using basic attack instead.");
	                basicAttack(target);
	            }
	        } else {
	            System.out.println("Invalid skill choice! Using basic attack instead.");
	            basicAttack(target);
	        }
	    }

	    public void heal(int amount) {
	        health = Math.min(maxHealth, health + amount);
	    }

	    public void takeDamage(int damage) {
	        health = Math.max(0, health - damage);
	    }

	    // Getters
	    public String getName() {
	        return name;
	    }

	    public String getRole() {
	        return role;
	    }

	    public int getHealth() {
	        return health;
	    }

	    public int getMaxHealth() {
	        return maxHealth;
	    }

	    public int getMana() {
	        return mana;
	    }

	    public int getMaxMana() {
	        return maxMana;
	    }

	    public String[] getSkills() {
	        return skills;
	    }

	    @Override
	    public String toString() {
	        return name + " (" + role + ") - HP: " + health + "/" + maxHealth + " MP: " + mana + "/" + maxMana;
	    }

	    @Override
	    public boolean equals(Object obj) {
	        if (this == obj)
	            return true;
	        if (obj == null || getClass() != obj.getClass())
	            return false;
	        Character other = (Character) obj;
	        return name.equals(other.name);
	    }
	
}
