package model;

import java.util.ArrayList;

public class Tile {
    private int x;
    private int y;
    private int gold;
    private int production;
    private int food;
    private Terrain terrain;
    private Feature feature;
    private ArrayList<Resource> resources;
    private ArrayList<River> rivers;
    private ArrayList<Improvement> improvements;
    private String visibility;
    private int citizen;

    Tile(int x,int y)
    {
        this.x=x;
        this.y=y;
    }

    public int getX () {
        return x;
    }

    public int getY () {
        return y;
    }

    public int getGold () {
        return gold;
    }

    public int getProduction () {
        return production;
    }

    public int getFood () {
        return food;
    }

    public Terrain getTerrain () {
        return terrain;
    }

    public Feature getFeature () {
        return feature;
    }

    public void setTerrain(Terrain terrain) {
        this.terrain = terrain;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
    }

    public ArrayList<Resource> getResources () {
        return resources;
    }

    public ArrayList<River> getRivers () {
        return rivers;
    }

    public ArrayList<Improvement> getImprovements () {
        return improvements;
    }

    public String getVisibility () {
        return visibility;
    }

    public void setVisibility (String visibility) {
        this.visibility = visibility;
    }

    public void addImprovement(Improvement improvement)
    {
        improvements.add(improvement);
    }

    public void setGold (int gold) {
        this.gold = gold;
    }

    public void setProduction (int production) {
        this.production = production;
    }

    public void setFood (int food) {
        this.food = food;
    }
}
