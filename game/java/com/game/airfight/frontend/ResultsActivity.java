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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;

import com.game.profile.airfight.R;
import com.game.airfight.backend.AirfightDataBase;
import com.game.airfight.backend.Airplane;
import com.game.airfight.backend.BackButtonOff;
import com.game.airfight.backend.Coordinate;
import com.game.airfight.backend.EnemySingleton;
import com.game.airfight.backend.FuselageSingleton;
import com.game.airfight.backend.MenuMusicHandler;
import com.game.airfight.backend.MyAirplanesSingleton;
import com.game.airfight.backend.MySingleton;

import java.util.ArrayList;

public class ResultsActivity extends AppCompatActivity {

    Context context = null;

    LinearLayout resultsLayout = null;

    private SoundPool soundPool = null;

    private int onClickSound = 0;

    private AirfightDataBase airfightDataBase = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_results);

        new BackButtonOff(this);

        context = getBaseContext();

        airfightDataBase = new AirfightDataBase(context);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);

        RelativeLayout resultsActivityLayout = (RelativeLayout)findViewById(R.id.resultsActivityLayout);
        RelativeLayout.LayoutParams gameLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        if (SDK_INT >= android.os.Build.VERSION_CODES.P){
            gameLayoutParams.setMargins(dpToPixels(45),dpToPixels(0),dpToPixels(45),dpToPixels(0));
        } else{
            gameLayoutParams.setMargins(dpToPixels(0),dpToPixels(0),dpToPixels(0),dpToPixels(0));
        }
        resultsActivityLayout.setLayoutParams(gameLayoutParams);

        resultsLayout = (LinearLayout)findViewById(R.id.resultsLayout);
        resultsLayout.post(new Runnable()
        {

            @Override
            public void run()
            {

                /*my airplanes*/
                ArrayList<Airplane> myAirplanes = MyAirplanesSingleton.getInstance().getMyFleet();

                /*enemy shots*/
                ArrayList<Coordinate> enemyShots = MySingleton.getInstance().getEnemyShots();

                generateMap(context);
                renderDefaultMap(context);
                renderMap(context, myAirplanes, enemyShots, context.getString(R.string.MY_FLEET));
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        AudioAttributes audioAttributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA).setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build();
        this.soundPool = new SoundPool.Builder().setMaxStreams(1).setAudioAttributes(audioAttributes).build();
        this.onClickSound = this.soundPool.load(this, R.raw.airplane_selection, 1);
    }

    @Override
    protected void onPause() {
        super.onPause();

        this.soundPool = null;
        this.onClickSound = 0;

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
        int width = resultsLayout.getMeasuredWidth();
        int height = (int)(0.5 * width);
        int square = (int)(((height - 2 * 12 ) / 12));
        int columnIndex = 0;
        int rowIndex = 0;

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

            resultsLayout.addView(linearLayoutRow);
        }
    }

    private void renderDefaultMap(Context context) {
        int mapRows = Integer.parseInt(context.getString(R.string.MAP_ROWS));
        int mapColumns = Integer.parseInt(context.getString(R.string.MAP_COLUMNS));
        int columnIndex = 1;
        int rowIndex = 1;

        for(columnIndex = 1; columnIndex <= mapColumns; columnIndex++) {
            LinearLayout ll = (LinearLayout)resultsLayout.getChildAt(columnIndex - 1);

            for(rowIndex = 1; rowIndex <= mapRows; rowIndex++) {
                TextView tv  = (TextView) ll.getChildAt(rowIndex - 1);
                tv.setBackgroundResource(R.drawable.gray_square_rounded_3dp_layout);
                tv.clearAnimation();
                tv.setText("");
            }
        }
    }

    private void renderMap(Context context, ArrayList<Airplane> airplanes, ArrayList<Coordinate> shots, String fleet) {

        int mapRows = Integer.parseInt(context.getString(R.string.MAP_ROWS));
        int mapColumns = Integer.parseInt(context.getString(R.string.MAP_COLUMNS));
        int columnIndex = 1;
        int rowIndex = 1;

        RoundedBitmapDrawable roundedBitmapDrawable = null;
        
        if(fleet.equals(context.getString(R.string.MY_FLEET))) {

            roundedBitmapDrawable = FuselageSingleton.getInstance().getAirplaneFuselageDrawable(airfightDataBase.getFuselageId());

        } else if (fleet.equals(context.getString(R.string.ENEMY_FLEET))) {

            roundedBitmapDrawable = EnemySingleton.getInstance(context).getEnemyFleet().getFuselageDrawable();
        }

        for(columnIndex = 1; columnIndex <= mapColumns; columnIndex++) {
            LinearLayout ll = (LinearLayout)resultsLayout.getChildAt(columnIndex - 1);

            for(rowIndex = 1; rowIndex <= mapRows; rowIndex++) {
                TextView tv = (TextView) ll.getChildAt(rowIndex - 1);

                for(Airplane airplane: airplanes) {

                    if(airplane.isPartOfAirplane(rowIndex, columnIndex)) {
                        tv.setBackground(roundedBitmapDrawable);
                    }
                }

                for(Coordinate enemyShot: shots) {

                    if( (rowIndex == enemyShot.getRow()) && (columnIndex == enemyShot.getColumn()) ) {

                        tv.setBackgroundResource(R.drawable.taken_shoot);
                    }
                }
            }
        }
    }

    private int dpToPixels(final float dip) {
        final float scale = this.getResources().getDisplayMetrics().density;
        return Math.round(dip * scale + 0.5f);
    }

    public void onClickMyFleetButton(View view) {

        this.soundPool.play(this.onClickSound, 1, 1, 0, 0, 1);

        /*my airplanes*/
        ArrayList<Airplane> myAirplanes = MyAirplanesSingleton.getInstance().getMyFleet();

        /*enemy shots*/
        ArrayList<Coordinate> enemyShots = MySingleton.getInstance().getEnemyShots();

        renderDefaultMap(context);
        renderMap(context, myAirplanes, enemyShots, context.getString(R.string.MY_FLEET));
    }

    public void onClickEnemyFleetButton(View view) {

        this.soundPool.play(this.onClickSound, 1, 1, 0, 0, 1);

        /*enemy airplanes*/
        ArrayList<Airplane> enemyFleet = EnemySingleton.getInstance(context).getEnemyFleet().getAirplanes();

        /*my shots*/
        ArrayList<Coordinate> myShots = EnemySingleton.getInstance(context).getMyShots();

        renderDefaultMap(context);
        renderMap(context, enemyFleet, myShots, context.getString(R.string.ENEMY_FLEET));
    }

    public void onClickMenuButton(View view) {

        this.soundPool.play(this.onClickSound, 1, 1, 0, 0, 1);

        MenuMusicHandler.getInstance().setNextActivity(getResources().getString(R.string.MENU_ACTIVITY));
        Intent intent = new Intent(ResultsActivity.this, MenuActivity.class);
        startActivity(intent);
    }
}