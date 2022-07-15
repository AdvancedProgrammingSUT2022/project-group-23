package controller;

import com.google.gson.Gson;
import model.Request;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class NetworkController {
    private static int SERVER_PORT = 8100;
    private static DataOutputStream dataOutputStream;
    private static DataInputStream dataInputStream;

    private static DataOutputStream secondOutputStream;
    private static DataInputStream secondInputStream;

    public NetworkController(Socket socket){

    }

    public static void connectToServer(){
        Socket socket;
        Socket listenerSocket;
        try {
            socket = new Socket("localhost", SERVER_PORT);
        }catch (IOException e){
            System.out.println("can't connect to server");
            return;
        }
        try {
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataInputStream = new DataInputStream(socket.getInputStream());
            String index = sendRequest(new Request("initiate non-listener"));
            listenerSocket = new Socket("localhost", SERVER_PORT);
            secondInputStream = new DataInputStream(listenerSocket.getInputStream());
            secondOutputStream = new DataOutputStream(listenerSocket.getOutputStream());
            Request request = new Request("initiate listener");
            request.getInfo().put("index", index);
            secondOutputStream.writeUTF(new Gson().toJson(request));
            secondOutputStream.flush();
        }catch (IOException e){
            System.out.println("can't read streams");
        }

    }
    public static String sendRequest(Request request){
        String json = new Gson().toJson(request);
        try {
            dataOutputStream.writeUTF(json);
            dataOutputStream.flush();
        }catch (IOException e){
            System.out.println("can't send request");
        }
        try {
            return dataInputStream.readUTF();
        }catch (IOException e){
            System.out.println("can't read response");
            return null;
        }
    }


}
