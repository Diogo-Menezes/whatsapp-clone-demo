package com.diogomenezes.whatsappclonedemo.objects;

public class Message {
    private String name;
    private String message;
    private String date;
    private int layoutPosition;

    public Message() {
    }

    public Message(String name, String message, String date, int layoutPosition) {
        this.name = name;
        this.message = message;
        this.date = date;
        this.layoutPosition = layoutPosition;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getLayoutPosition() {
        return layoutPosition;
    }

    public void setLayoutPosition(int layoutPosition) {
        this.layoutPosition = layoutPosition;
    }
}
