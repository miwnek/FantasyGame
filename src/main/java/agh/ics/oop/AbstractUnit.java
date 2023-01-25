package agh.ics.oop;

import java.util.Map;
import agh.ics.oop.EStats.Stats;
import agh.ics.oop.EUnits.UnitType;
import agh.ics.oop.EUnits.AttackType;

public class AbstractUnit implements IUnit {
    protected Map<Stats, Integer> stats;
    protected UnitType unit;
    protected AttackType attack;
    protected boolean special;

    @Override
    public boolean hasSpecial() {
        return special;
    }

    @Override
    public Map<Stats, Integer> getStats() {
        return stats;
    }

    @Override
    public UnitType getUnitType() {
        return unit;
    }

    @Override
    public AttackType getAttackType() {
        return attack;
    }
    public String attackTypeString() {
        if(attack == AttackType.MELEE) return "melee";
        else return "ranged";
    }

    //TODO: add sample images and their actual paths
    @Override
    public String getPicturePath() {
        switch(unit) {
            case ZOMBIE      -> {return "src/main/resources/undeadu1.png";}
            case SKELETON    -> {return "src/main/resources/undeadu2.png";}
            case VAMPIRE     -> {return "src/main/resources/undeadu3.png";}
            case NECROMANCER -> {return "src/main/resources/undeadu4.png";}
            case DEATHKNIGHT -> {return "src/main/resources/undeadu5.png";}
            case GOBLIN -> {return "src/main/resources/orcsu1.png";}
            case GRUNT -> {return "src/main/resources/orcsu2.png";}
            case SHAMAN -> {return "src/main/resources/orcsu3.png";}
            case OGRE -> {return "src/main/resources/orcsu4.png";}
            case AXEMASTER -> {return "src/main/resources/orcsu5.png";}
        }
        return null;
    }
    @Override
    public String toString() {
        switch(unit) {
            case SKELETON -> {return "Skeleton";}
            case ZOMBIE -> {return "Zombie";}
            case VAMPIRE -> {return "Vampire";}
            case NECROMANCER -> {return "Necromancer";}
            case DEATHKNIGHT -> {return "Death knight";}
            case GOBLIN -> {return "Goblin";}
            case GRUNT -> {return "Grunt";}
            case SHAMAN -> {return "Shaman";}
            case OGRE -> {return "Ogre";}
            case AXEMASTER -> {return "Axe master";}
            default -> {return "No such unit";}
        }
    }

}
