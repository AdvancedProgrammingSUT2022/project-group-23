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

public class TestUnitController extends GameController {



    @Test
    public void testFoundCity(){
        players=new ArrayList<>();
        User user=new User("omid","123","omid123");
        players.add(user);
        SettlerUnit unit=new SettlerUnit(9,9);
        CivilizationController civilizationController=new CivilizationController(players);
        CityController cityController= civilizationController.getCityController();
        UnitController unitController=civilizationController.getUnitController();
        currentPlayer.getUnits().add(unit);
        for (Unit currentPlayerUnit : currentPlayer.getUnits()) {
            currentPlayerUnit.setState("no");
        }
        unitController.selectUnit(9,9,false);
        unitController.foundCity();
        City isCity=cityController.getCityAtCoordinate(9,9);
        assertEquals(isCity.getHealth(),20);
        unitController.getConstructableUnits();
    }


    @Test
    public void testBuildRoad(){
        players=new ArrayList<>();
        User user=new User("omid","123","omid123");
        players.add(user);
        WorkerUnit unit=new WorkerUnit(9,9);
        CivilizationController civilizationController=new CivilizationController(players);
        CityController cityController= civilizationController.getCityController();
        UnitController unitController=civilizationController.getUnitController();
        currentPlayer.getUnits().add(unit);
        for (Unit currentPlayerUnit : currentPlayer.getUnits()) {
            currentPlayerUnit.setState("no");
        }
        GameController.setSelectedUnit(unit);
        assertEquals(unitController.buildRoad("Road"),"you don't have the right technology to build Road");
        currentPlayer.addTechnology(TechnologyDatabase.getTechnologies().get(8));
        unitController.buildRoad("Road");
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
        CityController cityController= civilizationController.getCityController();
        UnitController unitController=civilizationController.getUnitController();
        currentPlayer.getUnits().add(unit);
        for (Unit currentPlayerUnit : currentPlayer.getUnits()) {
            currentPlayerUnit.setState("no");
        }
        GameController.setSelectedUnit(unit);
        currentPlayer.addTechnology(TechnologyDatabase.getTechnologies().get(8));
        unitController.buildRoad("Road");
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
        CityController cityController= civilizationController.getCityController();
        UnitController unitController=civilizationController.getUnitController();
        currentPlayer.getUnits().add(unit);
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
        CityController cityController= civilizationController.getCityController();
        UnitController unitController=civilizationController.getUnitController();
        currentPlayer.getUnits().add(unit);
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
        CityController cityController= civilizationController.getCityController();
        UnitController unitController=civilizationController.getUnitController();
        currentPlayer.getUnits().add(unit);
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
        currentPlayer.getUnits().add(unit1);
        unit1.setX(9);
        unit1.setY(9);
        GameController.setSelectedUnit(unit1);
        unitController.lootTile();
        assertEquals(tiles[9][9].getImprovement(),null);
        GameController.setSelectedUnit(unit);
        unitController.healTile();
        for(int i=0;i<3;i++){
            for (Unit currentPlayerUnit : currentPlayer.getUnits()) {
                currentPlayerUnit.setState("no");
            }
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
        CityController cityController= civilizationController.getCityController();
        UnitController unitController=civilizationController.getUnitController();
        WorkerUnit unit=new WorkerUnit(2,2);
        tiles[2][2].setTerrain(new Terrain("Hill" , 0, 0, 2, 2, 25,35));
        tiles[2][2].setFeature(new Feature("Forest", 0, 1 ,1, 2, 25,10));
        tiles[3][3].setTerrain(new Terrain("Hill" , 0, 0, 2, 2, 25,35));
        tiles[3][3].setFeature(new Feature("Forest", 0, 1 ,1, 2, 25,10));
        tiles[4][4].setTerrain(new Terrain("Hill" , 0, 0, 2, 2, 25,35));
        tiles[4][4].setFeature(new Feature("Forest", 0, 1 ,1, 2, 25,10));
        user.addUnit(unit);
        for (Unit currentPlayerUnit : currentPlayer.getUnits()) {
            currentPlayerUnit.setState("no");
        }
        GameController.setSelectedUnit(unit);
        unitController.moveSelectedUnit(4,4);
        int size=unit.getMoves().size();
        for(int i=0;i<=size;i++){
            unitController.isTurnPossible();
        }
        assertEquals(unit.getX(),4);
    }

    @Test
    public void testAttackUnit(){
        players=new ArrayList<>();
        User user=new User("omid","123","omid123");
        players.add(user);
        User user1=new User("omid1","123","omid1231");
        players.add(user1);
        CivilizationController civilizationController=new CivilizationController(players);
        CityController cityController= civilizationController.getCityController();
        UnitController unitController=civilizationController.getUnitController();
        tiles[9][9].setTerrain(new Terrain("Hill" , 0, 0, 2, 2, 25,35));
        tiles[9][9].setFeature(new Feature("Forest", 0, 1 ,1, 2, 25,10));
        tiles[8][9].setTerrain(new Terrain("Grassland" , 0, 2, 0, 1, -33,40));
        tiles[8][9].setFeature(null);
        tiles[9][9].getRivers().clear();
        tiles[8][9].getRivers().clear();
        MilitaryUnit unit=new MilitaryUnit("Warrior", 40, "Melee", 2, 6, -1, -1, null, null);
        unit.setX(9);
        unit.setY(9);
        MilitaryUnit unit1=new MilitaryUnit("Archer", 70, "Archery", 2, 4, 6, 2, "Archery", null);
        unit1.setX(8);
        unit1.setY(9);
        currentPlayer.getUnits().add(unit);
        user1.getUnits().add(unit1);
        assertTrue(unitController.isZoneOfControl(9,9));
        unit1.setState("fortify");
        for (Unit currentPlayerUnit : currentPlayer.getUnits()) {
            currentPlayerUnit.setState("no");
        }
        unitController.selectUnit(9,9,true);
        assertEquals(unitController.reachableUnits().get(0),unit1);
        String output=unitController.attackUnit(unit1);
        assertEquals(output,"you have attacked the unit, but you and the unit are still alive!");
        assertEquals(unit.getHealth(),8);
        for(int i=0;i<2;i++){
            unit.setRemainingMoves(5);
            unitController.attackUnit(unit1);
        }
        unitController.deleteSelectedUnit();
    }

    @Test
    public void testAttackCity(){
        players=new ArrayList<>();
        User user=new User("omid","123","omid123");
        players.add(user);
        User user1=new User("omid1","123","omid1231");
        players.add(user1);
        CivilizationController civilizationController=new CivilizationController(players);
        CityController cityController= civilizationController.getCityController();
        UnitController unitController=civilizationController.getUnitController();
        tiles[9][9].setTerrain(new Terrain("Hill" , 0, 0, 2, 2, 25,35));
        tiles[9][9].setFeature(new Feature("Forest", 0, 1 ,1, 2, 25,10));
        MilitaryUnit unit=new MilitaryUnit("Warrior", 40, "Melee", 2, 6, -1, -1, null, null);
        unit.setX(9);
        unit.setY(9);
        currentPlayer=user1;
        cityController.createCity(8,7);
        currentPlayer=user;
        currentPlayer.getUnits().add(unit);
        for (Unit currentPlayerUnit : currentPlayer.getUnits()) {
            currentPlayerUnit.setState("no");
        }
        GameController.setSelectedUnit(unit);
        assertEquals(unitController.reachableCities().get(0),cityController.getCityAtCoordinate(8,7));
        String output=unitController.attackCity(cityController.getCityAtCoordinate(8,7));
        assertEquals(output,"you attacked the city, but you are alive and the city is still ok!");
        assertEquals(cityController.getCityAtCoordinate(8,7).getHealth(),15);
        for(int i=0;i<1;i++){
            unit.setRemainingMoves(5);
            unitController.attackCity(cityController.getCityAtCoordinate(8,7));
        }
    }

    @Test
    public void testUnitActions(){
        players=new ArrayList<>();
        User user=new User("omid","123","omid123");
        players.add(user);
        CivilizationController civilizationController=new CivilizationController(players);
        CityController cityController= civilizationController.getCityController();
        UnitController unitController=civilizationController.getUnitController();
        MilitaryUnit unit=new MilitaryUnit("Catapult", 100, "Siege", 2, 4, 14, 2, "Mathematics", "Iron");
        unit.setX(9);
        unit.setY(9);
        currentPlayer=user;
        currentPlayer.getUnits().add(unit);
        unitController.selectUnit(9,9,true);
        unitController.alert();
        assertEquals(unit.getState(),"alert");
        unitController.isTurnPossible();
        unitController.sleep();
        assertEquals(unit.getState(),"sleep");
        unitController.wake();
        assertEquals(unit.getState(),"ready");
        unitController.fortify();
        assertEquals(unit.getState(),"fortify");
        SettlerUnit unit1=new SettlerUnit(9,9);
        currentPlayer.getUnits().add(unit1);
        unitController.selectUnit(9,9,false);
        unitController.foundCity();
        unitController.selectUnit(9,9,true);
        unitController.garrison();
        assertEquals(unit.getState(),"garrison");
        unitController.rangeSetup();
        assertEquals(unit.getState(),"range setup");
    }

    @Test
    public void testTwoUnitsInOneTile(){
        players=new ArrayList<>();
        User user=new User("omid","123","omid123");
        players.add(user);
        CivilizationController civilizationController=new CivilizationController(players);
        CityController cityController= civilizationController.getCityController();
        UnitController unitController=civilizationController.getUnitController();
        MilitaryUnit unit=new MilitaryUnit("Warrior", 40, "Melee", 2, 6, -1, -1, null, null);
        unit.setX(9);
        unit.setY(9);
        MilitaryUnit unit1=new MilitaryUnit("Archer", 70, "Archery", 2, 4, 6, 2, "Archery", null);
        unit1.setX(9);
        unit1.setY(9);
        user.getUnits().add(unit);
        user.getUnits().add(unit1);
        assertTrue(unitController.hasTwoUnitsInSameTile(tiles[9][9]));
        user.getUnits().remove(unit1);
        WorkerUnit unit2=new WorkerUnit(9,9);
        user.getUnits().add(unit2);
        WorkerUnit unit3=new WorkerUnit(9,9);
        user.getUnits().add(unit3);
        assertTrue(unitController.hasTwoUnitsInSameTile(tiles[9][9]));
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
