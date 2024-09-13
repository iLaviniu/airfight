package com.game.airfight.backend;

import android.content.Context;

import com.game.profile.airfight.R;

import java.util.ArrayList;

public class AirplaneCollisionDetector {

    private Context context = null;

    public AirplaneCollisionDetector(Context _context) {

        context = _context;
    }

    public boolean isSuperpositionDetected(ArrayList<Airplane> airplanes, Airplane _airplaneToBeChecked, int randomRow, int randomColumn) {
        boolean collision = false;

        Airplane temporaryAirplane = new Airplane(-1, null);

        /*making a copy of airplane _airplaneToBeChecked.*/
        /*in this way the airplane _airplaneToBeChecked will not be moved, only the virtual copy of it.*/
        temporaryAirplane.copyAirplane(_airplaneToBeChecked.getHead(),
                _airplaneToBeChecked.getBody(),
                _airplaneToBeChecked.getLeftWing(),
                _airplaneToBeChecked.getRightWing(),
                _airplaneToBeChecked.getTail());

        temporaryAirplane.moveAirplaneToCoordinate(new Coordinate(randomRow, randomColumn));

        ArrayList<Coordinate> airplaneToBeCheckedCoordinates = temporaryAirplane.getAirplaneCoordinates();
        int airplaneToBeCheckedId = _airplaneToBeChecked.getAirplaneId();

        ArrayList<Coordinate> otherAirplaneCoordinates = null;

        for(Airplane airplane: airplanes) {

            if(airplaneToBeCheckedId != airplane.getAirplaneId()) {

                otherAirplaneCoordinates = airplane.getAirplaneCoordinates();

                if(checkTwoAirplanesCollision(airplaneToBeCheckedCoordinates, otherAirplaneCoordinates)) {
                    collision = true;
                }
            }
        }

        return collision;
    }

    public boolean isLinearCollisionDetected(ArrayList<Airplane> airplanes, Airplane _airplaneToBeChecked, String movement) {

        boolean collision = false;

        ArrayList<Coordinate> virtualAirplane = new ArrayList<>();
        ArrayList<Coordinate> airplaneToBeCheckedCoordinates = _airplaneToBeChecked.getAirplaneCoordinates();
        int airplaneToBeCheckedId = _airplaneToBeChecked.getAirplaneId();

        ArrayList<Coordinate> otherAirplaneCoordinates = null;

        if(movement.equals(context.getString(R.string.RIGHT))) {
            for(Coordinate coor: airplaneToBeCheckedCoordinates) {
                virtualAirplane.add(new Coordinate(coor.getRow(), coor.getColumn() + 1));
            }
        } else if(movement.equals(context.getString(R.string.LEFT))) {
            for(Coordinate coor: airplaneToBeCheckedCoordinates) {
                virtualAirplane.add(new Coordinate(coor.getRow(), coor.getColumn() - 1));
            }
        } else if(movement.equals(context.getString(R.string.UP))) {
            for(Coordinate coor: airplaneToBeCheckedCoordinates) {
                virtualAirplane.add(new Coordinate(coor.getRow() - 1, coor.getColumn()));
            }
        } else if(movement.equals(context.getString(R.string.DOWN))) {
            for(Coordinate coor: airplaneToBeCheckedCoordinates) {
                virtualAirplane.add(new Coordinate(coor.getRow() + 1, coor.getColumn()));
            }
        }

        for(Airplane airplane: airplanes) {

            if(airplaneToBeCheckedId != airplane.getAirplaneId()) {

                otherAirplaneCoordinates = airplane.getAirplaneCoordinates();

                if(checkTwoAirplanesCollision(virtualAirplane, otherAirplaneCoordinates)) {
                    collision = true;
                }
            }
        }

        return collision;
    }

    public boolean isRotationCollisionDetected(ArrayList<Airplane> airplanes, Airplane _airplaneToBeChecked) {

        boolean collision = false;

        ArrayList<Coordinate> virtualAirplaneCoordinates = null;

        ArrayList<Coordinate> otherAirplaneCoordinates = null;

        Airplane virtualAirplane = new Airplane(-1, null);

        /*making a copy of airplane _airplaneToBeChecked.*/
        /*in this way the airplane _airplaneToBeChecked will not be rotate, only the virtual copy of it.*/
        virtualAirplane.copyAirplane(_airplaneToBeChecked.getHead(),
                                    _airplaneToBeChecked.getBody(),
                                    _airplaneToBeChecked.getLeftWing(),
                                    _airplaneToBeChecked.getRightWing(),
                                    _airplaneToBeChecked.getTail());

        virtualAirplane.rotate();

        virtualAirplaneCoordinates = virtualAirplane.getAirplaneCoordinates();


        for(Airplane airplane: airplanes) {

            if(_airplaneToBeChecked.getAirplaneId() != airplane.getAirplaneId()) {

                otherAirplaneCoordinates = airplane.getAirplaneCoordinates();

                if(checkTwoAirplanesCollision(virtualAirplaneCoordinates, otherAirplaneCoordinates)) {
                    collision = true;
                }
            }

        }

        return collision;
    }

    private boolean checkTwoAirplanesCollision(ArrayList<Coordinate> _airplane1, ArrayList<Coordinate> _airplane2) {

        boolean collision = false;

        for(Coordinate coor1: _airplane1) {

            for(Coordinate coor2: _airplane2) {

                if( (coor1.getRow() == coor2.getRow()) && (coor1.getColumn() == coor2.getColumn()) ) {
                    collision = true;
                }
            }
        }
        return collision;
    }

}
