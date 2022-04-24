package controller;

import database.TerrainDatabase;
import model.Tile;
import model.User;

import java.util.ArrayList;
import java.util.Random;

public class CivilizationController {

    UnitController unitController;
    private ArrayList<User> players;

    private User currentPlayer;

    private final int mapWidth = 10;
    private final int mapHeight = 10;

    private int turn;
    private Tile[][] tiles = new Tile[mapHeight][mapWidth];

    public CivilizationController(ArrayList<User> players) {
        this.players = players;
        for (User user : players) user.newGame();
        currentPlayer = players.get(0);
        initializeMap();
        turn = 0;
        unitController = new UnitController(mapWidth, mapHeight, players, tiles, currentPlayer, turn);
    }

    public void initializeMap() {
        for (int i = 0; i < mapHeight; i++) {
            for (int j = 0; j < mapWidth; j++) {
                tiles[i][j] = new Tile(i, j);
                for (int k = 0; k < players.size(); k++) {
                    tiles[i][j].setVisibilityForUser("fog of war", k);
                }
                TerrainDatabase.addRandomTerrainAndFeatureToTile(tiles[i][j]);
            }
        }

        addRiversToMap();
    }

    private void addRiversToMap() {
        Random random = new Random();
        for (int i = 0; i < mapHeight; i++) {
            for (int j = 0; j < mapWidth; j++) {
                ArrayList<Integer> rivers = tiles[i][j].getRivers();
                for (int k = 1; k <= 6; k++) {
                    if (random.nextInt(10) == 0 && !rivers.contains(k)) {
                        rivers.add(k);
                        if (k == 1 && isCoordinateValid(i - 1, j)) tiles[i - 1][j].addRiver(4);
                        else if (k == 4 && isCoordinateValid(i + 1, j)) tiles[i + 1][j].addRiver(1);
                        else if (j % 2 == 0) {
                            if (k == 2 && isCoordinateValid(i - 1, j + 1)) tiles[i - 1][j + 1].addRiver(5);
                            else if (k == 3 && isCoordinateValid(i, j + 1)) tiles[i][j + 1].addRiver(6);
                            else if (k == 5 && isCoordinateValid(i, j - 1)) tiles[i][j - 1].addRiver(2);
                            else if (k == 6 && isCoordinateValid(i - 1, j - 1)) tiles[i - 1][j - 1].addRiver(3);
                        } else {
                            if (k == 2 && isCoordinateValid(i, j + 1)) tiles[i][j + 1].addRiver(5);
                            else if (k == 3 && isCoordinateValid(i + 1, j + 1)) tiles[i + 1][j + 1].addRiver(6);
                            else if (k == 5 && isCoordinateValid(i + 1, j - 1)) tiles[i + 1][j - 1].addRiver(2);
                            else if (k == 6 && isCoordinateValid(i, j - 1)) tiles[i][j - 1].addRiver(3);
                        }
                    }
                }
            }
        }
    }

    public boolean isCoordinateValid(int x, int y) {
        return x >= 0 && x < mapHeight && y >= 0 && y < mapWidth;
    }

    public String nextTurn() {
        //TODO check if unit needs action or ...
        String message;
        if(!(message = unitController.isTurnPossible()).equals("ok"))return message;
        this.turn = (turn + 1) % players.size();
        this.currentPlayer = players.get(turn);
        unitController.setCurrentPlayer(this.currentPlayer);
        unitController.setTurn(this.turn);
        unitController.checkVisibility();

        return "it's " + currentPlayer.getUsername() + "turn";
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

    public UnitController getUnitController() {
        return unitController;
    }

    public int getTurn() {
        return turn;
    }
}
