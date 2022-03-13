package com.iea.gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;


public class GuiMain {
    @FXML
    private Button fullyObs;
    Stage stage;
    public static String menu;
    public static String geneticCaller;
    private void getStage() {
        stage = (Stage) fullyObs.getScene().getWindow();

    }

    public void handleFullyObs(ActionEvent event) throws IOException {
        menu="Fully";
        getStage();
        FXMLLoader GuiFully = new FXMLLoader(Main.class.getResource("GuiFully.fxml"));
        Scene scene1 = new Scene(GuiFully.load());
        stage.setScene(scene1);
        stage.setMaximized(true);
    }

    public void handlePartlyObs(ActionEvent event) throws IOException {
        menu="Partly";
        getStage();
        FXMLLoader GuiPartly = new FXMLLoader(Main.class.getResource("GuiPartly.fxml"));
        Scene scene2 = new Scene(GuiPartly.load());
        stage.setScene(scene2);
        stage.setMaximized(true);
    }

    public void handleAdversarial(ActionEvent event) throws IOException {
        menu="Adv";
        getStage();
        FXMLLoader GuiAdv = new FXMLLoader(Main.class.getResource("GuiAdv.fxml"));
        Scene scene3 = new Scene(GuiAdv.load());
        stage.setScene(scene3);
        stage.setMaximized(true);
    }
}
