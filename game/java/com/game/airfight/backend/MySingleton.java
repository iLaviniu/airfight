package com.game.airfight.backend;

import java.util.ArrayList;

public class MySingleton {

    private static MySingleton instance = null;

    private static ArrayList<Coordinate> enemyShots = null;

    private static ArrayList<Coordinate> allEnemyShots = null;

    private int myHeadsNumber = 0;


    private MySingleton() {

    }

    public static MySingleton getInstance() {

        if(instance == null) {
            instance = new MySingleton();

            enemyShots = new ArrayList<>();
            allEnemyShots = new ArrayList<>();
        }
        return instance;
    }

    public ArrayList<Coordinate> getEnemyShots() {
        return enemyShots;
    }

    public ArrayList<Coordinate> getAllEnemyShots() {
        return allEnemyShots;
    }

    public void increaseMyHeadsNumber() {
        myHeadsNumber++;
    }

    public int getMyHeadsNumber() {
        return myHeadsNumber;
    }

    public void resetSingleton() {

        instance = null;
        enemyShots = null;
        allEnemyShots = null;
        myHeadsNumber = 0;
    }
}
