package com.miniproject.nutritionapp.HomeActivities;

public class ModelMessage {

    private final boolean isSender;
    private final String message,time;

    public boolean isSender() {
        return isSender;
    }

    public String getMessage() {
        return message;
    }

    public String getTime() {
        return time;
    }

    public ModelMessage(String message, String time, boolean isSender) {
        this.message = message;
        this.time = time;
        this.isSender = isSender;
    }
}
