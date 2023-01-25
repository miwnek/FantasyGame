# Champions of spell and blade

My project for the object-oriented-programming course at the AGH university in Cracow.
Project requirements [here](https://github.com/apohllo/obiektowe-lab/blob/master/proj2/Czempioni%20czaru%20i%20ostrza.md) (in polish).

## Description
This is an homage to the Heroes of Might and Magic 3 battle system. For the most part battle works in a similar way. Players pick their factions (for now Undead or Orcs stronghold) and then get to pick units for their final army from said factions. When a player hovers over a unit sprite, their stats and abilities (if they have any) show.
Unit costs are displayed under their sprites. When both players lock in their selections (not empty armies with a price under 200 gold) the battle begins. On each turn a unit can:
- attack,
- move then attack,
- use its special ability (if it has any),
- do nothing.

Unit priority is determined on ech unit's speed stat. For ranged units line of sight is determined whether there is an obstacle on a straight path from the middle of the attacking unit's hex tile to the middle of the target unit's hex tile. If a unit tries to move when there is an enemy unit on a neighboring tile, it gets counterattacked by the enemy unit. For now there are two special abilities in the game:
- necromancer can animate fallen enemy units as skeletons (if the tile where the unit died is not occupied),
- shaman can cast a fireball spell dealing damge to all enemy units on a chosen tile and its neighbors.
Sprites were downloaded from open source websites.
