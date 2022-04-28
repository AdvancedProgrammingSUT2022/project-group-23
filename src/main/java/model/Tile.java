package model;

import jdk.jshell.execution.Util;

import java.util.ArrayList;
import java.util.HashMap;

public class Tile {
    private int x;
    private int y;
    private Terrain terrain;
    private Feature feature;
    private Resource resource;
    private ArrayList<Integer> rivers;
    private Improvement improvement;
    private HashMap<Integer,String> visibility;
    private int citizen;

    public Tile(int x,int y)
    {
        this.x=x;
        this.y=y;
        this.rivers = new ArrayList<>();
        this.visibility = new HashMap<>();
    }

    public int getX () {
        return x;
    }

    public int getY () {
        return y;
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

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public ArrayList<Integer> getRivers () {
        return rivers;
    }

    public void addRiver(int riverPosition){
        if(!rivers.contains(riverPosition))rivers.add(riverPosition);
    }

    public Improvement getImprovement () {
        return improvement;
    }

    public String getVisibilityForUser (int userId) {
        return visibility.get(userId);
    }

    public void setVisibilityForUser (String visibility, int userId) {
        this.visibility.put(userId, visibility);
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

    public int getFood(){
        int food=terrain.getFood();
        if(feature!=null){
            food += feature.getFood();
        }
        if(resource!=null){
            food+= resource.getFood();
        }
        if(improvement!=null){
            food+=improvement.getFood();
        }
        return food;
    }

    public int getProduction(){
        int production=terrain.getProduction();
        if(feature!=null){
            production += feature.getProduction();
        }
        if(resource!=null){
            production+= resource.getProduction();
        }
        if(improvement!=null){
            production+=improvement.getProduction();
        }
        return production;
    }

    public int getGold(){
        int gold=terrain.getGold();
        if(feature!=null){
            gold += feature.getGold();
        }
        if(resource!=null){
            gold += resource.getGold();
        }
        if(improvement!=null){
            gold+=improvement.getGold();
        }
        gold += rivers.size();
        return gold;
    }
}
