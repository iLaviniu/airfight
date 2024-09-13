package com.game.airfight.backend;


import android.content.Context;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;

import com.game.profile.airfight.R;

import java.util.ArrayList;
import java.util.Random;

public class EnemyFleet {

    private int minRow = 3;
    private int maxRow = 10;

    private int minColumn = 3;
    private int maxColumn = 22;

    public ArrayList<Airplane> getAirplanes() {
        return airplanes;
    }

    ArrayList<Airplane> airplanes = new ArrayList<>();

    Random rand = new Random();

    private AirplaneCollisionDetector airplaneCollisionDetector = null;


    public RoundedBitmapDrawable getFuselageDrawable() {
        return fuselageDrawable;
    }

    private RoundedBitmapDrawable fuselageDrawable = null;

    public EnemyFleet(Context context) {

        int index = 0;
        int newAirplaneId = 1;

        int randomFuselageId_1_16 = rand.nextInt(16 - 1 + 1) + 1;

        fuselageDrawable = FuselageSingleton.getInstance().getAirplaneFuselageDrawable(randomFuselageId_1_16);


        airplaneCollisionDetector = new AirplaneCollisionDetector(context);

        /*airplane 1*/
        Airplane airplane1 = new Airplane(newAirplaneId);

        int randomRow = rand.nextInt(maxRow - minRow + 1) + minRow;
        int randomColumn = rand.nextInt(maxColumn - minColumn + 1) + minColumn;
        int randomRotation = rand.nextInt(6);

        airplane1.moveAirplaneToCoordinate(new Coordinate(randomRow, randomColumn));

        for(index = 0; index < randomRotation; index++) {
            airplane1.rotate();
        }

        airplanes.add(airplane1);


        /*airplane 2*/
        newAirplaneId = 2;
        Airplane airplane2 = new Airplane(newAirplaneId);

        randomRow = rand.nextInt(maxRow - minRow + 1) + minRow;
        randomColumn = rand.nextInt(maxColumn - minColumn + 1) + minColumn;

        while(airplaneCollisionDetector.isSuperpositionDetected(airplanes, airplane2, randomRow, randomColumn)){

            randomRow = rand.nextInt(maxRow - minRow + 1) + minRow;
            randomColumn = rand.nextInt(maxColumn - minColumn + 1) + minColumn;

        };
        airplane2.moveAirplaneToCoordinate(new Coordinate(randomRow, randomColumn));


        randomRotation = rand.nextInt(6);

        for(index = 0; index < randomRotation; index++) {
            if(!airplaneCollisionDetector.isRotationCollisionDetected(airplanes, airplane2)) {

                airplane2.rotate();
            }
        }

        airplanes.add(airplane2);


        /*airplane 3*/
        newAirplaneId = 3;
        Airplane airplane3 = new Airplane(newAirplaneId);

        randomRow = rand.nextInt(maxRow - minRow + 1) + minRow;
        randomColumn = rand.nextInt(maxColumn - minColumn + 1) + minColumn;

        while(airplaneCollisionDetector.isSuperpositionDetected(airplanes, airplane3, randomRow, randomColumn)){

            randomRow = rand.nextInt(maxRow - minRow + 1) + minRow;
            randomColumn = rand.nextInt(maxColumn - minColumn + 1) + minColumn;

        };
        airplane3.moveAirplaneToCoordinate(new Coordinate(randomRow, randomColumn));

        randomRotation = rand.nextInt(6);

        for(index = 0; index < randomRotation; index++) {
            if(!airplaneCollisionDetector.isRotationCollisionDetected(airplanes, airplane3)) {

                airplane3.rotate();
            }
        }

        airplanes.add(airplane3);
    }

    public Coordinate generateFireTarget() {

        int targetFireMaxRow = 12;
        int targetFireMinRow = 1;

        int targetFireMaxColumn = 24;
        int targetFireMinColumn = 1;

        int randomRow = rand.nextInt(targetFireMaxRow - targetFireMinRow + 1) + targetFireMinRow;
        int randomColumn = rand.nextInt(targetFireMaxColumn - targetFireMinColumn + 1) + targetFireMinColumn;

        return new Coordinate(randomRow, randomColumn);
    }
}
