package com.game.airfight.backend;

import android.media.MediaPlayer;

import androidx.appcompat.app.AppCompatActivity;

import com.game.profile.airfight.R;

public final class MenuMusicHandler {

    private static MenuMusicHandler instance = null;

    private static MediaPlayer mediaPlayer = null;

    private static String nextActivity = null;

    private MenuMusicHandler() {
    }

    public static MenuMusicHandler getInstance() {
        if(instance == null) {
            instance = new MenuMusicHandler();
        }

        return instance;
    }

    public void start(AppCompatActivity activity) {
        if(mediaPlayer == null) {
            /*create only for the first time*/
            mediaPlayer = MediaPlayer.create(activity, R.raw.menu_music);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }
    }

    public void stop() {
        if(mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer = null;
        }
    }

    public void setNextActivity(String activity) {
        nextActivity = activity;
    }

    public String getNextActivity() {
        return nextActivity;
    }

    // getters and setters
}
