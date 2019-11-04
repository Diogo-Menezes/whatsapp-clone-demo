package com.diogomenezes.whatsappclonedemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diogomenezes.whatsappclonedemo.adapter.MessageListAdapter;
import com.diogomenezes.whatsappclonedemo.models.Message;

import java.text.DateFormat;
import java.util.ArrayList;

import static com.diogomenezes.whatsappclonedemo.parse_Activities.ParseChatActivity.FRIEND_NAME;

public class ChatActivity extends AppCompatActivity implements MessageListAdapter.MessageClick, View.OnClickListener {
    private static final String TAG = "NewChatActivity";
    public static final int FROM_FRIEND = 0;
    public static final int FROM_USER = 1;

    //UI
    private RecyclerView mRecyclerView;
    private MessageListAdapter messageAdapter;
    private EditText messageEdit;
    private Button button;
    private ImageView camImage;

    //VARS
    private ArrayList<Message> mChatMessageList = new ArrayList<>();
    private Message mChatMessage;
    private DateFormat dateFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_chat);

        camImage = findViewById(R.id.camImage);
        messageEdit = findViewById(R.id.messageEdit);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView = findViewById(R.id.chatRecView);
        mRecyclerView.setLayoutManager(layoutManager);
        messageAdapter = new MessageListAdapter(mChatMessageList, this);
        mRecyclerView.setAdapter(messageAdapter);
        mRecyclerView.setHasFixedSize(true);
        activateTextWatcher();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        if (intent != null) {
            setTitle(intent.getStringExtra(FRIEND_NAME));
        }

        fakeMessages();
    }

    private void activateTextWatcher() {
        messageEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.i(TAG, "beforeTextChanged: called");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i(TAG, "onTextChanged: called");
                if (count > 0) {
                    camImage.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.i(TAG, "afterTextChanged: called");
                if (s.equals("")) {
                    camImage.setVisibility(View.VISIBLE);
                }
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


    @Override
    public void onClick(View v) {
    }
}
