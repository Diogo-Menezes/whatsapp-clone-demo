package com.diogomenezes.whatsappclonedemo.parse_Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.diogomenezes.whatsappclonedemo.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.diogomenezes.whatsappclonedemo.parse_Activities.ParseChatActivity.DATE;
import static com.diogomenezes.whatsappclonedemo.parse_Activities.ParseChatActivity.FROM;
import static com.diogomenezes.whatsappclonedemo.parse_Activities.ParseChatActivity.MESSAGE;
import static com.diogomenezes.whatsappclonedemo.parse_Activities.ParseChatActivity.MESSAGE_PARSE_CLASS;
import static com.diogomenezes.whatsappclonedemo.parse_Activities.ParseChatActivity.TO;
import static com.diogomenezes.whatsappclonedemo.parse_Activities.ParseLoginActivity.USERNAME;

public class ParseUserListActivity extends AppCompatActivity implements ListView.OnItemClickListener {
    public static final String FRIEND_NAME = "friend_name";

    private ListView userListView;
    private ArrayList<String> mFriendList = new ArrayList<>();
    private ArrayList<String> lastMessageList = new ArrayList<>();
    private ParseUser user;
    private SimpleAdapter simpleAdapter;
    private boolean hasFriends = false;
    private int counter = 0;
    private int lastMessageCount = 0;
    private List<Map<String, String>> nameMessageList = new ArrayList<>();
    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_friend_user_list);
        setTitle("Friend List");
        user = ParseUser.getCurrentUser();
        userListView = findViewById(R.id.userListView);
        simpleAdapter = new SimpleAdapter(this, nameMessageList, android.R.layout.simple_list_item_2, new String[]{FRIEND_NAME, MESSAGE}, new int[]{android.R.id.text1, android.R.id.text2});
        userListView.setAdapter(simpleAdapter);
        getFriendsList();
        userListView.setOnItemClickListener(this);

    }

    private void getLastMessage(final String friendName) {
        message = "";
        ParseQuery<ParseObject> query = ParseQuery.getQuery(MESSAGE_PARSE_CLASS);
        ParseQuery<ParseObject> query1 = ParseQuery.getQuery(MESSAGE_PARSE_CLASS);
        ArrayList<ParseQuery<ParseObject>> queries = new ArrayList<>();

        query.whereEqualTo(FROM, ParseUser.getCurrentUser().getUsername());
        query.whereEqualTo(TO, friendName);
        query1.whereEqualTo(TO, ParseUser.getCurrentUser().getUsername());
        query1.whereEqualTo(FROM, friendName);
        queries.add(query);
        queries.add(query1);

        ParseQuery<ParseObject> messageQuery = ParseQuery.or(queries);
        messageQuery.orderByDescending(DATE);
        messageQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects.size() > 0) {
                    for (int i = 0; i < 1; i++) {
                        message = objects.get(i).getString(MESSAGE);
                    }
                } else {
                    message = "No messages";
                }
                Map<String, String> data = new HashMap<>();
                data.put(FRIEND_NAME, friendName);
                data.put(MESSAGE, message);
                nameMessageList.add(data);
                simpleAdapter.notifyDataSetChanged();
            }
        });

    }

    public void getFriendsList() {
        ParseQuery<ParseUser> userParseQuery = ParseUser.getQuery();
        userParseQuery.whereNotEqualTo(USERNAME, user.getUsername());
        userParseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                mFriendList.clear();
                lastMessageList.clear();
                nameMessageList.clear();
                if (e == null && objects.size() > 0) {
                    for (ParseUser user : objects) {
                        mFriendList.add(user.getUsername());
                    }
                    counter = mFriendList.size();
                    hasFriends = true;
                    for (int i = 0; i < mFriendList.size(); i++) {
                        getLastMessage(mFriendList.get(i));
                    }
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (hasFriends) {
            Intent intent = new Intent(ParseUserListActivity.this, ParseChatActivity.class);
            intent.putExtra(FRIEND_NAME, mFriendList.get(position));
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.friend_list_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.sign_out_menu) {
            ParseUser.logOut();
            startActivity(new Intent(ParseUserListActivity.this, ParseLoginActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
