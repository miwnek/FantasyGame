package agh.ics.oop;


public interface IUnitGroup {

    public int getPlayer();
    public int getNumber();
    public int getLowestHealth();
    public HexTile getTile();
    public boolean isAt(HexTile pos);
    public AbstractUnit getUnitTemplate();
    public int getMaxNumber();
    public void setTile(HexTile newTile);
    public void setLowestHealth(int num);
    public void setNumber(int num);
}
