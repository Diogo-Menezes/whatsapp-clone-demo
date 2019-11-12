package com.diogomenezes.whatsappclonedemo.ui.contactList;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.diogomenezes.whatsappclonedemo.R;
import com.diogomenezes.whatsappclonedemo.ui.contactList.fragments.ContactListFragment;

public class FriendListActivity extends AppCompatActivity  {
    private static final String TAG = "FriendListActivity";
    public static final String FRIEND_ID = "friend_id";
    public static final String FRIEND_IMAGE = "friend_image_uri";
    public static int USER_ID = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        findViewById(R.id.fragment_host);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_host, new ContactListFragment()).commit();
    }

}
