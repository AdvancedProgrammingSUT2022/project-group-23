package model;

public class Unit {
    protected String name;
    protected int cost;
    protected int movement;
    protected int x;
    protected int y;

    public Unit(String name)
    {
        this.name=name;
    }

    public String getName () {
        return name;
    }

    public void setCost (int cost) {
        this.cost = cost;
    }

    public void setMovement (int movement) {
        this.movement = movement;
    }

    public void setX (int x) {
        this.x = x;
    }

    public void setY (int y) {
        this.y = y;
    }
}
