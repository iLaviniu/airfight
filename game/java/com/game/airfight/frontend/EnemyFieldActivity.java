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
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.game.profile.airfight.R;
import com.game.airfight.backend.AirfightDataBase;
import com.game.airfight.backend.Airplane;
import com.game.airfight.backend.BackButtonOff;
import com.game.airfight.backend.Coordinate;
import com.game.airfight.backend.EnemyFleet;
import com.game.airfight.backend.EnemySingleton;
import com.game.airfight.backend.MenuMusicHandler;

import java.util.ArrayList;

public class EnemyFieldActivity extends AppCompatActivity implements View.OnClickListener{

    Context context = null;

    LinearLayout enemyFieldPlatform = null;

    private SoundPool soundPool = null;

    private int onClickSound = 0;

    private int onFireSound = 0;

    private AirfightDataBase airfightDataBase = null;

    private Coordinate currentTarget = new Coordinate(0, 0);


    ImageView airplaneHeads = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_enemy_field);

        new BackButtonOff(this);

        context = getBaseContext();

        airfightDataBase = new AirfightDataBase(context);


        EnemySingleton.getInstance(context);


        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);

        RelativeLayout enemyFieldLayout = (RelativeLayout)findViewById(R.id.enemyFieldLayout);
        RelativeLayout.LayoutParams gameLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        if (SDK_INT >= android.os.Build.VERSION_CODES.P){
            gameLayoutParams.setMargins(dpToPixels(45),dpToPixels(0),dpToPixels(45),dpToPixels(0));
        } else{
            gameLayoutParams.setMargins(dpToPixels(0),dpToPixels(0),dpToPixels(0),dpToPixels(0));
        }
        enemyFieldLayout.setLayoutParams(gameLayoutParams);

        LinearLayout.LayoutParams paramsH = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        airplaneHeads = (ImageView)findViewById(R.id.airplaneHeads);
        Bitmap icon = null;

        if(EnemySingleton.getInstance(context).getEnemyHeadsNumber() == 1) {
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.one_button_gray);
            airplaneHeads.setImageBitmap(icon);
        } else if (EnemySingleton.getInstance(context).getEnemyHeadsNumber() == 2) {
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.tow_button_gray);
            airplaneHeads.setImageBitmap(icon);
        } else if (EnemySingleton.getInstance(context).getEnemyHeadsNumber() == 3) {
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.three_button_gray);
            airplaneHeads.setImageBitmap(icon);
        } else if (EnemySingleton.getInstance(context).getEnemyHeadsNumber() == 0) {
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.empty_button_gray);
            airplaneHeads.setImageBitmap(icon);
        }

        airplaneHeads.setImageBitmap(icon);
        airplaneHeads.setLayoutParams(paramsH);



        enemyFieldPlatform = (LinearLayout)findViewById(R.id.enemyFieldPlatform);
        enemyFieldPlatform.post(new Runnable()
        {

            @Override
            public void run()
            {
                generateMap(context);
                renderDefaultMap(context);
                renderMap(context, EnemySingleton.getInstance(context).getMyShots(), currentTarget, false);
            }
        });


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

        soundPool = null;

        onClickSound = 0;

        onFireSound = 0;
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
        int width = enemyFieldPlatform.getMeasuredWidth();
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
                tv.setOnClickListener(this);

                linearLayoutRow.addView(tv);
            }

            enemyFieldPlatform.addView(linearLayoutRow);
        }
    }

    private void renderDefaultMap(Context context) {
        int mapRows = Integer.parseInt(context.getString(R.string.MAP_ROWS));
        int mapColumns = Integer.parseInt(context.getString(R.string.MAP_COLUMNS));
        int columnIndex = 1;
        int rowIndex = 1;

        for(columnIndex = 1; columnIndex <= mapColumns; columnIndex++) {
            LinearLayout ll = (LinearLayout)enemyFieldPlatform.getChildAt(columnIndex - 1);

            for(rowIndex = 1; rowIndex <= mapRows; rowIndex++) {
                TextView tv  = (TextView) ll.getChildAt(rowIndex - 1);
                tv.setBackgroundResource(R.drawable.gray_square_rounded_3dp_layout);
                tv.clearAnimation();
                tv.setText("");
            }
        }
    }

    private void renderMap(Context context, ArrayList<Coordinate> myShots, Coordinate currentShot, boolean isCurrentShotPartOfFleet) {
        int mapRows = Integer.parseInt(context.getString(R.string.MAP_ROWS));
        int mapColumns = Integer.parseInt(context.getString(R.string.MAP_COLUMNS));
        int columnIndex = 1;
        int rowIndex = 1;

        for(columnIndex = 1; columnIndex <= mapColumns; columnIndex++) {
            LinearLayout ll = (LinearLayout)enemyFieldPlatform.getChildAt(columnIndex - 1);

            for(rowIndex = 1; rowIndex <= mapRows; rowIndex++) {
                TextView tv = (TextView) ll.getChildAt(rowIndex - 1);

                for (Coordinate myShot : myShots) {

                    if ((myShot.getRow() == rowIndex) && (myShot.getColumn() == columnIndex)) {

                        tv.setBackground(EnemySingleton.getInstance(context).getEnemyFleet().getFuselageDrawable());
                    }
                }

                if ((currentShot.getRow() == rowIndex) && (currentShot.getColumn() == columnIndex) && isCurrentShotPartOfFleet) {
                    tv.startAnimation(AnimationUtils.loadAnimation(context, R.anim.shot_animation));
                }
            }
        }
    }

    private int dpToPixels(final float dip) {
        final float scale = this.getResources().getDisplayMetrics().density;
        return Math.round(dip * scale + 0.5f);
    }

    @Override
    public void onClick(View view) {

        String selectedTag = String.valueOf(view.getTag());

        if(selectedTag.contains("map:")) {

            renderDefaultMap(context);

            view.setBackgroundResource(R.drawable.target_selected);

            this.soundPool.play(this.onClickSound, 1, 1, 0, 0, 1);

            String[] array = selectedTag.split(":");
            int selectedRow = Integer.parseInt(array[1]);
            int selectedColumn = Integer.parseInt(array[2]);

            currentTarget.setRow(selectedRow);
            currentTarget.setColumn(selectedColumn);
        }
        
        renderMap(context, EnemySingleton.getInstance(context).getMyShots(), currentTarget, false);
    }

    public void onClickFire(View view) {

        boolean isCurrentShotPartOfFleet = false;

        if(currentTarget.isTargetOnMap(context)) {
            this.soundPool.play(this.onFireSound, 1, 1, 0, 0, 1);

            if(isShootPartOfEnemyFleet(EnemySingleton.getInstance(context).getEnemyFleet().getAirplanes(), currentTarget)) {

                isCurrentShotPartOfFleet = true;

                if(!isShotAlreadyTaken(EnemySingleton.getInstance(context).getMyShots(), currentTarget)) {
                    EnemySingleton.getInstance(context).getMyShots().add(new Coordinate(currentTarget.getRow(), currentTarget.getColumn()));

                    /*check if is airplane head*/
                    isHeadShot(EnemySingleton.getInstance(context).getEnemyFleet().getAirplanes(), currentTarget);
                }
            }

            updateHeadsCardNumber();

            renderDefaultMap(context);
            renderMap(context, EnemySingleton.getInstance(context).getMyShots(), currentTarget, isCurrentShotPartOfFleet);

            currentTarget.setRow(0);
            currentTarget.setColumn(0);

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if (EnemySingleton.getInstance(context).getEnemyHeadsNumber() == 3) {

                MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.success_sound);
                mediaPlayer.setLooping(false);
                mediaPlayer.start();

                MenuMusicHandler.getInstance().setNextActivity(getResources().getString(R.string.RESULTS_ACTIVITY));
                Intent intent = new Intent(EnemyFieldActivity.this, ResultsActivity.class);
                startActivity(intent);
            } else {
                MenuMusicHandler.getInstance().setNextActivity(getResources().getString(R.string.MY_FIELD_ACTIVITY));
                Intent intent = new Intent(EnemyFieldActivity.this, MyFieldActivity.class);
                startActivity(intent);
            }

        } else {
            Toast.makeText(getBaseContext(), "Choose the target!", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isShootPartOfEnemyFleet(ArrayList<Airplane> enemyAirplanes, Coordinate myShot) {
        boolean isPart = false;
        for(Airplane enemyAirplane: enemyAirplanes) {

            if(enemyAirplane.isPartOfAirplane(myShot.getRow(), myShot.getColumn())) {
                isPart = true;
            }
        }
        return isPart;
    }

    private boolean isShotAlreadyTaken(ArrayList<Coordinate> myShots, Coordinate newShot) {

        boolean alreadyTaken = false;
        for(Coordinate myShot: myShots) {

            if ((myShot.getRow() == newShot.getRow()) && (myShot.getColumn() == newShot.getColumn())) {
                alreadyTaken = true;
                break;
            }
        }
        return alreadyTaken;
    }

    private void isHeadShot(ArrayList<Airplane> enemyAirplanes, Coordinate myShot) {

        for(Airplane enemyAirplane: enemyAirplanes) {

            if(enemyAirplane.isPartOfAirplane(myShot.getRow(), myShot.getColumn())) {

                if(enemyAirplane.isAirplaneHead(myShot.getRow(), myShot.getColumn())) {

                    EnemySingleton.getInstance(context).increaseEnemyHeadsNumber();
                }
            }
        }
    }

    private void updateHeadsCardNumber() {

        if(EnemySingleton.getInstance(context).getEnemyHeadsNumber() == 1) {
            Bitmap icon1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.one_button_gray);
            airplaneHeads.setImageBitmap(icon1);
        } else if (EnemySingleton.getInstance(context).getEnemyHeadsNumber() == 2) {
            Bitmap icon2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.tow_button_gray);
            airplaneHeads.setImageBitmap(icon2);
        } else if (EnemySingleton.getInstance(context).getEnemyHeadsNumber() == 3) {
            Bitmap icon3 = BitmapFactory.decodeResource(context.getResources(), R.drawable.three_button_gray);
            airplaneHeads.setImageBitmap(icon3);
        }
    }
}