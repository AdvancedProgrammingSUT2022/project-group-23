package controller;

import com.google.gson.Gson;
import model.Game;
import model.Request;
import model.User;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class NetworkController extends Thread{
    private static ArrayList<NetworkController> networkControllers = new ArrayList<>();

    private Game game;
    private User user;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private DataOutputStream secondOutputStream;
    private DataInputStream secondInputStream;

    public NetworkController(DataInputStream dataInputStream, DataOutputStream dataOutputStream){
        this.dataInputStream = dataInputStream;
        this.dataOutputStream = dataOutputStream;
        networkControllers.add(this);
        this.user = null;
        this.game = null;
    }


    @Override
    public void run(){
        while (true) {
            Request request;
            try{
                String requestJson = dataInputStream.readUTF();
                request = new Gson().fromJson(requestJson, Request.class);;
            }catch (IOException e){
                System.out.println("can't get request from client");
                break;
            }
            String response = "";
            switch (request.getType()) {
                case "addUser" -> response = RegisterController.getInstance().addUser(request);
                case "login" -> {
                    response = RegisterController.getInstance().login(request);
                    if (response.equals("user logged in successfully!"))
                        this.user = User.getUserByUsername(request.getInfo().get("username"));
                }
                case "changeNickname" -> response = ProfileController.getInstance().changeNickname(request, this.user);
                case "changePassword" -> response = ProfileController.getInstance().changePassword(request, this.user);
                case "logout" -> {
                    response = MainMenuController.getInstance().logout();
                    this.user = null;
                }
                case "lobbyGames" ->{
                    ArrayList<ArrayList<String>> gameList = new ArrayList<>();
                    for(Game game1 : Game.getGames()){
                        ArrayList<String> gameArray = new ArrayList<>();
                        gameList.add(gameArray);
                        gameArray.add(String.valueOf(game1.getCapacity()));
                        for(NetworkController player : game1.getPlayers()){
                            String nickname = player.getUser().getNickname();
                            gameArray.add(nickname);
                        }
                    }
                    response = new Gson().toJson(gameList);
                }
                case "createGame" -> {
                    if(this.game != null) response = "you've already joined/created a game";
                    else {
                        game = new Game(Integer.parseInt(request.getInfo().get("capacity")));
                        game.addPlayer(this);
                        response = "Game created successfully";
                    }
                }
                case "joinGame" -> {
                    if(this.game != null) response = "you've already joined/created a game";
                    else {
                        game = Game.getGames().get(Integer.parseInt(request.getInfo().get("gameNumber")));
                        if(game.addPlayer(this)){
                            game.start();
                        }
                        response = "You successfully joined this game";
                    }
                }
                case "exitWaitingForGame" -> {
                    if(this.game == null)response = "You're not in a game";
                    else {
                        game.getPlayers().remove(this);
                        game = null;
                        response = "You successfully exited the game";
                    }
                }
                case "nextTurn" -> {
                    game.setData(request.getInfo().get("gameData"));
                    game.setWaiting(false);
                }
            }


            try {
                dataOutputStream.writeUTF(response);
                dataOutputStream.flush();
            }catch (IOException e){
                System.out.println("can't send response to client");
            }

        }

    }

    public static ArrayList<NetworkController> getNetworkControllers() {
        return networkControllers;
    }

    public User getUser() {
        return user;
    }

    public void setSecondOutputStream(DataOutputStream secondOutputStream) {
        this.secondOutputStream = secondOutputStream;
    }

    public void setSecondInputStream(DataInputStream secondInputStream) {
        this.secondInputStream = secondInputStream;
    }

    public DataOutputStream getSecondOutputStream() {
        return secondOutputStream;
    }

    public DataInputStream getSecondInputStream() {
        return secondInputStream;
    }
}
