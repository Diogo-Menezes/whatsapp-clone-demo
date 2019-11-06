package com.diogomenezes.whatsappclonedemo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
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
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diogomenezes.whatsappclonedemo.adapter.MessageListAdapter;
import com.diogomenezes.whatsappclonedemo.models.Message;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static com.diogomenezes.whatsappclonedemo.models.Message.IMAGE_MESSAGE;
import static com.diogomenezes.whatsappclonedemo.models.Message.TEXT_MESSAGE;
import static com.diogomenezes.whatsappclonedemo.models.Message.VIDEO_MESSAGE;
import static com.diogomenezes.whatsappclonedemo.models.Message.VOICE_MESSAGE;
import static com.diogomenezes.whatsappclonedemo.parse_Activities.ParseChatActivity.FRIEND_NAME;

public class ChatActivity extends AppCompatActivity implements MessageListAdapter.MessageLongClick, View.OnClickListener, View.OnTouchListener {
    private static final String TAG = "NewChatActivity";
    public static final int FROM_FRIEND = 0;
    public static final int FROM_USER = 1;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 123;

    //UI
    private RecyclerView mRecyclerView;
    private MessageListAdapter messageAdapter;
    private EditText messageEdit;
    private FloatingActionButton sendButton;
    private ImageView camImage, attachImage;
    private LinearLayout sendMessageLayout;
    private LinearLayoutManager layoutManager;

    //VARS
    private ArrayList<Message> mChatMessageList = new ArrayList<>();
    private Message mChatMessage;
    private DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
    private SimpleDateFormat voiceFormat = new SimpleDateFormat("mm:ss");
    private int position;
    private boolean userScrolled = false;
    private int layoutManagerLastPosition;
    private long timePressed, timeStopped;
    private MediaRecorder recorder;
    private String fileName, voiceDuration;
    private Message mMessage;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;
    private SeekBar seekBar;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_chat);

        sendMessageLayout = findViewById(R.id.sendMessageLayout);
        sendButton = findViewById(R.id.floatingActionButton);
        attachImage = findViewById(R.id.attachImage);
        camImage = findViewById(R.id.camImage);
        messageEdit = findViewById(R.id.messageEdit);
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView = findViewById(R.id.chatRecView);
        mRecyclerView.setLayoutManager(layoutManager);
        messageAdapter = new MessageListAdapter(mChatMessageList, this);
        mRecyclerView.setAdapter(messageAdapter);
        mRecyclerView.setHasFixedSize(true);
        layoutManagerLastPosition = mChatMessageList.size() - 1;
        activateTextWatcher();
        sendButton.setOnClickListener(this);
        sendButton.setOnTouchListener(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        if (intent != null) {
            setTitle(intent.getStringExtra(FRIEND_NAME));
        }

        fakeMessages();
        layoutManagerLastPosition = mChatMessageList.size() - 1;


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


        switch (type) {
            case TEXT_MESSAGE:
                if (!messageEdit.getText().toString().isEmpty()) {
                    mMessage = new Message(TEXT_MESSAGE, messageEdit.getText().toString(), time, FROM_USER);
                    messageEdit.setText("");
                }
                break;
            case VOICE_MESSAGE:
                mMessage = new Message(VOICE_MESSAGE, time, fileName, voiceDuration, FROM_USER);
                break;
            case VIDEO_MESSAGE:
                //check for text
                mMessage = new Message(VIDEO_MESSAGE, fileName, time, FROM_USER);
                break;
            case IMAGE_MESSAGE:
                //check for text
                mMessage = new Message(IMAGE_MESSAGE, fileName, time, FROM_USER);
                break;
        }

        if (mMessage != null) {
            mChatMessageList.add(mMessage);
            position = mChatMessageList.size();
            messageAdapter.notifyItemInserted(position);

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
            mChatMessage = new Message(TEXT_MESSAGE, messages[i], messagesTime[i], from[i]);
            mChatMessageList.add(mChatMessage);

        }
        messageAdapter.notifyDataSetChanged();
        mRecyclerView.smoothScrollToPosition(mChatMessageList.size());
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
//            case R.id.chat_menu_action_more:
//
//                break;

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
    public void messageLongClicked(int position) {
        if (mChatMessageList.get(position).getType() == VOICE_MESSAGE) {
            playVoice(position);
        }
    }

    private void playVoice(int position) {

        if (mediaPlayer != null && !isPlaying) {
            mediaPlayer = null;
            seekBar = null;
        }
        File file = new File(mChatMessageList.get(position).getVoiceMailUri());
        mediaPlayer = MediaPlayer.create(this, Uri.fromFile(file));
        mediaPlayer.start();

        seekBar = layoutManager.findViewByPosition(position).findViewById(R.id.userVoiceSeekBar);
        seekBar.setTag(position);

        Log.i(TAG, "playVoice: seekbar tag" + seekBar.getTag().toString());
        final View root = layoutManager.findViewByPosition(position).getRootView();

        final TextView textView1 = root.findViewById(R.id.userVoiceDuration);
        seekBar.setMax(mediaPlayer.getDuration());

        //TIMER FOR CONTROL SEEKBAR
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (mediaPlayer!=null){
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    textView1.setText(String.valueOf(mediaPlayer.getCurrentPosition()));
                }
            }
        }, 0, 1000);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mediaPlayer.seekTo(progress);
                textView1.setText(String.valueOf(mediaPlayer.getCurrentPosition()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mediaPlayer.pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.start();
            }
        });
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.floatingActionButton && !messageEdit.getText().toString().isEmpty()) {
            sendMessage(TEXT_MESSAGE);
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("mm:ss", Locale.getDefault());

        boolean downTouch = false;
        if (v.getId() == R.id.floatingActionButton && messageEdit.getText().toString().isEmpty()) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    //Start Record
                    timePressed = System.currentTimeMillis();
                    recordAudio();
                    sendButton.animate().scaleX(1.6f).scaleY(1.6f).setDuration(100).start();
//                    downTouch = true;
                    return true;

                case MotionEvent.ACTION_UP:
//                    if (!downTouch) {
//                        v.performClick();
//                    } else {
                    //Stop Record
                    sendButton.animate().scaleX(1f).scaleY(1f).setDuration(100).start();
                    timeStopped = System.currentTimeMillis();
                    long duration = timeStopped - timePressed;
                    voiceDuration = voiceFormat.format(duration);
                    if (recorder != null) {
                        stopAudioRecord();
                        sendMessage(VOICE_MESSAGE);
                    }
//                    }
                    return true;
                default:
                    break;
            }
        }
        return false;
    }

    private void stopAudioRecord() {
        recorder.stop();
        recorder.release();
        recorder = null;
        System.out.println("PATH: " + fileName.toString());

    }

    public void recordAudio() {
        // Record to the external cache directory for visibility
        SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMddss");
        fileName = getExternalCacheDir().getAbsolutePath();
        fileName += "/PTT-" + sdf.format(System.currentTimeMillis()) + ".3gp";

        if (checkPermissions(Manifest.permission.RECORD_AUDIO)) {
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setOutputFile(fileName);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            try {
                recorder.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            recorder.start();
        }
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
}
