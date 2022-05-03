package controller;

import database.UnitsDatabase;
import model.*;

import java.util.ArrayList;
import java.util.HashMap;

public class CityController extends GameController{



    public String createCity(int x, int y){
        if(getCityAtCoordinate(x, y) != null) return "this tile belongs to a city";
        City city = new City(tiles[x][y]);
        currentPlayer.addCity(city);
        currentPlayer.setHappiness(currentPlayer.getHappiness()-5);
        Graph graph = createGraph();
        ArrayList<Tile> cityTiles = graph.getTilesAtDistance(coordinatesToNumber(x, y), 1);
        for(Tile tile : cityTiles){
            if(getCityAtCoordinate(tile.getX(),tile.getY()) == null)
                city.addTile(tile);
        }
        return "city founded";
    }

    public String selectCity(int x, int y){
        if(!isCoordinateValid(x, y))return "invalid coordinate";
        selectedCity = getCityAtCoordinate(x, y);
        if(selectedCity == null)return "this tiles doesn't belong to any city";
        if(getCityOwner(selectedCity) != currentPlayer){
            selectedCity = null;
            return "city doesn't belong to you";
        }
        return "city id : " + selectedCity.getId() + " - capital: (" + selectedCity.getCapital().getX() + "," + selectedCity.getCapital().getY() + ") number of citizens: "+selectedCity.getCountOfCitizens()+" number of tiles: "+selectedCity.getTiles().size();
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
    public User getCityOwner(City city){
        for(User user : players){
            for(City tempCity : user.getCities()){
                if(tempCity.equals(city)) return user;
            }
        }
        return null;
    }

    public ArrayList<Tile> possibleTilesForCitizen(Tile tile)
    {
        if(selectedCity == null) return null;
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

    public String putCitizenToWork(int x, int y) {
        Tile tile = tiles[x][y];
        if(selectedCity == null) return "no selected city";
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

    public String removeCitizen(int x, int y) {
        Tile tile = tiles[x][y];
        if(selectedCity == null) return "no selected city";
        for (Tile tile1 : selectedCity.getTilesWithCitizen()) {
            if(tile1.equals(tile)){
                selectedCity.removeCitizenFromTile(tile);
                return "citizen removed successfully";
            }
        }
        return "this tile doesn't have any citizen";
    }

    public String nextTurn(){
        for (City city : currentPlayer.getCities()) {
            if(city.getConstructingUnit() == null)return "you have to choose a production for city : " + city.getId();
        }
        for (City city : currentPlayer.getCities()) {
            city.setCanAttack(true);
            if(city.getHealth()<20) city.setHealth(city.getHealth()+1);
            for (Tile tile : city.getTiles()) {
                if(tile.getResource()!=null){
                    if(tile.getResource().getNeededImprovement()==null ||
                            (tile.getResource().getNeededImprovement()!=null && tile.getImprovement().getName().equals(tile.getResource().getNeededImprovement()))){
                        if(tile.getResource().isLuxury() && !currentPlayer.getLuxuryResources().contains(tile.getResource().getName())){
                            currentPlayer.addLuxuryResource(tile.getResource().getName());
                            currentPlayer.setHappiness(currentPlayer.getHappiness()+4);
                        }
                    }
                }
            }
            currentPlayer.setGold(currentPlayer.getGold() + city.gold());
            if(currentPlayer.getIsUnhappy() == 0)city.setFoodLeft(city.getFoodLeft() + city.totalFood());
            if(currentPlayer.getIsUnhappy() == 1)city.setFoodLeft(city.getFoodLeft() + city.totalFood() / 3);
            if(selectedCity.getConstructingUnit().equals("Settler"))city.setFoodLeft(0);
            if (city.getFoodLeft() >= (Math.pow(2, city.getCountOfCitizens())) && currentPlayer.getIsUnhappy()==0) {
                currentPlayer.setHappiness(currentPlayer.getHappiness()-3);
                city.setFoodLeft(0);
                city.setCountOfCitizens(city.getCountOfCitizens() + 1);
            }
            HashMap<String, Integer> waitedUnits = city.getWaitedUnits();
            waitedUnits.put(city.getConstructingUnit(),waitedUnits.get(city.getConstructingUnit()) - city.production());
            if(waitedUnits.get(city.getConstructingUnit()) <= 0){
                createUnit(city.getConstructingUnit(), city);
                waitedUnits.remove(city.getConstructingUnit());
                city.setConstructingUnit(null);
            }

        }
        return "ok";
    }
    public String constructUnit(String name){
        if(selectedCity == null)return "no city selected";
        HashMap<String, Integer> waitedUnits = selectedCity.getWaitedUnits();
        selectedCity.setConstructingUnit(name);
        if(!waitedUnits.containsKey(name)){
            int cost = 0;
            if(name.equals("Worker")) cost = 70;
            else if(name.equals("Settler")){
                if(selectedCity.getCountOfCitizens() < 2 )return "you can't build Settler in city with less than 2 citizens";
                if(currentPlayer.getIsUnhappy() == 1)return "you can't build Settler when your civilization is unhappy";
                cost = 89;
            }
            else {
                for (Unit unit : UnitsDatabase.getUnits()) {
                    if (unit.getName().equals(name)) {
                        cost = unit.getCost();
                        break;
                    }
                }
            }
            waitedUnits.put(name, cost);
        }
        return "unit is being constructed";

    }
    public void createUnit(String name, City city){
        if(name.equals("Settler"))currentPlayer.addUnit(new SettlerUnit(city.getCapital().getX(),city.getCapital().getY()));
        else if(name.equals("Worker"))currentPlayer.addUnit(new WorkerUnit(city.getCapital().getX(),city.getCapital().getY()));
        else {
            for (Unit unit : UnitsDatabase.getUnits()) {
                if (unit.getName().equals(name)) {
                    MilitaryUnit militaryUnit = (MilitaryUnit)unit;
                    MilitaryUnit newUnit = militaryUnit.getCopy();
                    newUnit.setX(city.getCapital().getX());
                    newUnit.setY(city.getCapital().getY());
                    currentPlayer.addUnit(newUnit);
                    break;
                }
            }
        }

    }

    public ArrayList<Tile> possibleTilesForPurchase(){
        if(selectedCity == null) return null;
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
        if(selectedCity == null) return "no selected city";
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

    public ArrayList<Unit> reachableUnits(){
        ArrayList<Tile> reachableTiles=new ArrayList<>();
        ArrayList<Unit> reachableUnits=new ArrayList<>();
        for (Tile tile : selectedCity.getTiles()) {
            for (Tile tile1 : createGraph().getTilesAtDistance(coordinatesToNumber(tile.getX(), tile.getY()), 1)) {
                if(!reachableTiles.contains(tile1) && !selectedCity.getTiles().contains(tile1)){
                    reachableTiles.add(tile1);
                }
            }
            for (Tile tile1 : createGraph().getTilesAtDistance(coordinatesToNumber(tile.getX(), tile.getY()), 2)) {
                if(!reachableTiles.contains(tile1) && !selectedCity.getTiles().contains(tile1)){
                    reachableTiles.add(tile1);
                }
            }
        }
        for (User player : players) {
            if(!player.equals(currentPlayer)){
                for (Unit unit : player.getUnits()) {
                    if(reachableTiles.contains(tiles[unit.getX()][unit.getY()]) && (unit instanceof  MilitaryUnit)){
                        reachableUnits.add(unit);
                    }
                }
            }
        }
        return reachableUnits;
    }

    public String attackUnit(Unit unit){
        if(!selectedCity.isCanAttack()){
            return "you already have attacked with this city";
        }
        MilitaryUnit militaryUnit = (MilitaryUnit) unit;
        militaryUnit.setHealth(militaryUnit.getHealth()-selectedCity.strength());
        selectedCity.setCanAttack(false);
        if(militaryUnit.getHealth()<=0){
            for (User player : players) {
                if (player.getUnits().contains(militaryUnit)){
                    player.getUnits().remove(militaryUnit);
                }
            }
            return "you killed the unit!";
        }else {
            return "you attacked the unit, but its still alive!";
        }
    }
}

