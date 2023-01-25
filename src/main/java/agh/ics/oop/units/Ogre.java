package agh.ics.oop.units;

import agh.ics.oop.AbstractUnit;

import java.util.HashMap;


import static agh.ics.oop.EStats.Stats.*;
import static agh.ics.oop.EUnits.AttackType.MELEE;
import static agh.ics.oop.EUnits.UnitType.OGRE;

public class Ogre extends AbstractUnit {
    public Ogre () {
        unit = OGRE;
        attack = MELEE;
        special = false;
        //populate stats
        stats = new HashMap<>();
        stats.put(COST,   35);
        stats.put(POWER,  20);
        stats.put(HEALTH, 40);
        stats.put(SPEED,  5);
    }
}
