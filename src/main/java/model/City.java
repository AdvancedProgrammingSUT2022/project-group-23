package model;

import java.util.ArrayList;
import java.util.HashMap;

public class City {
    private Tile capital;
    private int countOfCitizens;
    private ArrayList<Tile> tiles;
    private HashMap<Integer,Tile> citizenTiles;

    City(Tile capital)
    {
        this.capital=capital;
    }

    public Tile getCapital () {
        return capital;
    }

    public int getCountOfCitizens () {
        return countOfCitizens;
    }

    public ArrayList<Tile> getTiles () {
        return tiles;
    }

    public HashMap<Integer, Tile> getCitizenTiles () {
        return citizenTiles;
    }


    public void setCapital (Tile capital) {
        this.capital = capital;
    }

    public void setCountOfCitizens (int countOfCitizens) {
        this.countOfCitizens = countOfCitizens;
    }

    public void addTile(Tile tile)
    {
        tiles.add(tile);
    }

    public void removeCitizenFromTile(Tile tile)
    {

    }

    public void addCitizenToTile(Tile tile)
    {

    }


}
