package agh.ics.oop.gui;

import agh.ics.oop.EUnits;
import agh.ics.oop.EUnits.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SecondMenuController implements Initializable {
    private  static String playerOneArmy;
    private  static String playerTwoArmy;
    private static final String[] armies = {"Undead", "Orcs"};

    private final EUnits.UnitType[] undeadArmy = {EUnits.UnitType.ZOMBIE, EUnits.UnitType.SKELETON, EUnits.UnitType.VAMPIRE, EUnits.UnitType.NECROMANCER, EUnits.UnitType.DEATHKNIGHT};
    @FXML
    private  Button confirmButton;
    @FXML
    private  ChoiceBox<String> armyOneTwo;
    @FXML
    private  ChoiceBox<String> armyTwoTwo;

    @FXML
    private ImageView p1u1, p1u2, p1u3, p1u4, p1u5;
    @FXML
    private ImageView p2u1, p2u2, p2u3, p2u4, p2u5;

    public  void setValues(String poa, String pta) {
        playerOneArmy = poa;
        playerTwoArmy = pta;
        armyOneTwo.setValue(playerOneArmy);
        armyTwoTwo.setValue(playerTwoArmy);
        armyOneTwo.setDisable(true);
        armyTwoTwo.setDisable(true);
        confirmButton.setDisable(true);

        try {
            unitSelection(1);
            unitSelection(2);
        } catch (FileNotFoundException e){
            System.out.println("File not found");
        }

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        armyOneTwo.getItems().addAll(armies);
        armyTwoTwo.getItems().addAll(armies);
    }

    public void unitSelection(int player) throws FileNotFoundException {
        EUnits.UnitType[] armyTable = {EUnits.UnitType.SKELETON};
        ImageView[] imageTable;
        if(player == 1) { imageTable = new ImageView[]{p1u1, p1u2, p1u3, p1u4, p1u5};}
        else { imageTable = new ImageView[]{p2u1, p2u2, p2u3, p2u4, p2u5};}


        switch(playerOneArmy) {
            case "Undead" -> armyTable = undeadArmy;
            case "Orcs" -> System.out.println("Not implemented yet");
            default -> System.out.println("Only undead for now");
        }
        for(int i = 0; i < 5; i++) {
            imageTable[i].setImage(new Image(new FileInputStream(getPicturePath(armyTable[i]))));
        }
    }

    public String getPicturePath(UnitType unit) {
        switch(unit) {
            case ZOMBIE      -> {return "src/main/resources/undeadu1.png";}
            case SKELETON    -> {return "src/main/resources/undeadu2.png";}
            case VAMPIRE     -> {return "src/main/resources/undeadu3.png";}
            case NECROMANCER -> {return "src/main/resources/undeadu4.png";}
            case DEATHKNIGHT -> {return "src/main/resources/undeadu5.png";}
        }
        return null;
    }
}
