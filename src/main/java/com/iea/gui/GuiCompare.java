package com.iea.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class GuiCompare implements Initializable {
    private Stage stage;
    private Room r;
    private static int size;
    @FXML
    public Group room;
    @FXML
    private AnchorPane pane;
    @FXML
    private CheckBox bruteForce, branchAndBound, nearestNeighbor, genetic;
    private Path brute, branch, nearest, gen;
    private int delay = 300;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            int pWidth = (int) pane.getPrefWidth();
            int pHeight = (int) pane.getPrefHeight();
            int w = Main.game.getWidth();
            int h = Main.game.getHeight();
            size = Math.min((pWidth / w), (pHeight / h)) - 5;
            System.out.println(pWidth + "," + pHeight);
            r = drawRoom(Main.game);
        } catch (Exception e) {
        }
    }

    public Room drawRoom(Room room1) {
        Room r = new Room(room1);
        r.agent = new AgentGood(new Point(room1.agent.getPosition()), room1.agent.getPosition().x * (size + 5) + 3, room1.agent.getPosition().y * (size + 5) + 3, size, size, "");
        for (int i = 0; i < r.getHeight(); i++) {
            for (int j = 0; j < r.getWidth(); j++) {
                r.tiles[i][j] = new Tile(Main.game.tiles[i][j], i, j, j * (size + 5) + 3, i * (size + 5) + 3, size);
                r.tiles[i][j].setBlendMode(BlendMode.MULTIPLY);
                room.getChildren().add(r.tiles[i][j]);
                room.getChildren().add(r.tiles[i][j].getWallDown());
                room.getChildren().add(r.tiles[i][j].getWallRight());
            }
        }
        room.getChildren().add(r.agent);
        return r;
    }

    public void handleStart(ActionEvent event) {
        Main.game.agent.generateAdjacencyMatrix(Main.game.dirtArray, Main.game.tiles, false);
        if (branchAndBound.isSelected()) {
            Main.game.agent.branchAndBound();
            branch = new Path(Main.game.getPath(Main.game.agent));
        }
        if (bruteForce.isSelected()) {
            Main.game.agent.bruteForce();
            brute = new Path(Main.game.getPath(Main.game.agent));
        }
        if (nearestNeighbor.isSelected()) {
            Main.game.agent.nearestNeighbor();
            nearest = new Path(Main.game.getPath(Main.game.agent));
        }
        if (genetic.isSelected()) {
            Main.game.agent.genetic();
            gen = new Path(Main.game.getPath(Main.game.agent));
        }
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    if (bruteForce.isSelected()) {
                        Point p = brute.path().remove(0);
                        r.tiles[p.y][p.x].setFill(Color.YELLOW);
                    }
                    if (branchAndBound.isSelected()) {
                        Point p = branch.path().remove(0);
                        r.tiles[p.y][p.x].setFill(Color.GREEN);
                    }
                    if (nearestNeighbor.isSelected()) {
                        Point p = nearest.path().remove(0);
                        r.tiles[p.y][p.x].setFill(Color.RED);
                    }
                    if (genetic.isSelected()) {
                        Point p = gen.path().remove(0);
                        r.tiles[p.y][p.x].setFill(Color.BLUE);
                    }
                } catch (Exception e) {
                    timer.purge();
                    timer.cancel();
                }
            }
        }, 0, delay);
    }

    public void handleGenetic(ActionEvent event) throws IOException {
        GuiMain.geneticCaller="GuiCompare";
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("GuiGenetic.fxml"));
        Scene guiGen = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setScene(guiGen);
        stage.show();
    }
    public void handleReset(ActionEvent event) {
        stage = (Stage) room.getScene().getWindow();
        stage.setScene(Main.scene1);
        Main.game = new Room();
    }
}
