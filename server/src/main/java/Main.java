

import com.google.gson.Gson;
import controller.NetworkController;
import model.Request;
import model.User;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static int SERVER_PORT = 50000;
    public static void main(String[] args){
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(SERVER_PORT);
        }catch (IOException e){
            System.out.println("can't access port");
            return;
        }

        while (true){
            try {
                Socket socket = serverSocket.accept();
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                Request request;
                try{
                    String requestJson = dataInputStream.readUTF();
                    request = new Gson().fromJson(requestJson, Request.class);
                }catch (IOException e){
                    System.out.println("can't get request from client");
                    break;
                }
                if(request.getType().equals("initiate non-listener")){
                    NetworkController networkController = new NetworkController(dataInputStream, dataOutputStream);
                    dataOutputStream.writeUTF(String.valueOf(NetworkController.getNetworkControllers().size() - 1));
                    dataOutputStream.flush();
                    networkController.start();
                }else{
                    int index = Integer.parseInt(request.getInfo().get("index"));
                    NetworkController.getNetworkControllers().get(index).setSecondInputStream(dataInputStream);
                    NetworkController.getNetworkControllers().get(index).setSecondOutputStream(dataOutputStream);
                }
            } catch (IOException e) {
                System.out.println("can't accept client");
            }
        }


    }
}
