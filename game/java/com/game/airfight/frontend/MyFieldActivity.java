package com.game.airfight.frontend;

import static android.os.Build.VERSION.SDK_INT;
import static android.view.Gravity.CENTER;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.game.profile.airfight.R;
import com.game.airfight.backend.AirfightDataBase;
import com.game.airfight.backend.Airplane;
import com.game.airfight.backend.BackButtonOff;
import com.game.airfight.backend.Coordinate;
import com.game.airfight.backend.Element;
import com.game.airfight.backend.EnemyFleet;
import com.game.airfight.backend.EnemySingleton;
import com.game.airfight.backend.FuselageSingleton;
import com.game.airfight.backend.MenuMusicHandler;
import com.game.airfight.backend.MyAirplanesSingleton;
import com.game.airfight.backend.MySingleton;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MyFieldActivity extends AppCompatActivity{

    Context context = null;

    LinearLayout myFieldPlatform = null;

    private SoundPool soundPool = null;

    private int onClickSound = 0;

    private int onFireSound = 0;

    private int airplaneHeadsNumber = 0;

    ImageView myFleetHeads = null;

    private AirfightDataBase airfightDataBase = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_field);

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

        RelativeLayout myFieldLayout = (RelativeLayout)findViewById(R.id.myFieldLayout);
        RelativeLayout.LayoutParams gameLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        if (SDK_INT >= android.os.Build.VERSION_CODES.P){
            gameLayoutParams.setMargins(dpToPixels(45),dpToPixels(0),dpToPixels(45),dpToPixels(0));
        } else{
            gameLayoutParams.setMargins(dpToPixels(0),dpToPixels(0),dpToPixels(0),dpToPixels(0));
        }
        myFieldLayout.setLayoutParams(gameLayoutParams);

        LinearLayout.LayoutParams paramsH = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        myFleetHeads = (ImageView)findViewById(R.id.myFleetHeads);

        Bitmap icon = null;

        if(MySingleton.getInstance().getMyHeadsNumber() == 1) {
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.one_button_gray);
            myFleetHeads.setImageBitmap(icon);
        } else if (MySingleton.getInstance().getMyHeadsNumber() == 2) {
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.tow_button_gray);
            myFleetHeads.setImageBitmap(icon);
        } else if (MySingleton.getInstance().getMyHeadsNumber() == 3) {
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.three_button_gray);
            myFleetHeads.setImageBitmap(icon);
        } else if (MySingleton.getInstance().getMyHeadsNumber() == 0) {
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.empty_button_gray);
            myFleetHeads.setImageBitmap(icon);
        }

        myFleetHeads.setImageBitmap(icon);
        myFleetHeads.setLayoutParams(paramsH);

        myFieldPlatform = (LinearLayout)findViewById(R.id.myFieldPlatform);
        myFieldPlatform.post(new Runnable()
        {

            @Override
            public void run()
            {

                EnemyFleet enemyFleet = EnemySingleton.getInstance(context).getEnemyFleet();

                ArrayList<Element> myHeadsRangesMediumDifficulty = MyAirplanesSingleton.getInstance().getHeadsRangesMediumDifficulty(context);
                ArrayList<Integer> myColumnsRanges = MyAirplanesSingleton.getInstance().getHeadsColumnsRanges(context);

                ArrayList<Element> myHeadsRangesHardDifficulty = MyAirplanesSingleton.getInstance().getHeadsRangesHardDifficulty(context);

                String gameMode = "";

                boolean retry = false;
                boolean foundInHeadsRangesMediumDifficulty = false;
                boolean foundInHeadsRangesHardDifficulty = false;
                boolean foundInRangesColumns = false;
                boolean shootAlreadyTaken = false;
                Coordinate enemyShot = new Coordinate(0, 0);
                ArrayList<Coordinate> allEnemyShots = MySingleton.getInstance().getAllEnemyShots();

                gameMode = airfightDataBase.getGameDifficulty();

                do {

                    enemyShot = enemyFleet.generateFireTarget();

                    shootAlreadyTaken = false;
                    for (Coordinate currentEnemyShot : allEnemyShots) {

                        if ((enemyShot.getRow() == currentEnemyShot.getRow()) && (enemyShot.getColumn() == currentEnemyShot.getColumn())) {
                            shootAlreadyTaken = true;
                        }
                    }

                    foundInRangesColumns = false;
                    for (Integer column : myColumnsRanges) {
                        if (enemyShot.getColumn() == column) {
                            foundInRangesColumns = true;
                            break;
                        }
                    }



                    if (gameMode.equals(context.getString(R.string.EASY))) {
                        if (shootAlreadyTaken == false) {

                            if (foundInRangesColumns == false) {

                                retry = true;
                            } else if (foundInRangesColumns == true) {

                                retry = false;
                            }
                        } else {

                            retry = true;
                        }
                    } else if (gameMode.equals(context.getString(R.string.MEDIUM))) {

                        if (shootAlreadyTaken == false) {

                            for (Element headRange : myHeadsRangesMediumDifficulty) {
                                if (headRange.isInRange(enemyShot.getRow(), enemyShot.getColumn())) {
                                    foundInHeadsRangesMediumDifficulty = true;
                                }
                            }

                            if (foundInHeadsRangesMediumDifficulty) {
                                retry = false;
                            } else {
                                retry = true;
                            }

                        } else {

                            retry = true;
                        }

                    }
                    else if (gameMode.equals(context.getString(R.string.HARD))) {

                        if (shootAlreadyTaken == false) {

                            for (Element headRange : myHeadsRangesHardDifficulty) {
                                if (headRange.isInRange(enemyShot.getRow(), enemyShot.getColumn())) {
                                    foundInHeadsRangesHardDifficulty = true;
                                }
                            }

                            if (foundInHeadsRangesHardDifficulty) {
                                retry = false;
                            } else {
                                retry = true;
                            }

                        } else {

                            retry = true;
                        }

                    }

                } while (retry);


                ArrayList<Airplane> myFleet = MyAirplanesSingleton.getInstance().getMyFleet();

                boolean isPartOfMyFleet = isShootPartOfMyFleet(myFleet, enemyShot);

                MySingleton.getInstance().getAllEnemyShots().add(enemyShot);

                if(isPartOfMyFleet) {
                    MySingleton.getInstance().getEnemyShots().add(enemyShot);

                    /*check if is airplane head*/
                    isHeadShot(myFleet, enemyShot);
                }

                Bitmap icon = null;
                if(MySingleton.getInstance().getMyHeadsNumber() == 1) {
                    icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.one_button_gray);
                    myFleetHeads.setImageBitmap(icon);
                } else if (MySingleton.getInstance().getMyHeadsNumber() == 2) {
                    icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.tow_button_gray);
                    myFleetHeads.setImageBitmap(icon);
                } else if (MySingleton.getInstance().getMyHeadsNumber() == 3) {
                    icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.three_button_gray);
                    myFleetHeads.setImageBitmap(icon);
                }

                generateMap(context);
                renderDefaultMap(context);
                renderMap(context, enemyShot, isPartOfMyFleet);
            }
        });

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.fire_sound);
                mediaPlayer.setLooping(false);
                mediaPlayer.start();

            }
        }, 1000);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                if (MySingleton.getInstance().getMyHeadsNumber() == 3) {

                    MediaPlayer mediaPlayerLostSound = MediaPlayer.create(context, R.raw.battle_lost_sound);
                    mediaPlayerLostSound.setLooping(false);
                    mediaPlayerLostSound.start();

                    MenuMusicHandler.getInstance().setNextActivity(getResources().getString(R.string.RESULTS_ACTIVITY));
                    Intent intent = new Intent(MyFieldActivity.this, ResultsActivity.class);
                    startActivity(intent);
                }
            }
        }, 2000);
    }

    @Override
    protected void onResume() {
        super.onResume();

        AudioAttributes audioAttributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA).setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build();
        this.soundPool = new SoundPool.Builder().setMaxStreams(1).setAudioAttributes(audioAttributes).build();
        this.onClickSound = this.soundPool.load(this, R.raw.airplane_selection, 1);
        this.onFireSound = this.soundPool.load(this, R.raw.fire_sound, 1);

    }

    @Override
    protected void onPause() {
        super.onPause();

        this.soundPool = null;
        this.onClickSound = 0;
        this.onFireSound = 0;
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
        int width = myFieldPlatform.getMeasuredWidth();
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
                String text = "map:" + String.valueOf(rowIndex) + ":" + String.valueOf(columnIndex);
                tv.setTag(text);

                linearLayoutRow.addView(tv);
            }

            myFieldPlatform.addView(linearLayoutRow);
        }
    }

    private void renderDefaultMap(Context context) {
        int mapRows = Integer.parseInt(context.getString(R.string.MAP_ROWS));
        int mapColumns = Integer.parseInt(context.getString(R.string.MAP_COLUMNS));
        int columnIndex = 1;
        int rowIndex = 1;

        for(columnIndex = 1; columnIndex <= mapColumns; columnIndex++) {
            LinearLayout ll = (LinearLayout)myFieldPlatform.getChildAt(columnIndex - 1);

            for(rowIndex = 1; rowIndex <= mapRows; rowIndex++) {
                TextView tv  = (TextView) ll.getChildAt(rowIndex - 1);
                tv.setBackgroundResource(R.drawable.gray_square_rounded_3dp_layout);
                tv.clearAnimation();
                tv.setText("");
            }
        }
    }

    private void renderMap(Context context, Coordinate enemyShot, boolean isPartOfMyFleet) {
        int mapRows = Integer.parseInt(context.getString(R.string.MAP_ROWS));
        int mapColumns = Integer.parseInt(context.getString(R.string.MAP_COLUMNS));
        int columnIndex = 1;
        int rowIndex = 1;



        ArrayList<Airplane> myFleet = MyAirplanesSingleton.getInstance().getMyFleet();
        ArrayList<Coordinate> enemyShots = MySingleton.getInstance().getEnemyShots();

        for(columnIndex = 1; columnIndex <= mapColumns; columnIndex++) {
            LinearLayout ll = (LinearLayout)myFieldPlatform.getChildAt(columnIndex - 1);

            for(rowIndex = 1; rowIndex <= mapRows; rowIndex++) {
                TextView tv = (TextView) ll.getChildAt(rowIndex - 1);

                for(Airplane airplane: myFleet) {
                    if(airplane.isPartOfAirplane(rowIndex, columnIndex)){
                        tv.setBackground(FuselageSingleton.getInstance().getAirplaneFuselageDrawable(airfightDataBase.getFuselageId()));

                    }
                }

                for(Coordinate currentEnemyShot: enemyShots) {

                    if ((currentEnemyShot.getRow() == rowIndex) && (currentEnemyShot.getColumn() == columnIndex)) {
                        tv.setBackgroundResource(R.drawable.taken_shoot);
                    }
                }

                if ((enemyShot.getRow() == rowIndex) && (enemyShot.getColumn() == columnIndex)) {
                    tv.startAnimation(AnimationUtils.loadAnimation(context, R.anim.shot_animation));
                    tv.setBackgroundResource(R.drawable.target_selected);
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

        if (MySingleton.getInstance().getMyHeadsNumber() < 3) {

            MenuMusicHandler.getInstance().setNextActivity(getResources().getString(R.string.ENEMY_FIELD_ACTIVITY));
            Intent intent = new Intent(MyFieldActivity.this, EnemyFieldActivity.class);
            startActivity(intent);
        }

    }

    private boolean isShootPartOfMyFleet(ArrayList<Airplane> myFleet, Coordinate enemyShot) {
        boolean isPart = false;
        for(Airplane enemyAirplane: myFleet) {

            if(enemyAirplane.isPartOfAirplane(enemyShot.getRow(), enemyShot.getColumn())) {
                isPart = true;
            }
        }
        return isPart;
    }

    private void isHeadShot(ArrayList<Airplane> myFleet, Coordinate enemyShot) {

        for(Airplane enemyAirplane: myFleet) {

            if(enemyAirplane.isPartOfAirplane(enemyShot.getRow(), enemyShot.getColumn())) {

                if(enemyAirplane.isAirplaneHead(enemyShot.getRow(), enemyShot.getColumn())) {

                    MySingleton.getInstance().increaseMyHeadsNumber();
                }
            }
        }
    }
}