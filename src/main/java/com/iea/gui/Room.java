package com.iea.gui;


import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import java.util.*;

public class Room {
    public Wall[][] horWalls;
    public Wall[][] verWalls;
    List<Point> dirtArray;
    List<Point> wallArray;
    Random rnd;
    boolean rumbaIsPlaced;
    int roomWidth;
    int roomHeight;
    int tileStatus;
    int emptyTiles;
    int dirt;
    int wall;
    int prevI;
    int prevJ;
    Tile[][] tiles;
    AgentGood agent;
    ArrayList<Agent> agents;
    Queue<Point> moves;

    Room() {
        moves = new ArrayDeque<>();
        tileStatus = 0;
        rumbaIsPlaced = false;
        rnd = new Random();
        dirtArray = new ArrayList<>();
        agents = new ArrayList<>();
    }

    Room(Room room) {
        roomHeight = room.getHeight();
        roomWidth = room.getWidth();
        moves = new ArrayDeque<>();
        agents = new ArrayList<>();
        dirtArray = new ArrayList<>();
        tiles = new Tile[room.getHeight()][room.getWidth()];
        tileStatus = 0;
        for (Point move:room.moves){
            this.moves.add(move);
        }
        for (int i = 0; i < room.getHeight(); i++) {
            for (int j = 0; j < room.getWidth(); j++) {
                tiles[i][j] = new Tile(room.getTiles()[i][j]);
            }
        }
        for (Agent agent : room.getAgents()) {
            agents.add(new Agent(agent));
        }
        for (Point dirt : room.getDirtArray()) {
            dirtArray.add(new Point(dirt));
        }
    }


    Path getPath(Agent agent) {
        Path fullPath = new Path();
        for (int i = 0; i < agent.getPath().length - 1; i++) {
            Path pathToNode = new Path(agent.getPathBetweenNode()[agent.getPath()[i]][agent.getPath()[i + 1]]);
            for (Point p : pathToNode.path().subList(1, pathToNode.size())) {
                fullPath.addNode(p);
            }
        }
        fullPath.setCost(agent.getCost());
        return fullPath;
    }

    void goToNext(Agent agent, Point p) {
        if (tiles[p.y][p.x].getStatus() != 3) {
            moveTo(agent, p);
            tiles[p.y][p.x].setStatus(3);
        }
    }

    void randomWalls(int number) {
        int row, column;
        for (int i = 0; i < number; i++) {
            double alpha = Math.random();
            row = rnd.nextInt(roomHeight);
            column = rnd.nextInt(roomWidth);
            if (alpha < 0.25) {
                if (!tiles[row][column].hasWallDown())
                    tiles[row][column].AddRemoveWallDown();
            } else if (alpha < 0.5) {
                if (!tiles[row][column].hasWallUp())
                    tiles[row][column].AddRemoveWallUp();
            } else if (alpha < 0.75) {
                if (!tiles[row][column].hasWallLeft())
                    tiles[row][column].AddRemoveWallLeft();
            } else {
                if (!tiles[row][column].hasWallRight())
                    tiles[row][column].AddRemoveWallRight();
            }
        }
    }

    void randomDirt(int number, List<Point> array) {
        Point p;
        int row, column;
        if (number <= emptyTiles) {
            emptyTiles -= number;
        } else {
            number = emptyTiles;
            emptyTiles = 0;
        }
        for (int i = 0; i < number; i++) {
            do {
                row = rnd.nextInt(roomHeight);
                column = rnd.nextInt(roomWidth);
                p = new Point(column, row);
            } while (!tiles[row][column].isClean());
            array.add(p);
            tiles[row][column].setStatus(1);
        }
    }

    EventHandler<MouseEvent> handleMouse = new EventHandler<>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            try {
                int size = (int) tiles[0][0].getWidth();
                int i = (int) ((mouseEvent.getX() - 3) / (size + 5));
                int j = (int) ((mouseEvent.getY() - 3) / (size + 5));
                try {
                    if (prevI == i && prevJ == j)
                        return;
                } catch (Exception e) {
                }
                prevI = i;
                prevJ = j;
                double x = (mouseEvent.getX() - 3) / (size + 5);
                double y = (mouseEvent.getY() - 3) / (size + 5);
                System.out.println("Clicked: " + i + "," + j);
                switch (tileStatus) {
                    case 0 -> {
                    }
                    case 1 -> {
                        if (tiles[j][i].getStatus() != 3) {
                            if (tiles[j][i].isClean()) {
                                dirtArray.add(new Point(i, j));
                                tiles[j][i].setStatus(1);
                                emptyTiles--;
                            } else {
                                dirtArray.remove(new Point(i, j));
                                tiles[j][i].setStatus(0);
                                emptyTiles++;
                            }
                        }
                    }
                    case 2 -> {
                        if (Math.abs(x - i) <= 0.1 || Math.abs(y - j) <= 0.1) {
                            if (Math.abs(x - i) <= Math.abs(y - j)) {
                                tiles[j][i].AddRemoveWallLeft();
                            } else {
                                tiles[j][i].AddRemoveWallUp();
                            }
                        } else {
                            if (Math.abs(x - i) >= Math.abs(y - j)) {
                                tiles[j][i].AddRemoveWallRight();
                            } else {
                                tiles[j][i].AddRemoveWallDown();
                            }
                        }
                    }
                    case 3 -> {
                        if (GuiMain.menu.equals("Fully") || GuiMain.menu.equals("Partly")) {
                            if (tiles[j][i].isClean()) {
                                Point prevPosition = new Point(agent.getPosition());
                                tiles[prevPosition.y][prevPosition.x].setStatus(0);
                                tiles[j][i].setStatus(3);
                                agent.setTranslateX(i * (size + 5) + 3);
                                agent.setTranslateY(j * (size + 5) + 3);
                                agent.setPosition(new Point(i, j));
                            }
                        } else {
                            if (tiles[j][i].isClean()) {
                                Point prevPosition = new Point(agents.get(agents.size() - 1).getPosition());
                                tiles[prevPosition.y][prevPosition.x].setStatus(0);
                                tiles[j][i].setStatus(3);
                                agents.get(agents.size() - 1).setTranslateX(i * (size + 5) + 3);
                                agents.get(agents.size() - 1).setTranslateY(j * (size + 5) + 3);
                                agents.get(agents.size() - 1).setPosition(new Point(i, j));
                            }
                        }
                    }
                }
            } catch (Exception e) {
            }
        }
    };

    public void moveTo(Agent agent, Point point) {
        if (agent.status == 1) {
            Point prev = new Point(agent.getPosition());
            if (agent.getPosition().x < point.x) {
                agent.moveRight();
                dirtify(prev);
            } else if (agent.getPosition().x > point.x) {
                agent.moveLeft();
                dirtify(prev);
            } else if (agent.getPosition().y < point.y) {
                agent.moveDown();
                dirtify(prev);
            } else if (agent.getPosition().y > point.y) {
                agent.moveUp();
                dirtify(prev);
            }
        } else {
            Point prev = new Point(agent.getPosition());
            if (agent.getPosition().x < point.x) {
                clean(point);
                agent.moveRight();
            } else if (agent.getPosition().x > point.x) {
                clean(point);
                agent.moveLeft();
            } else if (agent.getPosition().y < point.y) {
                clean(point);
                agent.moveDown();
            } else if (agent.getPosition().y > point.y) {
                clean(point);
                agent.moveUp();
            }
            tiles[prev.y][prev.x].setStatus(0);
        }
    }

    private void dirtify(Point p) {
        tiles[p.y][p.x].setStatus(1);
        if (!dirtArray.contains(new Point(p))) {
            dirtArray.add(new Point(p));
        }
    }

    private void clean(Point position) {
//        tiles[position.y][position.x].setStatus(0);
        dirtArray.remove(new Point(position));
    }


    public int getWidth() {
        return roomWidth;
    }


    public int getHeight() {
        return roomHeight;
    }

    public void setRoomWidth(int roomWidth) {
        this.roomWidth = roomWidth;
    }

    public void setRoomHeight(int roomHeight) {
        this.roomHeight = roomHeight;
    }

    public ArrayList<Agent> getAgents() {
        return agents;
    }

    public List<Point> getDirtArray() {
        return dirtArray;
    }

    public List<Point> getDirt() {
        return dirtArray;
    }

    public Tile[][] getTiles() {
        return tiles;
    }

}