package model;

import jdk.jshell.execution.Util;

import java.util.ArrayList;
import java.util.HashMap;

public class Tile {
    private int x;
    private int y;
    private int gold;
    private int production;
    private int food;
    private Terrain terrain;
    private Feature feature;
    private Resource resource;
    private ArrayList<Integer> rivers;
    private HashMap<Integer,ArrayList<Improvement>> improvements;
    private HashMap<Integer,String> visibility;
    private int citizen;

    public Tile(int x,int y)
    {
        this.x=x;
        this.y=y;
        this.rivers = new ArrayList<>();
        this.visibility = new HashMap<>();
        this.improvements = new HashMap<>();
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

    public Resource getResource () {
        return resource;
    }

    public ArrayList<Integer> getRivers () {
        return rivers;
    }

    public void addRiver(int riverPosition){
        if(!rivers.contains(riverPosition))rivers.add(riverPosition);
    }

    public ArrayList<Improvement> getImprovementsForUser (int userId) {
        return improvements.get(userId);
    }

    public String getVisibilityForUser (int userId) {
        return visibility.get(userId);
    }

    public void setVisibilityForUser (String visibility, int userId) {
        this.visibility.put(userId, visibility);
    }

    public void addImprovementForUser(Improvement improvement, int userId) {
        if(!improvements.containsKey(userId)) improvements.put(userId, new ArrayList<>());
        improvements.get(userId).add(improvement);
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

    public int getMovementCost(){
        int movementCost = 0;
        if(this.getFeature() != null){
            if(this.getFeature().getMovementCost() == -1)return -1;
            movementCost += this.getFeature().getMovementCost();
        }
        if(this.getTerrain() != null){
            if(this.getTerrain().getMovementCost() == -1)return -1;
            movementCost += this.getTerrain().getMovementCost();
        }
        return movementCost;
    }
}
