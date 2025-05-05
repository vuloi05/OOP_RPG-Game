public class Main {
    static boolean npcRescued = false;

    public static void main(String[] args) {
        // Khởi tạo hệ thống theo dõi nhiệm vụ
        QuestTracker tracker = new QuestTracker();

        // Tạo một NPC
        NPC npc = new NPC("Thợ săn già", "Tầng 2: Rừng rậm");

        // Tạo một nhiệm vụ với phần thưởng và điều kiện hoàn thành
        Quest rescueQuest = new Quest(
            "Q001",
            "Giải cứu người bị mắc kẹt",
            "Tìm và giải cứu nhà thám hiểm bị mắc kẹt trong rừng.",
            false,
            "Thảo dược hồi máu x5",
            () -> npcRescued
        );

        // Gán nhiệm vụ cho NPC
        npc.addQuest(rescueQuest);

        // Người chơi tương tác với NPC
        npc.interact();

        // Người chơi nhận nhiệm vụ từ NPC
        for (Quest q : npc.getQuests()) {
            tracker.acceptQuest(q);
        }

        // Hiển thị nhiệm vụ đang theo dõi
        tracker.showActiveQuests();

        // Mô phỏng người chơi hoàn thành điều kiện nhiệm vụ
        System.out.println("\n👣 Người chơi giải cứu NPC bị kẹt...");
        npcRescued = true;

        // Cập nhật và kiểm tra nhiệm vụ
        tracker.update();
        tracker.showActiveQuests();
    }
}
