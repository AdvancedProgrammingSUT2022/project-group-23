package model;

import java.util.ArrayList;

public class Improvement {
    private String name;
    private int gold;
    private int food;
    private int production;

    private String neededTechnology;
    private ArrayList<String> placesItCanBeBuild;

    public Improvement(String name, int gold, int food, int production, String neededTechnology, ArrayList<String> placesItCanBeBuild) {
        this.name = name;
        this.gold = gold;
        this.food = food;
        this.production = production;
        this.neededTechnology = neededTechnology;
        this.placesItCanBeBuild = placesItCanBeBuild;
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

    public void setGold (int gold) {
        this.gold = gold;
    }

    public void setFood (int food) {
        this.food = food;
    }

    public void setProduction (int production) {
        this.production = production;
    }

    public String getNeededTechnology () {
        return neededTechnology;
    }

    public ArrayList<String> getPlacesItCanBeBuild () {
        return placesItCanBeBuild;
    }
}
