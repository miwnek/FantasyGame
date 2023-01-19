package agh.ics.oop;

public class UnitGroup implements IUnitGroup{
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
    public int getMaxNumber() { return maxNumber; }
    @Override
    public void setTile(HexTile newTile) { this.tile = newTile; }

    @Override
    public void setLowestHealth(int num) {lowestHealth = num;}

    @Override
    public void setNumber(int num) {number = num;}

//    @Override
//    public int hashCode() {
//        EUnits.UnitType type = template.getUnitType();
//        int code;
//
//        switch(type) {
//            case SKELETON -> {code = 1;}
//            case ZOMBIE   -> {code = 11;}
//            case VAMPIRE  -> {code = 23;}
//            case NECROMANCER -> {code = 31;}
//            case DEATHKNIGHT -> {code = 53;}
//            default -> {code = 0;}
//        }
//        return (this.player * 131 + code*17);
//    }
}
