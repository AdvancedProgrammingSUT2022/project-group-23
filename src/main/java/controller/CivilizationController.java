package controller;

import model.User;

import java.util.ArrayList;

public class CivilizationController {
    private ArrayList<User> players;
    public CivilizationController(ArrayList<User> players){
        this.players = players;
        for(User user : players) user.newGame();
    }
}
