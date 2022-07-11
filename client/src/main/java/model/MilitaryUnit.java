package model;

public class MilitaryUnit extends Unit {
    private int strength;
    private int rangeStrength;
    private int range;

    private String combatType;

    public MilitaryUnit(String name, int cost, String combatType, int movement,int strength, int rangeStrength, int range, String neededTechnology, String neededResource)
    {
        super(name, cost, movement, neededTechnology, neededResource);
        this.strength = strength;
        this.range = range;
        this.rangeStrength = rangeStrength;
        this.combatType = combatType;
    }
    public MilitaryUnit getCopy(){
        return new MilitaryUnit(name, cost, combatType, movement, strength, rangeStrength, range, neededTechnology, neededResource);
    }

    public int getRange () {
        return range;
    }

    public int getStrength () {
        return strength;
    }

    public int getRangeStrength () {
        return rangeStrength;
    }

    public String getCombatType () {
        return combatType;
    }
}
