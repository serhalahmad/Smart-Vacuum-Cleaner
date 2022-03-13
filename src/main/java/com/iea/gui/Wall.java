package com.iea.gui;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class Wall extends Rectangle {
    private boolean isWall;


    Wall(int x, int y, int width, int height) {
        setFill(Color.WHITE);
        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);
        this.isWall = false;
    }

    public boolean getStatus() {
        return isWall;
    }

    public void setStatus(boolean wall) {
        this.isWall = wall;
        if (isWall)
            addWall();
        else
            removeWall();
    }

    public void addWall() {
        setFill(Color.BLACK);
        isWall = true;
    }

    public void removeWall() {
        setFill(Color.WHITE);
        isWall = false;
    }
}

