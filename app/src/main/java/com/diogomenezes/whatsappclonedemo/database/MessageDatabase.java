package com.diogomenezes.whatsappclonedemo.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.diogomenezes.whatsappclonedemo.models.Message;

@Database(entities = {Message.class}, version = 2,exportSchema = false)
public abstract class MessageDatabase extends RoomDatabase {

    public abstract MessageDao messageDao();
    private static final String DATABASE_NAME = "message_db";
    private static MessageDatabase instance;


    public static synchronized MessageDatabase getInstance(final Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(), MessageDatabase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();

        }
        return instance;
    }


}
