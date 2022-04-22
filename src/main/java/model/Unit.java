package model;

import java.util.ArrayList;

public class Unit {
    protected String name;

    protected String state;
    protected int cost;
    protected int movement;
    protected int remainingMoves;
    protected int x;
    protected int y;

    protected int health;
    protected ArrayList<Tile> moves;
    public Unit(String name, int cost, int movement, int x, int y) {
        this.name = name;
        this.cost = cost;
        this.movement = movement;
        this.x = x;
        this.y = y;
        this.health = 10;
        this.moves = null;
        this.state = "ready";
        this.remainingMoves = this.movement;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getRemainingMoves() {
        return remainingMoves;
    }

    public void setRemainingMoves(int remainingMoves) {
        this.remainingMoves = remainingMoves;
    }
}
