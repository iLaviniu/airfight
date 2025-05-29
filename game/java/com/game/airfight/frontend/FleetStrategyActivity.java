package com.game.airfight.frontend;

import static android.os.Build.VERSION.SDK_INT;
import static android.view.Gravity.CENTER;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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

import com.game.airfight.backend.SoundPoolSingleton;
import com.game.profile.airfight.R;
import com.game.airfight.backend.AirfightDataBase;
import com.game.airfight.backend.Airplane;
import com.game.airfight.backend.AirplaneCollisionDetector;
import com.game.airfight.backend.BackButtonOff;
import com.game.airfight.backend.FuselageSingleton;
import com.game.airfight.backend.LoadingPlatform;
import com.game.airfight.backend.MenuMusicHandler;
import com.game.airfight.backend.MyAirplanesSingleton;

import java.util.ArrayList;

public class FleetStrategyActivity extends AppCompatActivity {

    Context context = null;

    LinearLayout mapLayout = null;

    LoadingPlatform loadingPlatform = null;

    ArrayList<Airplane> airplanes = new ArrayList<>();

    ImageView airplaneSelector = null;

    ArrayList<Bitmap> airplanesIdsIcons = new ArrayList<>();

    private AirplaneCollisionDetector airplaneCollisionDetector = null;

    private AirfightDataBase airfightDataBase = null;

    private int fuselageId = -1;

    private static BackButtonOff backButtonOff = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_fleet_strategy);

        backButtonOff = new BackButtonOff(this);

        context = getBaseContext();

        airfightDataBase = new AirfightDataBase(context);

        fuselageId = airfightDataBase.getFuselageId();

        airplaneCollisionDetector = new AirplaneCollisionDetector(context);

        airplanesIdsIcons.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.airplanes_count_1));
        airplanesIdsIcons.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.airplanes_count_2));
        airplanesIdsIcons.add(BitmapFactory.decodeResource(context.getResources(), R.drawable.airplanes_count_3));

        loadingPlatform = new LoadingPlatform();

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        airplaneSelector = (ImageView)findViewById(R.id.airplaneSelector);
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.airplanes_count_0);
        airplaneSelector.setImageBitmap(icon);
        airplaneSelector.setLayoutParams(params);
        airplaneSelector.setTag(0);


        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);

        RelativeLayout playLayout = (RelativeLayout)findViewById(R.id.playLayout);
        RelativeLayout.LayoutParams gameLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        if (SDK_INT >= android.os.Build.VERSION_CODES.P){
            gameLayoutParams.setMargins(dpToPixels(45),dpToPixels(0),dpToPixels(45),dpToPixels(0));
        } else{
            gameLayoutParams.setMargins(dpToPixels(0),dpToPixels(0),dpToPixels(0),dpToPixels(0));
        }
        playLayout.setLayoutParams(gameLayoutParams);

        mapLayout = (LinearLayout)findViewById(R.id.mapLayout);


        generateMap(context);
        renderMap(context, loadingPlatform, null);

    }

    @Override
    protected void onResume() {
        super.onResume();

        MenuMusicHandler.getInstance().start(this);

    }

    @Override
    protected void onPause() {
        super.onPause();



        String nextActivity = MenuMusicHandler.getInstance().getNextActivity();

        if(!nextActivity.equals(getResources().getString(R.string.MENU_ACTIVITY))) {

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
        int width = airfightDataBase.getLayoutWidth();
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

            mapLayout.addView(linearLayoutRow);
        }
    }

    private void renderDefaultMap(Context context, LoadingPlatform loadingPlatform) {
        int mapRows = Integer.parseInt(context.getString(R.string.MAP_ROWS));
        int mapColumns = Integer.parseInt(context.getString(R.string.MAP_COLUMNS));
        int columnIndex = 1;
        int rowIndex = 1;

        for(columnIndex = 1; columnIndex <= mapColumns; columnIndex++) {
            LinearLayout ll = (LinearLayout)mapLayout.getChildAt(columnIndex - 1);

            for(rowIndex = 1; rowIndex <= mapRows; rowIndex++) {
                TextView tv  = (TextView) ll.getChildAt(rowIndex - 1);
                tv.setBackgroundResource(R.drawable.gray_square_rounded_3dp_layout);
                tv.clearAnimation();
                tv.setText("");

                if(loadingPlatform != null){
                    if(loadingPlatform.isPartOfPlatform(rowIndex, columnIndex)) {
                        tv.setBackgroundResource(R.drawable.orange_square_rounded_3dp_layout);

                    }
                }
            }
        }
    }

    private void renderMap(Context context, LoadingPlatform loadingPlatform, ArrayList<Airplane> airplanes) {
        int mapRows = Integer.parseInt(context.getString(R.string.MAP_ROWS));
        int mapColumns = Integer.parseInt(context.getString(R.string.MAP_COLUMNS));
        int columnIndex = 1;
        int rowIndex = 1;
        int selectedAirplaneId = (int)airplaneSelector.getTag();

        for(columnIndex = 1; columnIndex <= mapColumns; columnIndex++) {
            LinearLayout ll = (LinearLayout)mapLayout.getChildAt(columnIndex - 1);

            for(rowIndex = 1; rowIndex <= mapRows; rowIndex++) {
                TextView tv  = (TextView) ll.getChildAt(rowIndex - 1);

                if(loadingPlatform != null){
                    if(loadingPlatform.isPartOfPlatform(rowIndex, columnIndex)) {
                        tv.setBackgroundResource(R.drawable.orange_square_rounded_3dp_layout);

                    }
                }

                if(airplanes != null) {
                    for(Airplane airplane: airplanes) {
                        if(airplane.isPartOfAirplane(rowIndex, columnIndex)){
                            tv.setBackground(FuselageSingleton.getInstance().getAirplaneFuselageDrawable(fuselageId));

                            if (selectedAirplaneId == airplane.getAirplaneId()) {
                                tv.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in));
                            }
                        }
                    }
                }
            }
        }
    }

    private int dpToPixels(final float dip) {
        final float scale = this.getResources().getDisplayMetrics().density;
        return Math.round(dip * scale + 0.5f);
    }

    public void onClickAddAirplane(View view) {

        boolean isLoadPlatformEmpty = loadingPlatform.isLoadPlatformEmpty(airplanes);
        int newAirplaneId = airplanes.size() + 1;
        int newAirplaneIdIcon = newAirplaneId - 1;
        int airplanesFleet = Integer.parseInt(context.getString(R.string.AIRPLANES_FLEET));

        SoundPoolSingleton.getInstance(context).playOnClickSound();

        if( newAirplaneId <= airplanesFleet) {
            if(isLoadPlatformEmpty) {

                Airplane airplane = new Airplane(newAirplaneId, airplanesIdsIcons.get(newAirplaneIdIcon));

                airplaneSelector.setImageBitmap(airplanesIdsIcons.get(newAirplaneIdIcon));
                airplaneSelector.setTag(airplane.getAirplaneId());

                airplanes.add(airplane);

                renderDefaultMap(context, loadingPlatform);
                renderMap(context, loadingPlatform, airplanes);

            } else {
                Toast.makeText(getBaseContext(), "Move the airplane from the loading platform!", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getBaseContext(), "Maximum three airplanes!", Toast.LENGTH_LONG).show();
        }
    }

    public void onClickLeft(View view) {

        SoundPoolSingleton.getInstance(context).playOnClickSound();

        if(!airplanes.isEmpty()) {

            int tag = (int)airplaneSelector.getTag();
            int airplaneIndex = tag - 1;

            if(airplaneIndex < airplanes.size()) {
                if (airplanes.get(airplaneIndex).checkMovementToLeft(context)) {

                    if(!airplaneCollisionDetector.isLinearCollisionDetected(airplanes, airplanes.get(airplaneIndex), context.getString(R.string.LEFT))) {

                        airplanes.get(airplaneIndex).moveAirplaneToLeft();
                    }
                }

                renderDefaultMap(context, loadingPlatform);
                renderMap(context, loadingPlatform, airplanes);
            } else {
                Toast.makeText(getBaseContext(), "The plane " + String.valueOf(tag) + " is not shown on the map!", Toast.LENGTH_LONG).show();
            }
        }

    }

    public void onClickRight(View view) {

        SoundPoolSingleton.getInstance(context).playOnClickSound();

        if(!airplanes.isEmpty()) {

            int tag = (int)airplaneSelector.getTag();
            int airplaneIndex = tag - 1;

            if(airplaneIndex < airplanes.size()) {
                if (airplanes.get(airplaneIndex).checkMovementToRight(context)) {

                    if(!airplaneCollisionDetector.isLinearCollisionDetected(airplanes, airplanes.get(airplaneIndex), context.getString(R.string.RIGHT))) {

                        airplanes.get(airplaneIndex).moveAirplaneToRight();
                    }
                }

                renderDefaultMap(context, loadingPlatform);
                renderMap(context, loadingPlatform, airplanes);
            } else {
                Toast.makeText(getBaseContext(), "The plane " + String.valueOf(tag) + " is not shown on the map!", Toast.LENGTH_LONG).show();
            }
        }

    }

    public void onClickUp(View view) {

        SoundPoolSingleton.getInstance(context).playOnClickSound();

        if(!airplanes.isEmpty()) {

            int tag = (int)airplaneSelector.getTag();
            int airplaneIndex = tag - 1;

            if(airplaneIndex < airplanes.size()) {
                if (airplanes.get(airplaneIndex).checkMovementUp(context)) {

                    if(!airplaneCollisionDetector.isLinearCollisionDetected(airplanes, airplanes.get(airplaneIndex), context.getString(R.string.UP))) {

                        airplanes.get(airplaneIndex).moveAirplaneUp();
                    }
                }

                renderDefaultMap(context, loadingPlatform);
                renderMap(context, loadingPlatform, airplanes);
            } else {
                Toast.makeText(getBaseContext(), "The plane " + String.valueOf(tag) + " is not shown on the map!", Toast.LENGTH_LONG).show();
            }
        }

    }

    public void onClickDown(View view) {

        SoundPoolSingleton.getInstance(context).playOnClickSound();

        if(!airplanes.isEmpty()) {

            int tag = (int)airplaneSelector.getTag();
            int airplaneIndex = tag - 1;

            if(airplaneIndex < airplanes.size()){
                if(airplanes.get(airplaneIndex).checkMovementDown(context)) {

                    if(!airplaneCollisionDetector.isLinearCollisionDetected(airplanes, airplanes.get(airplaneIndex), context.getString(R.string.DOWN))) {

                        airplanes.get(airplaneIndex).moveAirplaneDown();
                    }
                }

                renderDefaultMap(context, loadingPlatform);
                renderMap(context, loadingPlatform, airplanes);
            } else {
                Toast.makeText(getBaseContext(), "The plane " + String.valueOf(tag) + " is not shown on the map!", Toast.LENGTH_LONG).show();
            }
        }

    }

    public void onClickAirplaneRotation(View view) {

        SoundPoolSingleton.getInstance(context).playOnClickSound();

        if(!airplanes.isEmpty()) {

            int tag = (int)airplaneSelector.getTag();
            int airplaneIndex = tag - 1;

            if(airplaneIndex < airplanes.size()) {

                if(!airplaneCollisionDetector.isRotationCollisionDetected(airplanes, airplanes.get(airplaneIndex))) {

                    airplanes.get(airplaneIndex).rotate();
                }

                renderDefaultMap(context, loadingPlatform);
                renderMap(context, loadingPlatform, airplanes);
            } else {
                Toast.makeText(getBaseContext(), "The plane " + String.valueOf(tag) + " is not shown on the map!", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void onClickAirplaneSelector(View view) {

        int numberOfAirplanes = airplanes.size();
        int tag = (int)airplaneSelector.getTag();
        int newTag = -1;
        Bitmap newIcon = null;


        SoundPoolSingleton.getInstance(context).playOnClickSound();

        if ( tag > 0 ) {

            if (tag == numberOfAirplanes) {
                newTag = 1;
            } else {
                newTag = tag + 1;
            }

            newIcon = airplanesIdsIcons.get(newTag - 1);

            airplaneSelector.setImageBitmap(newIcon);
            airplaneSelector.setTag(newTag);

            renderDefaultMap(context, loadingPlatform);
            renderMap(context, loadingPlatform, airplanes);
        }
    }

    public void onClickFleetStrategyDone(View view) {

        int airplanesFleet = Integer.parseInt(context.getString(R.string.AIRPLANES_FLEET));

        SoundPoolSingleton.getInstance(context).playOnClickSound();

        if(airplanes.size() == airplanesFleet) {

            MyAirplanesSingleton.getInstance().saveMyFleet(airplanes);

            airfightDataBase.updateBattleStatus("BATTLE_ON_GOING");

            MenuMusicHandler.getInstance().setNextActivity(getResources().getString(R.string.ENEMY_FIELD_ACTIVITY));
            Intent intent = new Intent(FleetStrategyActivity.this, EnemyFieldActivity.class);
            startActivity(intent);
            finish();

        } else {
            Toast.makeText(getBaseContext(), "Three airplanes are required for the fleet!", Toast.LENGTH_LONG).show();
        }
    }
}