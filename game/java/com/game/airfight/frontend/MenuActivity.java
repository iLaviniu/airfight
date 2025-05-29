package com.game.airfight.frontend;

import static android.os.Build.VERSION.SDK_INT;
import static android.view.Gravity.CENTER;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
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
import com.game.airfight.backend.BackButtonOff;
import com.game.airfight.backend.EnemySingleton;
import com.game.airfight.backend.FuselageSingleton;
import com.game.airfight.backend.MenuField;
import com.game.airfight.backend.MenuMusicHandler;
import com.game.airfight.backend.MyAirplanesSingleton;
import com.game.airfight.backend.MySingleton;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {

    Context context = null;

    LinearLayout menuSelector = null;

    private ArrayList<MenuField> menuFields = null;

    Airplane menuAirplaneFly = null;

    private AirfightDataBase airfightDataBase = null;

    private int fuselageId = -1;

    private int squareSize = 0;

    private static Handler handlerAirplane = null;
    private static Runnable runnableAirplane = null;

    private static BackButtonOff backButtonOff = null;

    private static String battleStatus = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);

        backButtonOff = new BackButtonOff(this);

        MenuField fleet = new MenuField(2, 2, 2,6);
        fleet.setText(getResources().getString(R.string.FLEET));
        fleet.setActive(true);
        fleet.getMenuElement().moveElementToRight();
        fleet.getMenuElement().moveElementToRight();
        MenuField build = new MenuField(4, 2, 4,6);
        build.setText(getResources().getString(R.string.BUILD));
        build.setActive(false);
        MenuField mode = new MenuField(6, 2, 6,5);
        mode.setText(getResources().getString(R.string.MODE));
        mode.setActive(false);
        MenuField leave = new MenuField(8, 2, 8,6);
        leave.setText(getResources().getString(R.string.LEAVE));
        leave.setActive(false);
        MenuField credits = new MenuField(10, 2, 10,8);
        credits.setText(getResources().getString(R.string.CREDITS));
        credits.setActive(false);

        menuFields = new ArrayList<>();
        menuFields.add(fleet);
        menuFields.add(build);
        menuFields.add(mode);
        menuFields.add(leave);
        menuFields.add(credits);

        menuAirplaneFly = new Airplane();
        menuAirplaneFly.flyReset();

        context = getBaseContext();

        SoundPoolSingleton.getInstance(context).initialize();

        /*in the very first game run*/
        airfightDataBase = new AirfightDataBase(context);
        if(airfightDataBase.getFuselageId() == -1) {
            airfightDataBase.updateFuselageId(1);
        }

        fuselageId = airfightDataBase.getFuselageId();

        battleStatus = airfightDataBase.getBattleStatus();
        if(battleStatus.equals("EMPTY")) {
            /*first time on the game*/
            airfightDataBase.updateBattleStatus("BATTLE_FINISHED");
        }

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);

        RelativeLayout menuLayout = (RelativeLayout)findViewById(R.id.menuLayout);
        RelativeLayout.LayoutParams gameLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        if (SDK_INT >= android.os.Build.VERSION_CODES.P){
            gameLayoutParams.setMargins(dpToPixels(45),dpToPixels(0),dpToPixels(45),dpToPixels(0));
        } else{
            gameLayoutParams.setMargins(dpToPixels(0),dpToPixels(0),dpToPixels(0),dpToPixels(0));
        }
        menuLayout.setLayoutParams(gameLayoutParams);

        menuSelector = (LinearLayout)findViewById(R.id.menuSelector);

        if(MyAirplanesSingleton.getInstance().getMyFleet().isEmpty()) {
            airfightDataBase.updateMyShotStatus("SHOT_NOT_TAKEN");
            airfightDataBase.updateCurrentFleet("EMPTY");
            airfightDataBase.updateBattleStatus("EMPTY");
            airfightDataBase.updateEnemyShotStatus("SHOT_NOT_TAKEN");
        }

        generateMap(context);
        renderDefaultMap(context);
        renderMap(context, menuFields, menuAirplaneFly);

        handlerAirplane = new Handler();
        runnableAirplane = new Runnable() {
            @Override
            public void run() {
                if(menuAirplaneFly.getAirplaneRotation() == 90) {
                    menuAirplaneFly.flyUp();
                } else if (menuAirplaneFly.getAirplaneRotation() == 270) {
                    menuAirplaneFly.flyDown();
                }

                int airplaneFlyTailRow = menuAirplaneFly.getTail().getStart().getRow();
                int airplaneFlyHeadRow = menuAirplaneFly.getHead().getStart().getRow();

                if( (airplaneFlyTailRow == -2) && (airplaneFlyHeadRow == -6) ) {
                    menuAirplaneFly.rotate();
                    menuAirplaneFly.rotate();
                }

                if( (airplaneFlyTailRow == 15) && (airplaneFlyHeadRow == 19) ) {
                    menuAirplaneFly.rotate();
                    menuAirplaneFly.rotate();
                }

                renderDefaultMap(context);
                renderMap(context, menuFields, menuAirplaneFly);

                handlerAirplane.postDelayed(this,1000);
            }
        };
        handlerAirplane.postDelayed(runnableAirplane,1000);
    }

    @Override
    protected void onResume() {
        super.onResume();

        MenuMusicHandler.getInstance().start(this);

    }

    @Override
    protected void onPause() {
        super.onPause();

        handlerAirplane.removeCallbacks(runnableAirplane);
        handlerAirplane.removeCallbacksAndMessages(null);


        String nextActivity = MenuMusicHandler.getInstance().getNextActivity();

        if (nextActivity == null) { /*sometime is null. sometime isn't*/
            nextActivity = getString(R.string.FLEET_STRATEGY_ACTIVITY);
        }

        if( !(nextActivity.equals(getResources().getString(R.string.FLEET_STRATEGY_ACTIVITY))) &&
            !(nextActivity.equals(getResources().getString(R.string.CREDITS_ACTIVITY))) &&
            !(nextActivity.equals(getResources().getString(R.string.BUILD_ACTIVITY))) &&
            !(nextActivity.equals(getResources().getString(R.string.MODE_ACTIVITY)))) {
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

            menuSelector.addView(linearLayoutRow);
        }

        FuselageSingleton.getInstance().generateFuselages(context, squareSize);

        if(airfightDataBase.getBattleStatus().equals("BATTLE_FINISHED")){

            EnemySingleton.getInstance(context).resetSingleton();

            MySingleton.getInstance().resetSingleton();

            MyAirplanesSingleton.getInstance().resetSingleton();
        } else if(airfightDataBase.getBattleStatus().equals("BATTLE_ON_GOING")) {

            findTheLastActivity();
        }
    }

    private void renderDefaultMap(Context context) {
        int mapRows = Integer.parseInt(context.getString(R.string.MAP_ROWS));
        int mapColumns = Integer.parseInt(context.getString(R.string.MAP_COLUMNS));
        int columnIndex = 1;
        int rowIndex = 1;

        for(columnIndex = 1; columnIndex <= mapColumns; columnIndex++) {
            LinearLayout ll = (LinearLayout)menuSelector.getChildAt(columnIndex - 1);

            for(rowIndex = 1; rowIndex <= mapRows; rowIndex++) {
                TextView tv  = (TextView) ll.getChildAt(rowIndex - 1);
                tv.setBackgroundResource(R.drawable.gray_square_rounded_3dp_layout);
                tv.clearAnimation();
                tv.setText("");
            }
        }
    }

    private void renderMap(Context context, ArrayList<MenuField> menuFields, Airplane menuAirplaneFly) {
        int mapRows = Integer.parseInt(context.getString(R.string.MAP_ROWS));
        int mapColumns = Integer.parseInt(context.getString(R.string.MAP_COLUMNS));
        int columnIndex = 1;
        int rowIndex = 1;

        for(columnIndex = 1; columnIndex <= mapColumns; columnIndex++) {
            LinearLayout ll = (LinearLayout)menuSelector.getChildAt(columnIndex - 1);

            for(rowIndex = 1; rowIndex <= mapRows; rowIndex++) {
                TextView tv = (TextView) ll.getChildAt(rowIndex - 1);

                if(menuAirplaneFly.isPartOfAirplane(rowIndex, columnIndex)) {
                    tv.setBackground(FuselageSingleton.getInstance().getAirplaneFuselageDrawable(fuselageId));
                }

                for (MenuField menuField : menuFields) {

                    if (menuField.isPartOfMenuField(rowIndex, columnIndex)) {

                        String menuFieldText = menuField.getText();
                        int startColumn = menuField.getMenuElement().getStart().getColumn();
                        String letter = String.valueOf(menuFieldText.charAt(columnIndex - startColumn));

                        tv.setText(letter);
                        tv.setGravity(CENTER);
                        tv.setTypeface(tv.getTypeface(), Typeface.BOLD);


                        if(menuField.isActive()) {
                            tv.setBackgroundResource(R.drawable.blue_square_rounded_3dp_layout);
                            tv.setTextColor(Color.parseColor("#212121"));
                        } else {
                            tv.setBackgroundResource(R.drawable.white_square_rounded_3dp_layout);
                            tv.setTextColor(Color.parseColor("#4b4b4b"));
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

    public void onClickUp(View view) {

        SoundPoolSingleton.getInstance(context).playOnClickSound();

        int activeIndex = 0;
        int menuFieldIndex = 0;
        int menuFieldNextIndex = 0;

        for(MenuField menuField: menuFields) {
            if(menuField.isActive()) {
                activeIndex = menuFieldIndex;
                menuField.setActive(false);
                menuField.getMenuElement().moveElementToLeft();
                menuField.getMenuElement().moveElementToLeft();
            }
            menuFieldIndex++;
        }

        if(activeIndex == 0) {
            menuFieldNextIndex = menuFields.size() - 1;
        } else {
            menuFieldNextIndex = activeIndex - 1;
        }

        menuFieldIndex = 0;
        for(MenuField menuField: menuFields) {
            if(menuFieldIndex == menuFieldNextIndex) {
                menuField.setActive(true);
                menuField.getMenuElement().moveElementToRight();
                menuField.getMenuElement().moveElementToRight();
            }
            menuFieldIndex++;
        }

        renderDefaultMap(context);
        renderMap(context, menuFields, menuAirplaneFly);
    }

    public void onClickDown(View view) {

        SoundPoolSingleton.getInstance(context).playOnClickSound();

        int activeIndex = 0;
        int menuFieldIndex = 0;
        int menuFieldNextIndex = 0;

        for(MenuField menuField: menuFields) {
            if(menuField.isActive()) {
                activeIndex = menuFieldIndex;
                menuField.setActive(false);
                menuField.getMenuElement().moveElementToLeft();
                menuField.getMenuElement().moveElementToLeft();
            }
            menuFieldIndex++;
        }

        if(activeIndex == (menuFields.size() - 1) ) {
            menuFieldNextIndex = 0;
        } else {
            menuFieldNextIndex = activeIndex + 1;
        }

        menuFieldIndex = 0;
        for(MenuField menuField: menuFields) {
            if(menuFieldIndex == menuFieldNextIndex) {
                menuField.setActive(true);
                menuField.getMenuElement().moveElementToRight();
                menuField.getMenuElement().moveElementToRight();
            }
            menuFieldIndex++;
        }

        renderDefaultMap(context);
        renderMap(context, menuFields, menuAirplaneFly);
    }

    public void onClickMenuSelected(View view) {

        SoundPoolSingleton.getInstance(context).playOnClickSound();

        for(MenuField menuField: menuFields) {
            if(menuField.isActive()) {
                String menuFieldText = menuField.getText();

                if(getResources().getString(R.string.FLEET).equals(menuFieldText)) {
                    MenuMusicHandler.getInstance().setNextActivity(getResources().getString(R.string.FLEET_STRATEGY_ACTIVITY));
                    Intent intent = new Intent(MenuActivity.this, FleetStrategyActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                } else if(getResources().getString(R.string.LEAVE).equals(menuFieldText)) {
                    this.finishAffinity();
                    System.exit(0);
                } else if (getResources().getString(R.string.CREDITS).equals(menuFieldText)) {
                    MenuMusicHandler.getInstance().setNextActivity(getResources().getString(R.string.CREDITS_ACTIVITY));
                    Intent intent = new Intent(MenuActivity.this, CreditsActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                } else if (getResources().getString(R.string.BUILD).equals(menuFieldText)) {
                    MenuMusicHandler.getInstance().setNextActivity(getResources().getString(R.string.BUILD_ACTIVITY));
                    Intent intent = new Intent(MenuActivity.this, BuildActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                } else if (getResources().getString(R.string.MODE).equals(menuFieldText)) {
                    MenuMusicHandler.getInstance().setNextActivity(getResources().getString(R.string.MODE_ACTIVITY));
                    Intent intent = new Intent(MenuActivity.this, DifficultyActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                }
            }
        }
    }

    private void findTheLastActivity() {

        String currentFleet = airfightDataBase.getCurrentFleet();

        if(currentFleet.equals("MY_FLEET")) {
            MenuMusicHandler.getInstance().setNextActivity(getResources().getString(R.string.MY_FIELD_ACTIVITY));
            Intent intent = new Intent(MenuActivity.this, MyFieldActivity.class);
            startActivity(intent);
            finish();
        } else if(currentFleet.equals("ENEMY_FLEET")) {
            MenuMusicHandler.getInstance().setNextActivity(getResources().getString(R.string.ENEMY_FIELD_ACTIVITY));
            Intent intent = new Intent(MenuActivity.this, EnemyFieldActivity.class);
            startActivity(intent);
            finish();

        } else {
            /*continue with main activity*/
        }
    }
}