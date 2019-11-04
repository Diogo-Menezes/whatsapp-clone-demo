package com.diogomenezes.whatsappclonedemo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diogomenezes.whatsappclonedemo.R;
import com.diogomenezes.whatsappclonedemo.models.MessageList;

import java.util.ArrayList;

import static com.diogomenezes.whatsappclonedemo.ChatActivity.FROM_FRIEND;
import static com.diogomenezes.whatsappclonedemo.ChatActivity.FROM_USER;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MessageViewHolder> {


    private ArrayList<MessageList> mChatMessageList;
    private MessageClick mMessageClick;

    public MessageListAdapter(ArrayList<MessageList> mChatMessage, MessageClick mMessageClick) {
        this.mChatMessageList = mChatMessage;
        this.mMessageClick = mMessageClick;
    }

    class MessageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView userMessageContent, userMesssageTime, friendMessageContent, friendMessageTime;
        private LinearLayout userMessageLayout, friendMessageLayout;
        private MessageClick messageClick;

        public MessageViewHolder(@NonNull View itemView, MessageClick click) {
            super(itemView);

            userMessageContent = itemView.findViewById(R.id.userMessageContent);
            userMesssageTime = itemView.findViewById(R.id.userMessageTime);
            userMessageLayout = itemView.findViewById(R.id.userMessageLayout);
            friendMessageLayout = itemView.findViewById(R.id.friendMessageLayout);
            friendMessageContent = itemView.findViewById(R.id.friendMessageContent);
            friendMessageTime = itemView.findViewById(R.id.friendMessageTime);
            this.messageClick = click;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            messageClick.messageClicked(getAdapterPosition());

        }
    }

    public interface MessageClick {
        void messageClicked(int position);
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MessageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_layout, parent, false), mMessageClick);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {

        if (mChatMessageList.get(position).getFrom() == FROM_USER) {
            holder.friendMessageLayout.setVisibility(View.GONE);
            holder.userMessageContent.setText(mChatMessageList.get(position).getMessage());
            holder.userMesssageTime.setText(mChatMessageList.get(position).getTime());
        }
        if (mChatMessageList.get(position).getFrom()==FROM_FRIEND){
            holder.userMessageLayout.setVisibility(View.GONE);
            holder.friendMessageContent.setText(mChatMessageList.get(position).getMessage());
            holder.friendMessageTime.setText(mChatMessageList.get(position).getTime());
        }

    }

    @Override
    public int getItemCount() {
        return mChatMessageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
}
