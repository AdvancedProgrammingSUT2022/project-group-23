package model;

import java.util.ArrayList;
import java.util.HashMap;

public class City {
    private static int countOfCities = 0;

    private int foodLeft=0;
    private int id;
    private Tile capital;
    private int countOfCitizens=1;
    private ArrayList<Tile> tiles = new ArrayList<>();
    private ArrayList<Tile> tilesWithCitizen = new ArrayList<>();

    private HashMap<String, Integer> waitedUnits = new HashMap<>();

    private String constructingUnit = null;
    public City(Tile capital)
    {
        this.capital=capital;
        this.tiles.add(capital);
        this.id = countOfCities;
        countOfCities++;
    }

    public Tile getCapital () {
        return capital;
    }

    public int getCountOfCitizens () {
        return countOfCitizens;
    }

    public void setCountOfCitizens (int countOfCitizens) {
        this.countOfCitizens = countOfCitizens;
    }

    public ArrayList<Tile> getTiles () {
        return tiles;
    }

    public ArrayList<Tile> getTilesWithCitizen () {
        return tilesWithCitizen;
    }

    public void setCapital (Tile capital) {
        this.capital = capital;
    }


    public void addTile(Tile tile)
    {
        tiles.add(tile);
    }

    public void removeCitizenFromTile(Tile tile)
    {
        for (Tile tile1 : tilesWithCitizen) {
            if(tile1.equals(tile))
            {
                tilesWithCitizen.remove(tile);
            }
        }
    }

    public void addCitizenToTile(Tile tile)
    {
        tilesWithCitizen.add(tile);
    }

    public int getFoodLeft () {
        return foodLeft;
    }

    public int getId() {
        return id;
    }

    public void setFoodLeft (int foodLeft) {
        this.foodLeft = foodLeft;
    }

    public HashMap<String, Integer> getWaitedUnits() {
        return waitedUnits;
    }

    public String getConstructingUnit() {
        return constructingUnit;
    }

    public void setConstructingUnit(String constructingUnit) {
        this.constructingUnit = constructingUnit;
    }

    public int totalFood(){
        int food=capital.getFood();
       for (Tile tile : tilesWithCitizen) {
           food += tile.getFood();
       }
       food -= countOfCitizens*2;
       return food;
   }

    public int production(){
        int production=capital.getProduction();
        for (Tile tile : tilesWithCitizen) {
            production += tile.getProduction();
        }
        production+=(countOfCitizens-tilesWithCitizen.size());
        return production;
    }

    public int gold(){
        int gold=capital.getGold();
        for (Tile tile : tilesWithCitizen) {
            gold += tile.getGold();
        }
        return gold;
    }


}
