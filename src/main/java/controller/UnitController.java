package controller;

import model.*;

import java.util.ArrayList;
import java.util.Random;

public class UnitController {

    private final int mapWidth;
    private final int mapHeight;
    private ArrayList<User> players;
    private Tile[][] tiles;

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
            while (getTileNonCombatUnit(x, y) != null){
                x = random.nextInt(mapHeight);
                y = random.nextInt(mapWidth);
            }
            user.addUnit(new SettlerUnit(x, y));
        }
    }
    public void moveUnit(Unit unit, int x, int y){
        //TODO unit movement

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
}
