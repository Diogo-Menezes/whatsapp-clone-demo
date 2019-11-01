package com.diogomenezes.whatsappclonedemo.objects;

import android.graphics.Bitmap;

public class ChatList {
    private String contactName;
    private String lastMessage;
    private Bitmap contactImage;
    private String date;
    private int unreadMessages;

    public ChatList(String contactName, String lastMessage, Bitmap contactImage, String date, int unreadMessages) {
        this.contactName = contactName;
        this.lastMessage = lastMessage;
        this.contactImage = contactImage;
        this.date = date;
        this.unreadMessages = unreadMessages;
    }

    public ChatList() {

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

    public Bitmap getContactImage() {
        return contactImage;
    }

    public void setContactImage(Bitmap contactImage) {
        this.contactImage = contactImage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getUnreadMessages() {
        return unreadMessages;
    }

    public void setUnreadMessages(int unreadMessages) {
        this.unreadMessages = unreadMessages;
    }
}
