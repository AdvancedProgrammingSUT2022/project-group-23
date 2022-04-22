import controller.RegisterController;
import model.User;
import org.junit.After;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class TestRegisterController {

    @Test
    public void testRegisterUser()
    {
        RegisterController registerController=RegisterController.getInstance();
        String output=registerController.addUser("omid","omid123","123");
        assertEquals(output,"user created successfully!");
        output=registerController.addUser("omid","omid123","123");
        assertEquals(output,"user with username omid already exists");
        output=registerController.addUser("omid1","omid123","123");
        assertEquals(output,"user with nickname omid123 already exists");
    }
    @Test
    public void testLogin()
    {
        RegisterController registerController=RegisterController.getInstance();
        registerController.addUser("omid","omid123","123");
        String output=registerController.login("omid1","omid123");
        assertEquals(output,"Username and password didn't match!");
        output=registerController.login("omid","omid1231");
        assertEquals(output,"Username and password didn't match!");
        output=registerController.login("omid","123");
        assertEquals(output,"user logged in successfully!");
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
