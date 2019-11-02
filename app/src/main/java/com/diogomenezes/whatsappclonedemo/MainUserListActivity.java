package com.diogomenezes.whatsappclonedemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diogomenezes.whatsappclonedemo.adapter.ChatListAdapter;
import com.diogomenezes.whatsappclonedemo.models.ChatList;

import java.util.ArrayList;

public class MainUserListActivity extends AppCompatActivity implements ChatListAdapter.ContactClick {


    //UI
    private RecyclerView recyclerView;

    //VARS
    private ChatListAdapter adapter;
    private ArrayList<ChatList> mChatList;
    private ChatList mChat;
    private Bitmap bitmap;

    private String[] mNames = {"Claudia Phung ", "Truman Blackstock ", "Madlyn Stults ",
            "Chu Talton ", "Cherry Hier ", "Denisha Shuster ",
            "Elinor Asher ", "Fredricka Castellanos ", "Nana Depaolo ", "Toni Webre"};

    private String[] messages = {
            "Inceptos volutpat nonummy. Condimentum tempus ac tortor accumsan non aenean.",
            "Volutpat sit duis. Varius. Posuere urna taciti convallis senectus praesent.",
            "Arcu a odio magna Gravida porttitor ullamcorper. Enim, at netus.",
            "Ultricies vivamus pellentesque at vivamus fermentum Non conubia eleifend orci.",
            "Inceptos torquent a quam auctor, ornare sem aliquam in sociosqu.",
            "Inceptos volutpat nonummy. ",
            "Volutpat sit duis. Varius. ",
            "Arcu a odio magna Gravida.",
            "Ultricies vivamus pellentesque at vivamus.",
            "Inceptos torquent a quam auctor, ornare sem aliquam in sociosqu."
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user_list);

        recyclerView = findViewById(R.id.userListRecView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.whatsapp_logo, options);

        mChatList = new ArrayList<>();
        for (int i = 0; i < mNames.length; i++) {
            mChat = new ChatList(mNames[i], messages[i], "Yesterday", ""+ i, bitmap);
            mChatList.add(mChat);
        }


        adapter = new ChatListAdapter(mChatList, this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void contactClick(int position) {
        Toast.makeText(this, mChatList.get(position).getContactName(), Toast.LENGTH_SHORT).show();
    }
}
