package com.diogomenezes.whatsappclonedemo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diogomenezes.whatsappclonedemo.adapter.ChatListAdapter;
import com.diogomenezes.whatsappclonedemo.models.ChatList;

import java.util.ArrayList;

public class MainUserListActivity extends AppCompatActivity implements ChatListAdapter.ContactClick {

    private RecyclerView recyclerView;
    private ChatListAdapter adapter;
    private ArrayList<ChatList> mChatList;
    private ChatList mChat;
    private Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user_list);

        recyclerView = findViewById(R.id.userListRecView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        mChat = new ChatList("Diogo", "Hi how are you??", "Yesterday", "", null);
        mChatList = new ArrayList<>();
        mChatList.add(mChat);
        adapter = new ChatListAdapter(mChatList, this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void contactClick(int position) {
        Toast.makeText(this, mChatList.get(position).getContactName(), Toast.LENGTH_SHORT).show();
    }
}
