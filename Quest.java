import java.util.function.Supplier;

public class Quest {
    private String id;
    private String title;
    private String description;
    private boolean isMainQuest;
    private boolean isCompleted;
    private String reward;
    private Supplier<Boolean> completionCondition;

    public Quest(String id, String title, String description, boolean isMainQuest, String reward, Supplier<Boolean> completionCondition) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.isMainQuest = isMainQuest;
        this.reward = reward;
        this.completionCondition = completionCondition;
        this.isCompleted = false;
    }

    public void checkAndComplete() {
        if (!isCompleted && completionCondition.get()) {
            completeQuest();
        }
    }

    private void completeQuest() {
        isCompleted = true;
        System.out.println("✅ Hoàn thành nhiệm vụ: " + title);
        System.out.println("🎁 Nhận phần thưởng: " + reward);
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public String getTitle() {
        return title;
    }

    public boolean isMainQuest() {
        return isMainQuest;
    }

    public String getDescription() {
        return description;
    }

    public String getReward() {
        return reward;
    }

    public String getId() {
        return id;
    }
}
