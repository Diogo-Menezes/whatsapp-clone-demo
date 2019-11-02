package com.diogomenezes.whatsappclonedemo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diogomenezes.whatsappclonedemo.R;
import com.diogomenezes.whatsappclonedemo.models.ChatMessage;

import java.util.ArrayList;

import static com.diogomenezes.whatsappclonedemo.NewChatActivity.FROM_FRIEND;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {


    private ArrayList<ChatMessage> mChatMessageList;
    private MessageClick mMessageClick;
    private int friendBackgroundColor;
    private int userBackgroundColor;

    public MessageAdapter(ArrayList<ChatMessage> mChatMessage, MessageClick mMessageClick) {
        this.mChatMessageList = mChatMessage;
        this.mMessageClick = mMessageClick;
    }

    class MessageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView messageContent, messsageTime;
        private LinearLayout messageBackground;
        private MessageClick messageClick;

        public MessageViewHolder(@NonNull View itemView, MessageClick click) {
            super(itemView);

            messageContent = itemView.findViewById(R.id.messageContent);
            messsageTime = itemView.findViewById(R.id.messageTime);
            messageBackground = itemView.findViewById(R.id.messageBackground);
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
        userBackgroundColor = parent.getResources().getColor(R.color.green);
        friendBackgroundColor = parent.getResources().getColor(R.color.messageGrey);
        return new MessageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_layout, parent, false), mMessageClick);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {

        holder.messageContent.setText(mChatMessageList.get(position).getMessage());
        holder.messsageTime.setText(mChatMessageList.get(position).getTime());
        if (mChatMessageList.get(position).getFrom() == FROM_FRIEND) {
            //COLOR GREY AND ALIGNED TO THE START
            holder.messageBackground.setBackgroundColor(friendBackgroundColor);
        } else {
            //COLOR GREEN AND ALIGNED TO THE END
            holder.messageBackground.setBackgroundColor(userBackgroundColor);
        }
    }

    @Override
    public int getItemCount() {
        return mChatMessageList.size();
    }
}
