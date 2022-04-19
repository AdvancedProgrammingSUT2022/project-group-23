package model;

import java.util.ArrayList;

public class Terrain {
    private String name;
    private int gold;
    private int food;
    private int production;
    private int movementCost;
    private int combatPercentage;
    private ArrayList<Resource> resources;

    Terrain(String name)
    {
        this.name=name;
    }

    public String getName () {
        return name;
    }

    public int getGold () {
        return gold;
    }

    public int getFood () {
        return food;
    }

    public int getProduction () {
        return production;
    }

    public int getMovementCost () {
        return movementCost;
    }

    public int getCombatPercentage () {
        return combatPercentage;
    }

    public ArrayList<Resource> getResources () {
        return resources;
    }

    public void setGold (int gold) {
        this.gold = gold;
    }

    public void setFood (int food) {
        this.food = food;
    }

    public void setProduction (int production) {
        this.production = production;
    }

    public void setCombatPercentage (int combatPercentage) {
        this.combatPercentage = combatPercentage;
    }

    public void setMovementCost (int movementCost) {
        this.movementCost = movementCost;
    }
}
