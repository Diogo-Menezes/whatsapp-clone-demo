package com.diogomenezes.whatsappclonedemo.parse_activities;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.diogomenezes.whatsappclonedemo.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ParseChatActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener, ListView.OnItemClickListener, View.OnClickListener {
    public static final String FRIEND_NAME = "friendName";
    private static final String TAG = "ChatActivity";
    public static final String MESSAGE_PARSE_CLASS = "Messages";
    public static final String FROM = "from";
    public static final String TO = "to";
    public static final String MESSAGE = "message";
    public static final String DATE = "createdAt";
    private static final String OBJECT_ID = "objectId";
    private static final String MESSAGE_DATE = "messageDate";


    private ListView messagesListView;
    private ConstraintLayout chatLayout;
    private ArrayList<String> messageIdList = new ArrayList<>();
    private ArrayList<Long> messageDateList = new ArrayList<>();
    private ParseUser user;
    private SimpleAdapter mAdapter;
    private boolean hasFriends = false;
    private String friendName;
    private boolean noMessages = true;
    private EditText messageEdit;
    private ArrayList<Map<String, String>> dataList = new ArrayList<Map<String, String>>();

    private DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatLayout = findViewById(R.id.chatLayout);
        chatLayout.setOnClickListener(this);
        user = ParseUser.getCurrentUser();
        messageEdit = findViewById(R.id.messageEdit);
        messagesListView = findViewById(R.id.messagesListView);
        mAdapter = new SimpleAdapter(this, dataList,
                R.layout.chat_layout,
                new String[]{"message", "from"},
                new int[]{R.id.text1Chat, R.id.text2Chat});


        messagesListView.setAdapter(mAdapter);
        messagesListView.setOnItemLongClickListener(this);

        if (getIntent().getStringExtra(FRIEND_NAME) != null) {
            friendName = getIntent().getStringExtra(FRIEND_NAME);
            setTitle(friendName);
        } else {
            finish();
        }
        getMessageList();
        if (noMessages) {
            //do something
        }

    }

    public void getMessageList() {
        if (noMessages) {
            dataList.clear();
        }
        ParseQuery<ParseObject> query = ParseQuery.getQuery(MESSAGE_PARSE_CLASS);
        ParseQuery query1 = ParseQuery.getQuery(MESSAGE_PARSE_CLASS);
        ArrayList<ParseQuery<ParseObject>> queries = new ArrayList<>();
        query.whereEqualTo(FROM, user.getUsername());
        query.whereEqualTo(TO, friendName);
        query1.whereEqualTo(TO, user.getUsername());
        query1.whereEqualTo(FROM, friendName);

        queries.add(query);
        queries.add(query1);

        ParseQuery<ParseObject> messageQuery = ParseQuery.or(queries);

        messageQuery.orderByAscending(DATE);
        messageQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects.size() > 0) {
                    dataList.clear();
                    messageIdList.clear();
                    for (ParseObject object : objects) {
                        Map<String, String> data = new HashMap<>();
                        long date = (long) object.get(MESSAGE_DATE);
                        String message = object.getString(MESSAGE);
                        String from = object.getString(FROM) + "\n " + dateFormat.format(date);
                        data.put(MESSAGE, message);
                        data.put(FROM, from);
                        dataList.add(data);
                        noMessages = false;
                        messageIdList.add(object.getString(OBJECT_ID));
                        messageDateList.add(date);
                    }
                    mAdapter.notifyDataSetChanged();
                    messagesListView.smoothScrollToPosition(dataList.size());
                }
            }
        });
    }

    public void sendMessage(View view) {
        if (!messageEdit.getText().toString().isEmpty()) {
            ParseObject parseObject = new ParseObject(MESSAGE_PARSE_CLASS);
            parseObject.put(MESSAGE, messageEdit.getText().toString());
            parseObject.put(FROM, user.getUsername());
            parseObject.put(TO, friendName);
            parseObject.put(MESSAGE_DATE, System.currentTimeMillis());
            parseObject.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        getMessageList();
                        messageEdit.setText("");
                        messageEdit.setHint("Type something...");
//                        Map<String, String> data = new HashMap<>();
//                        String message = messageEdit.getText().toString();
//                        String from = user.getUsername();
//                        data.put(MESSAGE, message);
//                        data.put(FROM, from);
//                        dataList.add(data);
                        mAdapter.notifyDataSetChanged();
                        getMessageList();
                    } else {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            messageEdit.requestFocus();
            messageEdit.setHint("Type something...");
        }

    }

    public void closeKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(messageEdit.getWindowToken(), 0);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int i, long id) {
        final int position = i;
        AlertDialog.Builder builder = new AlertDialog.Builder(ParseChatActivity.this);
        builder.setTitle("Delete").setMessage("Are you sure you want to delete this message?").setNegativeButton("Cancel", null);
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ParseQuery<ParseObject> query = ParseQuery.getQuery(MESSAGE_PARSE_CLASS);
                query.whereEqualTo(MESSAGE_DATE, messageDateList.get(position));
//                query.whereEqualTo(OBJECT_ID, messageIdList.get(position));
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
//                        Log.i(TAG, "message delete: "+ objects.size());
                        if (e == null && objects.size() > 0) {
                            for (ParseObject object : objects) {
                                object.deleteInBackground();
                                getMessageList();
//                                dataList.remove(position);
//                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
            }
        }).show();
        return true;
    }


    @Override
    public void onClick(View v) {
        closeKeyboard();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        closeKeyboard();
    }
}
