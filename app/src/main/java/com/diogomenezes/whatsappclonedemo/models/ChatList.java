package com.diogomenezes.whatsappclonedemo.models;

import android.graphics.Bitmap;

public class ChatList {

    private String contactName,
            lastMessage,
            date,
            unreadMessages;

    private Bitmap contactImage;



    public ChatList(String contactName, String lastMessage, String date, String unreadMessages, Bitmap contactImage) {
        this.contactName = contactName;
        this.lastMessage = lastMessage;
        this.date = date;
        this.unreadMessages = unreadMessages;
        this.contactImage = contactImage;
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

}
