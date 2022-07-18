package model;

import com.google.gson.Gson;
import controller.NetworkController;

import java.io.IOException;
import java.util.ArrayList;

public class Game extends Thread{
    private static ArrayList<Game> games = new ArrayList<>();
    private String data;
    private int capacity;
    private boolean hasStarted;
    private volatile boolean waiting;
    private ArrayList<NetworkController> players;

    public Game(int capacity) {
        this.data = null;
        this.capacity = capacity;
        this.players = new ArrayList<>();
        this.hasStarted = false;
        this.waiting = true;
        games.add(this);
    }
    public boolean addPlayer(NetworkController player){
        players.add(player);
        return players.size() == capacity;
    }

    public ArrayList<NetworkController> getPlayers() {
        return players;
    }

    public int getCapacity() {
        return capacity;
    }

    public static ArrayList<Game> getGames() {
        return games;
    }

    public boolean hasStarted() {
        return hasStarted;
    }

    @Override
    public void run(){
        Gson gson = new Gson();
        this.hasStarted = true;
        try {
            Request request = new Request("startGame");
            ArrayList<User> playingUsers = new ArrayList<>();
            for(NetworkController player : players){
                playingUsers.add(player.getUser());
            }
            request.getInfo().put("userData", gson.toJson(playingUsers));
            players.get(0).getSecondOutputStream().writeUTF(gson.toJson(request));
            players.get(0).getSecondOutputStream().flush();
            data = players.get(0).getSecondInputStream().readUTF();
            for (int i = 1; i < players.size(); i++) {
                NetworkController player = players.get(i);
                request = new Request("gameStarted");
                request.getInfo().put("gameData", data);
                player.getSecondOutputStream().writeUTF(gson.toJson(request));
                player.getSecondOutputStream().flush();
            }
        } catch (IOException e) {
            System.out.println("can't send start message to client");
            return;
        }
        try {
            while (true) {
                while (waiting) {
                    Thread.onSpinWait();
                }
                waiting = true;
                Request request = new Request("nextTurn");
                request.getInfo().put("gameData", data);
                for(NetworkController player : players){
                    player.getSecondOutputStream().writeUTF(gson.toJson(request));
                    player.getSecondOutputStream().flush();
                }
            }
        } catch (IOException e) {
            System.out.println("can't connect to client");
        }

    }

    public void setWaiting(boolean waiting) {
        this.waiting = waiting;
    }

    public void setData(String data) {
        this.data = data;
    }
}
