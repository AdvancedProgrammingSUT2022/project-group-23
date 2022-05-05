package controller;

import database.UnitsDatabase;
import model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class UnitController extends GameController {

    private CityController cityController;
    public UnitController(CityController cityController) {
        this.cityController = cityController;
        initializeUnits();
        checkVisibility();

    }


    public void initializeUnits(){
        Random random = new Random();
        for(User user : players){
            int x = random.nextInt(mapHeight);
            int y = random.nextInt(mapWidth);
            while (getTileNonCombatUnit(x, y) != null || tiles[x][y].getMovementCost() == -1){
                x = random.nextInt(mapHeight);
                y = random.nextInt(mapWidth);
            }
            user.addUnit(new SettlerUnit(x, y));
        }
    }
    public String moveUnit(Unit unit, int x, int y){
        checkVisibility();
        ArrayList<Integer> path;
        if(x != -1) {
            if(unit instanceof MilitaryUnit && getTileCombatUnit(x, y) != null)return "tile is occupied";
            if(!(unit instanceof MilitaryUnit) && getTileNonCombatUnit(x, y) != null)return "tile is occupied";
            Graph graph = createGraph();
            path = graph.getShortestPath(coordinatesToNumber(unit.getX(), unit.getY()), coordinatesToNumber(x, y), turn, this);
            if (path == null) return "can't get to destination";
        }else {
            path = unit.getMoves();
            if(path == null || path.isEmpty()) return "no chosen path";
        }

        while (!path.isEmpty() && unit.getRemainingMoves() > 0){
            if(unit instanceof MilitaryUnit && getTileCombatUnit(path.get(0) / mapWidth, path.get(0) % mapWidth) != null){
                path.clear();
                break;
            } else if(!(unit instanceof MilitaryUnit) && getTileNonCombatUnit(path.get(0) / mapWidth, path.get(0) % mapWidth) != null){
                path.clear();
                break;
            }
            unit.setRemainingMoves(unit.getRemainingMoves() - tiles[path.get(0) / mapWidth][path.get(0) % mapWidth].getMovementCost());
            if(isRiver(unit.getX(), unit.getY(), path.get(0) / mapWidth, path.get(0) % mapWidth)) unit.setRemainingMoves(0);
            unit.setX(path.get(0) / mapWidth);
            unit.setY(path.get(0) % mapWidth);
            path.remove(0);
        }
        if(unit.getRemainingMoves() < 0)unit.setRemainingMoves(0);
        unit.setMoves(path);
        checkVisibility();
        return "ok";
    }
    public boolean isRiver(int x1, int y1, int x2, int y2){
        //return false;
        if(x1 - 1 == x2 && y1 == y2 && tiles[x1][y1].getRivers().contains(1))return true;
        if(x1 + 1 == x2 && y1 == y2 && tiles[x1][y1].getRivers().contains(4))return true;

        if(y1 % 2 == 0){
          if(x1 == x2 && y1 + 1 == y2 && tiles[x1][y1].getRivers().contains(3))return true;
          if(x1 == x2 && y1 - 1 == y2 && tiles[x1][y1].getRivers().contains(5))return true;
          if(x1 - 1 == x2 && y1 + 1 == y2 && tiles[x1][y1].getRivers().contains(2))return true;
          if(x1 - 1 == x2 && y1 - 1 == y2 && tiles[x1][y1].getRivers().contains(6))return true;
        }else {
            if(x1 == x2 && y1 + 1 == y2 && tiles[x1][y1].getRivers().contains(2))return true;
            if(x1 == x2 && y1 - 1 == y2 && tiles[x1][y1].getRivers().contains(6))return true;
            if(x1 + 1 == x2 && y1 + 1 == y2 && tiles[x1][y1].getRivers().contains(3))return true;
            if(x1 + 1 == x2 && y1 - 1 == y2 && tiles[x1][y1].getRivers().contains(5))return true;
        }
        return false;
    }
    public void checkVisibility(){
        Graph graph = createGraph();
        for (int i = 0; i < mapHeight; i++) {
            for (int j = 0; j < mapWidth; j++) {
                if(tiles[i][j].getVisibilityForUser(turn).equals("visible"))
                    tiles[i][j].setVisibilityForUser("revealed", turn);
            }
        }
        for(Unit unit : currentPlayer.getUnits()) {
            ArrayList<Tile> visibleTiles = graph.getVisibleTiles(coordinatesToNumber(unit.getX(), unit.getY()), 2);
            for(Tile tile : visibleTiles){
                tile.setVisibilityForUser("visible", turn);
            }
        }
        for(City city : currentPlayer.getCities()){
            for(Tile tile : city.getTiles()){
                tile.setVisibilityForUser("visible", turn);
            }
            ArrayList<Tile> viewableTiles = cityController.possibleTilesForPurchase();
            for(Tile tile : viewableTiles){
                tile.setVisibilityForUser("visible", turn);
            }
        }
    }


    public Unit getTileCombatUnit(int x, int y){
        for(User user : players){
            for(Unit unit : user.getUnits()){
                if(unit.getX() == x && unit.getY() == y && unit instanceof MilitaryUnit){
                    return unit;
                }
            }
        }
        return null;
    }

    public Unit getTileNonCombatUnit(int x, int y){
        for(User user : players){
            for(Unit unit : user.getUnits()){
                if(unit.getX() == x && unit.getY() == y && !(unit instanceof MilitaryUnit)){
                    return unit;
                }
            }
        }
        return null;
    }

    public String selectUnit(int x, int y, boolean isMilitary){
        if(!isCoordinateValid(x, y))return "invalid coordinate";
        if(isMilitary) {
            selectedUnit = getTileCombatUnit(x, y);
            if(selectedUnit == null)return "no combat unit in coordinate";
        }
        else {
            selectedUnit = getTileNonCombatUnit(x, y);
            if(selectedUnit == null)return "no noncombat unit in coordinate";
        }
        return "unit selected - name: " + selectedUnit.getName() + " - belongs to : " + getUnitOwner(selectedUnit).getUsername() + " - remaining movement: " + selectedUnit.getRemainingMoves() + " - health: " + selectedUnit.getHealth();
    }

    public String moveSelectedUnit(int x, int y){
        if(!isCoordinateValid(x, y))return "invalid coordinate";
        if(!checkSelectedUnit().equals("ok"))return checkSelectedUnit();
        if(selectedUnit.getRemainingMoves() <= 0)return "no remaining moves";
        cancelActions();
        String message = null;
        if(!(message = moveUnit(selectedUnit, x, y)).equals("ok")) return message;
        return "unit is moving";

    }

    public String foundCity(){
        if(selectedUnit == null)return "no unit selected";
        if(!getUnitOwner(selectedUnit).equals(currentPlayer)) return "unit doesn't belong to you";
        if(!(selectedUnit instanceof SettlerUnit))return "unit is not Settler";
        if(selectedUnit.getRemainingMoves() <= 0)return "no remaining moves";
        String message = cityController.createCity(selectedUnit.getX(), selectedUnit.getY());
        if(message.equals("city founded"))deleteSelectedUnit();
        return message;

    }

    public String deleteSelectedUnit(){
        if(selectedUnit == null)return "no unit selected";
        if(!getUnitOwner(selectedUnit).equals(currentPlayer)) return "unit doesn't belong to you";
        currentPlayer.removeUnit(selectedUnit);
        checkVisibility();
        return "unit deleted successfully";
    }

    public User getUnitOwner(Unit unit){
        for(User user : players) {
            for (Unit tempUnit : user.getUnits()) {
                if (tempUnit.equals(unit)) {
                    return user;
                }
            }
        }
        return null;
    }

    public String sleep(){
        if(!checkSelectedUnit().equals("ok"))return checkSelectedUnit();
        cancelActions();
        selectedUnit.setState("sleep");
        return "unit is asleep";
    }
    public String wake(){
        if(!checkSelectedUnit().equals("ok"))return checkSelectedUnit();
        cancelActions();
        selectedUnit.setState("ready");
        return "unit is awake";
    }

    public String fortify(){
        if(!checkSelectedUnit().equals("ok"))return checkSelectedUnit();
        if(!(selectedUnit instanceof MilitaryUnit))return "unit is not military";
        cancelActions();
        selectedUnit.setState("fortify");
        return "unit is fortified";
    }

    public String alert(){
        if(!checkSelectedUnit().equals("ok"))return checkSelectedUnit();
        if(!(selectedUnit instanceof MilitaryUnit))return "unit is not military";
        cancelActions();
        selectedUnit.setState("alert");
        return "unit is on alert";
    }
    public String rangeSetup(){
        if(!checkSelectedUnit().equals("ok"))return checkSelectedUnit();
        if(!(selectedUnit instanceof MilitaryUnit))return "unit is not military";
        MilitaryUnit militaryUnit = (MilitaryUnit)selectedUnit;
        if(!militaryUnit.getCombatType().equals("Siege"))return "unit type is not Siege";
        if(selectedUnit.getRemainingMoves() == 0)return "no moves left";
        cancelActions();
        selectedUnit.setState("range setup");
        selectedUnit.setRemainingMoves(selectedUnit.getRemainingMoves() - 1);
        return "unit is set up";
    }
    public String isTurnPossible(){
        for(Unit unit : currentPlayer.getUnits()){
            if(unit.getState().equals("alert")){
                Graph graph = createGraph();
                ArrayList<Tile> visibleTiles = graph.getVisibleTiles(coordinatesToNumber(unit.getX(), unit.getY()), 2);
                for(Tile tile : visibleTiles){
                    Unit dangerUnit = getTileCombatUnit(tile.getX(), tile.getY());
                    if(!getUnitOwner(dangerUnit).equals(currentPlayer)){
                        unit.setState("ready");
                        break;
                    }
                }
            }
            if(unit.getRemainingMoves() > 0 && unit.getState().equals("ready")){
                moveUnit(unit, -1, -1);
                if(unit.getRemainingMoves() > 0){
                    return "unit needs action";
                }
            }
        }
        HashMap<Tile,Integer> processingTiles=currentPlayer.getProcessingTiles();
        HashMap<Tile,WorkerUnit> workingWorkers=currentPlayer.getWorkingWorkers();
        HashMap<Tile,Improvement> improvingTiles=currentPlayer.getImprovingTiles();
        HashMap<Tile,Integer> map=new HashMap<>();
        ArrayList<Tile> eliminatingFeatures=currentPlayer.getEliminatingFeatures();
        if(processingTiles!=null) {
            for (Map.Entry<Tile, Integer> entry : processingTiles.entrySet()) {
                if(workingWorkers.get(entry.getKey())!=null) processingTiles.put(entry.getKey(),entry.getValue()-1);
                if(processingTiles.get(entry.getKey())==0){
                    if(improvingTiles.get(entry.getKey())==null){
                        if(eliminatingFeatures.contains(entry.getKey())){
                            eliminatingFeatures.remove(entry.getKey());
                            entry.getKey().setFeature(null);
                            workingWorkers.get(entry.getKey()).setState("ready");
                            workingWorkers.remove(entry.getKey());
                        }else {
                            entry.getKey().setRoad(true);
                            workingWorkers.get(entry.getKey()).setState("ready");
                            workingWorkers.remove(entry.getKey());
                        }
                    }else {
                        entry.getKey().setImprovement(improvingTiles.get(entry.getKey()));
                        if(eliminatingFeatures.contains(entry.getKey())){
                            entry.getKey().setFeature(null);
                            eliminatingFeatures.remove(entry.getKey());
                        }
                        workingWorkers.get(entry.getKey()).setState("ready");
                        workingWorkers.remove(entry.getKey());
                        improvingTiles.remove(entry.getKey());
                    }
                }else {
                    map.put(entry.getKey(),entry.getValue());
                }
            }
            currentPlayer.setProcessingTiles(map);
        }
        for(Unit unit : currentPlayer.getUnits()) unit.setRemainingMoves(unit.getMovement());
        return "ok";
    }

    public String buildRoad(){
        if(!checkSelectedUnit().equals("ok"))return checkSelectedUnit();
        if(!(selectedUnit instanceof WorkerUnit))return "unit is not Worker";
        WorkerUnit unit = (WorkerUnit) selectedUnit;
        for (Technology technology : currentPlayer.getTechnologies()) {
            if(technology.getName().equals("Wheel")){
                if(tiles[unit.getX()][unit.getY()].getTerrain().getMovementCost()!=-1 ||
                        (tiles[unit.getX()][unit.getY()].getFeature()!=null && tiles[unit.getX()][unit.getY()].getFeature().getMovementCost()!=-1)){
                    if(unit.getRemainingMoves()==0 || unit.getState().equals("working")){
                        return "this worker can't build right now!";
                    }
                    if(tiles[unit.getX()][unit.getY()].isRoad()){
                        return "this tile is already a road";
                    }
                    cancelActions();
                    unit.setState("working");
                    unit.setRemainingMoves(0);
                    unit.setMoves(new ArrayList<>());
                    HashMap<Tile,Integer> processingRoads=currentPlayer.getProcessingTiles();
                    HashMap<Tile,WorkerUnit> roadWorkers=currentPlayer.getWorkingWorkers();
                    HashMap<Tile,Improvement> improvingTiles=currentPlayer.getImprovingTiles();
                    if(processingRoads.get(tiles[unit.getX()][unit.getY()])!= null && improvingTiles.get(tiles[unit.getX()][unit.getY()])==null){
                        roadWorkers.put(tiles[unit.getX()][unit.getY()],unit);
                    }else {
                        improvingTiles.remove(tiles[unit.getX()][unit.getY()]);
                        processingRoads.put(tiles[unit.getX()][unit.getY()],3);
                        roadWorkers.put(tiles[unit.getX()][unit.getY()],unit);
                    }
                    return "starting to build a road!";
                }
                return "you can't build a road on this tile!";
            }
        }
        return "you don't have the right technology to build road";
    }

    public String improveTile(Improvement improvement){
        if(!checkSelectedUnit().equals("ok"))return checkSelectedUnit();
        if(!(selectedUnit instanceof WorkerUnit))return "unit is not Worker";
        WorkerUnit unit = (WorkerUnit) selectedUnit;
        for (Technology technology : currentPlayer.getTechnologies()) {
            if(technology.getName().equals(improvement.getNeededTechnology())){
                if(improvement.getPlacesItCanBeBuild().contains(tiles[unit.getX()][unit.getY()].getTerrain().getName()) ||
                        (tiles[unit.getX()][unit.getY()].getFeature()!=null && improvement.getPlacesItCanBeBuild().contains(tiles[unit.getX()][unit.getY()].getFeature().getName()))){
                    if(tiles[unit.getX()][unit.getY()].getImprovement()==null){
                        if(unit.getRemainingMoves()==0 || unit.getState().equals("working")){
                            return "this worker can't improve right now!";
                        }
                        cancelActions();
                        unit.setState("working");
                        unit.setMoves(new ArrayList<>());
                        unit.setRemainingMoves(0);
                        HashMap<Tile,Integer>  processingTiles=currentPlayer.getProcessingTiles();
                        HashMap<Tile,WorkerUnit> workingWorkers= currentPlayer.getWorkingWorkers();
                        HashMap<Tile,Improvement> improvingTiles = currentPlayer.getImprovingTiles();
                        ArrayList<Tile> eliminatingFeatures=currentPlayer.getEliminatingFeatures();
                        if(processingTiles.get(tiles[unit.getX()][unit.getY()])!=null && improvingTiles.get(tiles[unit.getX()][unit.getY()])!=null && improvement.getName().equals(improvingTiles.get(tiles[unit.getX()][unit.getY()]).getName())) {
                            workingWorkers.put(tiles[unit.getX()][unit.getY()],unit);
                        }else {
                                processingTiles.put(tiles[unit.getX()][unit.getY()], 6);
                                if (tiles[unit.getX()][unit.getY()].getFeature() != null) {
                                    if (tiles[unit.getX()][unit.getY()].getFeature().getName().equals("Jungle")) {
                                        processingTiles.put(tiles[unit.getX()][unit.getY()], 13);
                                        eliminatingFeatures.add(tiles[unit.getX()][unit.getY()]);
                                    }
                                    if (tiles[unit.getX()][unit.getY()].getFeature().getName().equals("Marsh")) {
                                        processingTiles.put(tiles[unit.getX()][unit.getY()], 12);
                                        eliminatingFeatures.add(tiles[unit.getX()][unit.getY()]);
                                    }
                                    if (tiles[unit.getX()][unit.getY()].getFeature().getName().equals("Forest")) {
                                        processingTiles.put(tiles[unit.getX()][unit.getY()], 10);
                                        eliminatingFeatures.add(tiles[unit.getX()][unit.getY()]);
                                    }
                                }
                            workingWorkers.put(tiles[unit.getX()][unit.getY()],unit);
                            improvingTiles.put(tiles[unit.getX()][unit.getY()],improvement);
                        }
                        return "improving the tile!";
                    }
                    return "this tile is already improved";
                }
                return "you can't do this improvement on this tile!";
            }
        }
        return "you don't have the right technology to improve this tile";
    }

    public String eliminateFeature(){
        if(!checkSelectedUnit().equals("ok"))return checkSelectedUnit();
        if(!(selectedUnit instanceof WorkerUnit))return "unit is not Worker";
        WorkerUnit unit = (WorkerUnit) selectedUnit;
        if(tiles[unit.getX()][unit.getY()].getFeature()==null){
            return "this tile doesn't have any feature!";
        }
        if(!tiles[unit.getX()][unit.getY()].getFeature().getName().equals("Forest") && !tiles[unit.getX()][unit.getY()].getFeature().getName().equals("Jungle") && !tiles[unit.getX()][unit.getY()].getFeature().getName().equals("Marsh")){
            return "you can't eliminate this feature";
        }
        if(unit.getRemainingMoves()==0 || unit.getState().equals("working")){
            return "this worker can't improve right now!";
        }
        cancelActions();
        unit.setState("working");
        unit.setRemainingMoves(0);
        unit.setMoves(new ArrayList<>());
        ArrayList<Tile> eliminatingFeatures=currentPlayer.getEliminatingFeatures();
        HashMap<Tile,WorkerUnit> workingWorkers=currentPlayer.getWorkingWorkers();
        HashMap<Tile,Integer> processingTiles=currentPlayer.getProcessingTiles();
        if(tiles[unit.getX()][unit.getY()].getFeature().getName().equals("Forest")){
            processingTiles.put(tiles[unit.getX()][unit.getY()],4);
        }
        if(tiles[unit.getX()][unit.getY()].getFeature().getName().equals("Jungle")){
            processingTiles.put(tiles[unit.getX()][unit.getY()],7);
        }
        if(tiles[unit.getX()][unit.getY()].getFeature().getName().equals("Marsh")){
            processingTiles.put(tiles[unit.getX()][unit.getY()],6);
        }
        workingWorkers.put(tiles[unit.getX()][unit.getY()],unit);
        eliminatingFeatures.add(tiles[unit.getX()][unit.getY()]);
        return "eliminating this feature!";
    }
    public String eliminateRoad(){
        if(!checkSelectedUnit().equals("ok"))return checkSelectedUnit();
        if(!(selectedUnit instanceof WorkerUnit))return "unit is not Worker";
        WorkerUnit unit = (WorkerUnit) selectedUnit;
        if(!tiles[unit.getX()][unit.getY()].isRoad()){
            return "this is not a road to eliminate";
        }
        if(unit.getRemainingMoves()==0 || unit.getState().equals("working")){
            return "this worker can't improve right now!";
        }
        cancelActions();
        tiles[unit.getX()][unit.getY()].setRoad(false);
        unit.setMoves(new ArrayList<>());
        unit.setRemainingMoves(0);
        unit.setState("ready");
        return "road eliminated!";
    }

    public String checkSelectedUnit(){
        if(selectedUnit == null)return "no unit selected";
        if(!getUnitOwner(selectedUnit).equals(currentPlayer)) return "unit doesn't belong to you";
        return "ok";
    }
    public String cancelActions(){
        if(!checkSelectedUnit().equals("ok"))return checkSelectedUnit();
        selectedUnit.setMoves(new ArrayList<>());
        selectedUnit.setState("ready");
        if(selectedUnit.getName().equals("Worker")){
            currentPlayer.getWorkingWorkers().remove(tiles[selectedUnit.getX()][selectedUnit.getY()]);
        }
        return "actions canceled successfully";
    }

    public String lootTile(){
        if(!checkSelectedUnit().equals("ok"))return checkSelectedUnit();
        if(!(selectedUnit instanceof MilitaryUnit)) {
            return "this unit can't loot";
        }
        if(!tiles[selectedUnit.getX()][selectedUnit.getY()].isRoad() && tiles[selectedUnit.getX()][selectedUnit.getY()].getImprovement()==null){
            return "this tile has nothing to loot";
        }
        cancelActions();
        if(tiles[selectedUnit.getX()][selectedUnit.getY()].isRoad()){
            tiles[selectedUnit.getX()][selectedUnit.getY()].setHasLooted(true);
            tiles[selectedUnit.getX()][selectedUnit.getY()].setRoad(false);
        }
        if(tiles[selectedUnit.getX()][selectedUnit.getY()].getImprovement()!=null){
            tiles[selectedUnit.getX()][selectedUnit.getY()].setLootedImprovement(tiles[selectedUnit.getX()][selectedUnit.getY()].getImprovement());
            tiles[selectedUnit.getX()][selectedUnit.getY()].setImprovement(null);
            tiles[selectedUnit.getX()][selectedUnit.getY()].setHasLooted(true);
        }
        selectedUnit.setRemainingMoves(0);
        selectedUnit.setState("ready");
        return "looted!";
    }

    public String healTile(){
        if(!checkSelectedUnit().equals("ok"))return checkSelectedUnit();
        if(!(selectedUnit instanceof WorkerUnit)){
            return "this unit can't heal this tile";
        }
        WorkerUnit unit = (WorkerUnit) selectedUnit;
        if(!tiles[unit.getX()][unit.getY()].isHasLooted()){
            return "this tile is not looted!";
        }
        cancelActions();
        tiles[unit.getX()][unit.getY()].setHasLooted(false);
        HashMap<Tile,WorkerUnit> workingWorkers=currentPlayer.getWorkingWorkers();
        HashMap<Tile,Integer> processingTiles=currentPlayer.getProcessingTiles();
        HashMap<Tile,Improvement> improvingTiles=currentPlayer.getImprovingTiles();
        if(tiles[unit.getX()][unit.getY()].getLootedImprovement()!=null){
            improvingTiles.put(tiles[unit.getX()][unit.getY()],tiles[unit.getX()][unit.getY()].getLootedImprovement());
            tiles[unit.getX()][unit.getY()].setLootedImprovement(null);
        }
        unit.setRemainingMoves(0);
        processingTiles.put(tiles[unit.getX()][unit.getY()],3);
        workingWorkers.put(tiles[unit.getX()][unit.getY()],unit);
        unit.setState("working");
        return "healing!";
    }

    public ArrayList<Unit> reachableUnits(){
        MilitaryUnit unit = (MilitaryUnit) selectedUnit;
        ArrayList<Unit> reachableUnits = new ArrayList<>();
        ArrayList<Tile> reachableTiles = new ArrayList<>();
        if(unit.getRange()==-1){
            reachableTiles=createGraph().getTilesAtDistance(coordinatesToNumber(unit.getX(), unit.getY()),1);
        }else {
            for (int i=1;i<=unit.getRange();i++){
                reachableTiles.addAll(createGraph().getTilesAtDistance(coordinatesToNumber(unit.getX(), unit.getY()),i));
            }
        }
        for (User player : players) {
            if(!player.equals(currentPlayer)){
                for (Unit playerUnit : player.getUnits()) {
                    if(reachableTiles.contains(tiles[playerUnit.getX()][playerUnit.getY()])){
                        reachableUnits.add(playerUnit);
                    }
                }
            }
        }
        return reachableUnits;
    }

    public ArrayList<Unit> getConstructableUnits(){
        ArrayList<Unit> constructableUnits = new ArrayList<>();
        for(Unit unit : UnitsDatabase.getUnits()){
            if (currentPlayer.hasTechnology(unit.getNeededTechnology()) &&
                    currentPlayer.hasResource(unit.getNeededResource())) constructableUnits.add(unit);
        }
        constructableUnits.add(new SettlerUnit(-1, -1));
        constructableUnits.add(new WorkerUnit(-1, -1));
        return constructableUnits;
    }

    public ArrayList<City> reachableCities(){
        MilitaryUnit unit = (MilitaryUnit) selectedUnit;
        ArrayList<City> reachableCities = new ArrayList<>();
        ArrayList<Tile> reachableTiles = new ArrayList<>();
        if(unit.getRange()==-1){
            reachableTiles=createGraph().getTilesAtDistance(coordinatesToNumber(unit.getX(), unit.getY()),1);
        }else {
            for (int i=1;i<=unit.getRange();i++){
                reachableTiles.addAll(createGraph().getTilesAtDistance(coordinatesToNumber(unit.getX(), unit.getY()),i));
            }
        }
        for (User player : players) {
            if(!player.equals(currentPlayer)){
                for (City city : player.getCities()) {
                    for (Tile tile : city.getTiles()) {
                        if(reachableTiles.contains(tile)){
                            reachableCities.add(city);
                    }
                }
                }
            }
        }
        return reachableCities;
    }

    public String attackUnit(Unit unit){
        MilitaryUnit militaryUnit = (MilitaryUnit) selectedUnit;
        cancelActions();
        militaryUnit.setState("ready");
        unit.setState("ready");
        militaryUnit.setRemainingMoves(0);
        if(unit instanceof MilitaryUnit) {
            MilitaryUnit unit1 = (MilitaryUnit) unit;
            int strengthAttacker;
            int strengthDefender;
            int bonusAttacker=0;
            int bonusDefender=0;
            if(!militaryUnit.getName().equals("Scout"))
                bonusAttacker += tiles[militaryUnit.getX()][militaryUnit.getY()].getTerrain().getCombatPercentage();
            if(!unit1.getName().equals("Scout"))
                bonusDefender += tiles[unit1.getX()][unit1.getY()].getTerrain().getCombatPercentage();
            if(tiles[militaryUnit.getX()][militaryUnit.getY()].getFeature()!=null && !militaryUnit.getName().equals("Scout")){
                bonusAttacker += tiles[militaryUnit.getX()][militaryUnit.getY()].getFeature().getCombatPercentage();
            }
            if(tiles[unit1.getX()][unit1.getY()].getFeature()!=null && !unit1.getName().equals("Scout")){
                bonusDefender += tiles[unit1.getX()][unit1.getY()].getFeature().getCombatPercentage();
            }
            if(tiles[militaryUnit.getX()][militaryUnit.getY()].getTerrain().getName().equals("Hill")){
                bonusAttacker += 25;
            }
            if(unit1.getState().equals("fortify")){
                bonusDefender += 25;
            }
            if((militaryUnit.getName().equals("Spearman") || militaryUnit.getName().equals("Pikeman")) && unit1.getCombatType().equals("Mounted")){
                bonusAttacker += 100;
            }
            if(militaryUnit.getName().equals("Anti-Tank Gun") && unit1.getName().equals("Tank")){
                bonusAttacker += 10;
            }
            if (militaryUnit.getRange() == -1) {
                if(isRiver(militaryUnit.getX(),militaryUnit.getY(),unit1.getX(), unit1.getY())){
                    bonusAttacker -= 25;
                }
                strengthAttacker = ((100-(currentPlayer.getIsUnhappy()*25))*militaryUnit.getStrength()*(50+(militaryUnit.getHealth()*5)))/10000;
            } else {
                strengthAttacker = ((100-(currentPlayer.getIsUnhappy()*25))*militaryUnit.getRangeStrength()*(50+(militaryUnit.getHealth()*5)))/10000;
            }
            strengthAttacker += strengthAttacker*(bonusAttacker)/100;
            strengthDefender=(100-(getUnitOwner(unit).getIsUnhappy()*25))*unit1.getStrength()/100;
            strengthDefender += strengthDefender*(bonusDefender)/100;
            unit1.setHealth(unit1.getHealth() - strengthAttacker);
            if (militaryUnit.getRange() == -1) {
                militaryUnit.setHealth(militaryUnit.getHealth() - strengthDefender);
            }
            if(!militaryUnit.getName().equals("Horseman") && !militaryUnit.getName().equals("Knight") &&!militaryUnit.getName().equals("Cavalry")
                    &&!militaryUnit.getName().equals("Lancer")&&!militaryUnit.getName().equals("Panzer")&&!militaryUnit.getName().equals("Tank")){
                militaryUnit.setRemainingMoves(0);
            }
            if(militaryUnit.getHealth()<=0 || unit1.getHealth()<=0){
                if(unit1.getHealth()>militaryUnit.getHealth()) {
                    unit1.setHealth(unit1.getHealth()-militaryUnit.getHealth());
                    currentPlayer.getUnits().remove(militaryUnit);
                    return "your unit died!";
                }else {
                    User user = getUnitOwner(unit1);
                    militaryUnit.setHealth(militaryUnit.getHealth()-unit1.getHealth());
                    user.getUnits().remove(unit1);
                    if (militaryUnit.getRange() == -1) {
                        moveSelectedUnit(unit1.getX(), unit1.getY());
                    }
                    return "you killed the unit";
                }
            }else {
                return "you have attacked the unit, but you and the unit are still alive!";
            }
        }else {
            User user=getUnitOwner(unit);
            user.getUnits().remove(unit);
            WorkerUnit unit1 = (WorkerUnit) unit;
            currentPlayer.addUnit(unit1);
            return "you slaved this unit!";
        }
    }

    public String attackCity(City city){
        MilitaryUnit militaryUnit = (MilitaryUnit) selectedUnit;
        cancelActions();
        militaryUnit.setState("ready");
        militaryUnit.setRemainingMoves(0);
        int strength;
        int bonus=0;
        if(!militaryUnit.getName().equals("Scout"))
            bonus += tiles[militaryUnit.getX()][militaryUnit.getY()].getTerrain().getCombatPercentage();
        if(tiles[militaryUnit.getX()][militaryUnit.getY()].getFeature()!=null && !militaryUnit.getName().equals("Scout")){
            bonus += tiles[militaryUnit.getX()][militaryUnit.getY()].getFeature().getCombatPercentage();
        }
        if(tiles[militaryUnit.getX()][militaryUnit.getY()].getTerrain().getName().equals("Hill")){
            bonus += 25;
        }
        if(militaryUnit.getName().equals("Catapult") || militaryUnit.getName().equals("Trebuchet") || militaryUnit.getName().equals("Canon") || militaryUnit.getName().equals("Artillery") || militaryUnit.getName().equals("Tank")){
            bonus += 10;
        }
        if (militaryUnit.getRange() == -1) {
            strength = ((100-(currentPlayer.getIsUnhappy()*25))*militaryUnit.getStrength()*(50+(militaryUnit.getHealth()*5)))/10000;
        } else {
            strength = ((100-(currentPlayer.getIsUnhappy()*25))*militaryUnit.getRangeStrength()*(50+(militaryUnit.getHealth()*5)))/10000;
        }
        strength += strength*bonus/100;
        city.setHealth(city.getHealth()-strength);
        if(militaryUnit.getRange()==-1){
            militaryUnit.setHealth(militaryUnit.getHealth()-city.strength());
        }else {
            if(city.getHealth()<1)city.setHealth(1);
        }
        if(!militaryUnit.getName().equals("Horseman") && !militaryUnit.getName().equals("Knight") &&!militaryUnit.getName().equals("Cavalry")
                &&!militaryUnit.getName().equals("Lancer")&&!militaryUnit.getName().equals("Panzer")&&!militaryUnit.getName().equals("Tank")){
            militaryUnit.setRemainingMoves(0);
        }
        if(city.getHealth()<=0 || militaryUnit.getHealth()<=0){
            if(militaryUnit.getHealth()> city.getHealth()){
                militaryUnit.setHealth(militaryUnit.getHealth()-city.getHealth());
                return "dominated";
            }else {
                currentPlayer.getUnits().remove(militaryUnit);
                return "your unit died!";
            }
        }else {
            return "you attacked the city, but you are alive and the city is still ok!";
        }
    }

    public Unit getSelectedUnit() {
        return selectedUnit;
    }
}
