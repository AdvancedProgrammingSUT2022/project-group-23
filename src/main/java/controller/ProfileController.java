package controller;

import com.google.gson.Gson;
import model.User;

import java.io.FileWriter;
import java.io.IOException;

public class ProfileController {
    private static ProfileController instance;

    private ProfileController(){

    }

    public static ProfileController getInstance()
    {
        if(instance==null)
        {
            instance=new ProfileController();
        }
        return instance;
    }

    public String changeNickname(String nickname)
    {
        for (User user : User.getUsers()) {
            if(user.getNickname().equals(nickname) && !user.equals(User.getUserLogged()))
            {
                return "user with nickname "+nickname+" already exists";
            }
        }
        User.getUserLogged().setNickname(nickname);
        User.updateUsersInfo();
        return "nickname changed successfully!";
    }

    public String changeUsername(String username){
        for (User user : User.getUsers()){
            if(user.getUsername().equals(username) && !user.equals(User.getUserLogged())){
                return "user with username " + username + " already exists";
            }
        }
        User.getUserLogged().setUsername(username);
        User.updateUsersInfo();
        return "username changed successfully";
    }

    public String changePassword(String currentPassword, String newPassword)
    {
        if(!User.getUserLogged().getPassword().equals(currentPassword))
        {
            return "current password is invalid";
        }
        if(User.getUserLogged().getPassword().equals(newPassword))
        {
            return "please enter a new password";
        }
        User.getUserLogged().setPassword(newPassword);
        User.updateUsersInfo();
        return "password changed successfully!";
    }
}
