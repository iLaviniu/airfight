package com.game.airfight.backend;


public class MenuField {

    public Element getMenuElement() {
        return menuElement;
    }

    private Element menuElement = null;

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    private boolean isActive = false;

    private String text = "";

    public MenuField(int _startRow, int _startColumn, int _endRow, int _endColumn) {
        menuElement = new Element(new Coordinate(_startRow, _startColumn), new Coordinate(_endRow, _endColumn));
    }

    public boolean isPartOfMenuField(int _row, int _column) {

        return menuElement.isInRange(_row, _column);
    }
}
