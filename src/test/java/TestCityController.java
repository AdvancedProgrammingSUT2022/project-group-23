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
    public void testPutCitizenToWorkAndRemove(){
        players=new ArrayList<>();
        User user=new User("omid","123","omid123");
        players.add(user);
        CivilizationController civilizationController=new CivilizationController(players);
        CityController cityController=new CityController();
        cityController.createCity(3,3);
        String output=cityController.putCitizenToWork(8,8);
        assertEquals(output,"no selected city");
        cityController.selectCity(3,3);
        output=cityController.putCitizenToWork(8,8);
        assertEquals(output,"citizens can't work on this tile");
        output=cityController.putCitizenToWork(3,4);
        assertEquals(output,"citizen added to tile successfully");
        assertTrue(cityController.getCityAtCoordinate(3,3).getTilesWithCitizen().contains(tiles[3][4]));
        output=cityController.removeCitizen(5,5);
        assertEquals(output,"this tile doesn't have any citizen");
        output=cityController.removeCitizen(3,4);
        assertEquals(output,"citizen removed successfully");
        assertFalse(cityController.getCityAtCoordinate(3,3).getTilesWithCitizen().contains(tiles[3][4]));
    }

    @Test
    public void testPurchaseTile(){
        players=new ArrayList<>();
        User user=new User("omid","123","omid123");
        players.add(user);
        CivilizationController civilizationController=new CivilizationController(players);
        CityController cityController=new CityController();
        cityController.createCity(3,3);
        user.setGold(0);
        String output=cityController.purchaseTile(tiles[3][4]);
        assertEquals(output,"no selected city");
        cityController.selectCity(3,3);
        output=cityController.purchaseTile(tiles[9][9]);
        assertEquals(output,"you can't purchase this tile!");
        output=cityController.purchaseTile(tiles[4][5]);
        assertEquals(output,"you don't have enough gold to purchase this tile");
        user.setGold(50);
        output=cityController.purchaseTile(tiles[4][5]);
        assertEquals(output,"tile purchased successfully");
        assertTrue(cityController.getCityAtCoordinate(3,3).getTiles().contains(tiles[4][5]));
    }

    @Test
    public void testAttackUnit(){
        players=new ArrayList<>();
        User user=new User("omid","123","omid123");
        players.add(user);
        User user1=new User("omid1","123","omid1231");
        players.add(user1);
        CivilizationController civilizationController=new CivilizationController(players);
        CityController cityController=new CityController();
        UnitController unitController=new UnitController(cityController);
        tiles[9][9].setTerrain(new Terrain("Hill" , 0, 0, 2, 2, 25,35));
        tiles[9][9].setFeature(new Feature("Forest", 0, 1 ,1, 2, 25,10));
        MilitaryUnit unit=new MilitaryUnit("Warrior", 40, "Melee", 2, 6, -1, -1, null, null);
        unit.setX(9);
        unit.setY(9);
        user1.getUnits().add(unit);
        currentPlayer=user;
        cityController.createCity(8,7);
        String output=cityController.attackUnit(unit);
        assertEquals(output,"no selected city");
        cityController.selectCity(8,7);
        assertTrue(cityController.reachableUnits().contains(unit));
        output=cityController.attackUnit(unit);
        assertEquals(output,"you killed the unit!");
        assertFalse(user1.getUnits().contains(unit));
    }

    @Test
    public void testCreateUnit(){
        players=new ArrayList<>();
        User user=new User("omid","123","omid123");
        players.add(user);
        CivilizationController civilizationController=new CivilizationController(players);
        CityController cityController=new CityController();
        UnitController unitController=new UnitController(cityController);
        cityController.createCity(4,4);
        cityController.selectCity(4,4);
        String output=cityController.constructUnit("Scout");
        assertEquals(output,"unit is being constructed");
        for(int i=0;i<100;i++){
            cityController.nextTurn();
        }
        for (Unit unit : user.getUnits()) {
            if(unit.getName().equals("Scout")){
                assertTrue(true);
            }
        }
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

    @After
    public void unselectCity(){
        selectedCity=null;
    }

}
