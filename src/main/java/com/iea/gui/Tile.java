package com.iea.gui;


import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

class Tile extends Rectangle {
    private int row, column;
    private int status;
    private int count;
    boolean hasWallUp;
    boolean hasWallDown;
    boolean hasWallLeft;
    boolean hasWallRight;
    private Wall wallRight;
    private Wall wallDown;
    private static Image dirt;
    private static Image clean;

    static {
        clean=new Image(String.valueOf(Tile.class.getResource("Images/tile.png")));
        dirt = new Image(String.valueOf(Tile.class.getResource("Images/Dirt.png")));
    }


    //    Initialize tile through row and column
    Tile(int row, int column, int x, int y, int width, int height) {
        setFill(new ImagePattern(clean));
        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);
        this.row = row;
        this.column = column;
        this.hasWallDown = false;
        this.hasWallUp = false;
        this.hasWallLeft = false;
        this.hasWallRight = false;
        this.wallDown = new Wall(column * (width + 5) + 3, (row) * (width + 5) + 3 + width, width, 5);
        this.wallRight = new Wall((column) * (width + 5) + 3 + width, (row) * (width + 5) + 3, 5, width);
        count = 0;
    }

    public Tile(int i, int j) {
        this.hasWallDown = false;
        this.hasWallUp = false;
        this.hasWallLeft = false;
        this.hasWallRight = false;
    }

    public Tile(Tile tile) {
        this.hasWallDown = tile.hasWallDown;
        this.hasWallUp = tile.hasWallUp;
        this.hasWallLeft = tile.hasWallLeft;
        this.hasWallRight = tile.hasWallRight;
        this.row = tile.row;
        this.column = tile.column;
        this.setStatus(tile.getStatus());
    }

    public Tile(Tile tile, int i, int j, int x, int y, int size) {
        setX(x);
        setY(y);
        setWidth(size);
        setHeight(size);
        this.hasWallDown = tile.hasWallDown;
        this.hasWallUp = tile.hasWallUp;
        this.hasWallLeft = tile.hasWallLeft;
        this.hasWallRight = tile.hasWallRight;
        this.row = tile.row;
        this.column = tile.column;
        this.setStatus(tile.getStatus());
        this.wallDown = new Wall(column * (size + 5) + 3, (row) * (size + 5) + 3+ size, size, 5);
        this.wallRight = new Wall((column) * (size + 5) + 3 + size, (row) * (size + 5) +3, 5, size);
        this.wallDown.setStatus(tile.wallDown.getStatus());
        this.wallRight.setStatus(tile.wallRight.getStatus());
    }

    int getStatus() {
        return status;
    }

    void setStatus(int s) {
        status = s;
        switch (getStatus()) {
            case 0 -> setFill(new ImagePattern(clean));
            case 1 -> setFill(new ImagePattern(dirt));
//            case 2 -> setFill(Color.BLACK);
//            case 3 -> setFill(Color.BLACK);
        }
    }

    boolean isClean() {
        return status == 0;
    }

    boolean isDirty() {
        return status == 1;
    }

    public boolean isValid() {
        return (status == 0 || status == 1);
    }

    public void incrementCount() {
        count++;
    }

    public boolean hasWallRight() {
        return this.hasWallRight;
    }

    public boolean hasWallDown() {
        return this.hasWallDown;
    }

    public boolean hasWallLeft() {
        try {
            return Main.game.tiles[row][column - 1].hasWallRight();
        } catch (Exception e) {
            return true;
        }
    }

    public boolean hasWallUp() {
        try {
            return Main.game.tiles[row - 1][column].hasWallDown();
        } catch (Exception e) {
            return true;
        }
    }

    public void AddRemoveWallDown() {
        try {
            if(row==Main.game.getHeight()-1)
                return;
            boolean wall = !this.hasWallDown;
            this.hasWallDown = wall;
            if (wall) {
                Main.game.tiles[row][column].wallDown.addWall();
            } else {
                Main.game.tiles[row][column].wallDown.removeWall();
            }
            Main.game.tiles[row + 1][column].hasWallUp = wall;
        } catch (Exception e) {
        }
    }

    public void AddRemoveWallRight() {
        try {
            if(column==Main.game.getWidth()-1)
                return;
            boolean wall = !this.hasWallRight;
            this.hasWallRight = wall;
            if (wall) {
                Main.game.tiles[row][column].wallRight.addWall();
            } else {
                Main.game.tiles[row][column].wallRight.removeWall();
            }
            Main.game.tiles[row][column - 1].hasWallLeft = wall;
        } catch (Exception e) {
        }
    }

    public void AddRemoveWallLeft() {
        try {
            boolean wall = !this.hasWallLeft;
            this.hasWallLeft = wall;
            if (wall) {
                Main.game.tiles[row][column - 1].wallRight.addWall();
            } else {
                Main.game.tiles[row][column - 1].wallRight.removeWall();
            }
            Main.game.tiles[row][column - 1].hasWallRight = wall;
        } catch (Exception e) {
        }
    }

    public void AddRemoveWallUp() {
        try {
            boolean wall = !this.hasWallUp;
            this.hasWallUp = wall;
            if (wall) {
                Main.game.tiles[row - 1][column].wallDown.addWall();
            } else {
                Main.game.tiles[row - 1][column].wallDown.removeWall();
            }
            Main.game.tiles[row - 1][column].hasWallDown = wall;
        } catch (Exception e) {
        }
    }

    public Wall getWallRight() {
        return wallRight;
    }

    public Wall getWallDown() {
        return wallDown;
    }
}