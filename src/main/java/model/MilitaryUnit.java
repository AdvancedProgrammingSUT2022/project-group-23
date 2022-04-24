package model;

public class MilitaryUnit extends Unit {
    private int strength;
    private int rangeStrength;
    private int range;

    public MilitaryUnit(String name, int cost, int movement,int strength, int rangeStrength, int range, String neededTechnology, String neededResource)
    {
        super(name, cost, movement, neededTechnology, neededResource);
        this.strength = strength;
        this.range = range;
        this.rangeStrength = rangeStrength;
    }

}
