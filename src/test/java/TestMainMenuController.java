import controller.MainMenuController;
import controller.ProfileController;
import controller.RegisterController;
import model.User;
import org.junit.After;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class TestMainMenuController {

    @Test
    public void testLogout()
    {
        RegisterController registerController=RegisterController.getInstance();
        MainMenuController mainMenuController=MainMenuController.getInstance();
        registerController.addUser("omid","omid123","123");
        registerController.login("omid","123");
        String output=mainMenuController.logout();
        assertEquals(output,"user logged out successfully!");
    }

    @Test
    public void testHasUser()
    {
        RegisterController registerController=RegisterController.getInstance();
        MainMenuController mainMenuController=MainMenuController.getInstance();
        assertFalse(mainMenuController.hasUser("omid"));
        registerController.addUser("omid","omid123","123");
        registerController.login("omid","123");
        assertTrue(mainMenuController.hasUser("omid"));
    }

    @After
    public void deleteUserFromJson()
    {
        ArrayList<User> users=new ArrayList<User>();
        for (User user : User.getUsers()) {
            if(!user.getUsername().equals("omid"))
            {
                users.add(user);
            }
        }
        User.setUsers(users);
        User.updateUsersInfo();
    }
}
