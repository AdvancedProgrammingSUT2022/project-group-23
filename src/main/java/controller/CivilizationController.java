package controller;

import database.TerrainDatabase;
import model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class CivilizationController extends GameController{

    private UnitController unitController;
    private CityController cityController;

    public CivilizationController(ArrayList<User> players) {
        GameController.players = players;
        tiles = new Tile[mapHeight][mapWidth];
        for (User user : players) user.newGame();
        currentPlayer = players.get(0);
        initializeMap();
        turn = 0;
        unitController = new UnitController();
        cityController = new CityController();
    }

    public void initializeMap() {
        for (int i = 0; i < mapHeight; i++) {
            for (int j = 0; j < mapWidth; j++) {
                tiles[i][j] = new Tile(i, j);
                for (int k = 0; k < players.size(); k++) {
                    tiles[i][j].setVisibilityForUser("fog of war", k);
                }
                TerrainDatabase.addRandomTerrainAndFeatureToTile(tiles[i][j]);
                TerrainDatabase.addRandomResourceToTile(tiles[i][j]);
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


    public String nextTurn() {
        currentPlayer.getWaitedTechnologies().put(currentPlayer.getCurrentStudy().getName(),currentPlayer.getWaitedTechnologies().get(currentPlayer.getCurrentStudy().getName())-currentPlayer.totalCup());
        if(currentPlayer.getWaitedTechnologies().get(currentPlayer.getCurrentStudy().getName())<=0){
            currentPlayer.addTechnology(currentPlayer.getCurrentStudy());
            currentPlayer.setCurrentStudy(null);
            currentPlayer.getWaitedTechnologies().remove(currentPlayer.getCurrentStudy().getName());
        }
        //TODO check if unit needs action or ...
        String message;
        if(!(message = unitController.isTurnPossible()).equals("ok"))return message;
        turn = (turn + 1) % players.size();
        currentPlayer = players.get(turn);


        unitController.checkVisibility();


        return "it's " + currentPlayer.getUsername() + " turn";
    }

    public ArrayList<Unit> showUnitsInfo()
    {
        return currentPlayer.getUnits();
    }

    public ArrayList<City> showCitiesInfo()
    {
        return currentPlayer.getCities();
    }

    public Technology showCurrentStudy(){
        return currentPlayer.getCurrentStudy();
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

    public User getCurrentPlayer () {
        return currentPlayer;
    }

    public void studyTechnology(Technology technology){
        currentPlayer.setCurrentStudy(technology);
        HashMap<String,Integer> waitedTechnologies=currentPlayer.getWaitedTechnologies();
        if(!waitedTechnologies.containsKey(technology.getName())){
            waitedTechnologies.put(technology.getName(),technology.getCost());
        }
    }
}
