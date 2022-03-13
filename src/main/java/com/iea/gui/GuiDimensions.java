package com.iea.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GuiDimensions implements Initializable {
    @FXML
    private Button ok;
    @FXML
    private Spinner<Integer> width;
    @FXML
    private Spinner<Integer> height;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        width.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 100, 7));
        height.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 100, 7));
    }

    public void handleOk(ActionEvent event) throws IOException {
        FXMLLoader GuiRoom = null;
        System.out.println("Width: " + width.getValue() + "\n" + "Height: " + height.getValue());
        Stage stage = (Stage) ok.getScene().getWindow();
        Main.game.setRoomWidth(width.getValue());
        Main.game.setRoomHeight(height.getValue());
        if (GuiMain.menu.equals("Fully")) {
            GuiRoom = new FXMLLoader(Main.class.getResource("GuiFully.fxml"));
        } else if (GuiMain.menu.equals("Partly")) {
            GuiRoom = new FXMLLoader(Main.class.getResource("GuiPartly.fxml"));
        } else if (GuiMain.menu.equals("Adv")) {
            GuiRoom = new FXMLLoader(Main.class.getResource("GuiAdv.fxml"));
        }
        Scene scene = new Scene(GuiRoom.load());
        stage.setScene(scene);
        stage.setMaximized(true);
    }

}