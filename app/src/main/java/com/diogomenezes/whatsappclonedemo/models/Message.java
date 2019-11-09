package com.diogomenezes.whatsappclonedemo.models;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "message_table")
public class Message {

    public static final int TEXT_MESSAGE = 0;
    public static final int VOICE_MESSAGE = 1;
    public static final int IMAGE_MESSAGE = 2;
    public static final int VIDEO_MESSAGE = 3;

    public Message(String message, int from, int to, int type, long date) {
        this.message = message;
        this.from = from;
        this.to = to;
        this.type = type;
        this.date = date;
    }

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
    private Bitmap bitmap;

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

    public Message() {
    }

    public Message(int type) {
        this.type = type;
    }

    public Message(int type, String message, String time, int from, Bitmap bitmap) {
        this.type = type;
        this.message = message;
        this.time = time;
        this.from = from;
        this.bitmap = bitmap;
    }

    public Message(int type, String time, String voiceMailUri, String voiceMailDuration, int from) {
        this.type = type;
        this.time = time;
        this.from = from;
        this.voiceMailUri = voiceMailUri;
        this.voiceMailDuration = voiceMailDuration;
    }

    public Message(int type, String time, int from, Bitmap bitmap) {
        this.type = type;
        this.time = time;
        this.from = from;
        this.bitmap = bitmap;
    }

    public Message(int type, String message, String time, int from) {
        this.type = type;
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

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
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
}
