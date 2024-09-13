package com.game.airfight.backend;

import android.content.Context;

import java.util.ArrayList;

public class EnemySingleton {

    private static EnemySingleton instance = null;

    private static ArrayList<Coordinate> myShots = null;

    private static EnemyFleet enemyFleet = null;

    private int airplaneHeadsNumber = 0;


    private EnemySingleton() {

    }

    public static EnemySingleton getInstance(Context context) {

        if(instance == null) {
            instance = new EnemySingleton();

            enemyFleet = new EnemyFleet(context);

            myShots = new ArrayList<>();
        }
        return instance;
    }

    public ArrayList<Coordinate> getMyShots() {
        return myShots;
    }

    public EnemyFleet getEnemyFleet() {
        return enemyFleet;
    }

    public void increaseEnemyHeadsNumber() {
        airplaneHeadsNumber++;
    }

    public int getEnemyHeadsNumber() {
        return airplaneHeadsNumber;
    }

    public void resetSingleton() {

        instance = null;
        myShots = null;
        enemyFleet = null;
        airplaneHeadsNumber = 0;
    }
}
