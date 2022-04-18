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

    public String addUser(String username, String nickname, String password)
    {
        for (User user : User.getUsers()) {
            if (user.getUsername().equals(username)) {
                return "user with username " + username + " already exists";
            }
            if (user.getNickname().equals(nickname)) {
                return "user with nickname " + nickname + " already exists";
            }
        }
        User user= new User(username,password,nickname);
        User.addUser(user);
        return "user created successfully!";
    }
    public String login(String username,String password)
    {
        for (User user : User.getUsers()) {
            if(user.getUsername().equals(username))
            {
                if(!user.getPassword().equals(password))
                {
                    return "Username and password didn’t match!";
                }
                User.setUserLogged(user);
                return "user logged in successfully!";
            }
        }
        return "Username and password didn’t match!";
    }



}
