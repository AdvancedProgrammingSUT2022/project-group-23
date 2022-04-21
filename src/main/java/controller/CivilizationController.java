package controller;

import database.TerrainDatabase;
import model.Tile;
import model.User;

import java.util.ArrayList;

public class CivilizationController {
    private ArrayList<User> players;

    private int mapWidth = 10;
    private int mapHeight = 10;

    private Tile[][] tiles = new Tile[mapHeight][mapWidth];

    public CivilizationController(ArrayList<User> players){
        this.players = players;
        for(User user : players) user.newGame();
        initializeMap();
    }
    public void initializeMap(){
        for (int i = 0; i < mapHeight; i++) {
            for (int j = 0; j < mapWidth; j++) {
                tiles[i][j] = new Tile(i , j);
                TerrainDatabase.addRandomTerrainAndFeatureToTile(tiles[i][j]);
            }
        }
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public Tile[][] getTiles() {
        return tiles;
    }
}
