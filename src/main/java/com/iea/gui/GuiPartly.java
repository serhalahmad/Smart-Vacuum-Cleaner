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

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class GuiPartly implements Initializable {
    private Stage stage;
    private static int size;
    @FXML
    public Group room;
    @FXML
    private AnchorPane pane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            int pWidth = (int) pane.getPrefWidth();
            int pHeight = (int) pane.getPrefHeight();
            int w = Main.game.getWidth();
            int h = Main.game.getHeight();
            size = Math.min((pWidth / w), (pHeight / h)) - 5;
            System.out.println(pWidth + "," + pHeight);
            Main.game.tiles = new Tile[Main.game.getHeight()][Main.game.getWidth()];
            Main.game.verWalls = new Wall[Main.game.getHeight()][Main.game.getWidth()];
            Main.game.horWalls = new Wall[Main.game.getHeight()][Main.game.getWidth()];
            System.out.println("Adding tiles");
            drawRoom(Main.game);
        } catch (Exception e) {
        }
    }

    public void drawRoom(Room r) {
        for (int i = 0; i < r.getHeight(); i++) {
            for (int j = 0; j < r.getWidth(); j++) {
                r.tiles[i][j] = new Tile(i, j, j * (size + 5) + 3, i * (size + 5) + 3, size, size);
                room.getChildren().add(r.tiles[i][j]);
                room.getChildren().add(r.tiles[i][j].getWallDown());
                room.getChildren().add(r.tiles[i][j].getWallRight());
            }
        }
        room.addEventFilter(MouseEvent.MOUSE_CLICKED, r.handleMouse);
        room.addEventFilter(MouseEvent.MOUSE_DRAGGED, r.handleMouse);
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

    public void handleRandom(ActionEvent event) throws IOException {
        FXMLLoader GuiRandom = new FXMLLoader(Main.class.getResource("GuiRandom.fxml"));
        Scene scene3 = new Scene(GuiRandom.load());
        Stage stage = new Stage();
        stage.setScene(scene3);
        stage.show();
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

    public static int getSize() {
        return size;
    }

    public void handleChooseDim(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("GuiDimensions.fxml"));
        Scene guiDim = new Scene(fxmlLoader.load());
        stage = (Stage) room.getScene().getWindow();
        stage.setScene(guiDim);
    }

    public void handleSolve(ActionEvent event) {
        Main.game.agent.partiallyObservable();
    }
}

