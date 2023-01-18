package agh.ics.oop.units;

import agh.ics.oop.AbstractUnit;
import agh.ics.oop.AbstractUnitGroup;

import agh.ics.oop.EStats;
import agh.ics.oop.EUnits;

import java.util.HashMap;


import static agh.ics.oop.EStats.Stats.*;
import static agh.ics.oop.EUnits.AttackType.RANGED;
import static agh.ics.oop.EUnits.UnitType.NECROMANCER;

public class Necromancer extends AbstractUnit {
    private final int specialPower = 10;
    public Necromancer () {
        unit = NECROMANCER;
        attack = RANGED;
        special = true;
        //populate stats
        stats = new HashMap<>();
        stats.put(COST,   50);
        stats.put(POWER,  15);
        stats.put(HEALTH, 30);
        stats.put(SPEED,  7);
    }

    public int specialAbility(int num, AbstractUnitGroup group, int player) {
        AbstractUnit unit = group.getUnitTemplate();

        int count = group.getMaxNumber();
        int maxHealth = unit.getStats().get(HEALTH);
        int skeletonHealth = Skeleton.getHealth();
        int healthToGive = num *  specialPower * count * maxHealth * 2 / 3;

        return (healthToGive % skeletonHealth);
    }

}