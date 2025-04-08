package game4;

public class MonsterFactory {
    public static Monster createMonster(String type) {
        switch (type.toLowerCase()) {
            case "man-eating plant":
                return new Monster("Man-Eating Plant", 40, 15, "Vine Whip");
            case "poisonous mushroom":
                return new Monster("Poisonous Mushroom", 30, 10, "Spore Cloud");
            case "giant bat":
                return new Monster("Giant Bat", 50, 20, "Sonic Screech");
            case "poison spider":
                return new Monster("Poison Spider", 45, 25, "Web Shot");
            case "red dragon":
                return new Monster("Red Dragon", 200, 50, "Fire Breath");
            default:
                return new Monster("Slime", 20, 5, "Acid Splash");
        }
    }

    public static Monster createRandomMonster(int floor) {
        String[] floor1Monsters = { "Man-Eating Plant", "Poisonous Mushroom" };
        String[] floor2Monsters = { "Giant Bat", "Poison Spider" };
        String[] floor3Monsters = { "Giant Bat", "Poison Spider", "Red Dragon" };

        String[] availableMonsters;
        if (floor == 1) {
            availableMonsters = floor1Monsters;
        } else if (floor == 2) {
            availableMonsters = floor2Monsters;
        } else {
            availableMonsters = floor3Monsters;
        }

        String monsterType = availableMonsters[(int) (Math.random() * availableMonsters.length)];
        return createMonster(monsterType);
    }
}