package com.diogomenezes.whatsappclonedemo.util;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.diogomenezes.whatsappclonedemo.R;
import com.diogomenezes.whatsappclonedemo.models.Message;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class VoicePlayer implements MediaPlayer.OnCompletionListener, SeekBar.OnSeekBarChangeListener {
    private static final String TAG = "VoicePlayer";


    private ArrayList<Message> messageArrayList;
    private LinearLayoutManager layoutManager;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private SeekBar seekBar;
    private TextView textView;
    private int oldPosition;
    private boolean isCreated = false;
    private Context context;
    private SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
    private boolean isPaused = false;
    private Activity activity;
    private ImageView playButton;
    private Timer timer;
    private int pausedPosition;

    public VoicePlayer(Activity activity, ArrayList<Message> messageArrayList, LinearLayoutManager layoutManager) {
        this.messageArrayList = messageArrayList;
        this.layoutManager = layoutManager;
        this.activity = activity;
    }

    public VoicePlayer(Context context, ArrayList<Message> messageArrayList, SeekBar seekBar) {
        this.seekBar = seekBar;
        this.messageArrayList = messageArrayList;
        this.context = context;
    }


    public void play(int position) {
        Log.i(TAG, "play called: " + position);

        if (isPaused && position == oldPosition) {
            Log.i(TAG, "is paused called");
            mediaPlayer.start();
            isPaused = false;
            playButton.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_pause));
            createTimer();
        } else {
            if (!isCreated) {
                Log.i(TAG, "iscreated called false");
                createPlayer(position);
                createTimer();
            } else {
                if (position == oldPosition) {
                    Log.i(TAG, "iscreated called sameposition");
                    pausedPosition = mediaPlayer.getCurrentPosition();
                    mediaPlayer.seekTo(pausedPosition);
                    mediaPlayer.pause();
                    seekBar.setProgress(pausedPosition);
                    isPaused = true;
                    timer.cancel();
                    playButton.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_play));
                }
                if (position != oldPosition) {
                    Log.i(TAG, "iscreated called different position");
                    if (mediaPlayer.isPlaying()) {
                        seekBar.setProgress(0);
                        timer.cancel();
                        playButton.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_play));
                        mediaPlayer.seekTo(0);
                        mediaPlayer.pause();
                    }
                    createPlayer(position);
                }
            }
        }

    }

    private void createPlayer(int filePosition) {
        if (context != null) {
            activity = (Activity) context;
        }
        Log.i(TAG, "createPlayer: called " + filePosition);
        File file = new File(messageArrayList.get(filePosition).getVoiceMailUri());

        mediaPlayer = MediaPlayer.create(activity, Uri.fromFile(file));
        mediaPlayer.start();
        isPaused = false;
        isCreated = true;
        oldPosition = filePosition;

        seekBar = layoutManager.findViewByPosition(filePosition).findViewById(R.id.userVoiceSeekBar);
        textView = layoutManager.findViewByPosition(filePosition).findViewById(R.id.userVoiceDuration);
        playButton = layoutManager.findViewByPosition(filePosition).findViewById(R.id.playUserAudio);
        seekBar.setTag(filePosition);
        seekBar.setMax(mediaPlayer.getDuration());
        playButton.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_pause));
        createTimer();
        mediaPlayer.setOnCompletionListener(this);


        if (mediaPlayer != null) {
            seekBar.setOnSeekBarChangeListener(this);
        }
    }

    private void createTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        seekBar.setProgress(mediaPlayer.getCurrentPosition());
                        if (mediaPlayer == null) {
                            timer.cancel();
                        } else {
                            textView.setText(sdf.format(mediaPlayer.getCurrentPosition()));
                        }

                    }
                });

            }
        }, 200, 250);
    }




    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.i(TAG, "onCompletion: called");
        timer.cancel();
        playButton.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_play));
        if (seekBar != null) {
            seekBar.setProgress(0);
        }
        mediaPlayer.release();
        mediaPlayer = null;
        seekBar = null;
        isPaused = false;
        isCreated = false;

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        Log.i(TAG, "onProgressChanged: " + seekBar.getTag());
        if (fromUser) {
            if (mediaPlayer != null) {
                mediaPlayer.seekTo(progress);
                textView.setText(sdf.format(mediaPlayer.getCurrentPosition()));
            }

        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }
}

