import java.util.ArrayList;
import java.util.List;

public class QuestTracker {
    private List<Quest> activeQuests = new ArrayList<>();

    public void acceptQuest(Quest quest) {
        activeQuests.add(quest);
        System.out.println("📜 Nhận nhiệm vụ: " + quest.getTitle());
    }

    public void update() {
        for (Quest quest : activeQuests) {
            quest.checkAndComplete();
        }
    }

    public void showActiveQuests() {
        System.out.println("📘 Danh sách nhiệm vụ đang theo dõi:");
        for (Quest quest : activeQuests) {
            String status = quest.isCompleted() ? "✅" : "⏳";
            System.out.println(status + " " + quest.getTitle());
        }
    }
}
