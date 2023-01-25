package agh.ics.oop.gui;

import agh.ics.oop.*;
import agh.ics.oop.Map.HexGrid;
import agh.ics.oop.units.*;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.shape.Polygon;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;
import javafx.fxml.FXMLLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

import static agh.ics.oop.EStats.Stats.*;
import static java.lang.String.valueOf;

public class App{

    private final static int WINDOW_WIDTH = 1200;
    private final static int WINDOW_HEIGHT = 800;

    private final static double r = 30; // the inner radius from hexagon center to outer corner
    private final static double n = Math.sqrt(r * r * 0.75); // the inner radius from hexagon center to middle of the axis
    private final static double TILE_HEIGHT = 2 * r;
    private final static double TILE_WIDTH = 2 * n;

    private final static Button moveButton = new Button("Move");
    private final static Button attackButton = new Button("Attack");
    private static Button passButton = new Button("Pass turn");
    private static Button spButton = new Button("Special\nability");
    private static VBox buttonBox;

    private final static HashMap<Tile, Vector3D> tileToVec = new HashMap<>(); // ? is it useful?
    private final static HashMap<Vector3D, Tile> vecToTile = new HashMap<>();
    private final static HashMap<Vector3D, Label> vecToLabel = new HashMap<>();
    private final static HashMap<Vector3D, UnitGroup> vecToGroup = new HashMap<>();
    private final static HashMap<Vector3D, ImageView> vecToSprite = new HashMap<>();

    private final static ArrayList<UnitGroup> activeGroups = new ArrayList<>();
    private final static List<UnitGroup> deadGroups = new ArrayList<>();
    private final static List<Vector3D> obstacles = new ArrayList<>();
    private static List<UnitGroup> currTargets = new ArrayList<>();

    private static UnitGroup processedGroup;
    private static HexGrid map;
    private static int currPlayer = 0;
    private static ArrayList<HexTile> moveOptions = new ArrayList<>();
    private static ArrayList<Label>  groupLabels = new ArrayList<>();
    private static ArrayList<ImageView> groupSprites = new ArrayList<>();
    private final static Label turnLabelOne = new Label("Current turn:");
    private final static Label turnLabelTwo = new Label();
    private static Stage pStage;
    private static boolean flag = false;
    private final static Stage popupStage = new Stage();

    public App(){};
    public void start(Stage primaryStage, HashMap<EUnits.UnitType, Integer> armyOne, HashMap<EUnits.UnitType, Integer> armyTwo) throws FileNotFoundException {
        HBox mainBox = new HBox();
        pStage = primaryStage;
        pStage.setTitle("Champions of spell and blade");

        // CONFIGURING BUTTONS
        moveButton.setDisable(true);
        moveButton.setOnMouseClicked(event -> {
            preMove(processedGroup.getPlayer(), processedGroup);
            moveButton.setDisable(true);
            attackButton.setDisable(true);
            spButton.setDisable(true);
        });

        passButton.setDisable(true);
        passButton.setOnMouseClicked(event -> {
            for (HexTile tile : moveOptions) {
                Tile gTile = vecToTile.get(tile.getPos());
                gTile.setFill(Color.TRANSPARENT);
                gTile.setDisable(true);
            }
            for(UnitGroup pastTarget : currTargets) {
                vecToTile.get(pastTarget.getTile().getPos()).setFill(Color.TRANSPARENT);
                vecToTile.get(pastTarget.getTile().getPos()).setDisable(true);
            }

            if(flag) {
                for (Tile tile : tileToVec.keySet()) {
                    tile.setFill(Color.TRANSPARENT);
                    tile.setDisable(true);
                }
            }

            vecToTile.get(processedGroup.getTile().getPos()).setFill(Color.TRANSPARENT);
            shuffle();
            playerTurn(processedGroup.getPlayer(), processedGroup);
            try{updateLabels();} catch(FileNotFoundException e){System.out.println("Sprite loading failed");}

        });

        attackButton.setDisable(true);
        attackButton.setOnMouseClicked(event -> {
            moveButton.setDisable(true);
            attackButton.setDisable(true);
            spButton.setDisable(true);
            ArrayList<UnitGroup> targets;
            if(processedGroup.getUnitTemplate().getAttackType() == EUnits.AttackType.MELEE) targets = map.meleeTargets(processedGroup);
            else {
                targets = new ArrayList<>();
                for (UnitGroup group : activeGroups) {
                    if(group.getPlayer() != processedGroup.getPlayer() && canShoot(processedGroup, group)) targets.add(group);
                }
            }
            currTargets = targets;
            for (UnitGroup target : targets) {
                if(processedGroup.getPlayer() != target.getPlayer()) {
                    vecToTile.get(target.getTile().getPos()).setDisable(false);
                    vecToTile.get(target.getTile().getPos()).setFill(new Color(0.9, 0.85, 0., 0.8));
                    vecToTile.get(target.getTile().getPos()).setOnMouseClicked(e -> {
                        if (map.attack(processedGroup, target)) {
                            vecToGroup.remove(target.getTile().getPos());
                            activeGroups.remove(target);
                            deadGroups.add(target);
                            int sum = 0;
                            for(int i = 0; i < activeGroups.size(); i++) {
                                sum += activeGroups.get(i).getPlayer();
                            }
                            if(sum == 0) gameOver(0);
                            else if (sum == activeGroups.size()) gameOver(1);
                        }
                        vecToTile.get(target.getTile().getPos()).setFill(Color.TRANSPARENT);
                        vecToTile.get(target.getTile().getPos()).setDisable(true);
                        for(UnitGroup pastTarget : targets) {
                            vecToTile.get(pastTarget.getTile().getPos()).setFill(Color.TRANSPARENT);
                            vecToTile.get(pastTarget.getTile().getPos()).setDisable(true);
                        }
                        try{updateLabels();} catch(FileNotFoundException err){System.out.println("Sprite loading failed");}
                    });
                }
            }
        });

        spButton.setDisable(true);
        spButton.setOnMouseClicked(event -> {
            moveButton.setDisable(true);
            attackButton.setDisable(true);
            spButton.setDisable(true);
            if(processedGroup.getUnitTemplate().toString().equals("Necromancer")) {
                for(UnitGroup dead : deadGroups) {
                    if(!map.isOccupied(dead.getTile().getPos()) && dead.getPlayer() != processedGroup.getPlayer()) {
                        vecToTile.get(dead.getTile().getPos()).setDisable(false);
                        vecToTile.get(dead.getTile().getPos()).setFill(Color.ROSYBROWN);
                        vecToTile.get(dead.getTile().getPos()).setOnMouseClicked(e -> {
                            UnitGroup temp = map.necromancerSpecial(processedGroup, dead, processedGroup.getPlayer());
                            int idx = 0;
                            while(activeGroups.get(idx).getUnitTemplate().getStats().get(SPEED) > temp.getUnitTemplate().getStats().get(SPEED)) {
                                idx++;
                                if(idx == activeGroups.size()) break;
                            }
                            if(idx == activeGroups.size()) activeGroups.add(temp);
                            else activeGroups.add(idx, temp);

                            for(UnitGroup deaded : deadGroups) {
                                vecToTile.get(deaded.getTile().getPos()).setDisable(true);
                                vecToTile.get(deaded.getTile().getPos()).setFill(Color.TRANSPARENT);
                            }
                            deadGroups.remove(dead);
                            try{updateLabels();} catch(FileNotFoundException err){System.out.println("Sprite loading failed");}
                        });
                    }
                }
            } else { // Shaman's ability
                flag = true;
                for (Tile tile : tileToVec.keySet()) {
                    if(!obstacles.contains(tile.getVector())) {
                        tile.setDisable(false);
                        tile.setFill(Color.ORANGE);
                        tile.setOnMouseClicked(e -> {
                            flag = false;
                            fireball(processedGroup, tile.getVector());
                            for (Tile t : tileToVec.keySet()) {
                                t.setDisable(true);
                                t.setFill(Color.TRANSPARENT);
                            }
                        });
                    }
                }
            }
        });

        // TILE MAP
        AnchorPane tileMap = new AnchorPane();
        tileMap.setBackground(new Background(new BackgroundFill(Color.LIGHTSLATEGREY, CornerRadii.EMPTY, Insets.EMPTY)));

        //BUTTONS
        buttonBox = new VBox(50);
        buttonBox.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(2))));
        buttonBox.setPrefSize(200, WINDOW_HEIGHT);
        buttonBox.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        moveButton.setFont(Font.font("Helvetica", FontWeight.BOLD, 14));
        attackButton.setFont(Font.font("Helvetica", FontWeight.BOLD, 14));
        passButton.setFont(Font.font("Helvetica", FontWeight.BOLD, 14));
        spButton.setFont(Font.font("Helvetica", FontWeight.BOLD, 14));

        moveButton.setPrefSize(100,50);
        buttonBox.getChildren().add(moveButton);
        buttonBox.setAlignment(Pos.CENTER);

        attackButton.setPrefSize(100,50);
        buttonBox.getChildren().add(attackButton);

        passButton.setPrefSize(100,50);
        buttonBox.getChildren().add(passButton);

        spButton.setPrefSize(100,50);
        buttonBox.getChildren().add(spButton);

        VBox turnBox = new VBox(5);
        turnBox.setAlignment(Pos.CENTER);
        //turnLabelOne.setPrefSize(150, 80);
        turnLabelOne.setAlignment(Pos.BOTTOM_CENTER);
        //turnLabelOne.getStyleClass().add("outline");
        turnLabelOne.setFont(Font.font("Helvetica", FontWeight.BOLD, 25));
        turnLabelOne.setTextFill(Color.BLACK);

        //turnLabelTwo.setPrefSize(150, 80);
        turnLabelTwo.setAlignment(Pos.TOP_CENTER);
        turnLabelTwo.getStyleClass().add("outline");
        turnLabelTwo.setFont(Font.font("Helvetica", FontWeight.BOLD, 25));


        turnBox.getChildren().add(turnLabelOne);
        turnBox.getChildren().add(turnLabelTwo);
        buttonBox.getChildren().add(turnBox);

        // MAIN STAGE/SCENE
        mainBox.getChildren().add(tileMap);
        mainBox.getChildren().add(buttonBox);

        Scene content = new Scene(mainBox, WINDOW_WIDTH, WINDOW_HEIGHT);
        content.getStylesheets().addAll(getClass().getResource("/outline.css").toExternalForm());
        primaryStage.setScene(content);
        primaryStage.setResizable(false);

        // MAP BACKGROUND
        BackgroundImage myBI = new BackgroundImage(new Image(new FileInputStream("src/main/resources/gbg.png")), BackgroundRepeat.REPEAT,
                                BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        tileMap.setBackground(new Background(myBI));
        BackgroundImage myBI2 = new BackgroundImage(new Image(new FileInputStream("src/main/resources/bgBar.png")), BackgroundRepeat.REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        buttonBox.setBackground(new Background(myBI2));

        initMap(armyOne, armyTwo);

        int rowCount = 13; // how many rows of tiles should be created
        int tilesPerRow = 18; // the amount of tiles that are contained in each row
        int xStartOffset = 40; // offsets the entire field to the right
        int yStartOffset = 120; // offsets the entire field downwards

        for (int x = 0; x < tilesPerRow; x++) {
            for (int y = 0; y < rowCount; y++) {
                double xCoord = x * TILE_WIDTH + (y % 2) * n + xStartOffset;
                double yCoord = y * TILE_HEIGHT * 0.75 + yStartOffset;
                //Converting col,row to Vector3D
                Vector3D temp = new Vector3D(-6 + x + (y+1)/2, 12 - y, -6 - x - (y+1)/2 + y);

                Tile tile = new Tile(xCoord, yCoord, temp);
                tile.setOpacity(0.5); //OPACITY LEVEL
                tileMap.getChildren().add(tile);

                tileToVec.put(tile, temp);
                vecToTile.put(temp, tile);
            }
        }
        // Obstacle graphics
        Image rockImage = new Image(new FileInputStream("src/main/resources/rockImg.png"));
        HashMap<Vector3D, ImageView> vecToRock = new HashMap<>();
        for (Vector3D vec : obstacles) {
            //vecToTile.get(vec).setFill(new Color(100.0/256,45.0/256,0,0.7));
            vecToTile.get(vec).setFill(new Color(0.2,0.1,0.1,0.9));
            //vecToTile.get(vec).setOpacity(0.3);
            ImageView rock = new ImageView();
            PixelPair pos = tileMiddle(vecToTile.get(vec));
            rock.setImage(rockImage);
            rock.setLayoutX(pos.x - r * 4/3 - 5);
            rock.setLayoutY(pos.y - r * 6/5 + 2);
            //rock.setPreserveRatio(true);
            rock.setFitHeight(55);
            rock.setFitWidth(2*r + 30);
            rock.setImage(rockImage);
            vecToRock.put(vec, rock);
            tileMap.getChildren().add(rock);
        }

        makeTileLabels(tileMap);

        processedGroup = activeGroups.get(0);

        try{updateLabels();} catch(FileNotFoundException e){System.out.println("Sprite loading failed");}
        playerTurn(0, processedGroup);
        shuffle();
    }

    public void shuffle() {
        UnitGroup first = activeGroups.remove(0);
        activeGroups.add(first);
        processedGroup = first;
        currPlayer = processedGroup.getPlayer();
    }
    public void playerTurn (int playerNum, UnitGroup currGroup) {
        turnLabelTwo.setText("Player " + String.valueOf(playerNum + 1));
        turnLabelTwo.setTextFill(getPlayerColor(playerNum));

        moveButton.setDisable(false);
        passButton.setDisable(false);
        attackButton.setDisable(false);
        if(currGroup.getUnitTemplate().hasSpecial()) spButton.setDisable(false);
        vecToTile.get(currGroup.getTile().getPos()).setFill(getPlayerColor(currPlayer));
    }

    public void preMove(int playerNum, UnitGroup currGroup) {
        moveOptions =  map.placesToMove(currGroup);
        //vecToTile.get(currGroup.getTile().getPos()).setFill(Color.BLACK);

        for (HexTile tile : moveOptions) {
            Tile gTile = vecToTile.get(tile.getPos());

            if(!map.isOccupied(tile.getPos())) {
                gTile.setDisable(false);
                gTile.setFill(new Color(0.4,0.8,0.2,0.7));
                gTile.setOnMouseClicked(e -> {
                    vecToGroup.remove(processedGroup.getTile().getPos());
                    vecToGroup.put(gTile.getVector(), processedGroup);
                    postMove(playerNum, currGroup, moveOptions, gTile.getVector());
                });
            }
        }

    }

    public void postMove(int playerNum, UnitGroup currGroup, ArrayList<HexTile> moveOptions, Vector3D dest) {
        vecToTile.get(currGroup.getTile().getPos()).setFill(Color.TRANSPARENT);
        for (HexTile tile : moveOptions) {
            Tile gTile = vecToTile.get(tile.getPos());
            gTile.setFill(Color.TRANSPARENT);
            gTile.setDisable(true);
        }
        boolean flag = true;
        if(map.getsCounterattacked(currGroup)) {
            UnitGroup source = map.getCaSource(currGroup);
            if (map.attack(source, currGroup)) {
                vecToGroup.remove(currGroup.getTile().getPos());
                activeGroups.remove(currGroup);
                deadGroups.add(currGroup);
                int sum = 0;
                for(int i = 0; i < activeGroups.size(); i++) {
                    sum += activeGroups.get(i).getPlayer();
                }
                if(sum == 0) gameOver(0);
                else if (sum == activeGroups.size()) gameOver(1);
                flag = false;
            }
        }
        if(flag) {
            map.move(currGroup, dest);
            vecToTile.get(currGroup.getTile().getPos()).setFill(getPlayerColor(currPlayer));
        }

        try{updateLabels();} catch(FileNotFoundException e){System.out.println("Sprite loading failed");}
        attackButton.setDisable(false);
    }

    //public void attack

    public Color getPlayerColor (int player) {
        switch (player) {
            case 0 -> {return Color.RED;}
            case 1 -> {return Color.BLUE;}
            default -> {return Color.BLACK;}
        }
    }

    public void makeTileLabels(AnchorPane tileMap) {
        for (Tile tile : tileToVec.keySet()) {
            Label temp = new Label();
            PixelPair pos = tileMiddle(tile);
            temp.setLayoutX(pos.x - 10);
            temp.setLayoutY(pos.y + 5);
            temp.setTextFill(Color.GREENYELLOW);
            temp.getStyleClass().add("outline");

            ImageView img = new ImageView();
            img.setLayoutX(pos.x - r);
            img.setLayoutY(pos.y - r * 3);
            img.setPreserveRatio(true);
            img.setFitHeight(100);


            groupLabels.add(temp);
            vecToLabel.put(tileToVec.get(tile), temp);

            groupSprites.add(img);
            vecToSprite.put(tileToVec.get(tile), img);

            temp.setFont(new Font("Arial Black", 15));
            temp.setAlignment(Pos.TOP_CENTER);
            temp.setMinSize(20,5);
            tileMap.getChildren().add(temp);
            tileMap.getChildren().add(img);
        }
    }

    public void updateLabels() throws FileNotFoundException {
        for (Label l: groupLabels) {
            l.setVisible(false);
        }

        for (ImageView img : groupSprites) {
            img.setVisible(false);
        }

        for (UnitGroup group : activeGroups) {
            Vector3D pos = group.getTile().getPos();

            Label label = vecToLabel.get(pos);
            label.setVisible(true);
            label.setText(String.valueOf(group.getNumber()));

            ImageView img = vecToSprite.get(pos);
            img.setVisible(true);
            img.setImage(new Image(new FileInputStream(group.getUnitTemplate().getPicturePath())));
            img.setOnMouseEntered(e -> {
                popupMake(group);
            });
            img.setOnMouseExited(e -> {
                popupDelete();
            });
        }
    }

    public void initMap(HashMap<EUnits.UnitType, Integer> armyOne, HashMap<EUnits.UnitType, Integer> armyTwo) {
        initGroups(0, armyOne);
        initGroups(1, armyTwo);
        activeGroups.sort(new UnitGroupComparator());

        // Producing obstacles
        obstacles.add(new Vector3D(3,7,-10));
        obstacles.add(new Vector3D(2,7,-9));
        obstacles.add(new Vector3D(3,6,-9));
        obstacles.add(new Vector3D(2,6,-8));

        obstacles.add(new Vector3D(11,4,-15));
        obstacles.add(new Vector3D(10,4,-14));
        obstacles.add(new Vector3D(12,3,-15));
        obstacles.add(new Vector3D(11,3,-14));
        obstacles.add(new Vector3D(12,2,-14));

        map = new HexGrid(activeGroups, obstacles);

        initPositions(0);
        initPositions(1);

        map.fillingFinished();
    }

    public void initGroups(int player, HashMap<EUnits.UnitType, Integer> armyMap) {;
        for(EUnits.UnitType template : armyMap.keySet()) {
            activeGroups.add(new UnitGroup(player, armyMap.get(template), getConstructor(template)));
        }
    }

    public void initPositions(int player) {
        int count = 0;
        int startX;
        int startY;
        final int incrX = -1;
        final int incrY = 2;
        int z;

        if(player == 0) {startX = -1; startY = 2;}
        else            {startX = 16; startY = 3;}

        for(UnitGroup group : activeGroups) {
            if(group.getPlayer() == player) {
                z = -(startX + incrX * count + startY + incrY * count);

                group.setTile(map.getTile(new Vector3D(startX + incrX * count, startY + incrY * count, z)));
                vecToGroup.put(new Vector3D(startX + incrX * count, startY + incrY * count, z), group);
                count++;
            }
        }
    }

    public AbstractUnit getConstructor(EUnits.UnitType unit) {
        switch(unit) {
            case ZOMBIE -> {return new Zombie();}
            case SKELETON -> {return new Skeleton();}
            case VAMPIRE -> {return new Vampire();}
            case NECROMANCER -> {return new Necromancer();}
            case DEATHKNIGHT -> {return new DeathKnight();}
            case GOBLIN -> {return new Goblin();}
            case GRUNT -> {return new Grunt();}
            case SHAMAN -> {return new Shaman();}
            case OGRE -> {return new Ogre();}
            case AXEMASTER -> {return new AxeMaster();}
            default -> {return new Skeleton();}
        }
    }

    public boolean canShoot(UnitGroup source, UnitGroup target) {
        PixelPair sourcePair = tileMiddle(vecToTile.get(source.getTile().getPos()));
        PixelPair targetPair = tileMiddle(vecToTile.get(target.getTile().getPos()));
        double[] scale = {targetPair.x - sourcePair.x, targetPair.y - sourcePair.y};
        double startX = sourcePair.x;
        double startY = sourcePair.y;
        for(double i = 0.05; i < 1; i += 0.05) {
            Point2D checked = new Point2D(startX + scale[0] * i, startY + scale[1] * i);
            for (Vector3D vec : obstacles) {
                if(vecToTile.get(vec).contains(checked)) return false;
            }
        }
        return true;
    }

    public void gameOver(int player) {
        AnchorPane pane = new AnchorPane();
        BackgroundFill myBF = new BackgroundFill(Color.GREY, new CornerRadii(1),
                new Insets(0.0,0.0,0.0,0.0));
        Label over = new Label();
        over.setLayoutX(475);
        over.setLayoutY(280);

        over.setText("Player " + String.valueOf(player + 1) + " won!");
        over.getStyleClass().add("outline");
        over.setTextFill(getPlayerColor(currPlayer));
        over.setFont(Font.font("Helvetica", FontWeight.BOLD, 40));

        pane.getChildren().add(over);
        pane.setBackground(new Background(myBF));

        pStage.setScene(new Scene(pane, WINDOW_WIDTH, WINDOW_HEIGHT));
        pStage.getScene().getStylesheets().addAll(getClass().getResource("/outline.css").toExternalForm());
    }

    public void popupMake(UnitGroup group) {
        VBox box = new VBox();
        box.setSpacing(10);
        box.setPrefSize(140,160);
        AbstractUnit template = group.getUnitTemplate();

        Label name = new Label(template.toString());
        name.setAlignment(Pos.CENTER);
        name.setPrefWidth(140);
        Label currHealth = new Label("Top unit health: " + String.valueOf(group.getLowestHealth()));
        Label maxHealth = new Label("Max health: " + String.valueOf(group.getUnitTemplate().getStats().get(HEALTH)));
        Label power = new Label("Power: " + String.valueOf(group.getUnitTemplate().getStats().get(POWER)));
        Label speed = new Label("Speed: " + String.valueOf(group.getUnitTemplate().getStats().get(SPEED)));
        Label attack = new Label("Attack type: " + String.valueOf(group.getUnitTemplate().attackTypeString()));
        Label number = new Label("Units in group: " + String.valueOf(group.getNumber()));

        name.setFont(Font.font("Helvetica", FontWeight.BOLD, FontPosture.ITALIC, 20));
        power.setFont(Font.font("Helvetica", FontWeight.BOLD,12));
        currHealth.setFont(Font.font("Helvetica", FontWeight.BOLD,12));
        maxHealth.setFont(Font.font("Helvetica", FontWeight.BOLD,12));
        attack.setFont(Font.font("Helvetica", FontWeight.BOLD,12));
        speed.setFont(Font.font("Helvetica", FontWeight.BOLD,12));
        number.setFont(Font.font("Helvetica", FontWeight.BOLD,12));

        box.getChildren().addAll(name, currHealth, maxHealth, power, speed, attack, number);
        Scene scene = new Scene(box);
        popupStage.setScene(scene);
        Bounds bounds = buttonBox.localToScreen(buttonBox.getBoundsInLocal());
        int width = (int) bounds.getMinX();
        int height = (int) bounds.getMinY();
        popupStage.setX(width);
        popupStage.setY(height);
        popupStage.show();
    }

    public void fireball(UnitGroup source, Vector3D targetPos) {
        HexTile targetTile = map.getTile(targetPos);

        ArrayList<UnitGroup> targets = new ArrayList<>();
        if(map.hasGroup(targetPos) && map.getGroup(targetPos).getPlayer() != source.getPlayer()) {
            targets.add(map.getGroup(targetPos));
        }
        for (HexTile tile : targetTile.getNeighbours()) {
            if(map.hasGroup(tile.getPos()) && map.getGroup(tile.getPos()).getPlayer() != source.getPlayer()) {
                targets.add(map.getGroup(tile.getPos()));
            }
        }

        for (UnitGroup target : targets) {
            if (map.attack(source, target)) {
                vecToGroup.remove(target.getTile().getPos());
                activeGroups.remove(target);
                deadGroups.add(target);
                int sum = 0;
                for(int i = 0; i < activeGroups.size(); i++) {
                    sum += activeGroups.get(i).getPlayer();
                }
                if(sum == 0) gameOver(0);
                else if (sum == activeGroups.size()) gameOver(1);
            }
        }
        try{updateLabels();}catch(FileNotFoundException err){System.out.println("Couldn't update labels");}
    }

    public void popupDelete() {
        popupStage.close();
    }

    public PixelPair tileMiddle (Tile tile) {
        double sumX = 0;
        double sumY = 0;
        int xOrY = 0;
        for (Double point : tile.getPoints()) {
            if(xOrY % 2 == 0) sumX += point;
            else sumY += point;
            xOrY += 1;
        }
        return new PixelPair(sumX/6,sumY/6);
    }
    private class PixelPair {
        public final double x;
        public final double y;
        public PixelPair(double x, double y) {
            this.x = x;
            this.y = y;
        }
        @Override
        public String toString() {
            return String.join("", "(", String.valueOf(x) , ", ", String.valueOf(y), ")");
        }
    }

}
