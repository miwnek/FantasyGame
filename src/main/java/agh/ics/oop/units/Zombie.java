package agh.ics.oop.units;

import agh.ics.oop.AbstractUnit;

import agh.ics.oop.EStats;

import java.util.HashMap;


import static agh.ics.oop.EStats.Stats.*;
import static agh.ics.oop.EUnits.AttackType.MELEE;
import static agh.ics.oop.EUnits.UnitType.ZOMBIE;

public class Zombie extends AbstractUnit {
    public Zombie () {
        unit = ZOMBIE;
        attack = MELEE;
        special = false;
        //populate stats
        stats = new HashMap<>();
        stats.put(COST,   1);
        stats.put(POWER,  2);
        stats.put(HEALTH, 10);
        stats.put(SPEED,  4);
    }
}