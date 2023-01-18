package agh.ics.oop;

import agh.ics.oop.EStats.Stats;
import agh.ics.oop.EUnits.UnitType;
import agh.ics.oop.EUnits.AttackType;


import java.util.Map;

public interface IUnit {


    public boolean hasSpecial();
    public Map<Stats, Integer> getStats();
    public String getPicturePath();
    public UnitType getUnitType();
    public AttackType getAttackType();
}
