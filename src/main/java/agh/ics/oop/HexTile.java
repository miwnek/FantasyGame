package agh.ics.oop;

import java.util.ArrayList;

public class HexTile {
    // From top left = (0,0) to bottom right = (n, n)
    private final int row;
    private final int col;
    private final ArrayList<HexTile> neighbours;

    public HexTile (int initRow, int initCol, ArrayList<HexTile> initNeighbours) {
        this.row = initRow;
        this.col = initCol;
        this.neighbours = initNeighbours;
    }

    public boolean doesNeighbour(HexTile tile) {
        return neighbours.contains(tile);
    }

    public Vector2D getPos(){
        return new Vector2D(row, col);
    }

}
