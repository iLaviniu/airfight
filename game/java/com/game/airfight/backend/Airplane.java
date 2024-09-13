package com.game.airfight.backend;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.appcompat.app.AppCompatActivity;

import com.game.profile.airfight.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Airplane {

    private int airplaneId = 0;
    private Bitmap airplaneIdIcon = null;

    public Element getHead() {
        return head;
    }

    public Element getBody() {
        return body;
    }

    public Element getLeftWing() {
        return leftWing;
    }

    public Element getRightWing() {
        return rightWing;
    }

    public Element getTail() {
        return tail;
    }

    public void setHead(Element head) {
        this.head = head;
    }

    public void setBody(Element body) {
        this.body = body;
    }

    public void setLeftWing(Element leftWing) {
        this.leftWing = leftWing;
    }

    public void setRightWing(Element rightWing) {
        this.rightWing = rightWing;
    }

    public void setTail(Element tail) {
        this.tail = tail;
    }

    private Element head = null;
    private Element body = null;
    private Element leftWing = null;
    private Element rightWing = null;
    private Element tail = null;

    public Airplane(int _airplaneId, Bitmap _airplaneIdIcon) {

        airplaneId = _airplaneId;
        airplaneIdIcon = _airplaneIdIcon;

        head = new Element(new Coordinate(2, 4), new Coordinate(2, 4));
        body = new Element(new Coordinate(3, 4), new Coordinate(5, 4));
        leftWing = new Element(new Coordinate(3, 2), new Coordinate(3, 3));
        rightWing = new Element(new Coordinate(3, 5), new Coordinate(3, 6));
        tail = new Element(new Coordinate(6, 3), new Coordinate(6, 5));
    }

    public Airplane(int _airplaneId) {

        airplaneId = _airplaneId;

        head = new Element(new Coordinate(2, 4), new Coordinate(2, 4));
        body = new Element(new Coordinate(3, 4), new Coordinate(5, 4));
        leftWing = new Element(new Coordinate(3, 2), new Coordinate(3, 3));
        rightWing = new Element(new Coordinate(3, 5), new Coordinate(3, 6));
        tail = new Element(new Coordinate(6, 3), new Coordinate(6, 5));
    }

    public Airplane() {

        head = new Element(new Coordinate(4, 18), new Coordinate(4, 18));
        body = new Element(new Coordinate(5, 18), new Coordinate(7, 18));
        leftWing = new Element(new Coordinate(5, 16), new Coordinate(5, 17));
        rightWing = new Element(new Coordinate(5, 19), new Coordinate(5, 20));
        tail = new Element(new Coordinate(8, 17), new Coordinate(8, 19));
    }

    public void flyReset() {

        head = new Element(new Coordinate(15, 20), new Coordinate(15, 20));
        body = new Element(new Coordinate(16, 20), new Coordinate(18, 20));
        leftWing = new Element(new Coordinate(16, 18), new Coordinate(16, 19));
        rightWing = new Element(new Coordinate(16, 21), new Coordinate(16, 22));
        tail = new Element(new Coordinate(19, 19), new Coordinate(19, 21));
    }

    public void flyUp() {
        head.moveElementUp();
        body.moveElementUp();
        leftWing.moveElementUp();
        rightWing.moveElementUp();
        tail.moveElementUp();
    }

    public void flyDown() {
        head.moveElementDown();
        body.moveElementDown();
        leftWing.moveElementDown();
        rightWing.moveElementDown();
        tail.moveElementDown();
    }

    public void copyAirplane(Element _head, Element _body, Element _leftWing, Element _rightWing, Element _tail) {

        /*it is necessary to recreate all elements of the airplane and not copy the reference of the airplane*/

        Element newHead = new Element(new Coordinate(_head.getStart().getRow(), _head.getStart().getColumn()),
                                      new Coordinate(_head.getEnd().getRow(), _head.getEnd().getColumn()));

        Element newBody = new Element(new Coordinate(_body.getStart().getRow(), _body.getStart().getColumn()),
                                      new Coordinate(_body.getEnd().getRow(), _body.getEnd().getColumn()));

        Element newLeftWing = new Element(new Coordinate(_leftWing.getStart().getRow(), _leftWing.getStart().getColumn()),
                                          new Coordinate(_leftWing.getEnd().getRow(), _leftWing.getEnd().getColumn()));

        Element newRightWing = new Element(new Coordinate(_rightWing.getStart().getRow(), _rightWing.getStart().getColumn()),
                                           new Coordinate(_rightWing.getEnd().getRow(), _rightWing.getEnd().getColumn()));

        Element newTail = new Element(new Coordinate(_tail.getStart().getRow(), _tail.getStart().getColumn()),
                                      new Coordinate(_tail.getEnd().getRow(), _tail.getEnd().getColumn()));

        head = newHead;
        body = newBody;
        leftWing = newLeftWing;
        rightWing = newRightWing;
        tail = newTail;
    }

    public ArrayList<Coordinate> getAirplaneCoordinates() {

        ArrayList<Coordinate> headCoor = head.getElementCoordinates();
        ArrayList<Coordinate> bodyCoor = body.getElementCoordinates();
        ArrayList<Coordinate> leftWingCoor = leftWing.getElementCoordinates();
        ArrayList<Coordinate> rightWingCoor = rightWing.getElementCoordinates();
        ArrayList<Coordinate> tailCoor = tail.getElementCoordinates();

        ArrayList<Coordinate> coordinates = new ArrayList<>(headCoor);
        coordinates.addAll(bodyCoor);
        coordinates.addAll(leftWingCoor);
        coordinates.addAll(rightWingCoor);
        coordinates.addAll(tailCoor);

        return coordinates;
    }

    public int getAirplaneId() {
        return airplaneId;
    }

    public Bitmap getAirplaneIdIcon() {
        return airplaneIdIcon;
    }

    public boolean isPartOfAirplane(int _row, int _column) {

        boolean isHead = head.isInRange(_row, _column);
        boolean isBody = body.isInRange(_row, _column);
        boolean isLeftWing = leftWing.isInRange(_row, _column);
        boolean isRightWing = rightWing.isInRange(_row, _column);
        boolean isTail = tail.isInRange(_row, _column);

        if(isHead || isBody || isLeftWing || isRightWing || isTail){
            return true;
        } else {
            return false;
        }
    }

    public boolean isAirplaneHead(int _row, int _column) {

        return head.isInRange(_row, _column);
    }

    public void moveAirplaneToCoordinate(Coordinate _newCenterCoordinate) {

        head.getStart().setRow(_newCenterCoordinate.getRow() - 2);
        head.getStart().setColumn(_newCenterCoordinate.getColumn());
        head.getEnd().setRow(_newCenterCoordinate.getRow() - 2);
        head.getEnd().setColumn(_newCenterCoordinate.getColumn());

        body.getStart().setRow(_newCenterCoordinate.getRow() - 1);
        body.getStart().setColumn(_newCenterCoordinate.getColumn());
        body.getEnd().setRow(_newCenterCoordinate.getRow() + 1);
        body.getEnd().setColumn(_newCenterCoordinate.getColumn());

        leftWing.getStart().setRow(_newCenterCoordinate.getRow() - 1);
        leftWing.getStart().setColumn(_newCenterCoordinate.getColumn() - 2);
        leftWing.getEnd().setRow(_newCenterCoordinate.getRow() - 1);
        leftWing.getEnd().setColumn(_newCenterCoordinate.getColumn() - 1);

        rightWing.getStart().setRow(_newCenterCoordinate.getRow() - 1);
        rightWing.getStart().setColumn(_newCenterCoordinate.getColumn() + 1);
        rightWing.getEnd().setRow(_newCenterCoordinate.getRow() - 1);
        rightWing.getEnd().setColumn(_newCenterCoordinate.getColumn() + 2);

        tail.getStart().setRow(_newCenterCoordinate.getRow() + 2);
        tail.getStart().setColumn(_newCenterCoordinate.getColumn() - 1);
        tail.getEnd().setRow(_newCenterCoordinate.getRow() + 2);
        tail.getEnd().setColumn(_newCenterCoordinate.getColumn() + 1);
    }

    public boolean checkMovementToLeft(Context context) {

        if( head.checkMovementToLeft(context) &&
                body.checkMovementToLeft(context) &&
                leftWing.checkMovementToLeft(context) &&
                rightWing.checkMovementToLeft(context) &&
                tail.checkMovementToLeft(context)) {
            return true;
        } else {
            return false;
        }
    }

    public void moveAirplaneToLeft() {

        head.moveElementToLeft();
        body.moveElementToLeft();
        leftWing.moveElementToLeft();
        rightWing.moveElementToLeft();
        tail.moveElementToLeft();
    }

    public boolean checkMovementToRight(Context context) {

        if( head.checkMovementToRight(context) &&
                body.checkMovementToRight(context) &&
                leftWing.checkMovementToRight(context) &&
                rightWing.checkMovementToRight(context) &&
                tail.checkMovementToRight(context)) {
            return true;
        } else {
            return false;
        }
    }

    public void moveAirplaneToRight() {

        head.moveElementToRight();
        body.moveElementToRight();
        leftWing.moveElementToRight();
        rightWing.moveElementToRight();
        tail.moveElementToRight();
    }

    public boolean checkMovementUp(Context context) {

        if( head.checkMovementUp(context) &&
                body.checkMovementUp(context) &&
                leftWing.checkMovementUp(context) &&
                rightWing.checkMovementUp(context) &&
                tail.checkMovementUp(context)) {
            return true;
        } else {
            return false;
        }
    }

    public void moveAirplaneUp() {

        head.moveElementUp();
        body.moveElementUp();
        leftWing.moveElementUp();
        rightWing.moveElementUp();
        tail.moveElementUp();
    }

    public boolean checkMovementDown(Context context) {

        if( head.checkMovementDown(context) &&
                body.checkMovementDown(context) &&
                leftWing.checkMovementDown(context) &&
                rightWing.checkMovementDown(context) &&
                tail.checkMovementDown(context)) {
            return true;
        } else {
            return false;
        }
    }

    public void moveAirplaneDown() {

        head.moveElementDown();
        body.moveElementDown();
        leftWing.moveElementDown();
        rightWing.moveElementDown();
        tail.moveElementDown();
    }

    public int getAirplaneRotation() {
        int airplaneRotation = -1;
        Coordinate bodyStart = body.getStart();
        Coordinate bodyEnd = body.getEnd();

        int rowBodyStart = bodyStart.getRow();
        int columnBodyStart = bodyStart.getColumn();
        int rowBodyEnd = bodyEnd.getRow();
        int columnBodyEnd = bodyEnd.getColumn();

        if(rowBodyStart == rowBodyEnd) {
            if(columnBodyStart > columnBodyEnd) {
                airplaneRotation = 0;
            } else {
                airplaneRotation = 180;
            }
        } else if (columnBodyStart == columnBodyEnd) {
            if(rowBodyStart > rowBodyEnd) {
                airplaneRotation = 270;
            } else {
                airplaneRotation = 90;
            }
        }
        return airplaneRotation;
    }

    public Coordinate getAirplaneCenter() {
        int row = -1;
        int column = -1;
        Coordinate bodyStart = body.getStart();
        Coordinate bodyEnd = body.getEnd();

        int rowBodyStart = bodyStart.getRow();
        int columnBodyStart = bodyStart.getColumn();
        int rowBodyEnd = bodyEnd.getRow();
        int columnBodyEnd = bodyEnd.getColumn();

        if(rowBodyStart == rowBodyEnd) {
            row = rowBodyStart;
            if(columnBodyStart > columnBodyEnd) {
                column = columnBodyStart - 1;
            } else {
                column = columnBodyStart + 1;
            }
        } else if (columnBodyStart == columnBodyEnd) {
            column = columnBodyStart;
            if(rowBodyStart > rowBodyEnd) {
                row = rowBodyStart - 1;
            } else {
                row = rowBodyStart + 1;
            }
        }
        return new Coordinate(row, column);
    }

    public void rotate() {
        int rotation  = getAirplaneRotation();
        int newRotation = -1;

        if(rotation == 0) {
            newRotation = 270;
        } else {
            newRotation = rotation - 90;
        }

        if(newRotation == 0) {
            rotate0Degree();
        } else if (newRotation == 90) {
            rotate90Degree();
        } else if (newRotation == 180) {
            rotate180Degree();
        } else if (newRotation == 270) {
            rotate270Degree();
        }
    }

    public void rotate0Degree() {

        Coordinate center = getAirplaneCenter();

        head.moveElementTo(new Coordinate(center.getRow(), center.getColumn() + 2), new Coordinate(center.getRow(), center.getColumn() + 2));
        body.moveElementTo(new Coordinate(center.getRow(), center.getColumn() + 1), new Coordinate(center.getRow(), center.getColumn() - 1));
        leftWing.moveElementTo(new Coordinate(center.getRow() - 2, center.getColumn() + 1), new Coordinate(center.getRow() - 1, center.getColumn() + 1));
        rightWing.moveElementTo(new Coordinate(center.getRow() + 1, center.getColumn() + 1), new Coordinate(center.getRow() + 2, center.getColumn() + 1));
        tail.moveElementTo(new Coordinate(center.getRow() - 1, center.getColumn() - 2), new Coordinate(center.getRow() + 1, center.getColumn() - 2));
    }

    public void rotate90Degree() {

        Coordinate center = getAirplaneCenter();

        head.moveElementTo(new Coordinate(center.getRow() - 2, center.getColumn()), new Coordinate(center.getRow() - 2, center.getColumn()));
        body.moveElementTo(new Coordinate(center.getRow() - 1, center.getColumn()), new Coordinate(center.getRow() + 1, center.getColumn()));
        leftWing.moveElementTo(new Coordinate(center.getRow() - 1, center.getColumn() - 2), new Coordinate(center.getRow() - 1, center.getColumn() - 1));
        rightWing.moveElementTo(new Coordinate(center.getRow() - 1, center.getColumn() + 1), new Coordinate(center.getRow() - 1, center.getColumn() + 2));
        tail.moveElementTo(new Coordinate(center.getRow() + 2, center.getColumn() - 1), new Coordinate(center.getRow() + 2, center.getColumn() + 1));
    }

    public void rotate180Degree() {

        Coordinate center = getAirplaneCenter();

        head.moveElementTo(new Coordinate(center.getRow(), center.getColumn() - 2), new Coordinate(center.getRow(), center.getColumn() - 2));
        body.moveElementTo(new Coordinate(center.getRow(), center.getColumn() - 1), new Coordinate(center.getRow(), center.getColumn() + 1));
        leftWing.moveElementTo(new Coordinate(center.getRow() + 2, center.getColumn() - 1), new Coordinate(center.getRow() + 1, center.getColumn() - 1));
        rightWing.moveElementTo(new Coordinate(center.getRow() - 1, center.getColumn() - 1), new Coordinate(center.getRow() - 2, center.getColumn() - 1));
        tail.moveElementTo(new Coordinate(center.getRow() + 1, center.getColumn() + 2), new Coordinate(center.getRow() - 1, center.getColumn() + 2));
    }

    public void rotate270Degree() {

        Coordinate center = getAirplaneCenter();

        head.moveElementTo(new Coordinate(center.getRow() + 2, center.getColumn()), new Coordinate(center.getRow() + 2, center.getColumn()));
        body.moveElementTo(new Coordinate(center.getRow() + 1, center.getColumn()), new Coordinate(center.getRow() - 1, center.getColumn()));
        leftWing.moveElementTo(new Coordinate(center.getRow() + 1, center.getColumn() + 2), new Coordinate(center.getRow() + 1, center.getColumn() + 1));
        rightWing.moveElementTo(new Coordinate(center.getRow() + 1, center.getColumn() - 1), new Coordinate(center.getRow() + 1, center.getColumn() - 2));
        tail.moveElementTo(new Coordinate(center.getRow() - 2, center.getColumn() + 1), new Coordinate(center.getRow() - 2, center.getColumn() - 1));
    }
}
