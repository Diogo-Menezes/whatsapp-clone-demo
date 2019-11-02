package com.diogomenezes.whatsappclonedemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diogomenezes.whatsappclonedemo.adapter.ChatListAdapter;
import com.diogomenezes.whatsappclonedemo.models.ChatList;

import java.util.ArrayList;

import static com.diogomenezes.whatsappclonedemo.parse_Activities.ChatActivity.FRIEND_NAME;

public class MainUserListActivity extends AppCompatActivity implements ChatListAdapter.ContactClick, ChatListAdapter.OnImageClick {


    //UI
    private RecyclerView recyclerView;

    //VARS
    private ChatListAdapter adapter;
    private ArrayList<ChatList> mChatList;
    private ChatList mChat;
    private Bitmap bitmap;
    private int contactPosition = 0;
    private DialogFragment dialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user_list);


        recyclerView = findViewById(R.id.userListRecView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        addFakeUsers();

        adapter = new ChatListAdapter(mChatList, this, this);
        recyclerView.setAdapter(adapter);


    }

    private void addFakeUsers() {
        String[] mNames = {"Claudia Phung ", "Truman Blackstock ", "Madlyn Stults ",
                "Chu Talton ", "Cherry Hier ", "Denisha Shuster ",
                "Elinor Asher ", "Fredricka Castellanos ", "Nana Depaolo ", "Toni Webre"};

        String[] messages = {
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

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.random_contact_picture, options);

        mChatList = new ArrayList<>();
        for (int i = 0; i < mNames.length; i++) {
            mChat = new ChatList(mNames[i], messages[i], "Yesterday", "" + 100 + i, bitmap);
            mChatList.add(mChat);
        }
    }

    @Override
    public void contactClick(int position) {

        Intent intent = new Intent(MainUserListActivity.this, NewChatActivity.class);
        intent.putExtra(FRIEND_NAME, mChatList.get(position).getContactName());
        startActivity(intent);
    }

    @Override
    public void contactImageClick(int position) {
        System.out.println(mChatList.get(position));
        dialogFragment = new ContactInfoDialog();
        dialogFragment.show(getSupportFragmentManager(), "contacInfo");
        contactPosition = position;
    }

    public void videoClick(View view) {
        Toast.makeText(this, "VideoCall", Toast.LENGTH_SHORT).show();
        dialogFragment.dismiss();
    }

    public void phoneClick(View view) {
        Toast.makeText(this, "Call", Toast.LENGTH_SHORT).show();
        dialogFragment.dismiss();
    }

    public void messageClick(View view) {
        contactClick(contactPosition);
        dialogFragment.dismiss();
    }

    public void contactInfoClick(View view) {
        Toast.makeText(this, "Contact Info", Toast.LENGTH_SHORT).show();
        dialogFragment.dismiss();
    }

}
