package game4;

import java.util.ArrayList;
import java.util.List;

public class Party {
    private List<Character> members;

    public Party() {
        this.members = new ArrayList<>();
    }

    public void addMember(Character character) {
        members.add(character);
        System.out.println(character.getName() + " has joined the party!");
    }

    public void removeMember(Character character) {
        members.remove(character);
        System.out.println(character.getName() + " has left the party.");
    }

    public boolean isDefeated() {
        for (Character member : members) {
            if (member.getHealth() > 0) {
                return false;
            }
        }
        return true;
    }

    public void healAll(int amount) {
        for (Character member : members) {
            member.heal(amount);
        }
    }

    public void restoreManaAll(int amount) {
        for (Character member : members) {
            if (member instanceof Player) {
                Player player = (Player) member;
                player.heal(amount);
            }
        }
    }

    // Getters
    public List<Character> getMembers() {
        return members;
    }

    public int getSize() {
        return members.size();
    }

    public Character getMember(int index) {
        if (index >= 0 && index < members.size()) {
            return members.get(index);
        }
        return null;
    }

    public Character getRandomAliveMember() {
        List<Character> aliveMembers = new ArrayList<>();
        for (Character member : members) {
            if (member.getHealth() > 0) {
                aliveMembers.add(member);
            }
        }
        if (!aliveMembers.isEmpty()) {
            return aliveMembers.get((int) (Math.random() * aliveMembers.size()));
        }
        return null;
    }
}