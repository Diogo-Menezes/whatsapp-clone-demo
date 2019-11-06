package com.diogomenezes.whatsappclonedemo.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.diogomenezes.whatsappclonedemo.R;
import com.diogomenezes.whatsappclonedemo.models.Message;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.diogomenezes.whatsappclonedemo.ChatActivity.FROM_FRIEND;
import static com.diogomenezes.whatsappclonedemo.ChatActivity.FROM_USER;
import static com.diogomenezes.whatsappclonedemo.models.Message.IMAGE_MESSAGE;
import static com.diogomenezes.whatsappclonedemo.models.Message.TEXT_MESSAGE;
import static com.diogomenezes.whatsappclonedemo.models.Message.VIDEO_MESSAGE;
import static com.diogomenezes.whatsappclonedemo.models.Message.VOICE_MESSAGE;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MessageViewHolder> {

    private static final String TAG = "MessageListAdapter";

    private ArrayList<Message> mChatMessageList;
    private MessageLongClick mMessageClick;
    private Context context;
    private MediaPlayer mediaPlayer;
    SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
    private int pos;
    private View view;
    private boolean isPlaying = false;

    public MessageListAdapter(ArrayList<Message> mChatMessage, MessageLongClick mMessageClick) {
        this.mChatMessageList = mChatMessage;
        this.mMessageClick = mMessageClick;
    }

    class MessageViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {
        //TEXT_MESSAGE
        private TextView userMessageContent, userMesssageTime, friendMessageContent, friendMessageTime;
        private LinearLayout userMessageLayout, friendMessageLayout;

        //VOICE_MESSAGE
        private RelativeLayout userVoiceLayout, friendVoiceLayout;
        private CircleImageView userImage, friendImage;
        private MessageLongClick messageClick;
        private PlayButtonClicked playButtonClicked;
        private ImageButton friendPlayButton, userPlayButton;
        private TextView friendVoiceDuration, userVoiceDuration, userVoiceDate, friendVoiceDate;
        private SeekBar userSeekBar, friendSeekbar;

        public MessageViewHolder(@NonNull final View itemView, MessageLongClick click) {
            super(itemView);
            view = itemView;
            context = itemView.getContext();

            //TEXT_MESSAGE
            userMessageContent = itemView.findViewById(R.id.userMessageContent);
            userMesssageTime = itemView.findViewById(R.id.userMessageTime);
            userMessageLayout = itemView.findViewById(R.id.userMessageLayout);
            friendMessageLayout = itemView.findViewById(R.id.friendMessageLayout);
            friendMessageContent = itemView.findViewById(R.id.friendMessageContent);
            friendMessageTime = itemView.findViewById(R.id.friendMessageTime);

            //VOICE_MESSAGE
            userVoiceLayout = itemView.findViewById(R.id.userVoiceMessageLayout);
            userImage = itemView.findViewById(R.id.userImage);
            userPlayButton = itemView.findViewById(R.id.playUserAudio);
            userVoiceDuration = itemView.findViewById(R.id.userVoiceDuration);
            userVoiceDate = itemView.findViewById(R.id.userVoiceDate);
            userSeekBar = itemView.findViewById(R.id.userVoiceSeekBar);
            friendImage = itemView.findViewById(R.id.friendImage);
            friendVoiceLayout = itemView.findViewById(R.id.friendVoiceMessageLayout);
            friendPlayButton = itemView.findViewById(R.id.playFriendAudio);
            friendVoiceDuration = itemView.findViewById(R.id.friendAudioDuration);
            friendVoiceDate = itemView.findViewById(R.id.friendVoiceDate);
            friendSeekbar = itemView.findViewById(R.id.friendVoiceSeekBar);

            this.messageClick = click;
            userPlayButton.setOnClickListener(this);
            friendPlayButton.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }

        @Override
        public boolean onLongClick(View v) {
            messageClick.messageLongClicked(getAdapterPosition());
            return true;
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == userPlayButton.getId()) {
                Toast.makeText(context, "Play user", Toast.LENGTH_SHORT).show();
                messageClick.messageLongClicked(getAdapterPosition());
            }
            if (v.getId() == friendPlayButton.getId()) {
                Toast.makeText(context, "Play friend", Toast.LENGTH_SHORT).show();
                messageClick.messageLongClicked(getAdapterPosition());
            }
        }
    }

    public interface MessageLongClick {
        void messageLongClicked(int position);
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MessageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_layout, parent, false), mMessageClick);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        switch (mChatMessageList.get(position).getType()) {
            case TEXT_MESSAGE:
                if (mChatMessageList.get(position).getFrom() == FROM_USER) {
                    holder.friendMessageLayout.setVisibility(View.GONE);
                    holder.userMessageContent.setText(mChatMessageList.get(position).getMessage());
                    holder.userMesssageTime.setText(mChatMessageList.get(position).getTime());
                }
                if (mChatMessageList.get(position).getFrom() == FROM_FRIEND) {
                    holder.userMessageLayout.setVisibility(View.GONE);
                    holder.friendMessageContent.setText(mChatMessageList.get(position).getMessage());
                    holder.friendMessageTime.setText(mChatMessageList.get(position).getTime());
                }
                break;
            case VOICE_MESSAGE:
                if (mChatMessageList.get(position).getFrom() == FROM_USER) {
                    holder.friendMessageLayout.setVisibility(View.GONE);
                    holder.userMessageLayout.setVisibility(View.GONE);
                    holder.userVoiceLayout.setVisibility(View.VISIBLE);
                    holder.userVoiceDuration.setText(mChatMessageList.get(position).getVoiceMailDuration());
                    holder.userVoiceDate.setText(mChatMessageList.get(position).getTime());
//                    holder.userImage.setImageBitmap(mChatMessageList.get(position).getBitmap());
                }
                if (mChatMessageList.get(position).getFrom() == FROM_FRIEND) {
                    holder.friendMessageLayout.setVisibility(View.GONE);
                    holder.userMessageLayout.setVisibility(View.GONE);
                    holder.friendVoiceLayout.setVisibility(View.VISIBLE);
                    holder.friendVoiceDuration.setText(mChatMessageList.get(position).getVoiceMailDuration());
                    holder.friendVoiceDuration.setText(mChatMessageList.get(position).getTime());
//                    holder.friendImage.setImageBitmap(mChatMessageList.get(position).getBitmap());
                }

                break;
            case VIDEO_MESSAGE:
                break;
            case IMAGE_MESSAGE:
                break;

        }

    }

    @Override
    public int getItemCount() {
        return mChatMessageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void playVoice(int position, final SeekBar seekBar, TextView voiceDuration) {

    }
}
