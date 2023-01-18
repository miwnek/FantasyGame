package agh.ics.oop;

public class AbstractUnitGroup implements IUnitGroup{
    protected int player;
    protected int number;
    protected int maxNumber;
    protected int lowestHealth;
    protected HexTile tile;
    protected AbstractUnit template;

    @Override
    public int getPlayer() {
        return player;
    }

    @Override
    public int getNumber() {
        return number;
    }

    @Override
    public int getLowestHealth() {
        return lowestHealth;
    }

    @Override
    public HexTile getTile() {
        return tile;
    }

    @Override
    public boolean isAt(HexTile pos) {
        return tile.equals(pos);
    }

    @Override
    public AbstractUnit getUnitTemplate() {
        return template;
    }

    @Override
    public int getMaxNumber() {
        return maxNumber;
    }
}
