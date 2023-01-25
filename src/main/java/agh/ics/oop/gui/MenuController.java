package agh.ics.oop.gui;

import agh.ics.oop.EUnits.UnitType;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MenuController implements Initializable{
    private static Stage primaryStage;
    private String playerOneArmy = "Undead";
    private String playerTwoArmy = "Undead";

    @FXML
    private ChoiceBox<String> armyOne;
    @FXML
    private ChoiceBox<String> armyTwo;
    private final String[] armies = {"Undead", "Orcs"};

    public void confirm(ActionEvent e) throws Exception {
        System.out.println("Confirmed");
        new SecondMenu(primaryStage, playerOneArmy, playerTwoArmy);
        //app.start(primaryStage);
    }

    public static void setStage(Stage s) {
        primaryStage = s;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        armyOne.getItems().addAll(armies);
        armyOne.setOnAction(this::getArmyOne);
        armyOne.setValue("Undead");
        armyTwo.getItems().addAll(armies);
        armyTwo.setOnAction(this::getArmyTwo);
        armyTwo.setValue("Orcs");
    }

    public void getArmyOne(ActionEvent e) {
        playerOneArmy = armyOne.getValue();
        System.out.println(playerOneArmy);
    }
    public void getArmyTwo(ActionEvent e) {
        playerTwoArmy = armyTwo.getValue();
        System.out.println(playerTwoArmy);

    }
}
