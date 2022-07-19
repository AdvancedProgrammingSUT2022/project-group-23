package controller;

import com.google.gson.Gson;
import model.Request;
import model.User;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class NetworkController extends Thread{
    private static ArrayList<NetworkController> networkControllers = new ArrayList<>();

    private GameController gameController;
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
        this.gameController = null;
    }


    @Override
    public void run(){
        while (true) {
            Request request;
            try{
                String requestJson = dataInputStream.readUTF();
                request = new Gson().fromJson(requestJson, Request.class);;
            }catch (IOException e){
                System.out.println("client disconnected");
                if(this.user != null){
                    String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm").format(new java.util.Date());
                    this.user.setLastOnline(timeStamp);
                }
                networkControllers.remove(this);
                break;
            }
            String response = "";
            switch (request.getType()) {
                case "addUser" -> response = RegisterController.getInstance().addUser(request);
                case "login" -> {
                    response = RegisterController.getInstance().login(request);
                    if (response.equals("user logged in successfully!")) {
                        this.user = User.getUserByUsername(request.getInfo().get("username"));
                        this.user.setLastOnline("Online");
                    }
                }
                case "changeNickname" -> response = ProfileController.getInstance().changeNickname(request, this.user);
                case "changePassword" -> response = ProfileController.getInstance().changePassword(request, this.user);
                case "logout" -> {
                    response = MainMenuController.getInstance().logout();
                    String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm").format(new java.util.Date());
                    this.user.setLastOnline(timeStamp);
                    this.user = null;
                }
                case "lobbyGames" ->{
                    ArrayList<ArrayList<String>> gameList = new ArrayList<>();
                    for(GameController gameController1 : GameController.getGameControllers()){
                        ArrayList<String> gameArray = new ArrayList<>();
                        gameList.add(gameArray);
                        gameArray.add(String.valueOf(gameController1.getCapacity()));
                        for(NetworkController player : gameController1.getPlayers()){
                            String nickname = player.getUser().getNickname();
                            gameArray.add(nickname);
                        }
                    }
                    response = new Gson().toJson(gameList);
                }
                case "createGame" -> {
                    if(this.gameController != null) response = "you've already joined/created a game";
                    else {
                        gameController = new GameController(Integer.parseInt(request.getInfo().get("capacity")));
                        gameController.addPlayer(this);
                        response = "Game created successfully";
                    }
                }
                case "joinGame" -> {
                    if(this.gameController != null) response = "you've already joined/created a game";
                    else {
                        gameController = GameController.getGameControllers().get(Integer.parseInt(request.getInfo().get("gameNumber")));
                        if(gameController.addPlayer(this)){
                            gameController.start();
                        }
                        response = "You successfully joined this game";
                    }
                }
                case "exitWaitingForGame" -> {
                    if(this.gameController == null)response = "You're not in a game";
                    else {
                        gameController.getPlayers().remove(this);
                        if(gameController.getPlayers().isEmpty())GameController.getGameControllers().remove(gameController);
                        gameController = null;
                        response = "You successfully exited the game";
                    }
                }
                case "userInfo" -> {
                    response = new Gson().toJson(User.getUsers());
                }
                case "nextTurn" -> {
                    gameController.setData(request.getInfo().get("gameData"));
                    gameController.setWaiting(false);
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
