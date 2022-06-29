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
    private ArrayList<User> players;
    private Tile[][] tiles;
    private Unit selectedUnit;
    private City selectedCity;
    private int turn;
    private User currentPlayer;
    private CivilizationController civilizationController;
    public SaveDatabase() {
        mapHeight = GameController.getMapHeight();
        mapWidth = GameController.getMapWidth();
        players = GameController.getPlayers();
        tiles = GameController.getTiles();
        selectedUnit = GameController.getSelectedUnit();
        selectedCity = GameController.getSelectedCity();
        turn = GameController.getTurn();
        currentPlayer = GameController.getCurrentPlayer();
        civilizationController = GameController.getCivilizationController();
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

    public Unit getSelectedUnit() {
        return selectedUnit;
    }

    public City getSelectedCity() {
        return selectedCity;
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
}
