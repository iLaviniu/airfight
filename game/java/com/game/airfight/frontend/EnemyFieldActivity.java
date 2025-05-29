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
import android.os.Handler;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.game.airfight.backend.AirfightDataBase;
import com.game.airfight.backend.SoundPoolSingleton;
import com.game.profile.airfight.R;
import com.game.airfight.backend.Airplane;
import com.game.airfight.backend.BackButtonOff;
import com.game.airfight.backend.Coordinate;
import com.game.airfight.backend.EnemySingleton;
import com.game.airfight.backend.MenuMusicHandler;

import java.util.ArrayList;

public class EnemyFieldActivity extends AppCompatActivity implements View.OnClickListener{

    Context context = null;

    LinearLayout enemyFieldPlatform = null;


    private Coordinate currentTarget = new Coordinate(0, 0);

    ImageView airplaneHeads = null;

    private boolean isShotTaken = false;

    private AirfightDataBase airfightDataBase = null;


    private static Handler handler = null;
    private static Runnable runnableWait = null;

    private static Handler handler2 = null;
    private static Runnable runnableWait2 = null;

    private static BackButtonOff backButtonOff = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_enemy_field);

            backButtonOff = new BackButtonOff(this);

            context = getBaseContext();

            airfightDataBase = new AirfightDataBase(context);

            airfightDataBase.updateCurrentFleet("ENEMY_FLEET");



            airfightDataBase.updateEnemyShotStatus("SHOT_NOT_TAKEN");

            EnemySingleton.getInstance(context);


            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE);

            RelativeLayout enemyFieldLayout = (RelativeLayout) findViewById(R.id.enemyFieldLayout);
            RelativeLayout.LayoutParams gameLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            if (SDK_INT >= android.os.Build.VERSION_CODES.P) {
                gameLayoutParams.setMargins(dpToPixels(45), dpToPixels(0), dpToPixels(45), dpToPixels(0));
            } else {
                gameLayoutParams.setMargins(dpToPixels(0), dpToPixels(0), dpToPixels(0), dpToPixels(0));
            }
            enemyFieldLayout.setLayoutParams(gameLayoutParams);

            LinearLayout.LayoutParams paramsH = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            airplaneHeads = (ImageView) findViewById(R.id.airplaneHeads);
            Bitmap icon = null;

            if (EnemySingleton.getInstance(context).getEnemyHeadsNumber() == 1) {
                icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.airplanes_count_1);
                airplaneHeads.setImageBitmap(icon);
            } else if (EnemySingleton.getInstance(context).getEnemyHeadsNumber() == 2) {
                icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.airplanes_count_2);
                airplaneHeads.setImageBitmap(icon);
            } else if (EnemySingleton.getInstance(context).getEnemyHeadsNumber() == 3) {
                icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.airplanes_count_3);
                airplaneHeads.setImageBitmap(icon);
            } else if (EnemySingleton.getInstance(context).getEnemyHeadsNumber() == 0) {
                icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.airplanes_count_0);
                airplaneHeads.setImageBitmap(icon);
            }

            airplaneHeads.setImageBitmap(icon);
            airplaneHeads.setLayoutParams(paramsH);


            enemyFieldPlatform = (LinearLayout) findViewById(R.id.enemyFieldPlatform);


            generateMap(context);
            renderDefaultMap(context);
            renderMap(context, EnemySingleton.getInstance(context).getMyShots(), currentTarget, false);

        }  catch (Exception e) {

            System.out.println("Error: " + e.toString());
        }
        finally {
            System.out.println("Program continues after handling the exception.");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        try{
            if(airfightDataBase.getMyShotStatus().equals("SHOT_TAKEN")){

                isShotTaken = true;

                handler2 = new Handler();
                runnableWait2 = new Runnable() {
                    @Override
                    public void run() {
                        if (EnemySingleton.getInstance(context).getEnemyHeadsNumber() == 3) {

                            SoundPoolSingleton.getInstance(context).playSuccessSound();

                            MenuMusicHandler.getInstance().setNextActivity(getResources().getString(R.string.RESULTS_ACTIVITY));
                            Intent intent = new Intent(EnemyFieldActivity.this, ResultsActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            MenuMusicHandler.getInstance().setNextActivity(getResources().getString(R.string.MY_FIELD_ACTIVITY));
                            Intent intent = new Intent(EnemyFieldActivity.this, MyFieldActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                };
                handler2.postDelayed(runnableWait2, 2000);

            } else {
                isShotTaken = false;
            }
        }  catch (Exception e) {

            System.out.println("Error: " + e.toString());
        }
        finally {
            System.out.println("Program continues after handling the exception.");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        try{

            handler.removeCallbacks(runnableWait);
            handler.removeCallbacksAndMessages(null);

            handler2.removeCallbacks(runnableWait2);
            handler2.removeCallbacksAndMessages(null);
        }  catch (Exception e) {

            System.out.println("Error: " + e.toString());
        }
        finally {
            System.out.println("Program continues after handling the exception.");
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        try{

            Configuration old = newBase.getResources().getConfiguration();

            final Configuration override = new Configuration(newBase.getResources().getConfiguration());
            override.fontScale = 1.0f;
            newBase = newBase.createConfigurationContext(override);

            super.attachBaseContext(newBase);
        }  catch (Exception e) {

            System.out.println("Error: " + e.toString());
        }
        finally {
            System.out.println("Program continues after handling the exception.");
        }
    }

    private void generateMap(Context context) {
        int mapRows = Integer.parseInt(context.getString(R.string.MAP_ROWS));
        int mapColumns = Integer.parseInt(context.getString(R.string.MAP_COLUMNS));
        int width = airfightDataBase.getLayoutWidth();
        int height = (int)(0.5 * width);
        int square = (int)(((height - 2 * 12 ) / 12));
        int columnIndex = 0;
        int rowIndex = 0;

        try{

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
        }  catch (Exception e) {

            System.out.println("Error: " + e.toString());
        }
        finally {
            System.out.println("Program continues after handling the exception.");
        }
    }

    private void renderDefaultMap(Context context) {
        int mapRows = Integer.parseInt(context.getString(R.string.MAP_ROWS));
        int mapColumns = Integer.parseInt(context.getString(R.string.MAP_COLUMNS));
        int columnIndex = 1;
        int rowIndex = 1;

        try{

            for(columnIndex = 1; columnIndex <= mapColumns; columnIndex++) {
                LinearLayout ll = (LinearLayout)enemyFieldPlatform.getChildAt(columnIndex - 1);

                for(rowIndex = 1; rowIndex <= mapRows; rowIndex++) {
                    TextView tv  = (TextView) ll.getChildAt(rowIndex - 1);
                    tv.setBackgroundResource(R.drawable.gray_square_rounded_3dp_layout);
                    tv.clearAnimation();
                    tv.setText("");
                }
            }
        }  catch (Exception e) {

            System.out.println("Error: " + e.toString());
        }
        finally {
            System.out.println("Program continues after handling the exception.");
        }
    }

    private void renderMap(Context context, ArrayList<Coordinate> myShots, Coordinate currentShot, boolean isCurrentShotPartOfFleet) {
        int mapRows = Integer.parseInt(context.getString(R.string.MAP_ROWS));
        int mapColumns = Integer.parseInt(context.getString(R.string.MAP_COLUMNS));
        int columnIndex = 1;
        int rowIndex = 1;

        try{

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
        }  catch (Exception e) {

            System.out.println("Error: " + e.toString());
        }
        finally {
            System.out.println("Program continues after handling the exception.");
        }
    }

    private int dpToPixels(final float dip) {
        final float scale = this.getResources().getDisplayMetrics().density;
        return Math.round(dip * scale + 0.5f);
    }

    @Override
    public void onClick(View view) {

        try{
            String selectedTag = String.valueOf(view.getTag());

            if(selectedTag.contains("map:")) {

                renderDefaultMap(context);

                view.setBackgroundResource(R.drawable.target_selected);

                SoundPoolSingleton.getInstance(context).playOnClickSound();

                String[] array = selectedTag.split(":");
                int selectedRow = Integer.parseInt(array[1]);
                int selectedColumn = Integer.parseInt(array[2]);

                currentTarget.setRow(selectedRow);
                currentTarget.setColumn(selectedColumn);
            }

            renderMap(context, EnemySingleton.getInstance(context).getMyShots(), currentTarget, false);
        }  catch (Exception e) {

            System.out.println("Error: " + e.toString());
        }
        finally {
            System.out.println("Program continues after handling the exception.");
        }
    }

    public void onClickFire(View view) {

        boolean isCurrentShotPartOfFleet = false;

        try{
            if(!isShotTaken) {

                airfightDataBase.updateMyShotStatus("SHOT_TAKEN");

                if (currentTarget.isTargetOnMap(context)) {



                    SoundPoolSingleton.getInstance(context).playFireSound();

                    if (isShootPartOfEnemyFleet(EnemySingleton.getInstance(context).getEnemyFleet().getAirplanes(), currentTarget)) {

                        isCurrentShotPartOfFleet = true;

                        if (!isShotAlreadyTaken(EnemySingleton.getInstance(context).getMyShots(), currentTarget)) {
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

                    /*deactivate shot button*/
                    isShotTaken = true;



                    handler = new Handler();
                    runnableWait = new Runnable() {
                        @Override
                        public void run() {
                            if (EnemySingleton.getInstance(context).getEnemyHeadsNumber() == 3) {

                                SoundPoolSingleton.getInstance(context).playSuccessSound();

                                MenuMusicHandler.getInstance().setNextActivity(getResources().getString(R.string.RESULTS_ACTIVITY));
                                Intent intent = new Intent(EnemyFieldActivity.this, ResultsActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                MenuMusicHandler.getInstance().setNextActivity(getResources().getString(R.string.MY_FIELD_ACTIVITY));
                                Intent intent = new Intent(EnemyFieldActivity.this, MyFieldActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    };
                    handler.postDelayed(runnableWait, 2000);

                } else {
                    Toast.makeText(getBaseContext(), "Choose the target!", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getBaseContext(), "The shot was already taken!", Toast.LENGTH_LONG).show();
            }
        }  catch (Exception e) {

            System.out.println("Error: " + e.toString());
        }
            finally {
            System.out.println("Program continues after handling the exception.");
        }
    }

    private boolean isShootPartOfEnemyFleet(ArrayList<Airplane> enemyAirplanes, Coordinate myShot) {
        boolean isPart = false;

        try{
            for(Airplane enemyAirplane: enemyAirplanes) {

                if(enemyAirplane.isPartOfAirplane(myShot.getRow(), myShot.getColumn())) {
                    isPart = true;
                }
            }
        }  catch (Exception e) {

            System.out.println("Error: " + e.toString());
        }
        finally {
            System.out.println("Program continues after handling the exception.");
        }

        return isPart;
    }

    private boolean isShotAlreadyTaken(ArrayList<Coordinate> myShots, Coordinate newShot) {

        boolean alreadyTaken = false;

        try{
            for(Coordinate myShot: myShots) {

                if ((myShot.getRow() == newShot.getRow()) && (myShot.getColumn() == newShot.getColumn())) {
                    alreadyTaken = true;
                    break;
                }
            }
        }  catch (Exception e) {

            System.out.println("Error: " + e.toString());
        }
        finally {
            System.out.println("Program continues after handling the exception.");
        }

        return alreadyTaken;
    }

    private void isHeadShot(ArrayList<Airplane> enemyAirplanes, Coordinate myShot) {

        try{

            for(Airplane enemyAirplane: enemyAirplanes) {

                if(enemyAirplane.isPartOfAirplane(myShot.getRow(), myShot.getColumn())) {

                    if(enemyAirplane.isAirplaneHead(myShot.getRow(), myShot.getColumn())) {

                        EnemySingleton.getInstance(context).increaseEnemyHeadsNumber();
                    }
                }
            }

        }  catch (Exception e) {

            System.out.println("Error: " + e.toString());
        }
        finally {
            System.out.println("Program continues after handling the exception.");
        }
    }

    private void updateHeadsCardNumber() {

        try{

            if(EnemySingleton.getInstance(context).getEnemyHeadsNumber() == 1) {
                Bitmap icon1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.airplanes_count_1);
                airplaneHeads.setImageBitmap(icon1);
            } else if (EnemySingleton.getInstance(context).getEnemyHeadsNumber() == 2) {
                Bitmap icon2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.airplanes_count_2);
                airplaneHeads.setImageBitmap(icon2);
            } else if (EnemySingleton.getInstance(context).getEnemyHeadsNumber() == 3) {
                Bitmap icon3 = BitmapFactory.decodeResource(context.getResources(), R.drawable.airplanes_count_3);
                airplaneHeads.setImageBitmap(icon3);
            }
        }  catch (Exception e) {

            System.out.println("Error: " + e.toString());
        }
        finally {
            System.out.println("Program continues after handling the exception.");
        }
    }
}