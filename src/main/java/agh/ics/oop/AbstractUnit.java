package agh.ics.oop;

import java.util.Map;
import agh.ics.oop.EStats.Stats;
import agh.ics.oop.EUnits.UnitType;
import agh.ics.oop.EUnits.AttackType;

public class AbstractUnit implements IUnit {
    protected Map<Stats, Integer> stats;
    protected UnitType unit;
    protected AttackType attack;
    protected boolean special;

    @Override
    public boolean hasSpecial() {
        return special;
    }

    @Override
    public Map<Stats, Integer> getStats() {
        return stats;
    }

    @Override
    public UnitType getUnitType() {
        return unit;
    }

    @Override
    public AttackType getAttackType() {
        return attack;
    }

    //TODO: add sample images and their actual paths
    @Override
    public String getPicturePath() {
        switch(unit) {
            case ZOMBIE      -> {return "";}
            case SKELETON    -> {return " ";}
            case VAMPIRE     -> {return "  ";}
            case NECROMANCER -> {return "   ";}
            case DEATHKNIGHT -> {return "    ";}
        }
        return null;
    }

}
