package controller;

import com.google.gson.Gson;
import model.Request;
import model.User;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class NetworkController extends Thread{
    private static ArrayList<NetworkController> networkControllers = new ArrayList<>();

    private User user;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private DataOutputStream secondOutputStream;
    private DataInputStream secondInputStream;

    public NetworkController(DataInputStream dataInputStream, DataOutputStream dataOutputStream){
        this.dataInputStream = dataInputStream;
        this.dataOutputStream = dataOutputStream;
        networkControllers.add(this);
        user = null;
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
}
