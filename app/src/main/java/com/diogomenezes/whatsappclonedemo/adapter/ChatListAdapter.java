package com.diogomenezes.whatsappclonedemo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diogomenezes.whatsappclonedemo.R;
import com.diogomenezes.whatsappclonedemo.models.ContactList;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.chatListAdapterViewHolder> {

    private ArrayList<ContactList> mChatList;
    private ContactClick mOnContactClick;
    private OnImageClick mOnImageClick;

    public ChatListAdapter(ArrayList<ContactList> mChatList, ContactClick mOnContactClick, OnImageClick imageClick) {
        this.mChatList = mChatList;
        this.mOnContactClick = mOnContactClick;
        this.mOnImageClick = imageClick;
    }

    class chatListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView contactName, lastMessage, date, unreadMessages;
        private CircleImageView contactPicture;
        private ContactClick contactClick;
        private OnImageClick imageClick;

        public chatListAdapterViewHolder(@NonNull final View itemView, ContactClick click, OnImageClick imageClick) {
            super(itemView);
            contactName = itemView.findViewById(R.id.contactNameChatTextView);
            lastMessage = itemView.findViewById(R.id.lastMessageChatTextView);
            date = itemView.findViewById(R.id.dateChatTextView);
            unreadMessages = itemView.findViewById(R.id.unreadMessageCountChatTextView);
            contactPicture = itemView.findViewById(R.id.contactImageChatList);
            itemView.setOnClickListener(this);
            this.contactClick = click;
            this.imageClick = imageClick;

            contactPicture.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.contactImageChatList) {
                imageClick.contactImageClick(getAdapterPosition());
            } else {
                mOnContactClick.contactClick(getAdapterPosition());
            }

        }
    }

    public interface OnImageClick {
        void contactImageClick(int position);
    }


    public interface ContactClick {
        void contactClick(int position);
    }

    @NonNull
    @Override
    public chatListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list_item_layout, parent, false);
        return new chatListAdapterViewHolder(view, mOnContactClick, mOnImageClick);
    }

    @Override
    public void onBindViewHolder(@NonNull chatListAdapterViewHolder holder, int position) {
        holder.contactName.setText(mChatList.get(position).getContactName());
        holder.lastMessage.setText(mChatList.get(position).getLastMessage());
        holder.date.setText(mChatList.get(position).getDate());

        if (mChatList.get(position).getUnreadMessages().equals("") || mChatList.get(position).getUnreadMessages().equals("0")) {
            holder.unreadMessages.setVisibility(View.GONE);
        } else {
            holder.unreadMessages.setText(mChatList.get(position).getUnreadMessages());
//            int unread = Integer.valueOf(mChatList.get(position).getUnreadMessages());
//            if (unread>999){
//                holder.unreadMessages.setText("+999");
//
//            }
        }
        holder.contactPicture.setImageBitmap(mChatList.get(position).getContactImage());
    }

    @Override
    public int getItemCount() {
        return mChatList.size();
    }


}
