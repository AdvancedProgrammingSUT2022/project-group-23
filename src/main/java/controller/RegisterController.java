package controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
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


                ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
                scheduledExecutorService.scheduleAtFixedRate(() -> {
                    String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm").format(new java.util.Date());
                    user.setLastOnline(timeStamp);
                    User.updateUsersInfo();
                    if(!User.getUserLogged().equals(user))
                        scheduledExecutorService.shutdown();
                }, 0, 60, TimeUnit.SECONDS);


                return "user logged in successfully!";
            }
        }
        return "Username and password didn't match!";
    }
}
