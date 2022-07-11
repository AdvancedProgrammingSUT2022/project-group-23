package model;

public class Building {
    private String name;
    private int cost;
    private int food;
    private int happiness;
    private int cup;
    private int defence;
    private int maintenance;
    private String neededTechnology;
    private String neededBuilding;

    public Building(String name,int cost,int food,int happiness,int cup,int defence,int maintenance,String neededBuilding,String neededTechnology){
        this.name=name;
        this.cost=cost;
        this.food=food;
        this.happiness=happiness;
        this.cup=cup;
        this.defence=defence;
        this.maintenance=maintenance;
        this.neededBuilding=neededBuilding;
        this.neededTechnology=neededTechnology;
    }


    public String getName () {
        return name;
    }

    public int getCost () {
        return cost;
    }

    public int getFood () {
        return food;
    }

    public int getHappiness () {
        return happiness;
    }

    public int getCup () {
        return cup;
    }

    public int getDefence () {
        return defence;
    }

    public int getMaintenance () {
        return maintenance;
    }

    public String getNeededTechnology () {
        return neededTechnology;
    }

    public String getNeededBuilding () {
        return neededBuilding;
    }
}
