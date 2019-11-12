package com.diogomenezes.whatsappclonedemo.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "message_table")
public class Message {

    public static final int TEXT_MESSAGE = 0;
    public static final int VOICE_MESSAGE = 1;
    public static final int IMAGE_MESSAGE = 2;
    public static final int VIDEO_MESSAGE = 3;

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int messageID;

    @ColumnInfo(name = "message")
    private String message;

    @ColumnInfo(name = "message_time")
    private String time;

    @NonNull
    @ColumnInfo(name = "from_id")
    private int from;

    @NonNull
    @ColumnInfo(name = "to_id")
    private int to;

    @ColumnInfo(name = "image_message")
    private String image;

    @ColumnInfo(name = "voice_mail_uri")
    private String voiceMailUri;

    @ColumnInfo(name = "voice_mail_duration")
    private String voiceMailDuration;

    @NonNull
    @ColumnInfo(name = "message_type")
    private int type;

    @NonNull
    @ColumnInfo(name = "message_date")
    private long date;

    private long dateReceived;
    private long dateRead;
    private int sent;
    private int received;

    public Message() {
    }

    //MESSAGE CONSTRUCTOR
    @Ignore
    public Message(int type, String message, int from, int to, long date) {
        this.message = message;
        this.from = from;
        this.to = to;
        this.type = type;
        this.date = date;
    }

    @Ignore
    public Message(int type) {
        this.type = type;
    }

    @Ignore
    public Message(int type, String time, String voiceMailUri, String voiceMailDuration, int from, int to, long date) {
        this.type = type;
        this.time = time;
        this.from = from;
        this.voiceMailUri = voiceMailUri;
        this.voiceMailDuration = voiceMailDuration;
        this.date = date;
    }

    @Ignore
    public Message(int type, String message, String time, int from, long date) {
        this.type = type;
        this.message = message;
        this.time = time;
        this.from = from;
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getMessageID() {
        return messageID;
    }

    public void setMessageID(int messageID) {
        this.messageID = messageID;
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

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVoiceMailUri() {
        return voiceMailUri;
    }

    public void setVoiceMailUri(String voiceMailUri) {
        this.voiceMailUri = voiceMailUri;
    }

    public String getVoiceMailDuration() {
        return voiceMailDuration;
    }

    public void setVoiceMailDuration(String voiceMailDuration) {
        this.voiceMailDuration = voiceMailDuration;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(long dateReceived) {
        this.dateReceived = dateReceived;
    }

    public long getDateRead() {
        return dateRead;
    }

    public void setDateRead(long dateRead) {
        this.dateRead = dateRead;
    }

    public int getSent() {
        return sent;
    }

    public void setSent(int sent) {
        this.sent = sent;
    }

    public int getReceived() {
        return received;
    }

    public void setReceived(int received) {
        this.received = received;
    }
}
