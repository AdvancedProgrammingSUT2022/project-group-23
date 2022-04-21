package model;

import java.util.ArrayList;

public class Terrain {
    private String name;
    private int gold;
    private int food;
    private int production;
    private int movementCost;
    private int combatPercentage;

    public Terrain(String name, int gold, int food, int production, int movementCost, int combatPercentage) {
        this.name = name;
        this.gold = gold;
        this.food = food;
        this.production = production;
        this.movementCost = movementCost;
        this.combatPercentage = combatPercentage;
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
