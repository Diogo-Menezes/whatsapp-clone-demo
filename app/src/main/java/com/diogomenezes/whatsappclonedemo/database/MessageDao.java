package com.diogomenezes.whatsappclonedemo.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.diogomenezes.whatsappclonedemo.models.Message;

import java.util.List;

@Dao
public interface MessageDao {

    @Insert
    void insert(Message... messages);

    @Update
    void update(Message... messages);

    @Delete
    void delete(Message... messages);

    @Query("SELECT * FROM message_table WHERE from_id=:from AND to_id=:from2 OR from_id=:to AND to_id=:to2 ORDER BY message_date DESC")
    List<Message> getAllMessages(String from, String to, String to2, String from2);
}
