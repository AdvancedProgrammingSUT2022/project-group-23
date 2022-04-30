package controller;

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
        //TODO check visibility for tiles because of cities
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
        if(selectedUnit == null)return "no unit selected";
        if(!getUnitOwner(selectedUnit).equals(currentPlayer)) return "unit doesn't belong to you";
        if(selectedUnit.getRemainingMoves() <= 0)return "no remaining moves";
        String message = null;
        if(!(message = moveUnit(selectedUnit, x, y)).equals("ok")) return message;
        return "unit moved successfully";

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
        //TODO check visibility
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
    public String isTurnPossible(){
        for(Unit unit : currentPlayer.getUnits()){
            if(unit.getRemainingMoves() > 0 && unit.getState().equals("ready")){
                moveUnit(unit, -1, -1);
                if(unit.getRemainingMoves() > 0){
                    return "unit needs action";
                }
            }
        }
        HashMap<Tile,Integer> processingRoads=currentPlayer.getProcessingRoads();
        HashMap<Tile,WorkerUnit> workingWorkers=currentPlayer.getWorkingWorkers();
        HashMap<Tile,Integer> map=new HashMap<>();
        if(processingRoads!=null) {
            for (Map.Entry<Tile, Integer> entry : processingRoads.entrySet()) {
                processingRoads.put(entry.getKey(),entry.getValue()-1);
                if(processingRoads.get(entry.getKey())==0){
                    entry.getKey().setRoad(true);
                    workingWorkers.get(entry.getKey()).setState("ready");
                    workingWorkers.remove(entry.getKey());
                }else {
                    map.put(entry.getKey(),entry.getValue());
                }
            }
            currentPlayer.setProcessingRoads(map);
        }
        for(Unit unit : currentPlayer.getUnits()) unit.setRemainingMoves(unit.getMovement());
        return "ok";
    }

    public String buildRoad(WorkerUnit unit){
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
                    unit.setState("working");
                    unit.setRemainingMoves(0);
                    unit.setMoves(new ArrayList<>());
                    HashMap<Tile,Integer> processingRoads=currentPlayer.getProcessingRoads();
                    HashMap<Tile,WorkerUnit> roadWorkers=currentPlayer.getWorkingWorkers();
                    processingRoads.put(tiles[unit.getX()][unit.getY()],3);
                    roadWorkers.put(tiles[unit.getX()][unit.getY()],unit);
                }
                return "you can't build a road on this tile!";
            }
        }
        return "you don't have the right technology to build road";
    }

    public String improveTile(WorkerUnit unit,Improvement improvement){
        for (Technology technology : currentPlayer.getTechnologies()) {
            if(technology.getName().equals(improvement.getNeededTechnology())){
                if(improvement.getPlacesItCanBeBuild().contains(tiles[unit.getX()][unit.getY()].getTerrain().getName()) ||
                        (tiles[unit.getX()][unit.getY()].getFeature()!=null && improvement.getPlacesItCanBeBuild().contains(tiles[unit.getX()][unit.getY()].getFeature().getName()))){

                }
                return "you can't do this improvement on this tile!";
            }
        }
        return "you don't have the right technology to improve this tile";
    }

    public Unit getSelectedUnit() {
        return selectedUnit;
    }
}
