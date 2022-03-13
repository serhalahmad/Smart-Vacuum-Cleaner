package com.iea.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class GuiRandom implements Initializable {
    @FXML
    private Spinner<Integer> dirt;
    @FXML
    private Spinner<Integer> walls;

    public void handleOk(ActionEvent event) {
        Main.game.dirt = dirt.getValue();
        Main.game.wall = walls.getValue();
        Main.game.randomDirt(Main.game.dirt, Main.game.dirtArray);
        Main.game.randomWalls(Main.game.wall);
        Main.game.tileStatus = 0;
        Stage stage = (Stage) dirt.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dirt.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, 15));
        walls.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, 20));
    }
}
