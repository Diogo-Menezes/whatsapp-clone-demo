package com.diogomenezes.whatsappclonedemo.ui.chat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.diogomenezes.whatsappclonedemo.R;
import com.diogomenezes.whatsappclonedemo.ui.chat.adapter.MessageListAdapter;
import com.diogomenezes.whatsappclonedemo.database.MessageRepository;
import com.diogomenezes.whatsappclonedemo.models.Message;
import com.diogomenezes.whatsappclonedemo.util.VoicePlayer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static com.diogomenezes.whatsappclonedemo.models.Message.IMAGE_MESSAGE;
import static com.diogomenezes.whatsappclonedemo.models.Message.TEXT_MESSAGE;
import static com.diogomenezes.whatsappclonedemo.models.Message.VIDEO_MESSAGE;
import static com.diogomenezes.whatsappclonedemo.models.Message.VOICE_MESSAGE;
import static com.diogomenezes.whatsappclonedemo.ui.contactList.MainAct.FRIEND_ID;
import static com.diogomenezes.whatsappclonedemo.ui.contactList.MainAct.FRIEND_IMAGE;
import static com.diogomenezes.whatsappclonedemo.ui.contactList.MainAct.FRIEND_NAME;

public class ChatActivity extends AppCompatActivity implements MessageListAdapter.MessageClick, MessageListAdapter.MessageLongClick, View.OnClickListener, View.OnTouchListener {
    private static final String TAG = "NewChatActivity";
    public static final int FROM_FRIEND = 0;
    public static final int FROM_USER = 100;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 123;
    private static final long MINIMUM_VOICE_CLIP_DURATION = 900;    //900 Milliseconds

    //UI
    private RecyclerView mRecyclerView;
    private MessageListAdapter messageAdapter;
    private EditText messageEdit;
    private FloatingActionButton sendButton;
    private ImageView camImage, attachImage;
    private LinearLayout sendMessageLayout;
    private LinearLayoutManager layoutManager;
    private TextView voiceTextTime;
    private ImageView emojiImage, voiceMicImage;
    private TextView voiceDurationText;
    private VoicePlayer voicePlayer = null;


    //VARS
    private ArrayList<Message> mChatMessageList = new ArrayList<>();
    private Message mChatMessage;
    private DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
    private SimpleDateFormat voiceFormat = new SimpleDateFormat("mm:ss");
    private int position;
    private boolean userScrolled = false;
    private int layoutManagerLastPosition;
    private long timePressed, timeStopped, duration, timeCounter;
    private MediaRecorder recorder;
    private String voiceFileUri, voiceDuration;
    private Message mMessage;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;
    private SeekBar seekBar;
    private Timer timer;
    private int friendID;
    private String friendImageUri;
    private MessageRepository repository;

    // TODO: 11/11/2019 change to real userID and Imagelocation
    private int userID = 100;
    private String userImageUri = "https://images.unsplash.com/photo-1453396450673-3fe83d2db2c4?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=334&q=80";

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_chat);

        //UI
        voiceTextTime = findViewById(R.id.voiceTextTime);
        voiceMicImage = findViewById(R.id.voiceMic);
        emojiImage = findViewById(R.id.emojiImage);
        sendMessageLayout = findViewById(R.id.sendMessageLayout);
        messageEdit = findViewById(R.id.messageEdit);
        sendButton = findViewById(R.id.floatingActionButton);
        attachImage = findViewById(R.id.attachImage);
        camImage = findViewById(R.id.camImage);
        layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);


        Toolbar toolbar = findViewById(R.id.toolbar_chat_title);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();

        if (toolbar != null) {
            TextView textView = findViewById(R.id.toolbar_chat_name);
            ImageView imageView = findViewById(R.id.toolbar_chat_contactImage);
            if (intent != null) {
                Log.d(TAG, "onCreate: " + intent.getStringExtra(FRIEND_IMAGE));
                friendID = intent.getIntExtra(FRIEND_ID, -1);
                textView.setText(intent.getStringExtra(FRIEND_NAME));
                friendImageUri = intent.getStringExtra(FRIEND_IMAGE);
                Glide.with(this).load(friendImageUri).into(imageView);

            }
        }
        mRecyclerView = findViewById(R.id.chatRecView);
        mRecyclerView.setLayoutManager(layoutManager);
        messageAdapter = new MessageListAdapter(mChatMessageList, this, this, userImageUri, friendImageUri);
        mRecyclerView.setAdapter(messageAdapter);
        mRecyclerView.setHasFixedSize(true);
        activateTextWatcher();
        sendButton.setOnClickListener(this);
        sendButton.setOnTouchListener(this);

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                getMessages();
//                fakeMessages();
            }
        };
        handler.postDelayed(runnable, 0);
        activateListeners();
    }

    private void getMessages() {
        mChatMessageList.clear();
        repository = new MessageRepository(getApplication());
        mChatMessageList.addAll(repository.getAll(FROM_USER, friendID));
        messageAdapter.notifyDataSetChanged();
        mRecyclerView.smoothScrollToPosition(mChatMessageList.size());
    }


    private void activateListeners() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //state 1 = userScroll
                if (recyclerView.getScrollState() == 1) {
                    layoutManagerLastPosition = layoutManager.findLastVisibleItemPosition();

                    if (layoutManagerLastPosition == mChatMessageList.size() - 1) {
                        userScrolled = false;
                    } else {
                        userScrolled = true;
                    }
                }
            }
        });


        sendMessageLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (messageEdit.hasFocus() && !userScrolled) {
                    mRecyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
                }
            }
        });
    }


    private void activateTextWatcher() {

        messageEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()) {
                    camImage.animate().translationX(0f).alpha(1).setDuration(100);
                    attachImage.animate().translationX(0f).setDuration(100);
                    sendButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_mic));

                } else {
                    camImage.animate().translationX(200f).alpha(0).setDuration(100);
                    attachImage.animate().translationX(120f).setDuration(100);
                    sendButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_send));

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void sendMessage(int type) {
        position = 0;
        String time = timeFormat.format(System.currentTimeMillis());
        repository = new MessageRepository(getApplication());

        switch (type) {
            case TEXT_MESSAGE:
                if (!messageEdit.getText().toString().isEmpty()) {
                    mMessage = new Message();
                    mMessage.setType(TEXT_MESSAGE);
                    mMessage.setMessage(messageEdit.getText().toString());
                    messageEdit.setText("");
                }
                break;
            case VOICE_MESSAGE:
                mMessage = new Message();
                mMessage.setType(VOICE_MESSAGE);
                mMessage.setVoiceMailUri(voiceFileUri);
                mMessage.setVoiceMailDuration(voiceDuration);
                break;
            case VIDEO_MESSAGE:
                //check for text
//                mMessage = new Message(VIDEO_MESSAGE, voiceFileUri, time, FROM_USER);
                break;
            case IMAGE_MESSAGE:
                //check for text
//                mMessage = new Message(IMAGE_MESSAGE, voiceFileUri, time, FROM_USER);
                break;
        }

        if (mMessage != null) {
            mMessage.setTime(time);
            mMessage.setDate(System.currentTimeMillis());
            mMessage.setFrom(FROM_USER);
            mMessage.setTo(friendID);
            repository.insert(mMessage);
            mChatMessageList.add(mMessage);
            position = mChatMessageList.size();
            messageAdapter.notifyItemInserted(position);
            mMessage = null;
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mRecyclerView.smoothScrollToPosition(position);
                }
            }, 100);
            layoutManagerLastPosition = mChatMessageList.size() - 1;
        }
    }

    public void closeKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(messageEdit.getWindowToken(), 0);
    }

    private void fakeMessages() {

        String[] messages = {"Inceptos volutpat nonummy. Condimentum tempus ac tortor accumsan non aenean.",
                "Volutpat sit duis. Varius. Posuere urna taciti convallis senectus praesent.",
                "Arcu a odio magna Gravida porttitor ullamcorper. Enim, at netus.",
                "Ultricies vivamus pellentesque at vivamus fermentum Non conubia eleifend orci.",
                "Inceptos torquent a quam auctor, ornare sem aliquam in sociosqu.",
                "Inceptos volutpat nonummy. ",
                "Volutpat sit duis. Varius. ",
                "Arcu a odio magna Gravida.",
                "Ultricies vivamus pellentesque at vivamus.",
                "Inceptos torquent a quam auctor, ornare sem aliquam in sociosqu."};
        String[] messagesTime = {"00:10", "00:11", "00:12", "00:13", "00:15", "00:16", "00:18", "00:18", "00:19", "00:19"};
        Integer[] from = {0, 1, 0, 1, 0, 1, 0, 1, 0, 0};


        for (int i = 0; i < messages.length; i++) {
//            mChatMessage = new Message(TEXT_MESSAGE, messages[i], messagesTime[i], from[i]);
            mChatMessageList.add(mChatMessage);

        }
        messageAdapter.notifyDataSetChanged();
        mRecyclerView.scrollToPosition(mChatMessageList.size());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chat_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.chat_menu_action_video_call:

                break;
            case R.id.chat_menu_action_call:

                break;
            case R.id.chat_menu_action_watch_contact:

                break;
            case R.id.chat_menu_action_files:

                break;
            case R.id.chat_menu_action_search:

                break;
            case R.id.chat_menu_action_notifications:

                break;
            case R.id.chat_menu_action_background:
                break;
            case R.id.chat_menu_action_clean_chat:
                repository = new MessageRepository(getApplication());
                repository.deleteAll();
                mChatMessageList.clear();
                messageAdapter.notifyDataSetChanged();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void messageClicked(int position) {
        if (mChatMessageList.get(position).getType() == VOICE_MESSAGE) {
            if (voicePlayer == null) {

                voicePlayer = new VoicePlayer(this, mChatMessageList, layoutManager);
            }
            voicePlayer.play(position);


        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.floatingActionButton && !messageEdit.getText().toString().isEmpty()) {
            sendMessage(TEXT_MESSAGE);
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (v.getId() == R.id.floatingActionButton && messageEdit.getText().toString().isEmpty()) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    //Start Record
                    timePressed = System.currentTimeMillis();
                    timeStopped = System.currentTimeMillis();
                    duration = 0;
                    emojiImage.setVisibility(View.GONE);
                    messageEdit.setVisibility(View.GONE);
                    voiceMicImage.setVisibility(View.VISIBLE);
                    voiceTextTime.setVisibility(View.VISIBLE);
                    camImage.setVisibility(View.GONE);
                    attachImage.setVisibility(View.GONE);
                    sendButton.animate().scaleX(1.6f).scaleY(1.6f).setDuration(100).start();
                    recordAudio();
                    if (voicePlayer != null) {
                        voicePlayer.stop();
                        voicePlayer = null;
                    }
//                    startTimer();
                    return true;

                case MotionEvent.ACTION_UP:
                    //Stop Record
                    sendButton.animate().scaleX(1f).scaleY(1f).setDuration(100).start();
                    emojiImage.setVisibility(View.VISIBLE);
                    messageEdit.setVisibility(View.VISIBLE);
                    camImage.setVisibility(View.VISIBLE);
                    attachImage.setVisibility(View.VISIBLE);
                    voiceMicImage.setVisibility(View.GONE);
                    voiceTextTime.setVisibility(View.GONE);

                    timeStopped = System.currentTimeMillis();
                    duration = timeStopped - timePressed;
                    voiceDuration = voiceFormat.format(duration);

                    if (duration > MINIMUM_VOICE_CLIP_DURATION) {
                        Log.i(TAG, "onTouch: duration" + voiceFormat.format(duration));
                        stopAudioRecord();
                        sendMessage(VOICE_MESSAGE);
                    } else {
                        Log.i(TAG, "delete called: " + duration);
                        File file = new File(voiceFileUri);
                        file.delete();
                    }
                    if (timer != null) {
                        timer.cancel();
                        timer = null;
                    }
                    return true;
                default:
                    break;
            }
        }
        return false;
    }

    private void stopAudioRecord() {
        if (recorder != null) {
            try {
                recorder.stop();
                recorder.release();
                recorder = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void recordAudio() {
        // Record to the external cache directory for visibility
        final SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMddss");
        voiceFileUri = getExternalCacheDir().getAbsolutePath();
        voiceFileUri += "/PTT-" + sdf.format(System.currentTimeMillis()) + ".3gp";

        if (checkPermissions(Manifest.permission.RECORD_AUDIO)) {
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setOutputFile(voiceFileUri);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            try {
                recorder.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            MediaPlayer mp = MediaPlayer.create(this, R.raw.bow);
            mp.start();
            startTimer();
            timePressed = System.currentTimeMillis();
            recorder.start();
        }
    }

    private void startTimer() {

        voiceTextTime.setText("00:00");
        timeCounter = 0;
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Log.i(TAG, "timer is active");
                if (voiceMicImage.getAlpha() == 0f) {
                    voiceMicImage.setAlpha(1f);
                } else {
                    voiceMicImage.setAlpha(0f);
                }
                voiceTextTime.setText(voiceFormat.format(timeCounter));
                timeCounter += 500;
            }
        }, 500, 500);

    }


    public boolean checkPermissions(String permission) {
        if (ContextCompat.checkSelfPermission(ChatActivity.this, permission) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(ChatActivity.this, new String[]{permission}, REQUEST_RECORD_AUDIO_PERMISSION);
            return false;
        }
    }

    public void playUserAudio(View view) {
//        Toast.makeText(this, "Play Audio", Toast.LENGTH_SHORT).show();
    }

    public void playFriendAudio(View view) {
//        Toast.makeText(this, "Play Audio", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_RECORD_AUDIO_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Press and hold the voice button.", Toast.LENGTH_SHORT).show();
                    break;
                }
                //ADD OTHER PERMISSIONS HERE
            default:
                break;

        }
    }

    public void backPressClick(View view) {
        super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (voicePlayer != null) {
            voicePlayer.stop();
            voicePlayer = null;
        }
        super.onBackPressed();
    }

    @Override
    public void messageLongClicked(int position) {
        Toast.makeText(this, "Message Long Click", Toast.LENGTH_SHORT).show();
        //inflate context menu to delete message & other options
        mMessage = mChatMessageList.get(position);
        repository = new MessageRepository(getApplication());
        repository.delete(mMessage);
        mChatMessageList.remove(position);
        messageAdapter.notifyItemRemoved(position);
    }
}
