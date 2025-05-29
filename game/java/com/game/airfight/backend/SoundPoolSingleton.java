package com.game.airfight.backend;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;

import com.game.profile.airfight.R;

public class SoundPoolSingleton {

    private static SoundPoolSingleton instance = null;

    private static SoundPool soundPool = null;

    private static int onClickSound = 0;
    private static int battleLostSound = 0;
    private static int fireSound = 0;
    private static int successSound = 0;

    private SoundPoolSingleton() {

    }

    public static SoundPoolSingleton getInstance(Context context) {

        if(instance == null) {
            instance = new SoundPoolSingleton();

            AudioAttributes audioAttributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA).setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build();
            soundPool = new SoundPool.Builder().setMaxStreams(1).setAudioAttributes(audioAttributes).build();
            onClickSound = soundPool.load(context, R.raw.on_click_sound, 1);
            battleLostSound = soundPool.load(context, R.raw.battle_lost_sound, 1);
            fireSound = soundPool.load(context, R.raw.fire_sound, 1);
            successSound = soundPool.load(context, R.raw.success_sound, 1);

        }
        return instance;
    }

    public void initialize() {
        /*used to initialize the singleton for the first use*/
    }

    public void playOnClickSound() {
        soundPool.play(onClickSound, 1, 1, 0, 0, 1);
    }

    public void playBattleLostSound() {
        soundPool.play(battleLostSound, 1, 1, 0, 0, 1);
    }

    public void playFireSound() {
        soundPool.play(fireSound, 1, 1, 0, 0, 1);
    }

    public void playSuccessSound() {
        soundPool.play(successSound, 1, 1, 0, 0, 1);
    }
}
