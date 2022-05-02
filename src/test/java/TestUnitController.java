import controller.CityController;
import controller.CivilizationController;
import controller.GameController;
import controller.UnitController;
import database.TechnologyDatabase;
import database.TerrainDatabase;
import model.*;
import org.junit.After;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestUnitController extends GameController {



    @Test
    public void testBuildRoad(){
        players=new ArrayList<>();
        User user=new User("omid","123","omid123");
        players.add(user);
        WorkerUnit unit=new WorkerUnit(9,9);
        CivilizationController civilizationController=new CivilizationController(players);
        CityController cityController=new CityController();
        UnitController unitController=new UnitController(cityController);
        for (Unit currentPlayerUnit : currentPlayer.getUnits()) {
            currentPlayerUnit.setState("no");
        }
        GameController.setSelectedUnit(unit);
        assertEquals(unitController.buildRoad(),"you don't have the right technology to build road");
        currentPlayer.addTechnology(TechnologyDatabase.getTechnologies().get(8));
        unitController.buildRoad();
        unitController.isTurnPossible();
        unitController.isTurnPossible();
        unitController.isTurnPossible();
        assertTrue(tiles[9][9].isRoad());
    }

    @Test
    public void testEliminateFeature(){
        players=new ArrayList<>();
        User user=new User("omid","123","omid123");
        players.add(user);
        WorkerUnit unit=new WorkerUnit(9,9);
        CivilizationController civilizationController=new CivilizationController(players);
        CityController cityController=new CityController();
        UnitController unitController=new UnitController(cityController);
        for (Unit currentPlayerUnit : currentPlayer.getUnits()) {
            currentPlayerUnit.setState("no");
        }
        GameController.setSelectedUnit(unit);
        Feature feature=new Feature("Jungle", 0, 1 ,-1, 2, 25,-10);
        tiles[9][9].setFeature(feature);
        unitController.eliminateFeature();
        for(int i=0;i<7;i++){
            unitController.isTurnPossible();
        }
        assertEquals(tiles[9][9].getFeature(),null);
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
