package agh.ics.oop.units;

import agh.ics.oop.AbstractUnit;
import agh.ics.oop.UnitGroup;

import java.util.HashMap;


import static agh.ics.oop.EStats.Stats.*;
import static agh.ics.oop.EUnits.AttackType.RANGED;
import static agh.ics.oop.EUnits.UnitType.AXEMASTER;

public class AxeMaster extends AbstractUnit {
    public AxeMaster() {
        unit = AXEMASTER;
        attack = RANGED;
        special = false;
        //populate stats
        stats = new HashMap<>();
        stats.put(COST,   60);
        stats.put(POWER,  25);
        stats.put(HEALTH, 35);
        stats.put(SPEED,  7);
    }

}