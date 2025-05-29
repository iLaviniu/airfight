package com.game.airfight.frontend;

import static android.os.Build.VERSION.SDK_INT;
import static android.view.Gravity.CENTER;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.game.airfight.backend.SoundPoolSingleton;
import com.game.profile.airfight.R;
import com.game.airfight.backend.AirfightDataBase;
import com.game.airfight.backend.BackButtonOff;
import com.game.airfight.backend.MenuField;
import com.game.airfight.backend.MenuMusicHandler;

import java.util.ArrayList;

public class DifficultyActivity extends AppCompatActivity {

    Context context = null;

    LinearLayout menuSelector = null;

    private ArrayList<MenuField> menuFields = null;

    private AirfightDataBase airfightDataBase = null;


    private static BackButtonOff backButtonOff = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mode);

        backButtonOff = new BackButtonOff(this);

        context = getBaseContext();

        airfightDataBase = new AirfightDataBase(context);

        MenuField easy = new MenuField(2, 2, 2,5);
        easy.setText(getResources().getString(R.string.EASY));
        easy.setActive(false);
        MenuField medium = new MenuField(4, 2, 4,7);
        medium.setText(getResources().getString(R.string.MEDIUM));
        medium.setActive(false);
        MenuField hard = new MenuField(6, 2, 6,5);
        hard.setText(getResources().getString(R.string.HARD));
        hard.setActive(false);

        if(airfightDataBase.getGameDifficulty().equals(context.getString(R.string.EASY))) {
            easy.setActive(true);
            easy.getMenuElement().moveElementToRight();
            easy.getMenuElement().moveElementToRight();
        } else if (airfightDataBase.getGameDifficulty().equals(context.getString(R.string.MEDIUM))) {
            medium.setActive(true);
            medium.getMenuElement().moveElementToRight();
            medium.getMenuElement().moveElementToRight();
        } else if (airfightDataBase.getGameDifficulty().equals(context.getString(R.string.HARD))) {
            hard.setActive(true);
            hard.getMenuElement().moveElementToRight();
            hard.getMenuElement().moveElementToRight();
        }

        menuFields = new ArrayList<>();
        menuFields.add(easy);
        menuFields.add(medium);
        menuFields.add(hard);

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

        generateMap(context);
        renderDefaultMap(context);
        renderMap(context, menuFields);

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

            menuSelector.addView(linearLayoutRow);
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

    private void renderMap(Context context, ArrayList<MenuField> menuFields) {
        int mapRows = Integer.parseInt(context.getString(R.string.MAP_ROWS));
        int mapColumns = Integer.parseInt(context.getString(R.string.MAP_COLUMNS));
        int columnIndex = 1;
        int rowIndex = 1;

        for(columnIndex = 1; columnIndex <= mapColumns; columnIndex++) {
            LinearLayout ll = (LinearLayout)menuSelector.getChildAt(columnIndex - 1);

            for(rowIndex = 1; rowIndex <= mapRows; rowIndex++) {
                TextView tv = (TextView) ll.getChildAt(rowIndex - 1);

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
        renderMap(context, menuFields);
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
        renderMap(context, menuFields);
    }

    public void onClickModeSelected(View view) {

        SoundPoolSingleton.getInstance(context).playOnClickSound();

        for(MenuField menuField: menuFields) {
            if(menuField.isActive()) {
                String menuFieldText = menuField.getText();

                if(getResources().getString(R.string.EASY).equals(menuFieldText)) {
                    airfightDataBase.updateGameDifficulty(getResources().getString(R.string.EASY));
                    break;
                } else if(getResources().getString(R.string.MEDIUM).equals(menuFieldText)) {
                    airfightDataBase.updateGameDifficulty(getResources().getString(R.string.MEDIUM));
                    break;
                } else if (getResources().getString(R.string.HARD).equals(menuFieldText)) {
                    airfightDataBase.updateGameDifficulty(getResources().getString(R.string.HARD));
                    break;
                }
            }
        }

        MenuMusicHandler.getInstance().setNextActivity(getResources().getString(R.string.MENU_ACTIVITY));
        Intent intent = new Intent(DifficultyActivity.this, MenuActivity.class);
        startActivity(intent);
        finish();
    }
}