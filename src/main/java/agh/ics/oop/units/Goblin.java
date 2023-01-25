package agh.ics.oop.units;

import agh.ics.oop.AbstractUnit;

import agh.ics.oop.EStats;

import java.util.HashMap;


import static agh.ics.oop.EStats.Stats.*;
import static agh.ics.oop.EUnits.AttackType.MELEE;
import static agh.ics.oop.EUnits.UnitType.GOBLIN;

public class Goblin extends AbstractUnit {
    public Goblin () {
        unit = GOBLIN;
        attack = MELEE;
        special = false;
        //populate stats
        stats = new HashMap<>();
        stats.put(COST,   2);
        stats.put(POWER,  3);
        stats.put(HEALTH, 12);
        stats.put(SPEED,  5);
    }
}
