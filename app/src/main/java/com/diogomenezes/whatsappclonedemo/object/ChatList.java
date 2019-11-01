package com.diogomenezes.whatsappclonedemo.object;

import android.graphics.Bitmap;

public class ChatList {

    private String contactName,
            lastMessage,
            date,
            unreadMessages;

    private Bitmap contactImage;
    private int from;

    public ChatList() {
    }

    public ChatList(String contactName, String lastMessage, String date, String unreadMessages, Bitmap contactImage, int from) {
        this.contactName = contactName;
        this.lastMessage = lastMessage;
        this.date = date;
        this.unreadMessages = unreadMessages;
        this.contactImage = contactImage;
        this.from = from;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUnreadMessages() {
        return unreadMessages;
    }

    public void setUnreadMessages(String unreadMessages) {
        this.unreadMessages = unreadMessages;
    }

    public Bitmap getContactImage() {
        return contactImage;
    }

    public void setContactImage(Bitmap contactImage) {
        this.contactImage = contactImage;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }
}
