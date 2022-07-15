package database;

import controller.CivilizationController;
import controller.GameController;
import model.City;
import model.Tile;
import model.Unit;
import model.User;

import java.util.ArrayList;

public class SaveDatabase {
    private int mapWidth ;
    private int mapHeight ;
    private int currentYear;
    protected ArrayList<User> lostPlayers;
    protected int saveNumber;
    private ArrayList<User> players;
    private Tile[][] tiles;
    private int turn;
    private User currentPlayer;
    private CivilizationController civilizationController;
    public SaveDatabase() {
        mapHeight = GameController.getMapHeight();
        mapWidth = GameController.getMapWidth();
        players = GameController.getPlayers();
        tiles = GameController.getTiles();
        turn = GameController.getTurn();
        currentPlayer = GameController.getCurrentPlayer();
        civilizationController = GameController.getCivilizationController();
        currentYear = GameController.getCurrentYear();
        lostPlayers = GameController.getLostPlayers();
        saveNumber = GameController.getSaveNumber();
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public ArrayList<User> getPlayers() {
        return players;
    }

    public Tile[][] getTiles() {
        return tiles;
    }



    public int getTurn() {
        return turn;
    }

    public User getCurrentPlayer() {
        return currentPlayer;
    }

    public CivilizationController getCivilizationController() {
        return civilizationController;
    }

    public int getCurrentYear() {
        return currentYear;
    }

    public ArrayList<User> getLostPlayers() {
        return lostPlayers;
    }

    public int getSaveNumber() {
        return saveNumber;
    }
}
