package controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
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
    }

    public static RegisterController getInstance() {
        if(instance == null) {
            instance = new RegisterController();
        }
        return instance;
    }

    public String addUser(String username, String nickname, String password) {
        Request request = new Request("addUser");
        request.getInfo().put("nickname", nickname);
        request.getInfo().put("username", username);
        request.getInfo().put("password", password);
        request.getInfo().put("profilePictureURL", getClass().getResource("/images/profilePictures/" + new Random().nextInt(6) + ".png").toString());
        return NetworkController.sendRequest(request);
    }
    public String login(String username,String password) {
        Request request = new Request("login");
        request.getInfo().put("username", username);
        request.getInfo().put("password", password);
        String response = NetworkController.sendRequest(request);
        if(response.contains("successful")) User.setUsernameLogged(username);
        return response;
    }
}
