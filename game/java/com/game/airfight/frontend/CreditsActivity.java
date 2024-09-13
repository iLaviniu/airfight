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

import com.game.profile.airfight.R;
import com.game.airfight.backend.BackButtonOff;
import com.game.airfight.backend.MenuField;
import com.game.airfight.backend.MenuMusicHandler;

import java.util.ArrayList;

public class CreditsActivity extends AppCompatActivity {

    Context context = null;

    LinearLayout creditsTexts = null;

    private SoundPool soundPool = null;

    private int onClickSound = 0;

    private ArrayList<MenuField> credits = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_credits);

        new BackButtonOff(this);

        MenuField credit1 = new MenuField(2, 2, 2,11);
        credit1.setText(getResources().getString(R.string.CREDIT1));
        credit1.setActive(false);

        MenuField credit2 = new MenuField(4, 2, 4,8);
        credit2.setText(getResources().getString(R.string.CREDIT2));
        credit2.setActive(false);

        MenuField credit3 = new MenuField(6, 2, 6,10);
        credit3.setText(getResources().getString(R.string.CREDIT3));
        credit3.setActive(false);

        MenuField credit4 = new MenuField(8, 2, 8,8);
        credit4.setText(getResources().getString(R.string.CREDIT4));
        credit4.setActive(false);

        MenuField credit5 = new MenuField(10, 2, 10,7);
        credit5.setText(getResources().getString(R.string.CREDIT5));
        credit5.setActive(false);


        credits = new ArrayList<>();
        credits.add(credit1);
        credits.add(credit2);
        credits.add(credit3);
        credits.add(credit4);
        credits.add(credit5);

        context = getBaseContext();

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);

        RelativeLayout creditsLayout = (RelativeLayout)findViewById(R.id.creditsLayout);
        RelativeLayout.LayoutParams gameLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        if (SDK_INT >= android.os.Build.VERSION_CODES.P){
            gameLayoutParams.setMargins(dpToPixels(45),dpToPixels(0),dpToPixels(45),dpToPixels(0));
        } else{
            gameLayoutParams.setMargins(dpToPixels(0),dpToPixels(0),dpToPixels(0),dpToPixels(0));
        }
        creditsLayout.setLayoutParams(gameLayoutParams);

        creditsTexts = (LinearLayout)findViewById(R.id.creditsTexts);
        creditsTexts.post(new Runnable()
        {

            @Override
            public void run()
            {
                generateMap(context);
                renderDefaultMap(context);
                renderMap(context, credits);
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

        soundPool = null;

        onClickSound = 0;

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
        int width = creditsTexts.getMeasuredWidth();
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

            creditsTexts.addView(linearLayoutRow);
        }
    }

    private void renderDefaultMap(Context context) {
        int mapRows = Integer.parseInt(context.getString(R.string.MAP_ROWS));
        int mapColumns = Integer.parseInt(context.getString(R.string.MAP_COLUMNS));
        int columnIndex = 1;
        int rowIndex = 1;

        for(columnIndex = 1; columnIndex <= mapColumns; columnIndex++) {
            LinearLayout ll = (LinearLayout)creditsTexts.getChildAt(columnIndex - 1);

            for(rowIndex = 1; rowIndex <= mapRows; rowIndex++) {
                TextView tv  = (TextView) ll.getChildAt(rowIndex - 1);
                tv.setBackgroundResource(R.drawable.gray_square_rounded_3dp_layout);
                tv.clearAnimation();
                tv.setText("");
            }
        }
    }

    private void renderMap(Context context, ArrayList<MenuField> credits) {
        int mapRows = Integer.parseInt(context.getString(R.string.MAP_ROWS));
        int mapColumns = Integer.parseInt(context.getString(R.string.MAP_COLUMNS));
        int columnIndex = 1;
        int rowIndex = 1;

        for(columnIndex = 1; columnIndex <= mapColumns; columnIndex++) {
            LinearLayout ll = (LinearLayout)creditsTexts.getChildAt(columnIndex - 1);

            for(rowIndex = 1; rowIndex <= mapRows; rowIndex++) {
                TextView tv = (TextView) ll.getChildAt(rowIndex - 1);


                for (MenuField menuField : credits) {

                    if (menuField.isPartOfMenuField(rowIndex, columnIndex)) {

                        String menuFieldText = menuField.getText();
                        int startColumn = menuField.getMenuElement().getStart().getColumn();
                        String letter = String.valueOf(menuFieldText.charAt(columnIndex - startColumn));

                        tv.setText(letter);
                        tv.setGravity(CENTER);
                        tv.setTextColor(Color.parseColor("#EFC383"));
                        tv.setBackgroundResource(R.drawable.white_square_rounded_3dp_layout);

                    }
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
        Intent intent = new Intent(CreditsActivity.this, MenuActivity.class);
        startActivity(intent);
    }
}