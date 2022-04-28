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
        if(selectedCity.getTilesWithCitizen().contains(tile))
        {
            return "there is already a citizen working on this tile";
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

    public void nextTurn(){
        for (City city : currentPlayer.getCities()) {
            currentPlayer.setGold(currentPlayer.getGold() + city.gold());
            city.setFoodLeft(city.getFoodLeft() + city.totalFood());
            if (city.getFoodLeft() >= (Math.pow(2, city.getCountOfCitizens()))) {
                city.setFoodLeft(0);
                city.setCountOfCitizens(city.getCountOfCitizens() + 1);
            }
        }
    }

    public ArrayList<Tile> possibleTilesForPurchase(){
        ArrayList<Tile> possibleTiles= new ArrayList<Tile>();
        for (Tile tile : selectedCity.getTiles()) {
            for (Tile tile1 : createGraph().getTilesAtDistance(coordinatesToNumber(tile.getX(), tile.getY()), 1)) {
                if(!possibleTiles.contains(tile1) && !selectedCity.getTiles().contains(tile1)){
                    possibleTiles.add(tile1);
                }
            }
        }
        return possibleTiles;
    }

    public String purchaseTile(Tile tile){
        ArrayList<Tile> possibleTiles=possibleTilesForPurchase();
        if(!possibleTiles.contains(tile)){
            return "you can't purchase this tile!";
        }
        int price=tile.getTerrain().getPrice();
        if(tile.getFeature()!=null){
            price += tile.getFeature().getPrice();
        }
        if(currentPlayer.getGold()<price){
            return "you don't have enough gold to purchase this tile";
        }
        if(getCityAtCoordinate(tile.getX(),tile.getY())!=null){
            return "this tile belongs to another civilization";
        }
        currentPlayer.setGold(currentPlayer.getGold()-price);
        selectedCity.addTile(tile);
        return "tile purchased successfully";
    }
}
