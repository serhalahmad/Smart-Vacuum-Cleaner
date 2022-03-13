package com.iea.gui;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class AgentGood extends Agent {
    Point nextMove;
    int maxGain;
    int minGain;
    int depth = 5;
    int count;
    Image rumba;

    public AgentGood(Point position, int x, int y, int w, int h, String type) {
        super(position, x, y, w, h, type);
        nextMove = new Point(getPosition());
        rumba = new Image(String.valueOf(AgentGood.class.getResource("Images/Rumba.png")));
        setFill(new ImagePattern(rumba));
        status = 0;
    }

    public AgentGood(AgentGood agent) {
        super(agent);
        rumba = new Image(String.valueOf(AgentGood.class.getResource("Images/Rumba.png")));
        setFill(new ImagePattern(rumba));
        status = 0;
    }

    public AgentGood(AgentGood agent, int x, int y, int size) {
        super(agent, x, y, size);
    }

    public int start(Room room, boolean isRumba) {
        maxGain = Integer.MIN_VALUE;
        minGain = Integer.MAX_VALUE;
        Main.game.moves = new ArrayDeque<>();
        nextMove = getPosition();
        if (type.endsWith("Minimax")) {
            return minimax(Main.game, depth, isRumba);
        } else if (type.endsWith("AlphaBeta")) {
            return minimax(Main.game, depth, isRumba, Integer.MIN_VALUE, Integer.MAX_VALUE);
        } else {
            minGain = 0;
            return expectimax(Main.game, depth, isRumba);
        }
    }


    public int minimax(Room room, int moves, boolean isRumba) {
        if (done(room, moves)) {
            return evaluate(room);
        }
        if (isRumba) {
            ArrayList<Point> neighbors = room.agents.get(Main.game.agents.indexOf(this)).getPosition().getNeighbors(room);
            for (Point n : neighbors) {
                Room room1 = new Room(room);
                room1.goToNext(room1.agents.get(Main.game.agents.indexOf(this)), new Point(n));
                room1.moves.offer(new Point(n));
                int gain = minimax(room1, moves - 1, false);
                if (maxGain < gain) {
                    maxGain = gain;
                    nextMove = new Point(room1.moves.peek());
                }
            }
            return maxGain;
        } else {
            for (Agent agent : room.agents) {
                if (agent.type.startsWith("Bad")) {
                    ArrayList<Point> neighbors = agent.getPosition().getNeighbors(room);
                    for (Point n : neighbors) {
                        if (room.tiles[n.y][n.x].getStatus() != 1) {
                            Room room2 = new Room(room);
                            room2.goToNext(room2.agents.get(room.agents.indexOf(agent)), new Point(n));
                            int gain = minimax(room2, moves - 1, true);
                            if (minGain > gain) {
                                minGain = gain;
                            }
                        }
                    }
                }
            }
            if (minGain == Integer.MAX_VALUE)
                return minimax(room, moves - 1, true);
            else
                return minGain;
        }
    }

    private boolean done(Room room, int moves) {
        return moves == 0 || room.agents.get(Main.game.agents.indexOf(this)).getPosition().getNeighbors(room).size() == 0;
    }

    public int minimax(Room room, int moves, boolean isRumba, int alpha, int beta) {
        if (done(room, moves)) {
            return evaluate(room);
        }
        if (isRumba) {
            ArrayList<Point> neighbors = room.agents.get(Main.game.agents.indexOf(this)).getPosition().getNeighbors(room);
            for (Point n : neighbors) {
                Room room1 = new Room(room);
                room1.goToNext(room1.agents.get(Main.game.agents.indexOf(this)), new Point(n));
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
            return maxGain;
        } else {
            for (Agent agent : room.agents) {
                if (agent.type.startsWith("Bad")) {
                    ArrayList<Point> neighbors = agent.getPosition().getNeighbors(room);
                    for (Point n : neighbors) {
                        if (room.tiles[n.y][n.x].getStatus() != 1) {
                            Room room2 = new Room(room);
                            room2.goToNext(room2.agents.get(room.agents.indexOf(agent)), new Point(n));
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
            }
            if (minGain == Integer.MAX_VALUE)
                return minimax(room, moves - 1, true, alpha, beta);
            else
                return minGain;
        }
    }

    public int expectimax(Room room, int moves, boolean isRumba) {
        if (done(room, moves)) {
            return evaluate(room);
        }
        if (isRumba) {
            ArrayList<Point> neighbors = room.agents.get(Main.game.agents.indexOf(this)).getPosition().getNeighbors(room);
            for (Point n : neighbors) {
                Room room1 = new Room(room);
                room1.goToNext(room1.agents.get(Main.game.agents.indexOf(this)), new Point(n));
                room1.moves.offer(new Point(n));
                int gain = expectimax(room1, moves - 1, false);
                if (maxGain < gain) {
                    maxGain = gain;
                    nextMove = new Point(room1.moves.peek());
                }
            }
            return maxGain;
        } else {
            for (Agent agent : room.agents) {
                if (agent.type.startsWith("Bad")) {
                    count=0;
                    ArrayList<Point> neighbors = agent.getPosition().getNeighbors(room);
                    for (Point n : neighbors) {
                        if (room.tiles[n.y][n.x].getStatus() != 1) {
                            Room room2 = new Room(room);
                            room2.goToNext(room2.agents.get(room.agents.indexOf(agent)), new Point(n));
                            minGain += expectimax(room2, moves - 1, true);
                            count++;
                        }
                    }
                    if (count != 0)
                        minGain /= count;
                }
            }
            if (minGain == 0)
                return expectimax(room, moves - 1, true);
            else
                return minGain;
        }
    }

    private int evaluate(Room room) {
        int toReturn = 2*room.dirtArray.size();
        for (Agent agent : room.agents) {
            if (agent.type.startsWith("Bad")) {
                toReturn += room.agents.get(Main.game.agents.indexOf(this)).getDistance(room, agent.getPosition());
            } else {
                toReturn -= room.agents.get(Main.game.agents.indexOf(this)).getDistance(room, agent.getPosition());
            }
        }
        int min = Integer.MAX_VALUE;
        for (Point p : room.dirtArray) {
            min = Math.min(min, room.agents.get(Main.game.agents.indexOf(this)).getDistance(room, p));
        }
        toReturn += min ;
        return -toReturn;
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
