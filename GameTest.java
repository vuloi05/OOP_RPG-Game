package game4;

public class GameTest {
    public static void main(String[] args) {
        // Tạo nhân vật người chơi
        Player laios = new Player("Laios", "Warrior", 100, 30,
                new String[] { "Sword Slash", "Shield Bash", "Revitalize" });
        Player marcille = new Player("Marcille", "Mage", 70, 100,
                new String[] { "Fireball", "Ice Shard", "Lightning Bolt" });

        // Tạo nhóm
        Party party = new Party();
        party.addMember(laios);
        party.addMember(marcille);

        // Tạo quái vật
        Monster monster = MonsterFactory.createMonster("Red Dragon");

        // Chiến đấu đơn giản
        System.out.println("A " + monster.getName() + " appears!");

        while (monster.isAlive() && !party.isDefeated()) {
            // Nhân vật tấn công
            laios.basicAttack(monster);
            if (!monster.isAlive())
                break;

            marcille.useSkill(0, monster); // Fireball
            if (!monster.isAlive())
                break;

            // Quái vật tấn công
            Character target = party.getRandomAliveMember();
            if (target != null) {
                monster.attack(target);
            }
        }

        // Kết quả
        if (monster.isAlive()) {
            System.out.println("The party has been defeated!");
        } else {
            System.out.println("The " + monster.getName() + " has been defeated!");
            // Phần thưởng kinh nghiệm
            for (Character member : party.getMembers()) {
                if (member instanceof Player) {
                    ((Player) member).gainExperience(monster.getExperienceReward());
                }
            }
        }
    }
}