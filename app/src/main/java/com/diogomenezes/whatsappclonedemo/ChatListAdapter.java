package com.diogomenezes.whatsappclonedemo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diogomenezes.whatsappclonedemo.object.ChatList;

import java.util.ArrayList;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.chatListAdapterViewHolder> {

    private TextView contactName, lastMessage, date, unreadMessages;
    private ImageView contactPicture;
    private ArrayList<ChatList> mChatList;

    class chatListAdapterViewHolder extends RecyclerView.ViewHolder {
        public chatListAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            contactName = itemView.findViewById(R.id.contactNameChatTextView);
            lastMessage = itemView.findViewById(R.id.lastMessageChatTextView);
            date = itemView.findViewById(R.id.dateChatTextView);
            unreadMessages = itemView.findViewById(R.id.unreadMessageCountChatTextView);
            contactPicture = itemView.findViewById(R.id.contactImageChatList);
        }
    }

    @NonNull
    @Override
    public chatListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list_item_layout, parent, false);
        return new chatListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull chatListAdapterViewHolder holder, int position) {
        contactName.setText(mChatList.get(position).getContactName());
        lastMessage.setText(mChatList.get(position).getLastMessage());
        date.setText(mChatList.get(position).getDate());
        unreadMessages.setText(mChatList.get(position).getUnreadMessages());
        contactPicture.setImageBitmap(mChatList.get(position).getContactImage());
    }

    @Override
    public int getItemCount() {
        return mChatList.size();
    }


}
