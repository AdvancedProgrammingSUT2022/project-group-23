import controller.CityController;
import controller.CivilizationController;
import controller.GameController;
import controller.UnitController;
import database.ImprovementDatabase;
import database.TechnologyDatabase;
import model.*;
import org.junit.After;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class TestCityController extends GameController {
    @Test
    public void testCreateCity(){
        players=new ArrayList<>();
        User user=new User("omid","123","omid123");
        players.add(user);
        CivilizationController civilizationController=new CivilizationController(players);
        CityController cityController=new CityController();
        cityController.createCity(3,3);

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
