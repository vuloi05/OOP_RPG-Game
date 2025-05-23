package com.dungeondelicacy.rpg.systems;

public class Quest {
    private String title; // Tiêu đề nhiệm vụ
    private String description; // Mô tả nhiệm vụ
    private Item reward; // Phần thưởng khi hoàn thành

    public Quest(String title, String description, Item reward) {
        this.title = title;
        this.description = description;
        this.reward = reward;
    }

    // Getter
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Item getReward() {
        return reward;
    }

    @Override
    public String toString() {
        return title + ": " + description + (reward != null ? " | Reward: " + reward.getName() : "");
    }
}