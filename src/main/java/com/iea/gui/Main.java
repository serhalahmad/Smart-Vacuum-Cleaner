package com.iea.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static Scene scene1;
    public static Room game;
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader GuiMain = new FXMLLoader(Main.class.getResource("GuiMain.fxml"));
            scene1 = new Scene(GuiMain.load());
            stage.setTitle("Rumba");
            stage.setScene(scene1);
            stage.show();
            stage.setOnCloseRequest(windowEvent -> {
                Platform.exit();
                System.exit(0);
            });
            game=new Room();
        }catch (Exception ignored){}
    }

    public static void main(String[] args) {
        launch();
    }
}