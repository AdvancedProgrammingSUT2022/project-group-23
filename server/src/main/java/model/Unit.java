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

    protected String neededTechnology;
    protected String neededResource;
    protected int health;
    protected ArrayList<Integer> moves;
    public Unit(String name, int cost, int movement, String neededTechnology, String neededResource) {
        this.name = name;
        this.cost = cost;
        this.movement = movement;
        this.neededResource = neededResource;
        this.neededTechnology = neededTechnology;
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

    public int getMovement () {
        return movement;
    }

    public void setX (int x) {
        this.x = x;
    }

    public void setY (int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getHealth() {
        return health;
    }

    public int getRemainingMoves() {
        return remainingMoves;
    }

    public void setRemainingMoves(int remainingMoves) {
        this.remainingMoves = remainingMoves;
    }

    public ArrayList<Integer> getMoves() {
        return moves;
    }

    public void setMoves(ArrayList<Integer> moves) {
        this.moves = moves;
    }

    public String getNeededTechnology () {
        return neededTechnology;
    }

    public int getCost() {
        return cost;
    }

    public void setHealth (int health) {
        this.health = health;
    }

    public String getNeededResource() {
        return neededResource;
    }
}
