package agh.ics.oop;

import java.util.ArrayList;

public class HexTile {
    // From top left = (0,0) to bottom right = (n, n)
    private final int row;
    private final int col;

    private final int depth;
    private ArrayList<HexTile> neighbours;

    public HexTile (int initRow, int initCol, int initDepth) {
        this.row   = initRow;
        this.col   = initCol;
        this.depth = initDepth;
    }

    public boolean doesNeighbour(HexTile tile) {
        return neighbours.contains(tile);
    }

    public Vector3D getPos(){
        return new Vector3D(row, col, depth);
    }

    public void setNeighbours(ArrayList<HexTile> neigh) {
        neighbours = neigh;
    }


}
