package com.diogomenezes.whatsappclonedemo.database;

import android.app.Application;
import android.os.AsyncTask;

import com.diogomenezes.whatsappclonedemo.models.Message;

import java.util.ArrayList;
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
        new DeleteMessageAsycn(messageDao).execute(message);

    }

    public void deleteAll() {
        messageDao.deleteAll();

    }

    public List<Message> getAll(int from, int to) {
        List<Message> messages = new ArrayList<>();
        messages.addAll(messageDao.getAllMessages(from, to));
        return messages;
    }

    private static class DeleteMessageAsycn extends AsyncTask<Message, Void, Void> {

        private MessageDao messageDao;

        public DeleteMessageAsycn(MessageDao messageDao) {
            this.messageDao = messageDao;
        }

        @Override
        protected Void doInBackground(Message... messages) {
            messageDao.delete(messages[0]);
            return null;
        }
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
