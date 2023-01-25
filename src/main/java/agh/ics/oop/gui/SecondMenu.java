package agh.ics.oop.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SecondMenu {
    private   Stage primaryStage;
    private  String playerOneArmy;
    private  String playerTwoArmy;
    private  Parent sndRoot;

    public SecondMenu(Stage ps, String poa, String pta) throws Exception {
        primaryStage = ps;
        playerOneArmy = poa;
        playerTwoArmy = pta;
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/SecondMenu.fxml"));
        sndRoot = fxmlLoader.load();
        SecondMenuController controller  = fxmlLoader.getController();
        controller.setValues(playerOneArmy,playerTwoArmy);
        controller.setStage(primaryStage);
        primaryStage.setScene(new Scene(sndRoot));

    }



}
