package com.iea.gui;

import java.util.ArrayList;

public class Point extends java.awt.Point {
    static int value;

    public Point(Point p) {
        super(p);
    }

    public Point(int x, int y) {
        super(x, y);
    }

    @Override
    public String toString() {
        return "[" + x + ";" + y + "]";
    }

    public boolean isValid() {
        return this.x >= 0 && this.y >= 0 && this.x < Main.game.roomWidth && this.y < Main.game.roomHeight;
    }

    public ArrayList<Point> getNeighbors() {
        ArrayList<Point> toReturn = new ArrayList<>();
        if (!Main.game.tiles[this.y][this.x].hasWallRight() && new Point(this.x + 1, this.y).isValid()) {
            if (Main.game.tiles[this.y][this.x + 1].getStatus() != 3)
                toReturn.add(new Point(this.x + 1, this.y));
        }
        if (!Main.game.tiles[this.y][this.x].hasWallLeft() && new Point(this.x - 1, this.y).isValid()) {
            if (Main.game.tiles[this.y][this.x - 1].getStatus() != 3)
                toReturn.add(new Point(this.x - 1, this.y));
        }
        if (!Main.game.tiles[this.y][this.x].hasWallUp() && new Point(this.x, this.y - 1).isValid()) {
            if (Main.game.tiles[this.y - 1][this.x].getStatus() != 3)
                toReturn.add(new Point(this.x, this.y - 1));
        }
        if (!Main.game.tiles[this.y][this.x].hasWallDown() && new Point(this.x, this.y + 1).isValid()) {
            if (Main.game.tiles[this.y + 1][this.x].getStatus() != 3)
                toReturn.add(new Point(this.x, this.y + 1));
        }
        return toReturn;
    }
    public ArrayList<Point> getNeighbors(boolean b) {
        ArrayList<Point> toReturn = new ArrayList<>();
        if (!Main.game.tiles[this.y][this.x].hasWallRight() && new Point(this.x + 1, this.y).isValid()) {
                toReturn.add(new Point(this.x + 1, this.y));
        }
        if (!Main.game.tiles[this.y][this.x].hasWallLeft() && new Point(this.x - 1, this.y).isValid()) {
                toReturn.add(new Point(this.x - 1, this.y));
        }
        if (!Main.game.tiles[this.y][this.x].hasWallUp() && new Point(this.x, this.y - 1).isValid()) {
                toReturn.add(new Point(this.x, this.y - 1));
        }
        if (!Main.game.tiles[this.y][this.x].hasWallDown() && new Point(this.x, this.y + 1).isValid()) {
                toReturn.add(new Point(this.x, this.y + 1));
        }
        return toReturn;
    }
    public ArrayList<Point> getNeighbors(Room room) {
        ArrayList<Point> toReturn = new ArrayList<>();
        if (!room.tiles[this.y][this.x].hasWallRight() && new Point(this.x + 1, this.y).isValid()) {
            if (room.tiles[this.y][this.x + 1].getStatus() != 3)
                toReturn.add(new Point(this.x + 1, this.y));
        }
        if (!room.tiles[this.y][this.x].hasWallLeft() && new Point(this.x - 1, this.y).isValid()) {
            if (room.tiles[this.y][this.x - 1].getStatus() != 3)
                toReturn.add(new Point(this.x - 1, this.y));
        }
        if (!room.tiles[this.y][this.x].hasWallUp() && new Point(this.x, this.y - 1).isValid()) {
            if (room.tiles[this.y - 1][this.x].getStatus() != 3)
                toReturn.add(new Point(this.x, this.y - 1));
        }
        if (!room.tiles[this.y][this.x].hasWallDown() && new Point(this.x, this.y + 1).isValid()) {
            if (room.tiles[this.y + 1][this.x].getStatus() != 3)
                toReturn.add(new Point(this.x, this.y + 1));
        }
        return toReturn;
    }
    public ArrayList<Point> getNeighbors(Room room,Point destination) {
        ArrayList<Point> toReturn = new ArrayList<>();
        if (!room.tiles[this.y][this.x].hasWallRight() && new Point(this.x + 1, this.y).isValid()) {
            if (room.tiles[this.y][this.x + 1].getStatus() != 3 || new Point(this.x+1,this.y).equals(destination))
                toReturn.add(new Point(this.x + 1, this.y));
        }
        if (!room.tiles[this.y][this.x].hasWallLeft() && new Point(this.x - 1, this.y).isValid()) {
            if (room.tiles[this.y][this.x - 1].getStatus() != 3|| new Point(this.x-1,this.y).equals(destination))
                toReturn.add(new Point(this.x - 1, this.y));
        }
        if (!room.tiles[this.y][this.x].hasWallUp() && new Point(this.x, this.y - 1).isValid()) {
            if (room.tiles[this.y - 1][this.x].getStatus() != 3|| new Point(this.x,this.y-1).equals(destination))
                toReturn.add(new Point(this.x, this.y - 1));
        }
        if (!room.tiles[this.y][this.x].hasWallDown() && new Point(this.x, this.y + 1).isValid()) {
            if (room.tiles[this.y + 1][this.x].getStatus() != 3|| new Point(this.x,this.y+1).equals(destination))
                toReturn.add(new Point(this.x, this.y + 1));
        }
        return toReturn;
    }
}

