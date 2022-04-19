package controller;

import model.User;

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
        return "password changed successfully!";
    }
}
