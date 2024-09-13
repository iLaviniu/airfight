package com.game.airfight.backend;

import android.content.Context;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;

import com.game.profile.airfight.R;

import java.util.ArrayList;

public class FuselageSingleton {

    private static FuselageSingleton instance = null;

    public ArrayList<FuselagePart> getFuselageParts() {
        return fuselageParts;
    }

    private ArrayList<FuselagePart> fuselageParts = null;

    private FuselageSingleton() {


    }

    public static FuselageSingleton getInstance() {

        if(instance == null) {
            instance = new FuselageSingleton();
        }
        return instance;
    }

    public void generateFuselages(Context context, int squareSize) {

        AirfightDataBase airfightDataBase = new AirfightDataBase(context);

        FuselagePart fuselagePart1 = new FuselagePart(context, squareSize,2, 2, R.drawable.fuselage_1);
        FuselagePart fuselagePart2 = new FuselagePart(context, squareSize,4, 2, R.drawable.fuselage_2);
        FuselagePart fuselagePart3 = new FuselagePart(context, squareSize,2, 4, R.drawable.fuselage_3);
        FuselagePart fuselagePart4 = new FuselagePart(context, squareSize,4, 4, R.drawable.fuselage_4);

        FuselagePart fuselagePart5 = new FuselagePart(context, squareSize,9, 2, R.drawable.fuselage_5);
        FuselagePart fuselagePart6 = new FuselagePart(context, squareSize,11, 2, R.drawable.fuselage_6);
        FuselagePart fuselagePart7 = new FuselagePart(context, squareSize,9, 4, R.drawable.fuselage_7);
        FuselagePart fuselagePart8 = new FuselagePart(context, squareSize,11, 4, R.drawable.fuselage_8);

        FuselagePart fuselagePart9 = new FuselagePart(context, squareSize,2, 7, R.drawable.fuselage_9);
        FuselagePart fuselagePart10 = new FuselagePart(context, squareSize,4, 7, R.drawable.fuselage_10);
        FuselagePart fuselagePart11 = new FuselagePart(context, squareSize,2, 9, R.drawable.fuselage_11);
        FuselagePart fuselagePart12 = new FuselagePart(context, squareSize,4, 9, R.drawable.fuselage_12);

        FuselagePart fuselagePart13 = new FuselagePart(context, squareSize,9, 7, R.drawable.fuselage_13);
        FuselagePart fuselagePart14 = new FuselagePart(context, squareSize,11, 7, R.drawable.fuselage_14);
        FuselagePart fuselagePart15 = new FuselagePart(context, squareSize,9, 9, R.drawable.fuselage_15);
        FuselagePart fuselagePart16 = new FuselagePart(context, squareSize,11, 9, R.drawable.fuselage_16);

        fuselageParts = new ArrayList<>();
        fuselageParts.add(fuselagePart1);
        fuselageParts.add(fuselagePart2);
        fuselageParts.add(fuselagePart3);
        fuselageParts.add(fuselagePart4);
        fuselageParts.add(fuselagePart5);
        fuselageParts.add(fuselagePart6);
        fuselageParts.add(fuselagePart7);
        fuselageParts.add(fuselagePart8);
        fuselageParts.add(fuselagePart9);
        fuselageParts.add(fuselagePart10);
        fuselageParts.add(fuselagePart11);
        fuselageParts.add(fuselagePart12);
        fuselageParts.add(fuselagePart13);
        fuselageParts.add(fuselagePart14);
        fuselageParts.add(fuselagePart15);
        fuselageParts.add(fuselagePart16);

        int activeFuselageId = airfightDataBase.getFuselageId();
        int fuselagePartIndex = 0;
        for(FuselagePart fuselagePart: fuselageParts) {
            if( (activeFuselageId - 1) == fuselagePartIndex ) {
                fuselagePart.setActive(true);
            }
            fuselagePartIndex++;
        }
    }


    public RoundedBitmapDrawable getAirplaneFuselageDrawable(int fuselageId) {

        FuselagePart fp = fuselageParts.get(fuselageId - 1);
        return fp.getRoundedBitmapDrawable();
    }
}
