package com.game.airfight.backend;


import android.content.Context;

import com.game.profile.airfight.R;

public class Coordinate {

    public void setRow(int row) {
        this.row = row;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    private int row = 0;
    private int column = 0;

    public Coordinate(int _row, int _column) {
        row = _row;
        column = _column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public boolean checkMovementToLeft(Context context) {
        int mapRows = Integer.parseInt(context.getString(R.string.MAP_ROWS));
        int mapColumns = Integer.parseInt(context.getString(R.string.MAP_COLUMNS));

        if( ((row >= 1) && (row <= mapRows)) && (((column - 1) >= 1) && ((column - 1) <= mapColumns)) ) {
            return true;
        } else {
            return false;
        }
    }

    public void moveCoordinateToLeft() {
        column = column - 1;
    }

    public boolean checkMovementToRight(Context context) {
        int mapRows = Integer.parseInt(context.getString(R.string.MAP_ROWS));
        int mapColumns = Integer.parseInt(context.getString(R.string.MAP_COLUMNS));

        if( ((row >= 1) && (row <= mapRows)) && (((column + 1) >= 1) && ((column + 1) <= mapColumns)) ) {
            return true;
        } else {
            return false;
        }
    }

    public void moveCoordinateToRight() {
        column = column + 1;
    }

    public boolean checkMovementUp(Context context) {
        int mapRows = Integer.parseInt(context.getString(R.string.MAP_ROWS));
        int mapColumns = Integer.parseInt(context.getString(R.string.MAP_COLUMNS));

        if( (((row - 1) >= 1) && ((row - 1) <= mapRows)) && ((column >= 1) && (column <= mapColumns)) ) {
            return true;
        } else {
            return false;
        }
    }

    public void moveCoordinateUp() {
        row = row - 1;
    }

    public boolean checkMovementDown(Context context) {
        int mapRows = Integer.parseInt(context.getString(R.string.MAP_ROWS));
        int mapColumns = Integer.parseInt(context.getString(R.string.MAP_COLUMNS));

        if( (((row + 1) >= 1) && ((row + 1) <= mapRows)) && ((column >= 1) && (column <= mapColumns)) ) {
            return true;
        } else {
            return false;
        }
    }

    public void moveCoordinateDown() {
        row = row + 1;
    }

    public void moveCoordinateTo(int _row, int _column) {
        row = _row;
        column = _column;
    }

    public boolean isTargetOnMap(Context context) {

        int mapRows = Integer.parseInt(context.getString(R.string.MAP_ROWS));
        int mapColumns = Integer.parseInt(context.getString(R.string.MAP_COLUMNS));

        if( ((this.row >= 1) && (this.row <= mapRows)) && ((this.column >= 1) && (this.column <= mapColumns)) ){
            return true;
        } else {
            return false;
        }
    }

}
