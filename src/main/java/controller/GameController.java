package controller;

import model.*;

import java.util.ArrayList;

public class GameController {

    protected static int mapWidth ;
    protected static int mapHeight ;
    protected static ArrayList<User> players;
    protected static Tile[][] tiles;

    protected static Unit selectedUnit;
    protected static City selectedCity;
    protected static int turn;
    protected static User currentPlayer;
    protected static CivilizationController civilizationController;

    public static boolean isCoordinateValid(int x, int y) {
        return x >= 0 && x < mapHeight && y >= 0 && y < mapWidth;
    }

    protected static Graph createGraph() {
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

    protected static int coordinatesToNumber(int x, int y){
        return x * mapWidth + y;
    }

    public static void setSelectedUnit(Unit selectedUnit) {
        GameController.selectedUnit = selectedUnit;
    }

    public static City getSelectedCity() {
        return selectedCity;
    }

    public static void setSelectedCity(City city) {
        selectedCity=city;
    }

    public static void setMapWidth (int mapWidth) {
        GameController.mapWidth = mapWidth;
    }

    public static void setMapHeight (int mapHeight) {
        GameController.mapHeight = mapHeight;
    }

    public static void setPlayers(ArrayList<User> players) {
        GameController.players = players;
    }

    public static void setTiles(Tile[][] tiles) {
        GameController.tiles = tiles;
    }

    public static void setTurn(int turn) {
        GameController.turn = turn;
    }

    public static void setCurrentPlayer(User currentPlayer) {
        GameController.currentPlayer = currentPlayer;
    }

    public static void setCivilizationController(CivilizationController civilizationController) {
        GameController.civilizationController = civilizationController;
    }

    public static int getMapWidth() {
        return mapWidth;
    }

    public static int getMapHeight() {
        return mapHeight;
    }

    public static ArrayList<User> getPlayers() {
        return players;
    }

    public static Tile[][] getTiles() {
        return tiles;
    }

    public static Unit getSelectedUnit() {
        return selectedUnit;
    }

    public static int getTurn() {
        return turn;
    }

    public static User getCurrentPlayer() {
        return currentPlayer;
    }

    public static CivilizationController getCivilizationController() {
        return civilizationController;
    }
}
