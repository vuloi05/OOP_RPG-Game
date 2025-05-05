import java.util.ArrayList;
import java.util.List;

public class NPC {
    private String name;
    private String location;
    private List<Quest> quests;

    public NPC(String name, String location) {
        this.name = name;
        this.location = location;
        this.quests = new ArrayList<>();
    }

    public void addQuest(Quest quest) {
        quests.add(quest);
    }

    public void interact() {
        System.out.println("Bạn đang trò chuyện với " + name);
        if (quests.isEmpty()) {
            System.out.println(name + " không có nhiệm vụ nào cho bạn lúc này.");
        } else {
            System.out.println(name + " có các nhiệm vụ sau:");
            for (Quest q : quests) {
                System.out.println("- " + q.getTitle() + ": " + q.getDescription());
            }
        }
    }

    public List<Quest> getQuests() {
        return quests;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }
}
