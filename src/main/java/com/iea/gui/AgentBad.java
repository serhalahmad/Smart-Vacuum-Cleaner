package com.iea.gui;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.ArrayList;

public class AgentBad extends Agent {
    Point nextMove;
    int maxGain;
    int minGain;
    int depth = 5;
    static Image bad;

    static {
        bad = new Image(String.valueOf(AgentBad.class.getResource("Images/Garfield.png")));
    }

    //    Point position;
    AgentBad(Point position, int x, int y, int w, int h, String type) {
        super(position, x, y, w, h, type);
        nextMove = new Point(getPosition());
//        this.position=position;
        status = 1;
        setFill(new ImagePattern(bad));
    }

    AgentBad(AgentBad agent) {
        super(agent);
        status = 1;
        setFill(new ImagePattern(bad));
    }

    public int start(Room room, boolean notRumba) {
        maxGain = Integer.MIN_VALUE;
        minGain = Integer.MAX_VALUE;
        Main.game.moves = new ArrayDeque<>();
        nextMove = getPosition();
        if (type.endsWith("Minimax")) {
            return minimax(Main.game, depth, notRumba);
        } else {
            return minimax(Main.game, depth, notRumba, Integer.MIN_VALUE, Integer.MAX_VALUE);
        }
    }

    public int minimax(Room room, int moves, boolean notRumba) {
        if (done(room, moves)) {
            return room.dirtArray.size();
        }
        if (notRumba) {
            ArrayList<Point> neighbors = room.agents.get(Main.game.agents.indexOf(this)).getPosition().getNeighbors(room);
            for (Point n : neighbors) {
                if (room.tiles[n.y][n.x].getStatus() != 1) {
                    Room room1 = new Room(room);
                    room1.goToNext(room1.agents.get(Main.game.agents.indexOf(this)), n);
                    room1.moves.offer(new Point(n));
                    int gain = minimax(room1, moves - 1, false);
                    if (maxGain < gain) {
                        maxGain = gain;
                        nextMove = new Point(room1.moves.peek());
                    }
                }
            }
            return maxGain;
        } else {
            for (Agent agent : room.agents) {
                if (agent.type.startsWith("Good")) {
                    ArrayList<Point> neighbors = agent.getPosition().getNeighbors(room);
                    for (Point n : neighbors) {
                        Room room2 = new Room(room);
                        room2.goToNext(room2.agents.get(room.agents.indexOf(agent)), n);
                        int gain = minimax(room2, moves - 1, true);
                        if (minGain > gain) {
                            minGain = gain;
                        }
                    }
                }
            }
            return minimax(room, moves - 1, true);
        }
    }

    private boolean done(Room room, int moves) {
        boolean noTiles = true;
        for (Point p : room.agents.get(Main.game.agents.indexOf(this)).getPosition().getNeighbors(room)) {
            if (room.tiles[p.y][p.x].getStatus() == 0) {
                noTiles = false;
            }
        }
        return moves == 0 || noTiles || room.agents.get(Main.game.agents.indexOf(this)).getPosition().getNeighbors(room).size() == 0;
    }

    public int minimax(Room room, int moves, boolean notRumba, int alpha, int beta) {
        if (done(room, moves)) {
            return room.dirtArray.size();
        }
        if (notRumba) {
            ArrayList<Point> neighbors = room.agents.get(Main.game.agents.indexOf(this)).getPosition().getNeighbors(room);
            for (Point n : neighbors) {
                if (room.tiles[n.y][n.x].getStatus() != 1) {
                    Room room1 = new Room(room);
                    room1.goToNext(room1.agents.get(Main.game.agents.indexOf(this)), n);
                    room1.moves.offer(new Point(n));
                    int gain = minimax(room1, moves - 1, false, alpha, beta);
                    if (maxGain < gain) {
                        maxGain = gain;
                        nextMove = new Point(room1.moves.peek());
                    }
                    alpha = Math.max(alpha, maxGain);
                    if (beta <= alpha) {
                        break;
                    }
                }
            }
            return maxGain;
        } else {
            for (Agent agent : room.agents) {
                if (agent.type.startsWith("Good")) {
                    ArrayList<Point> neighbors = agent.getPosition().getNeighbors(room);
                    for (Point n : neighbors) {
                        Room room2 = new Room(room);
                        room2.goToNext(room2.agents.get(room.agents.indexOf(agent)), n);
                        int gain = minimax(room2, moves - 1, true, alpha, beta);
                        if (minGain > gain) {
                            minGain = gain;
                        }
                        beta = Math.min(beta, minGain);
                        if (beta <= alpha) {
                            break;
                        }
                    }
                }
            }
            return minimax(room, moves - 1, true, alpha, beta);
        }
    }

    public Point getNextMove() {
        return nextMove;
    }

    public void setNextMove(Point nextMove) {
        this.nextMove = nextMove;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }
}
