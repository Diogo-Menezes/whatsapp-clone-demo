package com.diogomenezes.whatsappclonedemo.ui.contactList.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diogomenezes.whatsappclonedemo.R;
import com.diogomenezes.whatsappclonedemo.adapter.ChatListAdapter;
import com.diogomenezes.whatsappclonedemo.models.ContactList;
import com.diogomenezes.whatsappclonedemo.ui.chat.ChatActivity;
import com.diogomenezes.whatsappclonedemo.ui.contactList.FriendListActivity;
import com.diogomenezes.whatsappclonedemo.ui.contactList.ImageClickDialog;

import java.util.ArrayList;

import static com.diogomenezes.whatsappclonedemo.parse_Activities.ParseChatActivity.FRIEND_NAME;
import static com.diogomenezes.whatsappclonedemo.ui.contactList.FriendListActivity.FRIEND_ID;
import static com.diogomenezes.whatsappclonedemo.ui.contactList.FriendListActivity.FRIEND_IMAGE;

public class ContactListFragment extends Fragment implements ChatListAdapter.ContactClick, ChatListAdapter.OnImageClick {

    //UI
    private RecyclerView recyclerView;

    //VARS
    private ChatListAdapter adapter;
    private ArrayList<ContactList> mChatList;
    private ContactList mChat;
    private Bitmap bitmap;
    private int contactPosition = 0;
    private DialogFragment dialogFragment;
    private ArrayList<Integer> userId = new ArrayList<>();
    private String friendImageUri;
    private ArrayList<String> friendImagesUri = new ArrayList<>();
    private ArrayList<Bitmap> images = new ArrayList<>();
    private Bitmap bitmap1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_friend_user_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        init(view);
        super.onViewCreated(view, savedInstanceState);
    }

    private void init(View view) {
        recyclerView = view.findViewById(R.id.userListRecView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        addFakeUsers();

        adapter = new ChatListAdapter(mChatList, this, this);
        recyclerView.setAdapter(adapter);

    }


    @Override
    public void contactImageClick(int position) {
        contactPosition = position;
        dialogFragment = new ImageClickDialog(mChatList.get(position).getContactName(), friendImagesUri.get(position));
        dialogFragment.show(getChildFragmentManager(),"contactDialog");
    }

    @Override
    public void contactClick(int position) {
//        Intent intent = new Intent(FriendListActivity.class, ChatActivity.class);
//        intent.putExtra(FRIEND_NAME, mChatList.get(position).getContactName());
//        intent.putExtra(FRIEND_ID, userId.get(position));
//        intent.putExtra(FRIEND_IMAGE, friendImagesUri.get(position));
//        startActivity(intent);

    }

    private void addFakeUsers() {
        String mNames[] = {"Claudia Phung ", "Truman Blackstock ", "Madlyn Stults ",
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

        friendImagesUri.add("https://images.pexels.com/photos/2092474/pexels-photo-2092474.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=9400");
        friendImagesUri.add("https://images.pexels.com/photos/2092709/pexels-photo-2092709.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940");
        friendImagesUri.add("https://images.pexels.com/photos/2120114/pexels-photo-2120114.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940");
        friendImagesUri.add("https://images.pexels.com/photos/2100063/pexels-photo-2100063.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940");
        friendImagesUri.add("https://images.pexels.com/photos/2128807/pexels-photo-2128807.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940");
        friendImagesUri.add("https://images.pexels.com/photos/2083751/pexels-photo-2083751.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940");
        friendImagesUri.add("https://images.pexels.com/photos/2104252/pexels-photo-2104252.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940");
        friendImagesUri.add("https://images.pexels.com/photos/1598087/pexels-photo-1598087.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940");
        friendImagesUri.add("https://images.pexels.com/photos/1787303/pexels-photo-1787303.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500");
        friendImagesUri.add("https://images.pexels.com/photos/2777429/pexels-photo-2777429.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500");

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.random_contact_picture, options);


        mChatList = new ArrayList<>();
        for (int i = 0; i < mNames.length; i++) {
            userId.add(i);
            mChat = new ContactList(mNames[i], messages[i], "Yesterday", "", friendImagesUri.get(i));
            mChatList.add(mChat);
        }
    }

    public void videoClick(View view) {
        Toast.makeText(view.getContext(), "VideoCall", Toast.LENGTH_SHORT).show();
        dialogFragment.dismiss();
    }

    public void phoneClick(View view) {
        Toast.makeText(view.getContext(), "Call", Toast.LENGTH_SHORT).show();
        dialogFragment.dismiss();
    }

    public void messageClick(View view) {
        contactClick(contactPosition);
        dialogFragment.dismiss();
    }

    public void contactInfoClick(View view) {
        Toast.makeText(view.getContext(), "Contact Info", Toast.LENGTH_SHORT).show();
        dialogFragment.dismiss();
    }
}
