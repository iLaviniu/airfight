package com.game.airfight.frontend;

import static android.os.Build.VERSION.SDK_INT;
import static android.view.Gravity.CENTER;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.game.profile.airfight.R;
import com.game.airfight.backend.AirfightDataBase;
import com.game.airfight.backend.Airplane;
import com.game.airfight.backend.BackButtonOff;
import com.game.airfight.backend.FuselagePart;
import com.game.airfight.backend.FuselageSingleton;
import com.game.airfight.backend.MenuMusicHandler;

import java.util.ArrayList;

public class BuildActivity extends AppCompatActivity {

    Context context = null;

    LinearLayout buildPlatform = null;

    private SoundPool soundPool = null;

    private int onClickSound = 0;

    private ArrayList<FuselagePart> fuselageParts = null;

    Airplane buildAirplane = null;

    private AirfightDataBase airfightDataBase = null;

    private int squareSize = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_build);

        new BackButtonOff(this);

        context = getBaseContext();

        fuselageParts = FuselageSingleton.getInstance().getFuselageParts();

        airfightDataBase = new AirfightDataBase(context);

        buildAirplane = new Airplane();

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);

        RelativeLayout buildLayout = (RelativeLayout)findViewById(R.id.buildLayout);
        RelativeLayout.LayoutParams gameLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        if (SDK_INT >= android.os.Build.VERSION_CODES.P){
            gameLayoutParams.setMargins(dpToPixels(45),dpToPixels(0),dpToPixels(45),dpToPixels(0));
        } else{
            gameLayoutParams.setMargins(dpToPixels(0),dpToPixels(0),dpToPixels(0),dpToPixels(0));
        }
        buildLayout.setLayoutParams(gameLayoutParams);

        buildPlatform = (LinearLayout)findViewById(R.id.buildPlatform);
        buildPlatform.post(new Runnable()
        {

            @Override
            public void run()
            {
                generateMap(context);
                renderDefaultMap(context);
                renderMap(context, fuselageParts, buildAirplane);

            }
        });




    }

    @Override
    protected void onResume() {
        super.onResume();

        AudioAttributes audioAttributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA).setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build();
        this.soundPool = new SoundPool.Builder().setMaxStreams(1).setAudioAttributes(audioAttributes).build();
        this.onClickSound = this.soundPool.load(this, R.raw.airplane_selection, 1);

        MenuMusicHandler.getInstance().start(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        this.soundPool = null;
        this.onClickSound = 0;

        String nextActivity = MenuMusicHandler.getInstance().getNextActivity();

        if( !( nextActivity.equals(getResources().getString(R.string.MENU_ACTIVITY)))) {
            MenuMusicHandler.getInstance().stop();
        }

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        Configuration old = newBase.getResources().getConfiguration();

        final Configuration override = new Configuration(newBase.getResources().getConfiguration());
        override.fontScale = 1.0f;
        newBase = newBase.createConfigurationContext(override);

        super.attachBaseContext(newBase);
    }

    private void generateMap(Context context) {
        int mapRows = Integer.parseInt(context.getString(R.string.MAP_ROWS));
        int mapColumns = Integer.parseInt(context.getString(R.string.MAP_COLUMNS));
        int width = buildPlatform.getMeasuredWidth();
        int height = (int)(0.5 * width);
        int square = (int)(((height - 2 * 12 ) / 12));
        int columnIndex = 0;
        int rowIndex = 0;

        squareSize = square;

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(1,1,1,1);

        for(columnIndex = 1; columnIndex <= mapColumns; columnIndex++) {

            LinearLayout linearLayoutRow = new LinearLayout(context);
            linearLayoutRow.setSoundEffectsEnabled(false);
            linearLayoutRow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            linearLayoutRow.setOrientation(LinearLayout.VERTICAL);
            linearLayoutRow.setGravity(CENTER);
            linearLayoutRow.setTag(columnIndex);
            linearLayoutRow.setBackgroundColor(Color.parseColor("#FFFFFF"));

            ArrayList<TextView> row = new ArrayList<>();

            for(rowIndex = 1; rowIndex <= mapRows; rowIndex++) {

                TextView tv = new TextView(context);
                tv.setSoundEffectsEnabled(false);
                tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tv.setWidth(square);
                tv.setHeight(square);
                tv.setLayoutParams(params);
                tv.setBackgroundResource(R.drawable.gray_square_rounded_3dp_layout);

                linearLayoutRow.addView(tv);
            }

            buildPlatform.addView(linearLayoutRow);
        }


    }

    private void renderDefaultMap(Context context) {
        int mapRows = Integer.parseInt(context.getString(R.string.MAP_ROWS));
        int mapColumns = Integer.parseInt(context.getString(R.string.MAP_COLUMNS));
        int columnIndex = 1;
        int rowIndex = 1;

        for(columnIndex = 1; columnIndex <= mapColumns; columnIndex++) {
            LinearLayout ll = (LinearLayout)buildPlatform.getChildAt(columnIndex - 1);

            for(rowIndex = 1; rowIndex <= mapRows; rowIndex++) {
                TextView tv  = (TextView) ll.getChildAt(rowIndex - 1);
                tv.setBackgroundResource(R.drawable.gray_square_rounded_3dp_layout);
                tv.clearAnimation();
                tv.setText("");
            }
        }
    }

    private void renderMap(Context context, ArrayList<FuselagePart> fuselageParts, Airplane buildAirplane) {
        int mapRows = Integer.parseInt(context.getString(R.string.MAP_ROWS));
        int mapColumns = Integer.parseInt(context.getString(R.string.MAP_COLUMNS));
        int columnIndex = 1;
        int rowIndex = 1;

        for(columnIndex = 1; columnIndex <= mapColumns; columnIndex++) {
            LinearLayout ll = (LinearLayout)buildPlatform.getChildAt(columnIndex - 1);

            for(rowIndex = 1; rowIndex <= mapRows; rowIndex++) {
                TextView tv = (TextView) ll.getChildAt(rowIndex - 1);


                for (FuselagePart fuselagePart : fuselageParts) {

                    if (fuselagePart.isPartOfMenuField(rowIndex, columnIndex)) {
                        tv.setBackground(fuselagePart.getRoundedBitmapDrawable());

                        if(fuselagePart.isActive()) {
                            tv.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in));
                        }
                    }

                }

                if(buildAirplane.isPartOfAirplane(rowIndex, columnIndex)) {

                    tv.setBackground(FuselageSingleton.getInstance().getAirplaneFuselageDrawable(airfightDataBase.getFuselageId()));
                }
            }
        }
    }

    private int dpToPixels(final float dip) {
        final float scale = this.getResources().getDisplayMetrics().density;
        return Math.round(dip * scale + 0.5f);
    }

    public void onClickBackButton(View view) {

        this.soundPool.play(this.onClickSound, 1, 1, 0, 0, 1);

        MenuMusicHandler.getInstance().setNextActivity(getResources().getString(R.string.MENU_ACTIVITY));
        Intent intent = new Intent(BuildActivity.this, MenuActivity.class);
        startActivity(intent);
    }

    public void onClickUp(View view) {

        this.soundPool.play(this.onClickSound, 1, 1, 0, 0, 1);

        int activeIndex = 0;
        int fuselagePartIndex = 0;
        int fuselagePartNextIndex = 0;
        int activeFuselageColor = -1;

        for(FuselagePart fuselagePart: fuselageParts) {
            if(fuselagePart.isActive()) {
                activeIndex = fuselagePartIndex;
                fuselagePart.setActive(false);
            }
            fuselagePartIndex++;
        }

        if(activeIndex == 0) {
            fuselagePartNextIndex = fuselageParts.size() - 1;
        } else {
            fuselagePartNextIndex = activeIndex - 1;
        }

        fuselagePartIndex = 0;
        for(FuselagePart fuselagePart: fuselageParts) {
            if(fuselagePartIndex == fuselagePartNextIndex) {
                fuselagePart.setActive(true);
            }
            fuselagePartIndex++;
        }

        airfightDataBase.updateFuselageId(fuselagePartNextIndex + 1);

        renderDefaultMap(context);
        renderMap(context, fuselageParts, buildAirplane);
    }

    public void onClickDown(View view) {

        this.soundPool.play(this.onClickSound, 1, 1, 0, 0, 1);

        int activeIndex = 0;
        int fuselagePartIndex = 0;
        int fuselagePartNextIndex = 0;

        for(FuselagePart fuselagePart: fuselageParts) {
            if(fuselagePart.isActive()) {
                activeIndex = fuselagePartIndex;
                fuselagePart.setActive(false);
            }
            fuselagePartIndex++;
        }

        if(activeIndex == (fuselageParts.size() - 1) ) {
            fuselagePartNextIndex = 0;
        } else {
            fuselagePartNextIndex = activeIndex + 1;
        }

        fuselagePartIndex = 0;
        for(FuselagePart fuselagePart: fuselageParts) {
            if(fuselagePartIndex == fuselagePartNextIndex) {
                fuselagePart.setActive(true);
            }
            fuselagePartIndex++;
        }

        airfightDataBase.updateFuselageId(fuselagePartNextIndex + 1);

        renderDefaultMap(context);
        renderMap(context, fuselageParts, buildAirplane);
    }
}