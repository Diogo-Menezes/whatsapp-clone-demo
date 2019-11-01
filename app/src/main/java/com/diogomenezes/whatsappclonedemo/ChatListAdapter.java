package com.diogomenezes.whatsappclonedemo;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.chatListAdapterViewHolder> {

    private TextView contactName, lastMessage, date, unreadMessages;
    private ImageView contactPicture;

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
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull chatListAdapterViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }


}
