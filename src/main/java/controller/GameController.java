package controller;

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

    public boolean isCoordinateValid(int x, int y) {
        return x >= 0 && x < mapHeight && y >= 0 && y < mapWidth;
    }

}
