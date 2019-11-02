package com.diogomenezes.whatsappclonedemo.models;

public class ChatMessage {

    private String message;
    private String time;
    private int from;

    public ChatMessage() {
    }

    public ChatMessage(String message, String time, int from) {
        this.message = message;
        this.time = time;
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }
}