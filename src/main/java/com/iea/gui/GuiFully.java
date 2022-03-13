package com.iea.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class GuiFully implements Initializable {
    private Stage stage;
    private int size;
    @FXML
    public Group room;
    @FXML
    private AnchorPane pane;
    int delay = 300;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            int pWidth = (int) pane.getPrefWidth();
            int pHeight = (int) pane.getPrefHeight();
            int w = Main.game.getWidth();
            int h = Main.game.getHeight();
            System.out.println(w + "," + h);
            size = Math.min((pWidth / w), (pHeight / h)) - 5;
            Main.game.tiles = new Tile[Main.game.getHeight()][Main.game.getWidth()];
            Main.game.verWalls = new Wall[Main.game.getHeight()][Main.game.getWidth()];
            Main.game.horWalls = new Wall[Main.game.getHeight()][Main.game.getWidth()];
            drawRoom();
        } catch (Exception ignored) {
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
    public static void runGenetic(){
        Main.game.agent.generateAdjacencyMatrix(Main.game.dirtArray, Main.game.tiles, false);
        Main.game.agent.genetic();
        Path path = Main.game.getPath(Main.game.agent);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    Point p = path.path().remove(0);
//                    System.out.println(p);
                    Main.game.goToNext(Main.game.agent, p);
                } catch (Exception ignored) {
                    timer.purge();
                    timer.cancel();
                }
            }
        }, 0, 300);
    }

    public boolean handleAddAgent(ActionEvent event) {
        if (!Main.game.rumbaIsPlaced) {
            Main.game.rumbaIsPlaced = true;
            Main.game.emptyTiles = (Main.game.getWidth() * Main.game.getHeight()) - 1;
            for (int i = 0; i < Main.game.getHeight(); i++) {
                for (int j = 0; j < Main.game.getWidth(); j++) {
                    if (Main.game.tiles[i][j].isClean()) {
                        Main.game.agent = new AgentGood(new Point(j, i), i * (size + 5) + 3, j * (size + 5) + 3, size, size, "Agent");
                        Main.game.tileStatus = 3;
                        return room.getChildren().add(Main.game.agent);
                    }
                }
            }
        }
        return false;
    }

    public void handleReset(ActionEvent event) {
        stage = (Stage) room.getScene().getWindow();
        stage.setScene(Main.scene1);
        Main.game = new Room();
    }

    public void handleBruteForce(ActionEvent event) {
        if (Main.game.rumbaIsPlaced) {
            Main.game.agent.generateAdjacencyMatrix(Main.game.dirtArray, Main.game.tiles, false);
            Main.game.agent.bruteForce();
            Path path = Main.game.getPath(Main.game.agent);
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    try {
                        Point p = path.path().remove(0);
//                    System.out.println(p);
                        Main.game.goToNext(Main.game.agent, p);
                    } catch (Exception e) {
                        timer.purge();
                        timer.cancel();
                    }
                }
            }, 0, delay);
        }
    }

    public void handleLCBB(ActionEvent event) {
        if (Main.game.rumbaIsPlaced) {
            Main.game.agent.generateAdjacencyMatrix(Main.game.dirtArray, Main.game.tiles, false);
            Main.game.agent.branchAndBound();
            Path path = Main.game.getPath(Main.game.agent);
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    try {
                        Point p = path.path().remove(0);
//                    System.out.println(p);
                        Main.game.goToNext(Main.game.agent, p);
                    } catch (Exception ignored) {
                        timer.purge();
                        timer.cancel();
                    }
                }
            }, 0, delay);
        }
    }

    public void handleNN(ActionEvent event) {
        if (Main.game.rumbaIsPlaced) {
            Main.game.agent.generateAdjacencyMatrix(Main.game.dirtArray, Main.game.tiles, false);
            Main.game.agent.nearestNeighbor();
            Path path = Main.game.getPath(Main.game.agent);
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    try {
                        Point p = path.path().remove(0);
//                    System.out.println(p);
                        Main.game.goToNext(Main.game.agent, p);
                    } catch (Exception ignored) {
                        timer.purge();
                        timer.cancel();
                    }
                }
            }, 0, delay);
        }
    }

    public void handleGenetic(ActionEvent event) throws IOException {
        if (Main.game.rumbaIsPlaced) {
            GuiMain.geneticCaller="GuiFully";
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("GuiGenetic.fxml"));
            Scene guiGen = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setScene(guiGen);
            stage.show();
        }
    }

    public void handleRandom(ActionEvent event) throws IOException {
        if (Main.game.rumbaIsPlaced) {
            FXMLLoader GuiRandom = new FXMLLoader(Main.class.getResource("GuiRandom.fxml"));
            Scene scene3 = new Scene(GuiRandom.load());
            Stage stage = new Stage();
            stage.setScene(scene3);
            stage.show();
        }
    }

    public void handleAddWalls(ActionEvent event) {
        if (Main.game.rumbaIsPlaced) {
            Main.game.tileStatus = 2;
        }
    }

    public void handleAddDirt(ActionEvent event) {
        if (Main.game.rumbaIsPlaced) {
            Main.game.tileStatus = 1;
        }
    }

    public int getSize() {
        return size;
    }

    public void handleChooseDim(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("GuiDimensions.fxml"));
        Scene guiDim = new Scene(fxmlLoader.load());
        stage = (Stage) room.getScene().getWindow();
        stage.setScene(guiDim);
    }

    public void handleCompare(ActionEvent event) throws IOException {
        if (Main.game.rumbaIsPlaced) {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("GuiCompare.fxml"));
            Scene guiComp = new Scene(fxmlLoader.load());
            stage = (Stage) room.getScene().getWindow();
            stage.setScene(guiComp);
        }
    }

}
