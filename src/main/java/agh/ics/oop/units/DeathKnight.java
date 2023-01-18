package agh.ics.oop.units;

import agh.ics.oop.AbstractUnit;

import agh.ics.oop.EStats;

import java.util.HashMap;


import static agh.ics.oop.EStats.Stats.*;
import static agh.ics.oop.EUnits.AttackType.MELEE;
import static agh.ics.oop.EUnits.UnitType.DEATHKNIGHT;

public class DeathKnight extends AbstractUnit {
    public DeathKnight () {
        unit = DEATHKNIGHT;
        attack = MELEE;
        special = false;
        //populate stats
        stats = new HashMap<>();
        stats.put(COST,   100);
        stats.put(POWER,  50);
        stats.put(HEALTH, 80);
        stats.put(SPEED,  8);
    }
}