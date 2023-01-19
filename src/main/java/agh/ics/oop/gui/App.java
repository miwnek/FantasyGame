package agh.ics.oop.gui;

import agh.ics.oop.Vector3D;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.shape.Polygon;
import javafx.scene.paint.Color;

import java.util.HashMap;

public class App extends Application {

    private final static int WINDOW_WIDTH = 1200;
    private final static int WINDOW_HEIGHT = 800;

    private final static double r = 30; // the inner radius from hexagon center to outer corner
    private final static double n = Math.sqrt(r * r * 0.75); // the inner radius from hexagon center to middle of the axis
    private final static double TILE_HEIGHT = 2 * r;
    private final static double TILE_WIDTH = 2 * n;
    private static HashMap<Tile, Vector3D> pixToVec = new HashMap<>();
    @Override
    public void start(Stage primaryStage) {
        AnchorPane tileMap = new AnchorPane();
        Scene content = new Scene(tileMap, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(content);
        primaryStage.setResizable(false);
        content.setFill(Color.LIGHTSLATEGREY);

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
            }
        }
        primaryStage.show();
    }

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
    }
}
