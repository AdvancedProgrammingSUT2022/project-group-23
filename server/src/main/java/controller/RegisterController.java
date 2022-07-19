package controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Request;
import model.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class RegisterController {
    private static RegisterController instance;

    private RegisterController() {
        try {
            String json= new String(Files.readAllBytes(Paths.get("src\\main\\resources\\saves\\userInfo\\UserInfo.json")));
            User.setUsers(new Gson().fromJson(json, new TypeToken<List<User>>(){}.getType()));
        } catch (IOException e) {
            System.out.println("ERROR");
        }
    }

    public static RegisterController getInstance() {
        if(instance==null) {
            instance=new RegisterController();
        }
        return instance;
    }

    public String addUser(Request request) {
        String username = request.getInfo().get("username");
        String nickname = request.getInfo().get("nickname");
        String password = request.getInfo().get("password");
        for (User user : User.getUsers()) {
            if (user.getUsername().equals(username)) {
                return "user with username " + username + " already exists";
            }
            if (user.getNickname().equals(nickname)) {
                return "user with nickname " + nickname + " already exists";
            }
        }
        new User(username, password, nickname, request.getInfo().get("profilePictureURL"));
        return "user created successfully!";
    }
    public String login(Request request) {
        String username = request.getInfo().get("username");
        String password = request.getInfo().get("password");
        for (User user : User.getUsers()) {
            if(user.getUsername().equals(username)) {
                if(!user.getPassword().equals(password)) {
                    return "Username and password didn't match!";
                }
                for(NetworkController networkController : NetworkController.getNetworkControllers()){
                    if(networkController.getUser() != null && networkController.getUser().getUsername().equals(username))
                        return "This username is already logged in another device!";
                }
                return "user logged in successfully!";
            }
        }
        return "Username and password didn't match!";
    }
}
