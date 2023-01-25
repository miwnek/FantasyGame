package agh.ics.oop.units;

import agh.ics.oop.AbstractUnit;
import agh.ics.oop.UnitGroup;

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

    public int specialAbility(int num, UnitGroup group) {
        AbstractUnit unit = group.getUnitTemplate();

        int count = group.getMaxNumber();
        int maxHealth = unit.getStats().get(HEALTH);
        int skeletonHealth = Skeleton.getHealth();
        int healthToGive = (num *  specialPower * count * maxHealth ) / 50;
        System.out.println(String.valueOf(num) + ", " + String.valueOf(specialPower) + ", " + String.valueOf(count) + ", " + String.valueOf(maxHealth) + ", ");
        System.out.println(String.valueOf(num *  specialPower * count * maxHealth * 2 / 3));
        System.out.println(String.valueOf(healthToGive / skeletonHealth));
        return Math.max(1, (healthToGive / skeletonHealth));
    }

}