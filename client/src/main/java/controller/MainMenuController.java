package controller;

import model.User;

public class MainMenuController {
    private static MainMenuController instance;

    private MainMenuController(){

    }

    public static MainMenuController getInstance()
    {
        if(instance==null)
        {
            instance=new MainMenuController();
        }
        return instance;
    }

    public String logout()
    {
        User.setUserLogged(null);
        return "user logged out successfully!";
    }

    public boolean hasUser(String username){
        for(User user : User.getUsers()){
            if(user.getUsername().equals(username))
                return true;}
        return false;
    }
}
