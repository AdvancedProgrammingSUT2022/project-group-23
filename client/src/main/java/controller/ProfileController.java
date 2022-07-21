package controller;

import com.google.gson.Gson;
import model.Request;
import model.User;

import java.io.FileWriter;
import java.io.IOException;

public class ProfileController {
    private static ProfileController instance;

    private ProfileController(){

    }

    public static ProfileController getInstance() {
        if(instance==null) {
            instance=new ProfileController();
        }
        return instance;
    }

    public String changeNickname(String nickname) {
        Request request = new Request("changeNickname");
        request.getInfo().put("nickname", nickname);
        return NetworkController.sendRequest(request);
    }


    public String changePassword(String currentPassword, String newPassword) {
        Request request = new Request("changePassword");
        request.getInfo().put("currentPassword", currentPassword);
        request.getInfo().put("newPassword", newPassword);
        return NetworkController.sendRequest(request);
    }

    public String changeProfilePicture(String newURL){
        Request request = new Request("changeProfilePicture");
        request.getInfo().put("URL", newURL);
        return NetworkController.sendRequest(request);
    }
}
