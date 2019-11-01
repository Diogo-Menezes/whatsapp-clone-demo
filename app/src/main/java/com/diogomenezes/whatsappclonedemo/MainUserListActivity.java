package com.diogomenezes.whatsappclonedemo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diogomenezes.whatsappclonedemo.object.ChatList;

import java.util.ArrayList;

public class MainUserListActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private ChatListAdapter adapter;
    private ArrayList<ChatList> mChatList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user_list);

        recyclerView = findViewById(R.id.userListRecView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);


        adapter = new ChatListAdapter();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }
}
