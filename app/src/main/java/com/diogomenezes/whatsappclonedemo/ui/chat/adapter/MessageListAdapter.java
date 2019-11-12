package com.diogomenezes.whatsappclonedemo.ui.chat.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.diogomenezes.whatsappclonedemo.R;
import com.diogomenezes.whatsappclonedemo.models.Message;
import com.diogomenezes.whatsappclonedemo.util.VoicePlayer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.diogomenezes.whatsappclonedemo.ui.chat.ChatActivity.FROM_FRIEND;
import static com.diogomenezes.whatsappclonedemo.ui.chat.ChatActivity.FROM_USER;
import static com.diogomenezes.whatsappclonedemo.models.Message.IMAGE_MESSAGE;
import static com.diogomenezes.whatsappclonedemo.models.Message.TEXT_MESSAGE;
import static com.diogomenezes.whatsappclonedemo.models.Message.VIDEO_MESSAGE;
import static com.diogomenezes.whatsappclonedemo.models.Message.VOICE_MESSAGE;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MessageViewHolder> {

    private static final String TAG = "MessageListAdapter";

    private ArrayList<Message> mChatMessageList;
    private String userImageUri, friendImageUri;
    private MessageClick mMessageClick;
    private MessageLongClick mMessageLongClick;
    private Context context;
    private MediaPlayer mediaPlayer;
    SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
    private int pos;
    private View view;
    private boolean isPlaying = false;

    public MessageListAdapter(ArrayList<Message> mChatMessage, MessageClick mMessageClick, MessageLongClick mMessageLongClick, String userImageUri, String friendImageUri) {
        this.mChatMessageList = mChatMessage;
        this.mMessageClick = mMessageClick;
        this.mMessageLongClick = mMessageLongClick;
        this.userImageUri = userImageUri;
        this.friendImageUri = friendImageUri;
    }

    class MessageViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener, SeekBar.OnSeekBarChangeListener {
        //TEXT_MESSAGE
        private TextView userMessageContent, userMesssageTime, friendMessageContent, friendMessageTime;
        private LinearLayout userMessageLayout, friendMessageLayout;

        //VOICE_MESSAGE
        private RelativeLayout userVoiceLayout, friendVoiceLayout;
        private CircleImageView userImage, friendImage;
        private MessageClick messageClick;
        private MessageLongClick messageLongClick;

        private ImageView friendPlayButton, userPlayButton;
        private TextView friendVoiceDuration, userVoiceDuration, userVoiceDate, friendVoiceDate;
        private SeekBar userSeekBar, friendSeekbar;
        private VoicePlayer voicePlayer;


        public MessageViewHolder(@NonNull final View itemView, MessageClick click, MessageLongClick longClick) {
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
            this.messageLongClick = longClick;
            userPlayButton.setOnClickListener(this);
            friendPlayButton.setOnClickListener(this);
            userSeekBar.setOnSeekBarChangeListener(this);
            friendSeekbar.setOnSeekBarChangeListener(this);
            itemView.setOnLongClickListener(this);

        }

        @Override
        public boolean onLongClick(View v) {
            messageLongClick.messageLongClicked(getAdapterPosition());
            return true;
        }

        @Override
        public void onClick(View v) {
            Log.i(TAG, "onClick: adapter called " + v.toString());
            if (v.getId() == userPlayButton.getId() || v.getId() == friendPlayButton.getId()) {
                messageClick.messageClicked(getAdapterPosition());
            }
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            TextView textView;
            if (mChatMessageList.get(getAdapterPosition()).getFrom() == FROM_FRIEND) {
                textView = itemView.findViewById(R.id.friendAudioDuration);
            } else {
                textView = itemView.findViewById(R.id.userVoiceDuration);
            }
            voicePlayer = new VoicePlayer(context, mChatMessageList, seekBar, view, textView);
            voicePlayer.createPlayerFromSeekbar(getLayoutPosition());

        }
    }

    public interface MessageLongClick {
        void messageLongClicked(int position);
    }

    public interface MessageClick {
        void messageClicked(int position);
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MessageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_layout, parent, false), mMessageClick, mMessageLongClick);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        switch (mChatMessageList.get(position).getType()) {
            case TEXT_MESSAGE:
                if (mChatMessageList.get(position).getFrom() == FROM_USER) {
                    holder.friendMessageLayout.setVisibility(View.GONE);
                    holder.userMessageContent.setText(mChatMessageList.get(position).getMessage());
                    holder.userMesssageTime.setText(mChatMessageList.get(position).getTime());
                } else {
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
//                    holder.userSeekBar.setTag(position);
                    Glide.with(holder.itemView).load(userImageUri).into(holder.userImage);
                } else {
                    holder.friendMessageLayout.setVisibility(View.GONE);
                    holder.userMessageLayout.setVisibility(View.GONE);
                    holder.friendVoiceLayout.setVisibility(View.VISIBLE);
                    holder.friendVoiceDuration.setText(mChatMessageList.get(position).getVoiceMailDuration());
                    holder.friendVoiceDate.setText(mChatMessageList.get(position).getTime());
//                    holder.friendSeekbar.setTag(mChatMessageList.get(position).getDate());// TODO: 11/11/2019 change..should be unique... ex.: userID + voiceDuration
                    Glide.with(context).load(friendImageUri).into(holder.friendImage);
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

}
