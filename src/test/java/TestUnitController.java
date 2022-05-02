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

import java.util.ArrayList;

import static org.junit.Assert.*;

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
        for(int i=0;i<3;i++){
            unitController.isTurnPossible();
        }
        assertTrue(tiles[9][9].isRoad());
    }

    @Test
    public void testEliminateRoad(){
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
        currentPlayer.addTechnology(TechnologyDatabase.getTechnologies().get(8));
        unitController.buildRoad();
        for(int i=0;i<3;i++){
            unitController.isTurnPossible();
        }
        unit.setRemainingMoves(2);
        unit.setState("ready");
        unitController.eliminateRoad();
        assertFalse(tiles[9][9].isRoad());
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

    @Test
    public void testImproveTile(){
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
        Improvement improvement= ImprovementDatabase.getImprovements().get(1);
        currentPlayer.addTechnology(TechnologyDatabase.getTechnologies().get(0));
        tiles[9][9].setTerrain(new Terrain("Grassland" , 0, 2, 0, 1, -33,40));
        tiles[9][9].setFeature(new Feature("Forest", 0, 1 ,1, 2, 25,10));
        unitController.improveTile(improvement);
        for(int i=0;i<10;i++){
            unitController.isTurnPossible();
        }
        assertEquals(tiles[9][9].getImprovement().getName(),improvement.getName());
        assertEquals(tiles[9][9].getFeature(),null);
    }

    @Test
    public void testLootAndHealTile(){
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
        Improvement improvement= ImprovementDatabase.getImprovements().get(1);
        currentPlayer.addTechnology(TechnologyDatabase.getTechnologies().get(0));
        tiles[9][9].setTerrain(new Terrain("Grassland" , 0, 2, 0, 1, -33,40));
        tiles[9][9].setFeature(new Feature("Forest", 0, 1 ,1, 2, 25,10));
        unitController.improveTile(improvement);
        for(int i=0;i<10;i++){
            unitController.isTurnPossible();
        }
        MilitaryUnit unit1=new MilitaryUnit("Archer", 70, "Archery", 2, 4, 6, 2, "Archery", null);
        unit1.setX(9);
        unit1.setY(9);
        GameController.setSelectedUnit(unit1);
        unitController.lootTile();
        assertEquals(tiles[9][9].getImprovement(),null);
        GameController.setSelectedUnit(unit);
        unitController.healTile();
        for(int i=0;i<3;i++){
            unitController.isTurnPossible();
        }
        assertEquals(tiles[9][9].getImprovement().getName(),improvement.getName());
    }

    @Test
    public void testMoveUnit(){
        players=new ArrayList<>();
        User user=new User("omid","123","omid123");
        players.add(user);
        CivilizationController civilizationController=new CivilizationController(players);
        CityController cityController=new CityController();
        UnitController unitController=new UnitController(cityController);
        Unit unit=UnitsDatabase.getUnits().get(0);
        unit.setX(2);
        unit.setY(2);
        user.addUnit(unit);
        for (Unit currentPlayerUnit : currentPlayer.getUnits()) {
            currentPlayerUnit.setState("no");
        }
        GameController.setSelectedUnit(unit);
        unitController.moveSelectedUnit(6,6);
        int size=unit.getMoves().size();
        System.out.println(size);
        for(int i=0;i<100;i++){
            unitController.isTurnPossible();
        }
        System.out.println(unit.getX());
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
