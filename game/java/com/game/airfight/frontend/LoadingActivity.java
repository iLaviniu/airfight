package com.game.airfight.frontend;

import static android.os.Build.VERSION.SDK_INT;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.game.airfight.backend.AirfightDataBase;
import com.game.airfight.backend.BackButtonOff;
import com.game.profile.airfight.R;


public class LoadingActivity extends AppCompatActivity {

    Context context = null;

    private AirfightDataBase airfightDataBase = null;
    
    LinearLayout loadingMain = null;


    private static Handler handlerMain = null;
    private static Runnable runnableMain = null;

    private static Handler handlerLoop = null;
    private static Runnable runnableLoop = null;

    private static int loopCounter = 0;

    private static TextView dot1 = null;
    private static TextView dot2 = null;
    private static TextView dot3 = null;
    private static TextView dot4 = null;

    private static BackButtonOff backButtonOff = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_loading);

        backButtonOff = new BackButtonOff(this);

        context = getBaseContext();

        loopCounter = 0;

        airfightDataBase = new AirfightDataBase(context);

        dot1 = (TextView)findViewById(R.id.dot1);
        dot2 = (TextView)findViewById(R.id.dot2);
        dot3 = (TextView)findViewById(R.id.dot3);
        dot4 = (TextView)findViewById(R.id.dot4);

        dot1.startAnimation(AnimationUtils.loadAnimation(context, R.anim.loading_fade_dot_1));
        dot2.startAnimation(AnimationUtils.loadAnimation(context, R.anim.loading_fade_dot_2));
        dot3.startAnimation(AnimationUtils.loadAnimation(context, R.anim.loading_fade_dot_3));
        dot4.startAnimation(AnimationUtils.loadAnimation(context, R.anim.loading_fade_dot_4));

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);

        RelativeLayout loadingLayout = (RelativeLayout)findViewById(R.id.loadingLayout);
        RelativeLayout.LayoutParams gameLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        if (SDK_INT >= android.os.Build.VERSION_CODES.P){
            gameLayoutParams.setMargins(dpToPixels(45),dpToPixels(0),dpToPixels(45),dpToPixels(0));
        } else{
            gameLayoutParams.setMargins(dpToPixels(0),dpToPixels(0),dpToPixels(0),dpToPixels(0));
        }
        loadingLayout.setLayoutParams(gameLayoutParams);

        loadingMain = (LinearLayout)findViewById(R.id.loadingMain);


        handlerMain = new Handler();
        runnableMain = new Runnable() {
            @Override
            public void run() {
                int width = loadingMain.getMeasuredWidth();
                airfightDataBase.updateLayoutWidth(width);

                Intent intent = new Intent(LoadingActivity.this, MenuActivity.class);
                startActivity(intent);
                finish();
            }
        };
        handlerMain.postDelayed(runnableMain, 3500);


        handlerLoop = new Handler();
        runnableLoop = new Runnable() {
            @Override
            public void run() {

                loopCounter++;

                dot1.clearAnimation();
                dot2.clearAnimation();
                dot3.clearAnimation();
                dot4.clearAnimation();

                if(loopCounter <= 5) {

                    dot1.startAnimation(AnimationUtils.loadAnimation(context, R.anim.loading_fade_dot_1));
                    dot2.startAnimation(AnimationUtils.loadAnimation(context, R.anim.loading_fade_dot_2));
                    dot3.startAnimation(AnimationUtils.loadAnimation(context, R.anim.loading_fade_dot_3));
                    dot4.startAnimation(AnimationUtils.loadAnimation(context, R.anim.loading_fade_dot_4));

                    handlerLoop.postDelayed(this, 500);
                } else {

                    dot1.setBackgroundResource(R.drawable.full_white);
                    dot2.setBackgroundResource(R.drawable.full_white);
                    dot3.setBackgroundResource(R.drawable.full_white);
                    dot4.setBackgroundResource(R.drawable.full_white);
                }
            }
        };
        handlerLoop.postDelayed(runnableLoop, 500);
        
    }

    @Override
    protected void onPause() {
        super.onPause();

        handlerMain.removeCallbacks(runnableMain);
        handlerMain.removeCallbacksAndMessages(null);


        handlerLoop.removeCallbacks(runnableLoop);
        handlerLoop.removeCallbacksAndMessages(null);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        Configuration old = newBase.getResources().getConfiguration();

        final Configuration override = new Configuration(newBase.getResources().getConfiguration());
        override.fontScale = 1.0f;
        newBase = newBase.createConfigurationContext(override);

        super.attachBaseContext(newBase);
    }


    private int dpToPixels(final float dip) {
        final float scale = this.getResources().getDisplayMetrics().density;
        return Math.round(dip * scale + 0.5f);
    }
}