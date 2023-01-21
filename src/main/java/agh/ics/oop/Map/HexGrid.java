package agh.ics.oop.Map;

import agh.ics.oop.EStats;
import agh.ics.oop.UnitGroup;
import agh.ics.oop.HexTile;
import agh.ics.oop.Vector3D;

import java.sql.Array;
import java.util.*;

public class HexGrid {
    private static final int rowCount    = 13;
    private static final int tilesPerRow = 18;
    private static Map<Vector3D, HexTile> tileMap = new HashMap<>();
    private static List<UnitGroup> activeGroups;// = new ArrayList<>();
    private static List<UnitGroup> deadGroups = new ArrayList<>();
    private static List<Vector3D> occupied;

    public HexGrid(List<UnitGroup> start, List<Vector3D> obs) {
        activeGroups = start;
        occupied = obs;

        // Initialize tiles and put them in the HashMap with their corresponding vectors as keys
        for(int x = 0; x < tilesPerRow; x++) {
            for(int y = 0; y < Math.min(rowCount, rowCount - (x - rowCount + 2)*2 + 1); y++) {
                tileMap.put(new Vector3D(x, y, -x - y), new HexTile(x, y, -x - y));
            }
        }
        for(int x = -1; x > (-(rowCount+1)/2); x--) {
            for(int y = (-x)*2; y < rowCount ; y++) {
                tileMap.put(new Vector3D(x, y, -x - y), new HexTile(x, y, -x - y));
                if (x==-3 && y == 7) System.out.println("lmao");
            }
        }

        for (Vector3D curr : tileMap.keySet()) {
            ArrayList<HexTile> temp = new ArrayList<>();
            for (Vector3D checked : tileMap.keySet()) {
                if (curr.doesNeighbour(checked)) {
                    temp.add(tileMap.get(checked));
                }
            }
            tileMap.get(curr).setNeighbours(temp);
        }
    }

    public int distance (HexTile a, HexTile b) {
        Vector3D aa = a.getPos();
        Vector3D bb = b.getPos();
        return aa.distance(bb);
    }

    public HexTile getTile (Vector3D pos) {
        return tileMap.get(pos);
    }

    public void move(UnitGroup group, Vector3D dest) {
        HexTile destTile = tileMap.get(dest);
        occupied.remove(group.getTile().getPos());
        occupied.add(dest);
        group.setTile(destTile);
    }

    //TODO: no clue if this is correct, have to test
    public ArrayList<HexTile> placesToMove (UnitGroup group) {
        HexTile location = group.getTile();
        int speed = group.getUnitTemplate().getStats().get(EStats.Stats.SPEED);

        ArrayList<HexTile> result = new ArrayList<>();
        ArrayList<HexTile> visited = new ArrayList<>();
        Queue<HexTile> toVisit = new LinkedList<>();
        toVisit.add(location);

        visited.add(location);
        while (!toVisit.isEmpty()) {
            HexTile curr = toVisit.remove();

            result.add(curr);

            ArrayList<HexTile> temp = curr.getNeighbours();
            for (HexTile neigh : temp) {

                if(!visited.contains(neigh) && distance(location, neigh) <= speed) {
                    toVisit.add(neigh);
                    visited.add(neigh);
                }
            }
        }

        result.remove(location);

        return result;
    }

    public void attack(UnitGroup source, UnitGroup target) {
        int damage = source.getUnitTemplate().getStats().get(EStats.Stats.POWER) * source.getNumber();
        if (damage < target.getLowestHealth()) {
            target.setLowestHealth(target.getLowestHealth() - damage);
        }
        else {
            damage -= target.getLowestHealth();
            int num = 1;
            int health = target.getUnitTemplate().getStats().get(EStats.Stats.HEALTH);

            if (damage >= (target.getNumber()-1) * health) {
                target.setNumber(0);
                target.setLowestHealth(0);
                activeGroups.remove(target);
                deadGroups.add(target);
                occupied.remove(target.getTile().getPos());
            }
            else {
                num += damage / health;
                target.setNumber(target.getNumber() - num);
                target.setLowestHealth(health - (damage % health));
            }

        }
    }


}

//    int rowCount = 13; // how many rows of tiles should be created
//    int tilesPerRow = 18; // the amount of tiles that are contained in each row
//    int xStartOffset = 40; // offsets the entire field to the right
//    int yStartOffset = 120; // offsets the entire field downwards

//    private final static int WINDOW_WIDTH = 1200;
//    private final static int WINDOW_HEIGHT = 800;
//
//    private final static double r = 30; // the inner radius from hexagon center to outer corner
//    private final static double n = Math.sqrt(r * r * 0.75); // the inner radius from hexagon center to middle of the axis
//    private final static double TILE_HEIGHT = 2 * r;
//    private final static double TILE_WIDTH = 2 * n;

