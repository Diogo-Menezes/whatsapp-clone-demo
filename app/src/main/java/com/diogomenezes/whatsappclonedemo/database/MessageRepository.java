package com.diogomenezes.whatsappclonedemo.database;

import android.app.Application;
import android.os.AsyncTask;

import com.diogomenezes.whatsappclonedemo.models.Message;

import java.util.List;

public class MessageRepository {

    private MessageDatabase messageDatabase;
    private MessageDao messageDao;
    private List<Message> allMessages;


    public MessageRepository(Application application) {
        MessageDatabase messageDatabase = MessageDatabase.getInstance(application);
        messageDao = messageDatabase.messageDao();
    }

    public void insert(Message message) {
        new InsertMessageAsync(messageDao).execute(message);
    }

    public void delete(Message message) {

    }

    public void deleteAll(Message message) {

    }

    private static class InsertMessageAsync extends AsyncTask<Message, Void, Void> {
        private MessageDao dao;

        private InsertMessageAsync(MessageDao messageDao) {
            this.dao = messageDao;
        }

        @Override
        protected Void doInBackground(Message... messages) {
            dao.insert(messages[0]);
            return null;
        }
    }

}
