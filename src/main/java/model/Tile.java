package model;


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
    private HashMap<Integer,ArrayList<String>> oldInfo;
    private boolean isRoad;
    private boolean hasLooted;
    private Improvement lootedImprovement;

    public Tile(int x,int y)
    {
        this.x=x;
        this.y=y;
        this.rivers = new ArrayList<>();
        this.visibility = new HashMap<>();
        this.oldInfo = new HashMap<>();
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


    public ArrayList<String> getOldInfoForUser(int userId) {
        return oldInfo.get(userId);
    }

    public void setOldInfoForUser (ArrayList<String> info, int userId) {
        this.oldInfo.put(userId, info);
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
        if(isRoad)return 1;
        return movementCost;
    }

    public int getPrice(){
        int price=terrain.getPrice();
        if(feature!=null){
            price += feature.getPrice();
        }
        return price;
    }

    public int getFood(){
        int food = 0;
        if(terrain != null) food = terrain.getFood();
        if(feature!=null){
            food += feature.getFood();
        }
        if(resource!=null){
            if(resource.getNeededImprovement() == null ||
                    (improvement != null && resource.getNeededImprovement().equals(improvement.getName())))
                food += resource.getFood();
        }
        if(improvement!=null){
            food+=improvement.getFood();
        }
        return food;
    }

    public int getProduction(){
        int production = 0;
        if(terrain != null)production = terrain.getProduction();
        if(feature!=null){
            production += feature.getProduction();
        }
        if(resource!=null){
            if(resource.getNeededImprovement() == null ||
                    (improvement != null && resource.getNeededImprovement().equals(improvement.getName())))
                production+= resource.getProduction();
        }
        if(improvement!=null){
            production+=improvement.getProduction();
        }
        return production;
    }

    public int getGold(){
        int gold = 0;
        if(terrain != null)gold = terrain.getGold();
        if(feature!=null){
            gold += feature.getGold();
        }
        if(resource!=null){
            if(resource.getNeededImprovement() == null ||
                    (improvement != null && resource.getNeededImprovement().equals(improvement.getName())))
                gold += resource.getGold();
        }
        if(improvement!=null){
            gold+=improvement.getGold();
        }
        gold += rivers.size();
        return gold;
    }

    public boolean isRoad () {
        return isRoad;
    }

    public void setRoad (boolean road) {
        isRoad = road;
    }

    public void setImprovement (Improvement improvement) {
        this.improvement = improvement;
    }

    public Improvement getLootedImprovement () {
        return lootedImprovement;
    }

    public void setLootedImprovement (Improvement lootedImprovement) {
        this.lootedImprovement = lootedImprovement;
    }

    public boolean isHasLooted () {
        return hasLooted;
    }

    public void setHasLooted (boolean hasLooted) {
        this.hasLooted = hasLooted;
    }
}
