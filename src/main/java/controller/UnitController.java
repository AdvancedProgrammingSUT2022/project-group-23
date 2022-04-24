package controller;

import model.*;

import java.util.ArrayList;
import java.util.Random;

public class UnitController {

    private final int mapWidth;
    private final int mapHeight;
    private ArrayList<User> players;
    private Tile[][] tiles;

    private Unit selectedUnit;
    private int turn;
    private User currentPlayer;

    public UnitController(int mapWidth, int mapHeight, ArrayList<User> players, Tile[][] tiles, User currentPlayer, int turn) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.players = players;
        this.tiles = tiles;
        this.currentPlayer = currentPlayer;
        this.turn = turn;
        initializeUnits();
        checkVisibility();
    }

    public void setTurn(int turn) {
        this.turn = turn;
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
    public boolean moveUnit(Unit unit, int x, int y){
        checkVisibility();
        ArrayList<Integer> path;
        if(x != -1) {
            if(unit instanceof MilitaryUnit && getTileCombatUnit(x, y) != null)return false;
            if(!(unit instanceof MilitaryUnit) && getTileNonCombatUnit(x, y) != null)return false;
            Graph graph = createGraph();
            path = graph.getShortestPath(coordinatesToNumber(unit.getX(), unit.getY()), coordinatesToNumber(x, y), turn);
            if (path == null) return false;
        }else {
            path = unit.getMoves();
            if(path == null || path.isEmpty()) return false;
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
        return true;
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
            ArrayList<Tile> visibleTiles = graph.getVisibleTiles(coordinatesToNumber(unit.getX(), unit.getY()));
            for(Tile tile : visibleTiles){
                tile.setVisibilityForUser("visible", turn);
            }
        }
        //TODO check visibility for tiles because of cities
    }

    private Graph createGraph() {
        Graph graph = new Graph(mapHeight, mapWidth, tiles);
        for (int i = 0; i < mapHeight; i++) {
            for (int j = 0; j < mapHeight; j++) {
                if(isCoordinateValid(i - 1, j)) graph.addEdge(coordinatesToNumber(i, j),coordinatesToNumber(i - 1, j));
                if(isCoordinateValid(i + 1, j)) graph.addEdge(coordinatesToNumber(i, j),coordinatesToNumber(i + 1, j));
                if(j % 2 == 0){
                    if(isCoordinateValid(i - 1, j - 1)) graph.addEdge(coordinatesToNumber(i, j),coordinatesToNumber(i - 1, j - 1));
                    if(isCoordinateValid(i - 1, j + 1)) graph.addEdge(coordinatesToNumber(i, j),coordinatesToNumber(i - 1, j + 1));
                    if(isCoordinateValid(i, j - 1)) graph.addEdge(coordinatesToNumber(i, j),coordinatesToNumber(i, j - 1));
                    if(isCoordinateValid(i, j + 1)) graph.addEdge(coordinatesToNumber(i, j),coordinatesToNumber(i, j + 1));
                } else {
                    if(isCoordinateValid(i, j - 1)) graph.addEdge(coordinatesToNumber(i, j),coordinatesToNumber(i, j - 1));
                    if(isCoordinateValid(i, j + 1)) graph.addEdge(coordinatesToNumber(i, j),coordinatesToNumber(i, j + 1));
                    if(isCoordinateValid(i + 1, j - 1)) graph.addEdge(coordinatesToNumber(i, j),coordinatesToNumber(i + 1, j - 1));
                    if(isCoordinateValid(i + 1, j + 1)) graph.addEdge(coordinatesToNumber(i, j),coordinatesToNumber(i + 1, j + 1));
                }
            }
        }
        return graph;
    }

    private int coordinatesToNumber(int x, int y){
        return x * mapWidth + y;
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

    public void setCurrentPlayer(User currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public boolean isCoordinateValid(int x, int y) {
        return x >= 0 && x < mapHeight && y >= 0 && y < mapWidth;
    }

    public String selectUnit(int x, int y, boolean isMilitary){
        if(!isCoordinateValid(x, y))return "invalid coordinate";
        if(isMilitary) {
            this.selectedUnit = getTileCombatUnit(x, y);
            if(this.selectedUnit == null)return "no combat unit in coordinate";
        }
        else {
            this.selectedUnit = getTileNonCombatUnit(x, y);
            if(this.selectedUnit == null)return "no noncombat unit in coordinate";
        }
        return "unit selected - name: " + selectedUnit.getName() + " - belongs to : " + getUnitOwner(selectedUnit).getUsername() + " - remaining movement: " + selectedUnit.getRemainingMoves() + " - health: " + selectedUnit.getHealth();


    }
    public String moveSelectedUnit(int x, int y){
        if(!isCoordinateValid(x, y))return "invalid coordinate";
        if(selectedUnit == null)return "no unit selected";
        if(!getUnitOwner(selectedUnit).equals(currentPlayer)) return "unit doesn't belong to you";
        if(selectedUnit.getRemainingMoves() <= 0)return "no remaining moves";
        if(!moveUnit(selectedUnit, x, y)) return "destination invalid";
            //TODO return why destination is invalid
        return "unit moved successfully";

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
        for(Unit unit : currentPlayer.getUnits()) unit.setRemainingMoves(unit.getMovement());
        return "ok";
    }

}
