package com.game.airfight.backend;

import android.content.Context;

import com.game.profile.airfight.R;

import java.util.ArrayList;

public class MyAirplanesSingleton {

    private static MyAirplanesSingleton instance = null;

    private static ArrayList<Airplane> myAirplanes = new ArrayList<>();

    private MyAirplanesSingleton() {

    }

    public static MyAirplanesSingleton getInstance() {

        if(instance == null) {
            instance = new MyAirplanesSingleton();

        }
        return instance;
    }

    public void saveMyFleet(ArrayList<Airplane> _myAirplanes) {

        myAirplanes = _myAirplanes;
    }

    public ArrayList<Airplane> getMyFleet() {

        return myAirplanes;
    }

    public void resetSingleton() {

        instance = null;

        myAirplanes.clear();
    }

    public ArrayList<Integer> getHeadsColumnsRanges(Context context) {

        ArrayList<Integer> columns = new ArrayList<>();
        int columnHead = 0;
        Element ch = null;

        int mapColumns = Integer.parseInt(context.getString(R.string.MAP_COLUMNS));


        for(Airplane airplane: myAirplanes) {

            ch = airplane.getHead();
            columnHead = ch.getStart().getColumn();

            columns.add(columnHead);

            if( (columnHead - 1) >= 1 ){
                columns.add((columnHead - 1));
            }

            if( (columnHead - 2) >= 1 ){
                columns.add((columnHead - 2));
            }

            if( (columnHead + 1) <= mapColumns ){
                columns.add((columnHead + 1));
            }

            if( (columnHead + 2) <= mapColumns ){
                columns.add((columnHead + 2));
            }
        }

        return columns;
    }

    public ArrayList<Element> getHeadsRangesMediumDifficulty(Context context) {

        ArrayList<Element> headsRanges = new ArrayList<>();

        int mapRows = Integer.parseInt(context.getString(R.string.MAP_ROWS));
        int mapColumns = Integer.parseInt(context.getString(R.string.MAP_COLUMNS));

        Element head = null;

        Element headRange = null;

        int startRow = 0;
        int startColumn = 0;

        int endRow = 0;
        int endColumn = 0;

        for(Airplane airplane: myAirplanes) {

            head = airplane.getHead();

            startRow = head.getStart().getRow();
            startColumn = head.getStart().getColumn();

            endRow = head.getEnd().getRow();
            endColumn = head.getEnd().getColumn();

            if( (head.getStart().getRow() - 1) >= 1) {
                startRow = (head.getStart().getRow() - 1);
            }

            if( (head.getStart().getRow() - 2) >= 1) {
                startRow = (head.getStart().getRow() - 2);
            }

            if( (head.getStart().getRow() - 3) >= 1) {
                startRow = (head.getStart().getRow() - 3);
            }

            if( (head.getStart().getColumn() - 1) >= 1) {
                startColumn = (head.getStart().getColumn() - 1);
            }

            if( (head.getStart().getColumn() - 2) >= 1) {
                startColumn = (head.getStart().getColumn() - 2);
            }

            if( (head.getStart().getColumn() - 3) >= 1) {
                startColumn = (head.getStart().getColumn() - 3);
            }

            if( (head.getEnd().getRow() + 1) <= mapRows) {
                endRow = (head.getEnd().getRow() + 1);
            }

            if( (head.getEnd().getRow() + 2) <= mapRows) {
                endRow = (head.getEnd().getRow() + 2);
            }

            if( (head.getEnd().getRow() + 3) <= mapRows) {
                endRow = (head.getEnd().getRow() + 3);
            }

            if( (head.getEnd().getColumn() + 1) <= mapColumns) {
                endColumn = (head.getEnd().getColumn() + 1);
            }

            if( (head.getEnd().getColumn() + 2) <= mapColumns) {
                endColumn = (head.getEnd().getColumn() + 2);
            }

            if( (head.getEnd().getColumn() + 3) <= mapColumns) {
                endColumn = (head.getEnd().getColumn() + 3);
            }

            headRange = new Element(new Coordinate(startRow,startColumn), new Coordinate(endRow,endColumn));

            headsRanges.add(headRange);
        }

        return headsRanges;
    }

    public ArrayList<Element> getHeadsRangesHardDifficulty(Context context) {

        ArrayList<Element> headsRanges = new ArrayList<>();

        int mapRows = Integer.parseInt(context.getString(R.string.MAP_ROWS));
        int mapColumns = Integer.parseInt(context.getString(R.string.MAP_COLUMNS));

        Element head = null;

        Element headRange = null;


        int startRow = 0;
        int startColumn = 0;

        int endRow = 0;
        int endColumn = 0;

        for(Airplane airplane: myAirplanes) {

            head = airplane.getHead();

            startRow = head.getStart().getRow();
            startColumn = head.getStart().getColumn();

            endRow = head.getEnd().getRow();
            endColumn = head.getEnd().getColumn();



            if( (head.getStart().getRow() - 1) >= 1) {
                startRow = (head.getStart().getRow() - 1);
            }





            if( (head.getStart().getColumn() - 1) >= 1) {
                startColumn = (head.getStart().getColumn() - 1);
            }





            if( (head.getEnd().getRow() + 1) <= mapRows) {
                endRow = (head.getEnd().getRow() + 1);
            }





            if( (head.getEnd().getColumn() + 1) <= mapColumns) {
                endColumn = (head.getEnd().getColumn() + 1);
            }





            headRange = new Element(new Coordinate(startRow,startColumn), new Coordinate(endRow,endColumn));

            headsRanges.add(headRange);
        }

        return headsRanges;
    }
}
