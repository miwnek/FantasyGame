package agh.ics.oop.gui;

import agh.ics.oop.AbstractUnit;
import agh.ics.oop.EStats.*;
import agh.ics.oop.EUnits;
import agh.ics.oop.EUnits.*;

import agh.ics.oop.UnitGroup;
import agh.ics.oop.units.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import static agh.ics.oop.EStats.Stats.*;

public class SecondMenuController implements Initializable {
    App app = new App();
    private Stage primaryStage;
    private final static Stage popupStage = new Stage();
    private  static String playerOneArmy;
    private  static String playerTwoArmy;
    private static final String[] armies = {"Undead", "Orc stronghold"};

    private final EUnits.UnitType[] undeadArmy = {EUnits.UnitType.ZOMBIE, EUnits.UnitType.SKELETON, EUnits.UnitType.VAMPIRE, EUnits.UnitType.NECROMANCER, EUnits.UnitType.DEATHKNIGHT};
    private final int[] undeadCosts = {new Zombie().getStats().get(COST), new Skeleton().getStats().get(COST), new Vampire().getStats().get(COST),
                                        new Necromancer().getStats().get(COST), new DeathKnight().getStats().get(COST)};
    private final EUnits.UnitType[] orcsArmy = {UnitType.GOBLIN,UnitType.GRUNT, UnitType.SHAMAN, UnitType.OGRE, UnitType.AXEMASTER};
    private final int[] orcCosts = {new Goblin().getStats().get(COST), new Grunt().getStats().get(COST), new Shaman().getStats().get(COST),
                                    new Ogre().getStats().get(COST), new AxeMaster().getStats().get(COST)};
    @FXML
    private  Button confirmButton;
    @FXML
    private  ChoiceBox<String> armyOneTwo;
    @FXML
    private  ChoiceBox<String> armyTwoTwo;

    @FXML
    private ImageView p1u1, p1u2, p1u3, p1u4, p1u5;
    private ArrayList<ImageView> p1Images, p2Images;
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

        p1Images = new ArrayList<>(List.of(p1u1, p1u2, p1u3, p1u4, p1u5));
        p2Images = new ArrayList<>(List.of(p2u1, p2u2, p2u3, p2u4, p2u5));
    }

    public void unitSelection(int player) throws FileNotFoundException {
        EUnits.UnitType[] armyTable = {EUnits.UnitType.SKELETON};
        ImageView[] imageTable;
        String playerArmy;
        if(player == 1) { imageTable = new ImageView[]{p1u1, p1u2, p1u3, p1u4, p1u5}; playerArmy = playerOneArmy;}
        else { imageTable = new ImageView[]{p2u1, p2u2, p2u3, p2u4, p2u5}; playerArmy = playerTwoArmy;}

        switch(playerArmy) {
            case "Undead" -> armyTable = undeadArmy;
            case "Orc stronghold" -> armyTable = orcsArmy;
            default -> System.out.println("No such army");
        }
        for(int i = 0; i < 5; i++) {
            imageTable[i].setImage(new Image(new FileInputStream(getPicturePath(armyTable[i]))));
            int finalI = i;
            imageTable[i].setOnMouseEntered(e -> {
                popupMake(imageTable[finalI], finalI);
            });
            imageTable[i].setOnMouseExited(e -> {
                popupDelete();
            });
        }
    }

    public String getPicturePath(UnitType unit) {
        switch(unit) {
            case ZOMBIE      -> {return "src/main/resources/undeadu1.png";}
            case SKELETON    -> {return "src/main/resources/undeadu2.png";}
            case VAMPIRE     -> {return "src/main/resources/undeadu3.png";}
            case NECROMANCER -> {return "src/main/resources/undeadu4.png";}
            case DEATHKNIGHT -> {return "src/main/resources/undeadu5.png";}
            case GOBLIN -> {return "src/main/resources/orcsu1.png";}
            case GRUNT -> {return "src/main/resources/orcsu2.png";}
            case SHAMAN -> {return "src/main/resources/orcsu3.png";}
            case OGRE -> {return "src/main/resources/orcsu4.png";}
            case AXEMASTER -> {return "src/main/resources/orcsu5.png";}
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
                case "Orc stronghold" -> {
                    for(int i = 0; i < 5; i++) {
                        p1Labels[i].setText(String.join("", unitToString(orcsArmy[i]), "\nCost: ", String.valueOf(orcCosts[i])));
                    }
                }
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
                case "Orc stronghold" -> {
                    for(int i = 0; i < 5; i++) {
                        p2Labels[i].setText(String.join("", unitToString(orcsArmy[i]), "\nCost: ", String.valueOf(orcCosts[i])));
                    }
                }
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
            case GOBLIN -> {return "Goblin";}
            case GRUNT -> {return "Grunt";}
            case SHAMAN -> {return "Shaman";}
            case OGRE -> {return "Ogre";}
            case AXEMASTER -> {return "Axe master";}
            default -> {return "No such unit";}
        }
    }

    public void proceedOne() throws FileNotFoundException {
        if (!p1ready) {
            int costSum = 0;
            int[] costs = new int[5];
            UnitType[] units = new UnitType[5];

            switch (playerOneArmy) {
                case "Undead" -> {
                  costs = undeadCosts;
                  units = undeadArmy;
                } case "Orc stronghold" -> {
                    costs = orcCosts;
                    units = orcsArmy;
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
                    if ((int) p1spinners[i].getValue() > 0) p1finalArmy.put(units[i], (int) p1spinners[i].getValue());
                }

                if (p2ready) {
                    app.start(primaryStage, p1finalArmy, p2finalArmy);
                }
                else { p1ready = true; p1Status.setText("Player 1 is ready");}
            }
        }
    }

    public void proceedTwo() throws FileNotFoundException {
        if (!p2ready) {
            int costSum = 0;
            int[] costs = new int[5];
            UnitType[] units = new UnitType[5];

            switch (playerTwoArmy) {
                case "Undead" -> {
                    costs = undeadCosts;
                    units = undeadArmy;
                } case "Orc stronghold" -> {
                    costs = orcCosts;
                    units = orcsArmy;
                }
                default -> {
                    System.out.println("Something went wrong");
                }
            }

            for (int i = 0; i < 5; i++) {
                costSum = costSum + (((int) p2spinners[i].getValue()) * costs[i]);
            }

            if (costSum == 0) {
                p2Status.setText("Pick at least one unit");
            } else if (costSum > 200) {
                p2Status.setText("Not enough gold");
            } else {

                for (int i = 0; i < 5; i++) {
                    p2spinners[i].setDisable(true);
                    if ((int) p2spinners[i].getValue() > 0) p2finalArmy.put(units[i], (int) p2spinners[i].getValue());
                }

                if (p1ready) {
                    app.start(primaryStage, p1finalArmy, p2finalArmy);
                }
                else { p2ready = true; p2Status.setText("Player 2 is ready");}
            }
        }
    }
    public void popupMake(ImageView img, int unitIdx) {
        VBox box = new VBox();
        box.setSpacing(10);
        box.setPrefSize(140,160);
        AbstractUnit template = new Skeleton();

        if(p1Images.contains(img)) {
            switch(playerOneArmy) {
                case "Undead" -> {
                    switch(unitIdx + 1) {
                        case 1  -> template = new Zombie();
                        case 2  -> template = new Skeleton();
                        case 3  -> template = new Vampire();
                        case 4  -> template = new Necromancer();
                        case 5  -> template = new DeathKnight();
                    }
                }
                case "Orc stronghold" -> {
                    switch(unitIdx + 1) {
                        case 1  -> template = new Goblin();
                        case 2  -> template = new Grunt();
                        case 3  -> template = new Shaman();
                        case 4  -> template = new Ogre();
                        case 5  -> template = new AxeMaster();
                    }
                }
            }
        } else {
            switch(playerTwoArmy) {
                case "Undead" -> {
                    switch(unitIdx + 1) {
                        case 1  -> template = new Zombie();
                        case 2  -> template = new Skeleton();
                        case 3  -> template = new Vampire();
                        case 4  -> template = new Necromancer();
                        case 5  -> template = new DeathKnight();
                    }
                }
                case "Orc stronghold" -> {
                    switch(unitIdx + 1) {
                        case 1  -> template = new Goblin();
                        case 2  -> template = new Grunt();
                        case 3  -> template = new Shaman();
                        case 4  -> template = new Ogre();
                        case 5  -> template = new AxeMaster();
                    }
                }
            }
        }
        Label name = new Label(template.toString());
        name.setAlignment(Pos.CENTER);
        name.setPrefWidth(140);
        Label power = new Label("Power: " + String.valueOf(template.getStats().get(POWER)));
        Label health = new Label("Health: " + String.valueOf(template.getStats().get(HEALTH)));
        Label speed = new Label("Speed: " + String.valueOf(template.getStats().get(SPEED)));
        Label attack = new Label("Attack type: " + template.attackTypeString());
        Label special = new Label();
        if(template.hasSpecial()) {
            if(template.toString().equals("Necromancer")) {
                special.setText("Special ability:\n   resurrects fallen enemy \n   unit groups as skeletons");
                box.setPrefSize(170, 210);
                name.setPrefWidth(170);

            }
            else {
                special.setText("Special ability:\n   casts an AOE fireball damaging\n   all enemy unit groups on the\n   chosen tile and neighboring ones");
                name.setPrefWidth(220);
                box.setPrefSize(220, 230);
            }
        } else {
            special.setText("Special ability: none");
        }
        name.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.ITALIC, 20));
        power.setFont(Font.font("Helvetica", FontWeight.BOLD,12));
        health.setFont(Font.font("Helvetica", FontWeight.BOLD,12));
        speed.setFont(Font.font("Helvetica", FontWeight.BOLD,12));
        attack.setFont(Font.font("Helvetica", FontWeight.BOLD,12));
        special.setFont(Font.font("Helvetica", FontWeight.BOLD,12));

        box.getChildren().addAll(name, power, health, speed, attack, special);
        Scene scene = new Scene(box);
        popupStage.setScene(scene);

        Bounds bounds = confirmButton.localToScreen(confirmButton.getBoundsInLocal());
        int offsetX = (int) bounds.getMinX();
        int offsetY = (int) bounds.getMinY();
        popupStage.setX(offsetX- 25);
        popupStage.setY(offsetY- 175);

        popupStage.show();
    }

    public void popupDelete() {
        popupStage.close();
    }

    public void setStage(Stage ps) { primaryStage = ps;}

}
