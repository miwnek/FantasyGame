package agh.ics.oop.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Menu extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(this.getClass().getResource("/Menu.fxml"));
        MenuController.setStage(primaryStage);
        primaryStage.setTitle("Menu");
        primaryStage.setScene(new Scene(root,600,400));
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
