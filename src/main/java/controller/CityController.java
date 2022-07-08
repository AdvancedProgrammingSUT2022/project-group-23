package controller;

import database.BuildingDatabase;
import database.UnitsDatabase;
import model.*;

import java.util.ArrayList;
import java.util.HashMap;

public class CityController extends GameController{
    public CityController(){
    }

    public String createCity(int x, int y){
        if(getCityAtCoordinate(x, y) != null) return "this tile belongs to a city";
        City city = new City(tiles[x][y]);
        if(currentPlayer.getCities().isEmpty()) city.getBuildings().add(BuildingDatabase.getBuildings().get(0));
        currentPlayer.addCity(city);
        currentPlayer.setHappiness(currentPlayer.getHappiness()-5);
        Graph graph = createGraph();
        ArrayList<Tile> cityTiles = graph.getTilesAtDistance(coordinatesToNumber(x, y), 1);
        for(Tile tile : cityTiles){
            if(getCityAtCoordinate(tile.getX(),tile.getY()) == null)
                city.addTile(tile);
        }
        currentPlayer.setScore(currentPlayer.getScore()+100);
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
        ArrayList<Tile> possibleTilesFinal=new ArrayList<>();
        ArrayList<Tile> possibleTiles=createGraph().getTilesAtDistance(coordinatesToNumber(tile.getX(), tile.getY()),2);
        for (Tile tile1 : createGraph().getTilesAtDistance(coordinatesToNumber(tile.getX(), tile.getY()), 1)) {
            possibleTiles.add(tile1);
        }
        for (Tile possibleTile : possibleTiles) {
            if(selectedCity.getTiles().contains(possibleTile))
            {
                possibleTilesFinal.add(possibleTile);
            }
        }
        return possibleTiles;
    }

    public String putCitizenToWork(int x, int y) {
        Tile tile = tiles[x][y];
        if(selectedCity == null) return "no selected city";
        ArrayList<Tile> possibleTiles= possibleTilesForCitizen(selectedCity.getCapital());
        if(!possibleTiles.contains(tile)) {
            return "citizens can't work on this tile";
        }
        if(selectedCity.getCountOfCitizens()-selectedCity.getTilesWithCitizen().size()==0) {
            return "there is no workless citizen, you need to remove a citizen from a tile first";
        }
        if(selectedCity.getTilesWithCitizen().contains(tile)) {
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
            if(city.getConstructingUnit() == null && city.getConstructingBuilding()==null)return "you have to choose a production for city : " + city.getId();
        }
        for (City city : currentPlayer.getCities()) {
            city.setCanAttack(true);
            if(city.getHealth()<20) city.setHealth(city.getHealth()+1);
            for (Tile tile : city.getTiles()) {
                if(tile.getResource()!=null){
                    if(tile.getResource().getNeededImprovement()==null ||
                            (tile.getResource().getNeededImprovement()!=null && tile.getImprovement()!=null && tile.getImprovement().getName().equals(tile.getResource().getNeededImprovement()))){
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
            if(city.getConstructingUnit()!=null && city.getConstructingUnit().equals("Settler")) city.setFoodLeft(0);
            if (city.getFoodLeft() >= (Math.pow(2, city.getCountOfCitizens())) && currentPlayer.getIsUnhappy()==0) {
                currentPlayer.setHappiness(currentPlayer.getHappiness()-3);
                city.setFoodLeft(0);
                city.setCountOfCitizens(city.getCountOfCitizens() + 1);
            }
            if(city.getFoodLeft()<=-5){
                if((city.getCountOfCitizens()-city.getTilesWithCitizen().size())>0){
                    city.setCountOfCitizens(city.getCountOfCitizens()-1);
                }else {
                    city.setCountOfCitizens(city.getCountOfCitizens()-1);
                    city.getTilesWithCitizen().remove(0);
                }
                city.setFoodLeft(0);
            }
            if(city.getConstructingUnit()!=null) {
                HashMap<String, Integer> waitedUnits = city.getWaitedUnits();
                waitedUnits.put(city.getConstructingUnit(), waitedUnits.get(city.getConstructingUnit()) - city.production());
                if (waitedUnits.get(city.getConstructingUnit()) <= 0) {
                    createUnit(city.getConstructingUnit(), city);
                    currentPlayer.addNotification("you have constructed unit : " + city.getConstructingUnit());
                    waitedUnits.remove(city.getConstructingUnit());
                    city.setConstructingUnit(null);
                }
            }else {
                HashMap<String, Integer> waitedBuildings = city.getWaitedBuildings();
                waitedBuildings.put(city.getConstructingBuilding(), waitedBuildings.get(city.getConstructingBuilding()) - city.production());
                if (waitedBuildings.get(city.getConstructingBuilding()) <= 0) {
                    createBuilding(city.getConstructingBuilding(), city);
                    currentPlayer.addNotification("you have constructed building : " + city.getConstructingBuilding());
                    waitedBuildings.remove(city.getConstructingBuilding());
                    city.setConstructingBuilding(null);
                }
            }
            for (Building building : city.getBuildings()) {
                currentPlayer.setGold(currentPlayer.getGold()-building.getMaintenance());
            }
        }
        return "ok";
    }
    public String constructUnit(String name){
        if(selectedCity == null)return "no city selected";
        HashMap<String, Integer> waitedUnits = selectedCity.getWaitedUnits();
        selectedCity.setConstructingUnit(name);
        selectedCity.setConstructingBuilding(null);
        if(!waitedUnits.containsKey(name)){
            if(name.equals("Settler")) {
                if (selectedCity.getCountOfCitizens() < 2)
                    return "you can't build Settler in city with less than 2 citizens";
                if (currentPlayer.getIsUnhappy() == 1)
                    return "you can't build Settler when your civilization is unhappy";
            }
            int cost = getUnitCost(name);
            waitedUnits.put(name, cost);
        }
        return "unit is being constructed";
    }

    private int getUnitCost(String name){
        int cost = 0;
        if(name.equals("Worker")) cost = 70;
        else if(name.equals("Settler")) cost = 89;
        else {
            for (Unit unit : UnitsDatabase.getUnits()) {
                if (unit.getName().equals(name)) {
                    cost = unit.getCost();
                    break;
                }
            }
        }
        return cost;
    }
    public String purchaseUnitWithGold(String name){
        if(selectedCity == null)return "no city selected";
        if(name.equals("Settler")) {
            if (selectedCity.getCountOfCitizens() < 2)
                return "you can't build Settler in city with less than 2 citizens";
            if (currentPlayer.getIsUnhappy() == 1)
                return "you can't build Settler when your civilization is unhappy";
        }
        int cost = getUnitCost(name);
        if(currentPlayer.getGold() < cost)return "you don't have enough gold to build this unit";
        currentPlayer.setGold(currentPlayer.getGold() - cost);
        createUnit(name,selectedCity);

        currentPlayer.addNotification("you have constructed unit : " + name);
        return "unit is constructed";
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

    public ArrayList<Building> constructableBuildingsForSelectedCity(){
        ArrayList<Building> buildings=new ArrayList<>();
        for (Building building : BuildingDatabase.getBuildings()) {
            if((building.getNeededBuilding()==null || selectedCity.getBuildings().contains(building))&&(building.getNeededTechnology()==null || currentPlayer.hasTechnology(building.getNeededTechnology()))){
                buildings.add(building);
            }
        }
        return buildings;
    }

    public String constructBuilding(String name){
        if(selectedCity == null)return "no city selected";
        HashMap<String, Integer> waitedBuildings = selectedCity.getWaitedBuildings();
        selectedCity.setConstructingBuilding(name);
        selectedCity.setConstructingUnit(null);
        if(!waitedBuildings.containsKey(name)){
            int cost= BuildingDatabase.findBuilding(name).getCost();
            waitedBuildings.put(name, cost);
        }
        return "Building is being constructed";
    }

    public String purchaseBuildingWithGold(String name){
        if(selectedCity == null)return "no city selected";
        int cost= BuildingDatabase.findBuilding(name).getCost();
        if(currentPlayer.getGold() < cost)return "you don't have enough gold to build this building";
        currentPlayer.setGold(currentPlayer.getGold() - cost);
        createBuilding(name,selectedCity);
        currentPlayer.addNotification("you have constructed building : " + name);
        return "building is constructed";
    }

    public void createBuilding(String name, City city){
            for (Building building : BuildingDatabase.getBuildings()) {
                if (building.getName().equals(name)) {
                    city.addBuilding(building);
                    currentPlayer.setHappiness(currentPlayer.getHappiness()+ BuildingDatabase.findBuilding(name).getHappiness());
                    city.setFoodLeft(city.getFoodLeft()+ BuildingDatabase.findBuilding(name).getFood());
                    break;
                }
            }
    }

    public ArrayList<Tile> possibleTilesForPurchase(City city){
        if(city == null) return null;
        ArrayList<Tile> possibleTiles= new ArrayList<Tile>();
        for (Tile tile : city.getTiles()) {
            for (Tile tile1 : createGraph().getTilesAtDistance(coordinatesToNumber(tile.getX(), tile.getY()), 1)) {
                if(!possibleTiles.contains(tile1) && !city.getTiles().contains(tile1)){
                    possibleTiles.add(tile1);
                }
            }
        }
        return possibleTiles;
    }

    public String purchaseTile(Tile tile){
        if(selectedCity == null) return "no selected city";
        ArrayList<Tile> possibleTiles=possibleTilesForPurchase(selectedCity);
        if(!possibleTiles.contains(tile)){
            return "you can't purchase this tile!";
        }
        int price = tile.getPrice();
        if(currentPlayer.getGold()<price){
            return "you don't have enough gold to purchase this tile";
        }
        if(getCityAtCoordinate(tile.getX(),tile.getY())!=null){
            return "this tile belongs to another city";
        }
        currentPlayer.setGold(currentPlayer.getGold()-price);
        selectedCity.addTile(tile);
        currentPlayer.setScore(currentPlayer.getScore()+10);
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
        if(selectedCity == null) return "no selected city";
        if(!selectedCity.isCanAttack()){
            return "you already have attacked with this city";
        }
        MilitaryUnit militaryUnit = (MilitaryUnit) unit;
        militaryUnit.setHealth(militaryUnit.getHealth()-(selectedCity.strength()/3));
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

    public String eliminateCity(City city){
        String output="you eliminate the city";
        if(!getCityOwner(city).isHasCapitalFallen() && city.getBuildings().contains(BuildingDatabase.findBuilding("Palace"))){
            getCityOwner(city).setHasCapitalFallen(true);
        }
        if(getCityOwner(city).getCities().size()==1) {
            output += " and the city owner lost the game!";
            lostPlayers.add(getCityOwner(city));
            players.remove(getCityOwner(city));
            currentPlayer.setScore(currentPlayer.getScore()+200);
        }else {
            if(city.getBuildings().contains(BuildingDatabase.findBuilding("Palace"))) {
                for (City city1 : getCityOwner(city).getCities()) {
                    if (!city1.equals(city)) {
                        city1.getBuildings().add(BuildingDatabase.findBuilding("Palace"));
                    }
                }
            }
            getCityOwner(city).getCities().remove(city);
        }
        currentPlayer.setScore(currentPlayer.getScore()+50);
        return output;
    }

    public String annexCity(City city){
        String output="you annexed this city to your cities";
        if(!getCityOwner(city).isHasCapitalFallen() && city.getBuildings().contains(BuildingDatabase.findBuilding("Palace"))){
            getCityOwner(city).setHasCapitalFallen(true);
        }
        city.setHealth(20);
        GameController.getCurrentPlayer().setHappiness(GameController.getCurrentPlayer().getHappiness() - 5);
        GameController.getCurrentPlayer().getCities().add(city);
        if(getCityOwner(city).getCities().size()==1){
            output+=" and the owner lost the game!";
            lostPlayers.add(getCityOwner(city));
            players.remove(getCityOwner(city));
            currentPlayer.setScore(currentPlayer.getScore()+200);
        }else {
            if(city.getBuildings().contains(BuildingDatabase.findBuilding("Palace"))) {
                for (City city1 : getCityOwner(city).getCities()) {
                    if (!city1.equals(city)) {
                        city1.getBuildings().add(BuildingDatabase.findBuilding("Palace"));
                    }
                }
            }
            getCityOwner(city).getCities().remove(city);
        }
        currentPlayer.setScore(currentPlayer.getScore()+50);
        return output;
    }

}

