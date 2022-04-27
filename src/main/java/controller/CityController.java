package controller;

import model.*;

import java.util.ArrayList;

public class CityController extends GameController{

    private City selectedCity;

    public String createCity(int x, int y){
        if(getCityAtCoordinate(x, y) != null) return "this tile belongs to a city";
        City city = new City(tiles[x][y]);
        currentPlayer.addCity(city);
        Graph graph = createGraph();
        ArrayList<Tile> cityTiles = graph.getTilesAtDistance(coordinatesToNumber(x, y), 1);
        for(Tile tile : cityTiles){
            if(getCityAtCoordinate(tile.getX(),tile.getY()) == null)
                city.addTile(tile);
        }
        return "city founded";
    }

    public City getCityAtCoordinate(int x, int y){
        for(User user : players){
            for(City city : user.getCities()){
                for(Tile tile : city.getTiles()){
                    if(tile.getX() == x && tile.getY() == y) return city;
                }
            }
        }
        return null;
    }

    public ArrayList<Tile> possibleTilesForCitizen(Tile tile)
    {
        ArrayList<Tile> possibleTiles=createGraph().getTilesAtDistance(coordinatesToNumber(tile.getX(), tile.getY()),2);
        for (Tile tile1 : createGraph().getTilesAtDistance(coordinatesToNumber(tile.getX(), tile.getY()), 1)) {
            possibleTiles.add(tile1);
        }
        for (Tile possibleTile : possibleTiles) {
            if(!selectedCity.getTiles().contains(possibleTile))
            {
                possibleTiles.remove(possibleTile);
            }
        }
        return possibleTiles;
    }

    public String putCitizenToWork(Tile tile)
    {
        ArrayList<Tile> possibleTiles= possibleTilesForCitizen(selectedCity.getCapital());
        if(!possibleTiles.contains(tile))
        {
            return "citizens can't work on this tile";
        }
        if(selectedCity.getCountOfCitizens()-selectedCity.getTilesWithCitizen().size()==0)
        {
            return "there is no workless citizen, you need to remove a citizen from a tile first";
        }
        selectedCity.addCitizenToTile(tile);
        return "citizen added to tile successfully";
    }

    public String removeCitizen(Tile tile)
    {
        for (Tile tile1 : selectedCity.getTilesWithCitizen()) {
            if(tile1.equals(tile)){
                selectedCity.removeCitizenFromTile(tile);
                return "citizen removed successfully";
            }
        }
        return "this tile doesn't have any citizen";
    }
}
