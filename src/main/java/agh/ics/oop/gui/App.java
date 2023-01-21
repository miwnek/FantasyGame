package agh.ics.oop.gui;

import agh.ics.oop.HexTile;
import agh.ics.oop.Map.HexGrid;
import agh.ics.oop.UnitGroup;
import agh.ics.oop.Vector3D;
import agh.ics.oop.units.Skeleton;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.shape.Polygon;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;
import javafx.fxml.FXMLLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.lang.String.valueOf;

public class App extends Application {

    private final static int WINDOW_WIDTH = 1200;
    private final static int WINDOW_HEIGHT = 800;

    private final static double r = 30; // the inner radius from hexagon center to outer corner
    private final static double n = Math.sqrt(r * r * 0.75); // the inner radius from hexagon center to middle of the axis
    private final static double TILE_HEIGHT = 2 * r;
    private final static double TILE_WIDTH = 2 * n;

    private static Button moveButton = new Button("Move");
    private static Button passButton = new Button("Pass turn");

    private static HashMap<Tile, Vector3D> pixToVec = new HashMap<>(); // ? is it useful?
    private static HashMap<Vector3D, Tile> vecToTile = new HashMap<>();
    private static List<UnitGroup> activeGroups = new ArrayList<>();
    private static UnitGroup processedGroup;
    private static HexGrid map;
    private static int playerCount = 1;
    private static int currPlayer = 0;
    private static boolean GAME_OVER = false;

    private static ArrayList<HexTile> moveOptions = new ArrayList<>();
    @Override
    public void start(Stage primaryStage) {
        HBox mainBox = new HBox();

        // CONFIGURING BUTTONS
        moveButton.setDisable(true);
        moveButton.setOnMouseClicked(event -> {
            preMove(0, processedGroup);
            moveButton.setDisable(true);
            passButton.setDisable(false);
        });

        passButton.setDisable(true);
        passButton.setOnMouseClicked(event -> {
            moveButton.setDisable(true);
            //TODO: add a queue and get a next group from the queue
            playerTurn(0, processedGroup);
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
        map = new HexGrid(activeGroups, new ArrayList<>());
        activeGroups.get(0).setTile(map.getTile(new Vector3D(0,0,0)));

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

                pixToVec.put(tile, temp);
                vecToTile.put(temp, tile);
            }
        }
        primaryStage.show();

        processedGroup = activeGroups.get(0);

        playerTurn(0, processedGroup);

        ArrayList<HexTile> test = new ArrayList<>();
        HexTile testTile = new HexTile(0,0,0);
        test.add(testTile);
        System.out.println(test.contains(testTile));

    }

    // TODO: add actual options

    public void playerTurn (int playerNum, UnitGroup currGroup) {
        moveButton.setDisable(false);
        passButton.setDisable(false);
        vecToTile.get(currGroup.getTile().getPos()).setFill(Color.BLACK);
    }

    public void preMove(int playerNum, UnitGroup currGroup) {
        moveOptions =  map.placesToMove(currGroup);
        //vecToTile.get(currGroup.getTile().getPos()).setFill(Color.BLACK);

        for (HexTile tile : moveOptions) {
            Tile gTile = vecToTile.get(tile.getPos());

            gTile.setDisable(false);
            gTile.setFill(Color.GREENYELLOW);
            gTile.setOnMouseClicked(e -> {
                postMove(playerNum, currGroup, moveOptions, gTile.getVector());
            });
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
        vecToTile.get(currGroup.getTile().getPos()).setFill(Color.BLACK);
    }

    // TODO: #@!#$#%$!#%@$!%$#%#$%@
    private class Tile extends Polygon {
        private final Vector3D vector;
        Tile(double x, double y, Vector3D vec) {
            // creates the polygon using the corner coordinates
            this.getPoints().addAll(
                    x, y,
                    x, y + r,
                    x + n, y + r * 1.5,
                    x + TILE_WIDTH, y + r,
                    x + TILE_WIDTH, y,
                    x + n, y - r * 0.5
            );
            this.vector = vec;

            // set up the visuals and a click listener for the tile
            this.setFill(Color.TRANSPARENT);
            setStrokeWidth(1);
            setStroke(Color.BLACK);

            setOnMouseClicked(e -> System.out.println(this.vector.toString()));
        }

        String stringPos() {
            return this.vector.toString();
        }

        Vector3D getVector() { return vector;}
    }
}
