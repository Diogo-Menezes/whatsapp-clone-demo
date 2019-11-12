package com.diogomenezes.whatsappclonedemo.models;

public class ContactList {

    private String contactName,
            lastMessage,
            date,
            unreadMessages,
            contactImage;


    public ContactList(String contactName, String lastMessage, String date, String unreadMessages, String contactImage) {
        this.contactName = contactName;
        this.lastMessage = lastMessage;
        this.date = date;
        this.unreadMessages = unreadMessages;
        this.contactImage = contactImage;
    }

    public ContactList() {
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

    public String getContactImage() {
        return contactImage;
    }

    public void setContactImage(String contactImage) {
        this.contactImage = contactImage;
    }

}
