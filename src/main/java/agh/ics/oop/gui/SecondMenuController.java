package agh.ics.oop.gui;

import agh.ics.oop.EStats.*;
import agh.ics.oop.EUnits;
import agh.ics.oop.EUnits.*;

import agh.ics.oop.units.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import static agh.ics.oop.EStats.Stats.*;

public class SecondMenuController implements Initializable {
    private  static String playerOneArmy;
    private  static String playerTwoArmy;
    private static final String[] armies = {"Undead", "Orcs"};

    private final EUnits.UnitType[] undeadArmy = {EUnits.UnitType.ZOMBIE, EUnits.UnitType.SKELETON, EUnits.UnitType.VAMPIRE, EUnits.UnitType.NECROMANCER, EUnits.UnitType.DEATHKNIGHT};
    private final int[] undeadCosts = {new Zombie().getStats().get(COST), new Skeleton().getStats().get(COST), new Vampire().getStats().get(COST),
                                        new Necromancer().getStats().get(COST), new DeathKnight().getStats().get(COST)};
    @FXML
    private  Button confirmButton;
    @FXML
    private  ChoiceBox<String> armyOneTwo;
    @FXML
    private  ChoiceBox<String> armyTwoTwo;

    @FXML
    private ImageView p1u1, p1u2, p1u3, p1u4, p1u5;
    @FXML
    private Spinner<Integer> p1sp1, p1sp2, p1sp3, p1sp4, p1sp5;
    private Spinner[] p1spinners;
    @FXML
    private ImageView p2u1, p2u2, p2u3, p2u4, p2u5;
    @FXML
    private Spinner<Integer> p2sp1, p2sp2, p2sp3, p2sp4, p2sp5;
    private Spinner[] p2spinners;

    @FXML
    private Label p1l1, p1l2, p1l3, p1l4, p1l5;
    private Label[] p1Labels;
    @FXML
    private Label p2l1, p2l2, p2l3, p2l4, p2l5;
    private Label[] p2Labels;
    @FXML
    private Label p1Status, p2Status;
    private boolean p1ready = false, p2ready = false;
    private HashMap<UnitType, Integer> p1finalArmy= new HashMap<>(), p2finalArmy = new HashMap<>();

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

        fillLabels(1);
        fillLabels(2);

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        armyOneTwo.getItems().addAll(armies);
        armyTwoTwo.getItems().addAll(armies);

        SpinnerValueFactory<Integer> vf1 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,200);
        SpinnerValueFactory<Integer> vf2 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,200);
        SpinnerValueFactory<Integer> vf3 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,200);
        SpinnerValueFactory<Integer> vf4 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,200);
        SpinnerValueFactory<Integer> vf5 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,200);
        vf1.setValue(0);
        vf2.setValue(0);
        vf3.setValue(0);
        vf4.setValue(0);
        vf5.setValue(0);
        p1sp1.setValueFactory(vf1);
        p1sp2.setValueFactory(vf2);
        p1sp3.setValueFactory(vf3);
        p1sp4.setValueFactory(vf4);
        p1sp5.setValueFactory(vf5);

        SpinnerValueFactory<Integer> vf21 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,200);
        SpinnerValueFactory<Integer> vf22 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,200);
        SpinnerValueFactory<Integer> vf23 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,200);
        SpinnerValueFactory<Integer> vf24 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,200);
        SpinnerValueFactory<Integer> vf25 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,200);
        vf21.setValue(0);
        vf22.setValue(0);
        vf23.setValue(0);
        vf24.setValue(0);
        vf25.setValue(0);
        p2sp1.setValueFactory(vf21);
        p2sp2.setValueFactory(vf22);
        p2sp3.setValueFactory(vf23);
        p2sp4.setValueFactory(vf24);
        p2sp5.setValueFactory(vf25);

        p1Labels = new Label[]{p1l1, p1l2, p1l3, p1l4, p1l5};
        p2Labels = new Label[]{p2l1, p2l2, p2l3, p2l4, p2l5};
        p1spinners = new Spinner[]{p1sp1, p1sp2, p1sp3, p1sp4, p1sp5};
        p2spinners = new Spinner[]{p2sp1, p2sp2, p2sp3, p2sp4, p2sp5};
    }

    public void unitSelection(int player) throws FileNotFoundException {
        EUnits.UnitType[] armyTable = {EUnits.UnitType.SKELETON};
        ImageView[] imageTable;
        String playerArmy;
        if(player == 1) { imageTable = new ImageView[]{p1u1, p1u2, p1u3, p1u4, p1u5}; playerArmy = playerOneArmy;}
        else { imageTable = new ImageView[]{p2u1, p2u2, p2u3, p2u4, p2u5}; playerArmy = playerTwoArmy;}

        switch(playerArmy) {
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

    public void fillLabels(int player) {
        if(player == 1) {
            switch(playerOneArmy) {
                case "Undead" -> {
                    for(int i = 0; i < 5; i++) {
                        p1Labels[i].setText(String.join("", unitToString(undeadArmy[i]), "\nCost: ", String.valueOf(undeadCosts[i])));
                    }
                }
                case "Orcs" -> {System.out.println("Implement orcs labels");}
                default -> {System.out.println("No such army");}
            }
        }
        else {
            switch(playerTwoArmy) {
                case "Undead" -> {
                    for(int i = 0; i < 5; i++) {
                        p2Labels[i].setText(String.join("", unitToString(undeadArmy[i]), "\nCost: ", String.valueOf(undeadCosts[i])));
                    }
                }
                case "Orcs" -> {System.out.println("Implement orcs labels");}
                default -> {System.out.println("No such army");}
            }
        }
    }

    public String unitToString(UnitType unit) {
        switch (unit) {
            case SKELETON -> {return "Skeleton";}
            case ZOMBIE -> {return "Zombie";}
            case VAMPIRE -> {return "Vampire";}
            case NECROMANCER -> {return "Necromancer";}
            case DEATHKNIGHT -> {return "Death knight";}
            default -> {return "No such unit";}
        }
    }

    public void proceedOne() {
        //TODO: changng selection would actually be doable? disabling button?
        if (!p1ready) {
            int costSum = 0;
            int[] costs = new int[5];
            UnitType[] units = new UnitType[5];

            switch (playerOneArmy) {
                case "Undead" -> {
                  costs = undeadCosts;
                  units = undeadArmy;
                }
                default -> System.out.println("Something went wrong");
            }

            for (int i = 0; i < 5; i++) {
                costSum = costSum + (((int) p1spinners[i].getValue()) * costs[i]);
            }

            if (costSum == 0) {
                p1Status.setText("Choose at least one unit");
            } else if (costSum > 200) {
                p1Status.setText("Not enough gold");
            } else {

                for (int i = 0; i < 5; i++) {
                    p1spinners[i].setDisable(true);
                    //TODO: hashcode? would be easy but maybe it works
                    if ((int) p1spinners[i].getValue() > 0) p1finalArmy.put(units[i], (int) p1spinners[i].getValue());
                }

                //TODO: launch app
                if (p2ready) { System.out.println("Game starts here"); }
                else { p1ready = true; p1Status.setText("Player 1 is ready");}
            }
        }
    }

    public void proceedTwo() {
        //TODO: changng selection would actually be doable? disabling button?
        if (!p2ready) {
            int costSum = 0;
            int[] costs = new int[5];
            UnitType[] units = new UnitType[5];

            switch (playerTwoArmy) {
                case "Undead" -> {
                    costs = undeadCosts;
                    units = undeadArmy;
                }
                default -> {
                    System.out.println("Something went wrong");
                }
            }

            for (int i = 0; i < 5; i++) {
                costSum = costSum + (((int) p2spinners[i].getValue()) * costs[i]);
            }

            if (costSum == 0) {
                p2Status.setText("Choose at least one unit");
            } else if (costSum > 200) {
                p2Status.setText("Not enough gold");
            } else {

                for (int i = 0; i < 5; i++) {
                    p2spinners[i].setDisable(true);
                    //TODO: hashcode? would be easy but maybe it works
                    if ((int) p2spinners[i].getValue() > 0) p2finalArmy.put(units[i], (int) p2spinners[i].getValue());
                }

                //TODO: launch app
                if (p1ready) { System.out.println("Game starts here"); }
                else { p2ready = true; p2Status.setText("Player 2 is ready");}
            }
        }
    }

}
