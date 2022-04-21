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
            if(user.getNickname().equals(nickname))
            {
                return "user with nickname "+nickname+" already exists";
            }
        }
        User.getUserLogged().setNickname(nickname);
        try {
            FileWriter writer=new FileWriter("src\\main\\resources\\UsersInfo.json");
            writer.write(new Gson().toJson(User.getUsers()));
            writer.close();
        } catch (IOException e) {
            System.out.println("ERROR");
        }
        return "nickname changed successfully!";
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
        try {
            FileWriter writer=new FileWriter("src\\main\\resources\\UsersInfo.json");
            writer.write(new Gson().toJson(User.getUsers()));
            writer.close();
        } catch (IOException e) {
            System.out.println("ERROR");
        }
        return "password changed successfully!";
    }
}
