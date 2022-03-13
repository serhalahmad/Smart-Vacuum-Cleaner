package com.iea.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class GuiGenetic implements Initializable {
    @FXML
    private Spinner<Integer> population;
    @FXML
    private Spinner<Integer> epochs;
    @FXML
    private Spinner<Double> crossOverRate;
    @FXML
    private Spinner<Double> mutationRate;

    public GuiGenetic() {
    }

    public void handleOk(ActionEvent event) {
        Main.game.agent.population=population.getValue();
        Main.game.agent.epoch=epochs.getValue();
        Main.game.agent.crossOverRate=crossOverRate.getValue();
        Main.game.agent.mutationRate=mutationRate.getValue();
        Stage stage = (Stage) population.getScene().getWindow();
        stage.close();
        if(GuiMain.geneticCaller.equals("GuiFully")){
            GuiFully.runGenetic();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        population.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100000, 1000));
        epochs.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100000, 100));
        crossOverRate.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 1, 0.7,0.1));
        mutationRate.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 1, 0.3,0.1));
    }
}
