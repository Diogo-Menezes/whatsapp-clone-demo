package com.diogomenezes.whatsappclonedemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.diogomenezes.whatsappclonedemo.ChatActivity.MESSAGE_PARSE_CLASS;
import static com.diogomenezes.whatsappclonedemo.MainActivity.USERNAME;

public class UserListActivity extends AppCompatActivity implements ListView.OnItemClickListener {
    public static final String FRIEND_NAME = "friend_name";

    private ListView userListView;
    private ArrayList<String> mUserList = new ArrayList<>();
    private ParseUser user;
    private ArrayAdapter mAdapter;
    private boolean hasFriends = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list_activity);
        setTitle("Friend List");
        user = ParseUser.getCurrentUser();
        userListView = findViewById(R.id.userListView);
        mUserList.add("Contact 1");
        mAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, mUserList);
        userListView.setAdapter(mAdapter);
        getFriendsList();

        userListView.setOnItemClickListener(this);

    }

    public void getFriendsList() {
        ParseQuery<ParseUser> userParseQuery = ParseUser.getQuery();
        userParseQuery.whereNotEqualTo(USERNAME, user.getUsername());
        userParseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                mUserList.clear();
                if (e == null && objects.size() > 0) {
                    for (ParseUser user : objects) {
                        mUserList.add(user.getUsername());
                    }
                    hasFriends = true;
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (hasFriends) {
            Intent intent = new Intent(UserListActivity.this, ChatActivity.class);
            intent.putExtra(FRIEND_NAME, mUserList.get(position));
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
