package com.diogomenezes.whatsappclonedemo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diogomenezes.whatsappclonedemo.R;
import com.diogomenezes.whatsappclonedemo.models.ChatList;

import java.util.ArrayList;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.chatListAdapterViewHolder> {

    private ArrayList<ChatList> mChatList;// = new ArrayList<>();
    private ContactClick mOnContactClick;

    public ChatListAdapter(ArrayList<ChatList> mChatList, ContactClick mOnContactClick) {
        this.mChatList = mChatList;
        this.mOnContactClick = mOnContactClick;
    }

    class chatListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView contactName, lastMessage, date, unreadMessages;
        private ImageView contactPicture;
        private ContactClick mOnContactClick;

        public chatListAdapterViewHolder(@NonNull View itemView, ContactClick contactClick) {
            super(itemView);
            contactName = itemView.findViewById(R.id.contactNameChatTextView);
            lastMessage = itemView.findViewById(R.id.lastMessageChatTextView);
            date = itemView.findViewById(R.id.dateChatTextView);
            unreadMessages = itemView.findViewById(R.id.unreadMessageCountChatTextView);
            contactPicture = itemView.findViewById(R.id.contactImageChatList);
            this.mOnContactClick = contactClick;
        }

        @Override
        public void onClick(View v) {
            mOnContactClick.contactClick(getAdapterPosition());

        }
    }

    public interface ContactClick {
        void contactClick(int position);
    }

    @NonNull
    @Override
    public chatListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list_item_layout, parent, false);
        return new chatListAdapterViewHolder(view, mOnContactClick);
    }

    @Override
    public void onBindViewHolder(@NonNull chatListAdapterViewHolder holder, int position) {
        holder.contactName.setText(mChatList.get(position).getContactName());
        holder.lastMessage.setText(mChatList.get(position).getLastMessage());
        holder.date.setText(mChatList.get(position).getDate());
        holder.unreadMessages.setText(mChatList.get(position).getUnreadMessages());
        holder.contactPicture.setImageBitmap(mChatList.get(position).getContactImage());
    }

    @Override
    public int getItemCount() {
        return mChatList.size();
    }


}
