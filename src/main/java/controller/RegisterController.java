package controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class RegisterController {
    private static RegisterController instance;

    private RegisterController()
    {
        try {
            String json= new String(Files.readAllBytes(Paths.get("src\\main\\resources\\UsersInfo.json")));
            User.setUsers(new Gson().fromJson(json, new TypeToken<List<User>>(){}.getType()));
        } catch (IOException e) {
            System.out.println("ERROR");
        }
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
        new User(username,password,nickname);
        return "user created successfully!";
    }
    public String login(String username,String password)
    {
        for (User user : User.getUsers()) {
            if(user.getUsername().equals(username))
            {
                if(!user.getPassword().equals(password))
                {
                    return "Username and password didn't match!";
                }
                User.setUserLogged(user);
                return "user logged in successfully!";
            }
        }
        return "Username and password didn't match!";
    }
}
