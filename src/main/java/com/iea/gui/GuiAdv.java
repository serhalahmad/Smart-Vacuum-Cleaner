package com.iea.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class GuiAdv implements Initializable {
    private Stage stage;
    private static int size;
    @FXML
    public Group room;
    @FXML
    private AnchorPane pane;
    @FXML
    private Spinner<Integer> depth;
    private Timer timer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        depth.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 5));
        try {
            int pWidth = (int) pane.getPrefWidth();
            int pHeight = (int) pane.getPrefHeight();
            System.out.println(pWidth + "," + pHeight);
            int w = Main.game.getWidth();
            int h = Main.game.getHeight();
            size = Math.min((pWidth / w), (pHeight / h)) - 5;
            System.out.println("size=" + size);
            Main.game.tiles = new Tile[Main.game.getHeight()][Main.game.getWidth()];
            Main.game.verWalls = new Wall[Main.game.getHeight()][Main.game.getWidth()];
            Main.game.horWalls = new Wall[Main.game.getHeight()][Main.game.getWidth()];
            System.out.println("Adding tiles");
            drawRoom();
        } catch (Exception e) {
        }
    }

    private void drawRoom() {
        for (int i = 0; i < Main.game.getHeight(); i++) {
            for (int j = 0; j < Main.game.getWidth(); j++) {
                Main.game.tiles[i][j] = new Tile(i, j, j * (size + 5) + 3, i * (size + 5) + 3, size, size);
                room.getChildren().add(Main.game.tiles[i][j]);
                room.getChildren().add(Main.game.tiles[i][j].getWallDown());
                room.getChildren().add(Main.game.tiles[i][j].getWallRight());
            }
        }
        room.addEventFilter(MouseEvent.MOUSE_CLICKED, Main.game.handleMouse);
        room.addEventFilter(MouseEvent.MOUSE_DRAGGED, Main.game.handleMouse);
    }


    public void handleReset(ActionEvent event) {
        try {
            timer.cancel();
        }catch (Exception e){}
        timer.cancel();
        timer.purge();
        stage = (Stage) room.getScene().getWindow();
        stage.setScene(Main.scene1);
        Main.game = new Room();
    }


    public void handleRandom(ActionEvent event) throws IOException {
        FXMLLoader GuiRandom = new FXMLLoader(Main.class.getResource("GuiRandom.fxml"));
        Scene scene3 = new Scene(GuiRandom.load());
        Stage stage = new Stage();
        stage.setScene(scene3);
        stage.show();
    }

    public void handleAddWalls(ActionEvent event) {
        Main.game.tileStatus = 2;
    }

    public void handleAddDirt(ActionEvent event) {
        Main.game.tileStatus = 1;
    }

    public static int getSize() {
        return size;
    }

    public void handleChooseDim(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("GuiDimensions.fxml"));
        Scene guiDim = new Scene(fxmlLoader.load());
        stage = (Stage) room.getScene().getWindow();
        stage.setScene(guiDim);
    }

    public boolean handleGoodMiniMax(ActionEvent event) {
        Main.game.emptyTiles = (Main.game.getWidth() * Main.game.getHeight()) - Main.game.agents.size();
        for (int i = 0; i < Main.game.getWidth(); i++) {
            for (int j = 0; j < Main.game.getHeight(); j++) {
                if (Main.game.tiles[j][i].isClean()) {
                    Agent agent = new AgentGood(new Point(i, j), i * (size + 5) + 3, j * (size + 5) + 3, size, size, "GoodMinimax");
                    agent.setDepth(depth.getValue());
                    Main.game.agents.add(agent);
                    Main.game.tiles[j][i].setStatus(3);
                    Main.game.tileStatus = 3;
                    return room.getChildren().add(Main.game.agents.get(Main.game.agents.size() - 1));
                }
            }
        }
        return false;
    }

    public boolean handleGoodAlphaBeta(ActionEvent event) {
        Main.game.emptyTiles = (Main.game.getWidth() * Main.game.getHeight()) - Main.game.agents.size();
        for (int i = 0; i < Main.game.getWidth(); i++) {
            for (int j = 0; j < Main.game.getHeight(); j++) {
                if (Main.game.tiles[j][i].isClean()) {
                    Agent agent = new AgentGood(new Point(i, j), i * (size + 5) + 3, j * (size + 5) + 3, size, size, "GoodAlphaBeta");
                    agent.setDepth(depth.getValue());
                    Main.game.agents.add(agent);
                    Main.game.tiles[j][i].setStatus(3);
                    Main.game.tileStatus = 3;
                    return room.getChildren().add(agent);
                }
            }
        }
        return false;
    }

    public boolean handleGoodExpectimax(ActionEvent event) {
        Main.game.emptyTiles = (Main.game.getWidth() * Main.game.getHeight()) - Main.game.agents.size();
        for (int i = 0; i < Main.game.getWidth(); i++) {
            for (int j = 0; j < Main.game.getHeight(); j++) {
                if (Main.game.tiles[j][i].isClean()) {
                    Agent agent = new AgentGood(new Point(i, j), i * (size + 5) + 3, j * (size + 5) + 3, size, size, "GoodExpectimax");
                    agent.setDepth(depth.getValue());
                    Main.game.agents.add(agent);
                    Main.game.tiles[j][i].setStatus(3);
                    Main.game.tileStatus = 3;
                    return room.getChildren().add(Main.game.agents.get(Main.game.agents.size() - 1));
                }
            }
        }
        return false;
    }

    public boolean handleBadMinimax(ActionEvent event) {
        Main.game.emptyTiles = (Main.game.getWidth() * Main.game.getHeight()) - Main.game.agents.size();
        for (int i = 0; i < Main.game.getWidth(); i++) {
            for (int j = 0; j < Main.game.getHeight(); j++) {
                if (Main.game.tiles[j][i].isClean()) {
                    Agent agent = new AgentBad(new Point(i, j), i * (size + 5) + 3, j * (size + 5) + 3, size, size, "BadMinimax");
                    agent.setDepth(depth.getValue());
                    Main.game.agents.add(agent);
                    Main.game.tiles[j][i].setStatus(3);
                    Main.game.tileStatus = 3;
                    return room.getChildren().add(agent);
                }
            }
        }
        return false;
    }

    public boolean handleBadAlphaBeta(ActionEvent event) {
        Main.game.emptyTiles = (Main.game.getWidth() * Main.game.getHeight()) - Main.game.agents.size();
        for (int i = 0; i < Main.game.getWidth(); i++) {
            for (int j = 0; j < Main.game.getHeight(); j++) {
                if (Main.game.tiles[j][i].isClean()) {
                    Agent agent = new AgentBad(new Point(i, j), i * (size + 5) + 3, j * (size + 5) + 3, size, size, "BadAlphaBeta");
                    agent.setDepth(depth.getValue());
                    Main.game.agents.add(agent);
                    Main.game.tiles[j][i].setStatus(3);
                    Main.game.tileStatus = 3;
                    return room.getChildren().add(agent);
                }
            }
        }
        return false;
    }


    public void handleSolve(ActionEvent event) {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    for (Agent agent : Main.game.agents) {
                        System.out.println("Gain: "+agent.start(Main.game, true));
//                        System.out.println("Agent Position: "+agent.getPosition()+"\nMove: "+agent.getNextMove());
                        Main.game.goToNext(agent,agent.getNextMove());
                    }
                    System.out.println("\n\n\n");
                } catch (Exception e) {
                    timer.purge();
                    timer.cancel();
                }
            }
        }, 0, 250);
    }
}


