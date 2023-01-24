package agh.ics.oop.gui;

import agh.ics.oop.HexTile;
import agh.ics.oop.Map.HexGrid;
import agh.ics.oop.UnitGroup;
import agh.ics.oop.Vector3D;
import agh.ics.oop.units.Skeleton;
import agh.ics.oop.units.Zombie;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.shape.Polygon;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;
import javafx.fxml.FXMLLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    private final static HashMap<Tile, Vector3D> tileToVec = new HashMap<>(); // ? is it useful?
    private final static HashMap<Vector3D, Tile> vecToTile = new HashMap<>();
    private final static HashMap<Vector3D, Label> vecToLabel = new HashMap<>();
    private final static HashMap<Vector3D, UnitGroup> vecToGroup = new HashMap<>();
    private final static List<UnitGroup> activeGroups = new ArrayList<>();
    private static UnitGroup processedGroup;
    private static HexGrid map;
    private final static int playerCount = 2;
    private static int currPlayer = 0;
    private static boolean GAME_OVER = false;
    private static ArrayList<HexTile> moveOptions = new ArrayList<>();
    private static ArrayList<Label>  groupLabels = new ArrayList<>();

    public App(){};
    public void start(Stage primaryStage) {
        HBox mainBox = new HBox();

        // CONFIGURING BUTTONS
        moveButton.setDisable(true);
        moveButton.setOnMouseClicked(event -> {
            preMove(processedGroup.getPlayer(), processedGroup);
            moveButton.setDisable(true);
            passButton.setDisable(false);
            attackButton.setDisable(true);
        });

        passButton.setDisable(true);
        passButton.setOnMouseClicked(event -> {
            moveButton.setDisable(true);
            for (HexTile tile : moveOptions) {
                Tile gTile = vecToTile.get(tile.getPos());
                gTile.setFill(Color.TRANSPARENT);
                gTile.setDisable(true);
            }
            //TODO: add a queue and get a next group from the queue - half done
            vecToTile.get(processedGroup.getTile().getPos()).setFill(Color.TRANSPARENT);
            shuffle();
            playerTurn(processedGroup.getPlayer(), processedGroup);
            updateLabels();
        });

        attackButton.setDisable(true);
        attackButton.setOnMouseClicked(event -> {
            moveButton.setDisable(true);
            attackButton.setDisable(true);
            for (UnitGroup target : map.meleeTargets(processedGroup)) {
                vecToTile.get(target.getTile().getPos()).setDisable(false);
                vecToTile.get(target.getTile().getPos()).setFill(Color.YELLOW);
                vecToTile.get(target.getTile().getPos()).setOnMouseClicked(e -> {
                    if(map.attack(processedGroup, target)) {
                        vecToGroup.remove(target.getTile().getPos());
                        activeGroups.remove(target);
                    }
                    System.out.println("Attack!");
                    vecToTile.get(target.getTile().getPos()).setFill(Color.TRANSPARENT);
                    vecToTile.get(target.getTile().getPos()).setDisable(true);
                    updateLabels();
                });
            }
        });

        // TILE MAP
        AnchorPane tileMap = new AnchorPane();
        tileMap.setBackground(new Background(new BackgroundFill(Color.LIGHTSLATEGREY, CornerRadii.EMPTY, Insets.EMPTY)));

        //BUTTONS
        VBox buttonBox = new VBox(50);
        buttonBox.setPrefSize(200, WINDOW_HEIGHT);
        buttonBox.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        moveButton.setPrefSize(100,50);
        buttonBox.getChildren().add(moveButton);
        buttonBox.setAlignment(Pos.CENTER);

        attackButton.setPrefSize(100,50);
        buttonBox.getChildren().add(attackButton);

        passButton.setPrefSize(100,50);
        buttonBox.getChildren().add(passButton);

        // MAIN STAGE/SCENE
        mainBox.getChildren().add(tileMap);
        mainBox.getChildren().add(buttonBox);

        Scene content = new Scene(mainBox, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(content);
        primaryStage.setResizable(false);
        content.setFill(Color.LIGHTSLATEGREY);

        //TODO: add adding into client
        activeGroups.add(new UnitGroup(0, 10, new Skeleton()));
        activeGroups.add(new UnitGroup(1, 20, new Zombie()));

        map = new HexGrid(activeGroups, new ArrayList<>());

        activeGroups.get(1).setTile(map.getTile(new Vector3D(11,12,-23)));
        activeGroups.get(0).setTile(map.getTile(new Vector3D(0,0,0)));
        vecToGroup.put(new Vector3D(11,12,-23), activeGroups.get(1));
        vecToGroup.put(new Vector3D(0,0,0), activeGroups.get(0));

        map.fillingFinished();

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
                tileMap.getChildren().add(tile);

                tileToVec.put(tile, temp);
                vecToTile.put(temp, tile);
            }
        }
        primaryStage.show();

        makeTileLabels(tileMap);
        System.out.println(tileMiddle(vecToTile.get(new Vector3D(0,0,0))).toString());
        System.out.println(tileMiddle(vecToTile.get(new Vector3D(1,0,-1))).toString());

        processedGroup = activeGroups.get(0);

        updateLabels();
        playerTurn(0, processedGroup);
        shuffle();

    }

    // TODO: add actual options

    public void shuffle() {
        UnitGroup first = activeGroups.remove(0);
        activeGroups.add(first);
        processedGroup = first;
        currPlayer = processedGroup.getPlayer();
    }
    public void playerTurn (int playerNum, UnitGroup currGroup) {
        moveButton.setDisable(false);
        passButton.setDisable(false);
        attackButton.setDisable(false);
        vecToTile.get(currGroup.getTile().getPos()).setFill(getPlayerColor(currPlayer));
    }

    public void preMove(int playerNum, UnitGroup currGroup) {
        moveOptions =  map.placesToMove(currGroup);
        //vecToTile.get(currGroup.getTile().getPos()).setFill(Color.BLACK);

        for (HexTile tile : moveOptions) {
            Tile gTile = vecToTile.get(tile.getPos());

            if(!map.isOccupied(tile.getPos())) {
                gTile.setDisable(false);
                gTile.setFill(Color.GREENYELLOW);
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
        map.move(currGroup, dest);
        vecToTile.get(currGroup.getTile().getPos()).setFill(getPlayerColor(currPlayer));

        updateLabels();
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
            temp.setLayoutX(pos.x);
            temp.setLayoutY(pos.y);
            groupLabels.add(temp);
            vecToLabel.put(tileToVec.get(tile), temp);
            temp.setFont(new Font("Arial Black", 16));
            tileMap.getChildren().add(temp);
        }
    }

    public void updateLabels() {
        for (Label l: groupLabels) {
            l.setVisible(false);
        }
        for (UnitGroup group : activeGroups) {
            Vector3D pos = group.getTile().getPos();
            Label label = vecToLabel.get(pos);
            label.setVisible(true);
            label.setText( String.valueOf(group.getNumber()) );
        }
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
