package controller;

import model.*;

import java.util.ArrayList;
import java.util.Random;

public class UnitController {

    private final int mapWidth;
    private final int mapHeight;
    private ArrayList<User> players;
    private Tile[][] tiles;

    public UnitController(int mapWidth, int mapHeight, ArrayList<User> players, Tile[][] tiles) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.players = players;
        this.tiles = tiles;
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
}
