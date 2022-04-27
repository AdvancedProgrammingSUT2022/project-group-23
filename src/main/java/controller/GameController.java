package controller;

import model.Graph;
import model.Tile;
import model.Unit;
import model.User;

import java.util.ArrayList;

public class GameController {

    protected static int mapWidth = 10;
    protected static int mapHeight = 10;
    protected static ArrayList<User> players;
    protected static Tile[][] tiles;

    protected static Unit selectedUnit;
    protected static int turn;
    protected static User currentPlayer;

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
}