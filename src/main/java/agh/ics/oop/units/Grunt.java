package agh.ics.oop.units;

import agh.ics.oop.AbstractUnit;

import agh.ics.oop.EStats;

import java.util.HashMap;


import static agh.ics.oop.EStats.Stats.*;
import static agh.ics.oop.EUnits.AttackType.MELEE;
import static agh.ics.oop.EUnits.UnitType.GRUNT;

public class Grunt extends AbstractUnit {
    public Grunt () {
        unit = GRUNT;
        attack = MELEE;
        special = false;
        //populate stats
        stats = new HashMap<>();
        stats.put(COST,   10);
        stats.put(POWER,  5);
        stats.put(HEALTH, 20);
        stats.put(SPEED,  6);
    }
}
