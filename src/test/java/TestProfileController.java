import controller.MainMenuController;
import controller.ProfileController;
import controller.RegisterController;
import model.User;
import org.junit.After;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class TestProfileController {
    @Test
    public void testChangePassword()
    {
        RegisterController registerController=RegisterController.getInstance();
        ProfileController profileController=ProfileController.getInstance();
        registerController.addUser("omid","omid123","123");
        registerController.login("omid","123");
        String output=profileController.changePassword("1234","1234");
        assertEquals(output,"current password is invalid");
        output=profileController.changePassword("123","123");
        assertEquals(output,"please enter a new password");
        output=profileController.changePassword("123","1234");
        assertEquals(output,"password changed successfully!");
    }

    @Test
    public void testChangeNickname()
    {
        RegisterController registerController=RegisterController.getInstance();
        ProfileController profileController=ProfileController.getInstance();
        registerController.addUser("omid","omid123","123");
        registerController.login("omid","123");
        String output;
        output=profileController.changeNickname("omid1234");
        assertEquals(output,"nickname changed successfully!");
    }

    @Test
    public void testChangeUsername()
    {
        RegisterController registerController=RegisterController.getInstance();
        ProfileController profileController=ProfileController.getInstance();
        registerController.addUser("omid","omid123","123");
        registerController.login("omid","123");
        String output;
        output=profileController.changeUsername("omid1");
        assertEquals(output,"username changed successfully");
    }


    @After
    public void deleteUserFromJson()
    {
        ArrayList<User> users=new ArrayList<User>();
        for (User user : User.getUsers()) {
            if(!user.getUsername().equals("omid") && !user.getUsername().equals("omid1"))
            {
                users.add(user);
            }
        }
        User.setUsers(users);
        User.updateUsersInfo();
    }
}
