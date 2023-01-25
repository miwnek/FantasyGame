package agh.ics.oop.units;

import agh.ics.oop.AbstractUnit;
import agh.ics.oop.UnitGroup;

import java.util.HashMap;


import static agh.ics.oop.EStats.Stats.*;
import static agh.ics.oop.EUnits.AttackType.MELEE;
import static agh.ics.oop.EUnits.UnitType.SHAMAN;

public class Shaman extends AbstractUnit {
    public Shaman () {
        unit = SHAMAN;
        attack = MELEE;
        special = true;
        //populate stats
        stats = new HashMap<>();
        stats.put(COST,   30);
        stats.put(POWER,  10);
        stats.put(HEALTH, 20);
        stats.put(SPEED,  6);
    }


}