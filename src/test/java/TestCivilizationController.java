import controller.CityController;
import controller.CivilizationController;
import controller.GameController;
import controller.UnitController;
import database.ImprovementDatabase;
import database.TechnologyDatabase;
import database.UnitsDatabase;
import model.*;
import org.junit.After;
import org.junit.Test;
import view.GameView;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class TestCivilizationController extends GameController {


    @Test
    public void testNextTurn(){
        players=new ArrayList<>();
        User user=new User("omid","123","omid123");
        players.add(user);
        User user1=new User("omid1","123","omid1231");
        players.add(user1);
        CivilizationController civilizationController=new CivilizationController(players);
        CityController cityController=new CityController();
        UnitController unitController=new UnitController(cityController);
        currentPlayer=user;
        String output=civilizationController.nextTurn();
        assertEquals(output,"unit needs action");
        for (Unit unit : user.getUnits()) {
            unit.setState("na");
        }
        output=civilizationController.nextTurn();
        assertEquals(output,"it's omid1 turn");
    }

    @Test
    public void testGetterAndSetter(){
        players=new ArrayList<>();
        User user=new User("omid","123","omid123");
        players.add(user);
        CivilizationController civilizationController=new CivilizationController(players);
        CityController cityController=new CityController();
        UnitController unitController=new UnitController(cityController);
        civilizationController.getCurrentPlayer();
        civilizationController.getCityController();
        civilizationController.getUnitController();
        civilizationController.showUnitsInfo();
        civilizationController.showCitiesInfo();
        civilizationController.studyTechnology(TechnologyDatabase.getTechnologies().get(0));
        assertEquals(currentPlayer.getCurrentStudy(),TechnologyDatabase.getTechnologies().get(0));
        civilizationController.getMapHeight();
        civilizationController.getMapWidth();
        civilizationController.showCurrentStudy();
        civilizationController.getTiles();
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
