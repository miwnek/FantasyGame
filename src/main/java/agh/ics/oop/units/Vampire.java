package agh.ics.oop.units;

import agh.ics.oop.AbstractUnit;

import agh.ics.oop.EStats;

import java.util.HashMap;


import static agh.ics.oop.EStats.Stats.*;
import static agh.ics.oop.EUnits.AttackType.MELEE;
import static agh.ics.oop.EUnits.UnitType.VAMPIRE;

public class Vampire extends AbstractUnit {
    public Vampire () {
        unit = VAMPIRE;
        attack = MELEE;
        special = false;
        //populate stats
        stats = new HashMap<>();
        stats.put(COST,   20);
        stats.put(POWER,  10);
        stats.put(HEALTH, 20);
        stats.put(SPEED,  10);
    }
}