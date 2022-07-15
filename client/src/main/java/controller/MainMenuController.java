package controller;

import model.Request;
import model.User;

public class MainMenuController {
    private static MainMenuController instance;

    private MainMenuController(){

    }

    public static MainMenuController getInstance() {
        if(instance==null) {
            instance=new MainMenuController();
        }
        return instance;
    }

    public String logout() {
        Request request = new Request("logout");
        return NetworkController.sendRequest(request);
    }

}
