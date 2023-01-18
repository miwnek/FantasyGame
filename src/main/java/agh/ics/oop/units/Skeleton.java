package agh.ics.oop.units;

import agh.ics.oop.AbstractUnit;

import agh.ics.oop.EStats;

import java.util.HashMap;


import static agh.ics.oop.EStats.Stats.*;
import static agh.ics.oop.EUnits.AttackType.RANGED;
import static agh.ics.oop.EUnits.UnitType.SKELETON;

public class Skeleton extends AbstractUnit {
    private static final int health = 8;
     public Skeleton () {
         unit = SKELETON;
         attack = RANGED;
         special = false;
         //populate stats
         stats = new HashMap<>();
         stats.put(COST,   5);
         stats.put(POWER,  3);
         stats.put(HEALTH, 8);
         stats.put(SPEED,  6);
     }

     public static int getHealth() {
         return health;
     }
}
