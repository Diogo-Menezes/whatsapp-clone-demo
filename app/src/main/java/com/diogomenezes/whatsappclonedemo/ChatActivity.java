package com.diogomenezes.whatsappclonedemo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import static com.diogomenezes.whatsappclonedemo.parse_Activities.ParseChatActivity.FRIEND_NAME;

public class ChatActivity extends AppCompatActivity implements MessageListAdapter.MessageClick, View.OnClickListener, View.OnTouchListener {
    private static final String TAG = "NewChatActivity";
    public static final int FROM_FRIEND = 0;
    public static final int FROM_USER = 1;
    private static final int VOICE_RECORD = 0;

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
    private DateFormat dateFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
    private int position;
    private boolean userScrolled = false;
    private int layoutManagerLastPosition;
    long timePressed;

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
        messageEdit.setOnEditorActionListener(editorActionListener);
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
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                Log.i(TAG, "onScrollStateChanged: " +newState);
                if (newState == 0) {
                    layoutManagerLastPosition = layoutManager.findLastVisibleItemPosition();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.i(TAG, "onScrolled: " + layoutManagerLastPosition + " " + layoutManager.findLastVisibleItemPosition());
            }
        });


        sendMessageLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                Log.i(TAG, "onLayoutChange: called");

                if (messageEdit.hasFocus() && !userScrolled) {
                    mRecyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
                }
            }
        });
    }

    private TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            Log.i(TAG, "onEditorAction: " + actionId);
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                Log.i(TAG, "onEditorAction: called");
                return true;
            }
            return false;
        }
    };

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

    public void sendMessage(View view) {
        position = 0;
        Log.i(TAG, "sendMessage: List size start: " + messageAdapter.getItemCount());
        if (!messageEdit.getText().toString().isEmpty()) {
            String message = messageEdit.getText().toString();
            String time = dateFormat.format(System.currentTimeMillis());
            mChatMessageList.add(new Message(message, time, FROM_USER));
            messageEdit.setText("");
            position = mChatMessageList.size();
            messageAdapter.notifyItemInserted(position);
            Log.i(TAG, "sendMessage: item count: " + messageAdapter.getItemCount());
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
            mChatMessage = new Message(messages[i], messagesTime[i], from[i]);
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
    public void messageClicked(int position) {
        Toast.makeText(this, mChatMessageList.get(position).getMessage(), Toast.LENGTH_SHORT).show();
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.floatingActionButton && !messageEdit.getText().toString().isEmpty()) {
            sendMessage(v);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("mm:ss", Locale.getDefault());
        if (v.getId() == R.id.floatingActionButton && messageEdit.getText().toString().isEmpty()) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                timePressed = System.currentTimeMillis();
                //Start Record
                recordAudio();
                sendButton.animate().scaleX(1.6f).scaleY(1.6f).start();
            }

            if (event.getAction() == MotionEvent.ACTION_UP) {
                //Stop Record
                timePressed = System.currentTimeMillis() - timePressed;
                Log.i(TAG, "onTouch: Record time " + format.format(timePressed));
                sendButton.animate().scaleX(1f).scaleY(1f).start();
            }
            return true;
        }


        return false;
    }

    public void recordAudio() {
        if (checkPermissions(Manifest.permission.RECORD_AUDIO)){
            //START RECORD
            Toast.makeText(this, "Start Record", Toast.LENGTH_SHORT).show();
        }
    }


    public boolean checkPermissions(String permission) {
        if (ContextCompat.checkSelfPermission(ChatActivity.this, permission) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(ChatActivity.this, new String[]{permission}, VOICE_RECORD);
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case VOICE_RECORD:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Press and hold the voice button.", Toast.LENGTH_SHORT).show();
                }
        }
    }
}
