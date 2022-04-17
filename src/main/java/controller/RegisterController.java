package controller;

import model.User;

import java.util.regex.Matcher;

public class RegisterController {
    private static RegisterController instance;

    private RegisterController()
    {

    }

    public static RegisterController getInstance()
    {
        if(instance==null)
        {
            instance=new RegisterController();
        }
        return instance;
    }

    public String addUser(Matcher matcher)
    {
        for (User user : User.getUsers()) {
            if (user.getUsername().equals(matcher.group("username"))) {
                return "user with username " + matcher.group("username") + " already exists";
            }
            if (user.getNickname().equals(matcher.group("nickname"))) {
                return "user with nickname " + matcher.group("nickname") + " already exists";
            }
        }
        User user= new User(matcher.group("username"),matcher.group("password"),matcher.group("nickname"));
        User.addUser(user);
        return "user created successfully!";
    }

}
