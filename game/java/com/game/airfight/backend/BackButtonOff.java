package com.game.airfight.backend;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

public class BackButtonOff {

    public BackButtonOff(AppCompatActivity activity) {

        activity.getOnBackPressedDispatcher().addCallback(activity, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                /*do nothing*/
            }
        });
    }
}
