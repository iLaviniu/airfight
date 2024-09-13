package com.game.airfight.backend;

import android.content.Context;

import java.util.ArrayList;

public class Element {

    private Coordinate start = null;
    private Coordinate end = null;

    public Element(Coordinate _start, Coordinate _end) {
        start = _start;
        end = _end;
    }

    public ArrayList<Coordinate> getElementCoordinates() {

        ArrayList<Coordinate> coordinates = new ArrayList<>();
        int startRow = start.getRow();
        int startColumn = start.getColumn();
        int endRow = end.getRow();
        int endColumn = end.getColumn();
        int auxiliar = -1;
        int rowIndex = -1;
        int columnIndex = -1;

        if(startRow > endRow) {
            auxiliar = startRow;
            startRow = endRow;
            endRow = auxiliar;
        }

        if(startColumn > endColumn) {
            auxiliar = startColumn;
            startColumn = endColumn;
            endColumn = auxiliar;
        }

        for(rowIndex = startRow; rowIndex <= endRow; rowIndex++) {

            for(columnIndex = startColumn; columnIndex <= endColumn; columnIndex++) {

                coordinates.add(new Coordinate(rowIndex, columnIndex));
            }
        }
        return coordinates;
    }

    public boolean isInRange(int _row, int _column) {

        int startRow = start.getRow();
        int startColumn = start.getColumn();
        int endRow = end.getRow();
        int endColumn = end.getColumn();
        int auxiliar = -1;

        if(startRow > endRow) {
            auxiliar = startRow;
            startRow = endRow;
            endRow = auxiliar;
        }

        if(startColumn > endColumn) {
            auxiliar = startColumn;
            startColumn = endColumn;
            endColumn = auxiliar;
        }

        if( ((_row >= startRow) && (_row <= endRow)) && ((_column >= startColumn) && (_column <= endColumn)) ){
            return true;
        } else {
            return false;
        }

    }

    public Coordinate getStart() {
        return start;
    }

    public Coordinate getEnd() {
        return end;
    }

    public boolean checkMovementToLeft(Context context) {
        if(start.checkMovementToLeft(context) && end.checkMovementToLeft(context)) {
            return true;
        } else {
            return false;
        }
    }

    public void moveElementToLeft() {
        start.moveCoordinateToLeft();
        end.moveCoordinateToLeft();
    }

    public boolean checkMovementToRight(Context context) {
        if(start.checkMovementToRight(context) && end.checkMovementToRight(context)) {
            return true;
        } else {
            return false;
        }
    }

    public void moveElementToRight() {
        start.moveCoordinateToRight();
        end.moveCoordinateToRight();
    }

    public boolean checkMovementUp(Context context) {
        if(start.checkMovementUp(context) && end.checkMovementUp(context)) {
            return true;
        } else {
            return false;
        }
    }

    public void moveElementUp() {
        start.moveCoordinateUp();
        end.moveCoordinateUp();
    }

    public boolean checkMovementDown(Context context) {
        if(start.checkMovementDown(context) && end.checkMovementDown(context)) {
            return true;
        } else {
            return false;
        }
    }

    public void moveElementDown() {
        start.moveCoordinateDown();
        end.moveCoordinateDown();
    }

    public void moveElementTo(Coordinate _startCoordinates, Coordinate _endCoordinates) {
        start.moveCoordinateTo(_startCoordinates.getRow(), _startCoordinates.getColumn());
        end.moveCoordinateTo(_endCoordinates.getRow(), _endCoordinates.getColumn());
    }
}
