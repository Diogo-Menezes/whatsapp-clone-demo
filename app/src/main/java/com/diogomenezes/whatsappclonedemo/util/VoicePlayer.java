package com.diogomenezes.whatsappclonedemo.util;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.View;
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

import static com.diogomenezes.whatsappclonedemo.ui.contactList.MainActivity.USER_ID;

public class VoicePlayer implements MediaPlayer.OnCompletionListener, SeekBar.OnSeekBarChangeListener {
    private static final String TAG = "VoicePlayer";

    private static final int IS_NULL = -1;
    private static final int IS_CREATED = 1;
    private static final int IS_PLAYING = 3;
    private static final int IS_PAUSED = 2;


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
    private View view;
    private ImageView playButton;
    private Timer timer;
    private int pausedPosition;
    private int mediaPlayerState = IS_NULL;

    public VoicePlayer(Activity activity, ArrayList<Message> messageArrayList, LinearLayoutManager layoutManager) {
        this.messageArrayList = messageArrayList;
        this.layoutManager = layoutManager;
        this.activity = activity;
    }

    public VoicePlayer(Context context, ArrayList<Message> messageArrayList, SeekBar seekBar, View view, TextView textView) {
        this.seekBar = seekBar;
        this.messageArrayList = messageArrayList;
        this.context = context;
        this.view = view;
        this.textView = textView;
    }


    public void play(int position) {
        switch (mediaPlayerState) {
            case IS_CREATED:
                if (position == oldPosition) {
                    mediaPlayer.start();
                    mediaPlayerState = IS_PLAYING;
                    getViews(position);
                    createTimer();
                } else {
                    createPlayer(position);
                }
                break;

            case IS_PAUSED:
                if (position == oldPosition) {
                    mediaPlayer.start();
                    mediaPlayerState = IS_PLAYING;
                    playButton.setImageDrawable(
                            activity.getResources().getDrawable(R.drawable.ic_pause));
                    createTimer();
                } else {
                    stop();
                    createPlayer(position);
                }
                break;

            case IS_PLAYING:
                if (position == oldPosition) {
                    Log.i(TAG, "iscreated called sameposition");
                    pausedPosition = mediaPlayer.getCurrentPosition();
                    mediaPlayer.seekTo(pausedPosition);
                    mediaPlayer.pause();
                    seekBar.setProgress(pausedPosition);
                    mediaPlayerState = IS_PAUSED;
                    timer.cancel();
                    playButton.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_play));
                } else {
                    Log.i(TAG, "iscreated called different position");
                    stop();
                    createPlayer(position);
                }
                break;
            case IS_NULL:
                createPlayer(position);
                break;
            default:
                break;
        }
    }

    private void getViews(int position) {
        if (messageArrayList.get(position).getFrom() == USER_ID) {
            seekBar = layoutManager.findViewByPosition(position).findViewById(R.id.userVoiceSeekBar);
            textView = layoutManager.findViewByPosition(position).findViewById(R.id.userVoiceDuration);
            playButton = layoutManager.findViewByPosition(position).findViewById(R.id.playUserAudio);
        } else {
            seekBar = layoutManager.findViewByPosition(position).findViewById(R.id.friendVoiceSeekBar);
            textView = layoutManager.findViewByPosition(position).findViewById(R.id.friendAudioDuration);
            playButton = layoutManager.findViewByPosition(position).findViewById(R.id.playFriendAudio);
        }
        seekBar.setTag(position);
    }

    private void createPlayer(int filePosition) {
        if (context != null) {
            activity = (Activity) context;
        }
        Log.i(TAG, "createPlayer: called " + filePosition);
        File file = new File(messageArrayList.get(filePosition).getVoiceMailUri());
        mediaPlayer = MediaPlayer.create(activity, Uri.fromFile(file));
        getViews(filePosition);
        seekBar.setMax(mediaPlayer.getDuration());

        if (seekBar.getProgress() > 0) {
            seekBar.setProgress(seekBar.getProgress());
            mediaPlayer.seekTo(seekBar.getProgress());
        } else {
            seekBar.setProgress(0);
        }

        mediaPlayer.start();
        mediaPlayerState = IS_PLAYING;
        oldPosition = filePosition;
        playButton.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_pause));
        mediaPlayer.setOnCompletionListener(this);
        createTimer();

        if (mediaPlayer != null) {
            seekBar.setOnSeekBarChangeListener(this);
        }
    }

    public void createPlayerFromSeekbar(int position) {
        if (mediaPlayerState == IS_NULL) {

            File file = new File(messageArrayList.get(position).getVoiceMailUri());
            mediaPlayer = MediaPlayer.create(context, Uri.fromFile(file));
            seekBar.setTag(position);
            seekBar.setMax(mediaPlayer.getDuration());
            int progress = (seekBar.getProgress() * mediaPlayer.getDuration()) / 100;


            if (seekBar.getProgress() > 0) {
                seekBar.setProgress(progress);
                mediaPlayer.seekTo(progress);
                textView.setText(sdf.format(mediaPlayer.getCurrentPosition()));
            } else {
                seekBar.setProgress(0);
            }
            mediaPlayerState = IS_CREATED;
            oldPosition = position;
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
                        if (mediaPlayer == null) {
                            timer.cancel();
                        } else {
                            seekBar.setProgress(mediaPlayer.getCurrentPosition());
                            textView.setText(sdf.format(mediaPlayer.getCurrentPosition()));
                        }

                    }
                });

            }
        }, 0, 100);
    }

    public void stop() {
        Log.i(TAG, "stop: called");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    timer.cancel();
                    playButton.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_play));
                    textView.setText(sdf.format(mediaPlayer.getDuration()));
                    mediaPlayer.release();
                    mediaPlayer = null;
                    mediaPlayerState = IS_NULL;
                    seekBar.setProgress(0);
                    seekBar = null;
                }
            }
        };

        runnable.run();
    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.i(TAG, "onCompletion: called");
        stop();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        Log.i(TAG, "onProgressChanged: " + seekBar.getTag());
        if (fromUser && Integer.valueOf(seekBar.getTag().toString()) == oldPosition) {
            if (mediaPlayer != null) {
                mediaPlayer.seekTo(progress);
                textView.setText(sdf.format(mediaPlayer.getCurrentPosition()));
            }

        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            mediaPlayerState = IS_PLAYING;
            playButton.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_pause));
        }


    }
}

