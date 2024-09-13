package com.game.airfight.backend;

import java.util.ArrayList;

public class LoadingPlatform {

    private Element loadingPlatform = null;

    public LoadingPlatform() {
        loadingPlatform = new Element(new Coordinate(2,2), new Coordinate(6, 6));
    }

    public boolean isPartOfPlatform(int _row, int _column) {

        boolean isHead = loadingPlatform.isInRange(_row, _column);

        if(isHead){
            return true;
        } else {
            return false;
        }
    }
    
    public boolean isLoadPlatformEmpty(ArrayList<Airplane> airplanes) {

        boolean isPlatfromEmpty = true;

        for(Airplane airplane: airplanes) {
            ArrayList<Coordinate> airplaneCoordinates = airplane.getAirplaneCoordinates();

            for(Coordinate coor: airplaneCoordinates) {
                if(loadingPlatform.isInRange(coor.getRow(), coor.getColumn())) {
                    isPlatfromEmpty = false;
                }
            }
        }

        return isPlatfromEmpty;
    }
}
