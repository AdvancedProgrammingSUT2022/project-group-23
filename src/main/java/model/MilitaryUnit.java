package model;

public class MilitaryUnit extends Unit {
    private int strength;
    private int rangeStrength;
    private int range;

    public MilitaryUnit(String name, int cost, int movement, int x, int y)
    {
        super(name, cost, movement, x, y);
    }

}
