package com.miniproject.nutritionapp.AdminActivities;

public class ModelAdminChat {

    private final String chatId;
    private final String chatName;

    private final String recieverId;

    public ModelAdminChat(String chatId, String chatName, String recieverId) {
        this.chatId = chatId;
        this.chatName = chatName;
        this.recieverId = recieverId;
    }

    public String getRecieverId() {
        return recieverId;
    }

    public String getChatId() {
        return chatId;
    }

    public String getChatName() {
        return chatName;
    }
}
