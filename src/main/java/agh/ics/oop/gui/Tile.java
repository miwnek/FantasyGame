package agh.ics.oop.gui;

import agh.ics.oop.Vector3D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

// TODO: #@!#$#%$!#%@$!%$#%#$%@
public class Tile extends Polygon {
    private final Vector3D vector;

    private final static double r = 30; // the inner radius from hexagon center to outer corner
    private final static double n = Math.sqrt(r * r * 0.75); // the inner radius from hexagon center to middle of the axis
    private final static double TILE_HEIGHT = 2 * r;
    private final static double TILE_WIDTH = 2 * n;
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
        setStrokeWidth(2);
        setStroke(Color.BLACK);

        //setOnMouseClicked(e -> System.out.println(this.vector.toString()));
    }

    String stringPos() {
        return this.vector.toString();
    }

    Vector3D getVector() { return vector;}

}


