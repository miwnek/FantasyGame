package agh.ics.oop.gui;

import static agh.ics.oop.EStats.Stats.SPEED;

import agh.ics.oop.UnitGroup;

import java.util.Comparator;

public class UnitGroupComparator implements Comparator<UnitGroup> {
    @Override
    public int compare(UnitGroup o1, UnitGroup o2) {
        if(o1.getUnitTemplate().getStats().get(SPEED) > o2.getUnitTemplate().getStats().get(SPEED)) return -1;
        else if (o1.getUnitTemplate().getStats().get(SPEED) < o2.getUnitTemplate().getStats().get(SPEED)) return 1;
        else return 0;
    }
}
