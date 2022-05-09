package view;


import controller.CityController;
import controller.CivilizationController;
import controller.GameController;
import controller.UnitController;
import database.ImprovementDatabase;
import database.ResourceDatabase;
import database.TerrainDatabase;
import database.UnitsDatabase;
import enums.Commands;
import model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;

public class GameView {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";
    private static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    private static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    private static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    private static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    private static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    private static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    private static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    private static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    private CivilizationController civilizationController;

    private UnitController unitController;
    private CityController cityController;

    public GameView(ArrayList<String> usernames){
        System.out.println(usernames);

        ArrayList<User> players = new ArrayList<>();
        ArrayList<User> allUsers = User.getUsers();
        for(String username : usernames){
            for(User user : allUsers){
                if(user.getUsername().equals(username)){
                    players.add(user);
                    break;
                }
            }
        }
        civilizationController = new CivilizationController(players);
        unitController = civilizationController.getUnitController();
        cityController = civilizationController.getCityController();

    }
    public void run(Scanner scanner){
        String input;
        while (true){
            input = scanner.nextLine();
            Matcher matcher;
            if (input.equals("show map"))drawMap();
            else if((matcher = Commands.getCommandMatcher(input, Commands.SELECT_COMBAT_UNIT)) != null)
                System.out.println(unitController.selectUnit(Integer.parseInt(matcher.group("x")),Integer.parseInt(matcher.group("y")), true));
            else if((matcher = Commands.getCommandMatcher(input, Commands.SELECT_NONCOMBAT_UNIT)) != null)
                System.out.println(unitController.selectUnit(Integer.parseInt(matcher.group("x")),Integer.parseInt(matcher.group("y")), false));
            else if((matcher = Commands.getCommandMatcher(input, Commands.SELECT_CITY)) != null)
                System.out.println(cityController.selectCity(Integer.parseInt(matcher.group("x")),Integer.parseInt(matcher.group("y"))));
            else if((matcher = Commands.getCommandMatcher(input, Commands.MOVE_UNIT)) != null)
                System.out.println(unitController.moveSelectedUnit(Integer.parseInt(matcher.group("x")),Integer.parseInt(matcher.group("y"))));
            else if((matcher = Commands.getCommandMatcher(input, Commands.ADD_CITIZEN_TO_TILE)) != null)
                System.out.println(cityController.putCitizenToWork(Integer.parseInt(matcher.group("x")),Integer.parseInt(matcher.group("y"))));
            else if((matcher = Commands.getCommandMatcher(input, Commands.REMOVE_CITIZEN_FROM_TILE)) != null)
                System.out.println(cityController.removeCitizen(Integer.parseInt(matcher.group("x")),Integer.parseInt(matcher.group("y"))));
            else if((matcher = Commands.getCommandMatcher(input, Commands.UNIT_BUILD)) != null)
                unitBuild(matcher.group("name"));
            else if((matcher = Commands.getCommandMatcher(input, Commands.UNIT_REMOVE)) != null)
                unitRemove(matcher.group("name"));
            else if((matcher = Commands.getCommandMatcher(input, Commands.CHEAT_GOLD)) != null)
                civilizationController.getCurrentPlayer().setGold(civilizationController.getCurrentPlayer().getGold()+Integer.parseInt(matcher.group("amount")));
            else if((matcher = Commands.getCommandMatcher(input, Commands.CHEAT_TURN)) != null){
                for(int i=0;i<Integer.parseInt(matcher.group("amount"));i++){
                    civilizationController.nextTurn();
                }
            }
            else if((matcher = Commands.getCommandMatcher(input, Commands.CHEAT_HAPPINESS)) != null)
                civilizationController.getCurrentPlayer().setHappiness(civilizationController.getCurrentPlayer().getHappiness()+Integer.parseInt(matcher.group("amount")));
            else if((matcher = Commands.getCommandMatcher(input, Commands.CHEAT_UNIT_FULL_HEALTH)) != null){
                for (Unit unit : civilizationController.getCurrentPlayer().getUnits()) {
                    unit.setHealth(10);
                }
            }
            else if((matcher = Commands.getCommandMatcher(input, Commands.CHEAT_CITY_FULL_HEALTH)) != null){
                for (City city : civilizationController.getCurrentPlayer().getCities()) {
                    city.setHealth(20);
                }
            }
            else if((matcher = Commands.getCommandMatcher(input, Commands.CHEAT_INCREASE_CITIZEN)) != null){
                for (City city : civilizationController.getCurrentPlayer().getCities()) {
                   if(city.getId()==Integer.parseInt(matcher.group("id"))){
                       city.setCountOfCitizens(city.getCountOfCitizens()+Integer.parseInt(matcher.group("amount")));
                       break;
                   }
                }
            }
            else if((matcher = Commands.getCommandMatcher(input, Commands.CHEAT_BUILD_ROAD)) != null)
                civilizationController.getTiles()[Integer.parseInt(matcher.group("x"))][Integer.parseInt(matcher.group("y"))].setRoad(true);
            else if((matcher = Commands.getCommandMatcher(input, Commands.CHEAT_INCREASE_MOVEMENT)) != null){
                for (Unit unit : civilizationController.getCurrentPlayer().getUnits()) {
                    unit.setRemainingMoves(unit.getRemainingMoves()+Integer.parseInt(matcher.group("amount")));
                }
            }
            else if((matcher = Commands.getCommandMatcher(input, Commands.CHEAT_CREATE_CITY)) != null)
                cityController.createCity(Integer.parseInt(matcher.group("x")),Integer.parseInt(matcher.group("y")));
            else if((matcher = Commands.getCommandMatcher(input, Commands.CHEAT_FINISH_STUDY)) != null){
                civilizationController.getCurrentPlayer().addTechnology(civilizationController.getCurrentPlayer().getCurrentStudy());
                civilizationController.getCurrentPlayer().getWaitedTechnologies().remove(civilizationController.getCurrentPlayer().getCurrentStudy().getName());
                civilizationController.getCurrentPlayer().setCurrentStudy(null);
            }
            else if((matcher = Commands.getCommandMatcher(input, Commands.CHEAT_BUILD_UNIT)) != null){
                for (City city : civilizationController.getCurrentPlayer().getCities()) {
                    if(city.getId()==Integer.parseInt(matcher.group("id"))){
                        cityController.createUnit(matcher.group("name"),city);
                    }
                }
            }
            else if(input.equals("unit repair tile")) System.out.println(unitController.healTile());
            else if(input.equals("unit loot tile")) System.out.println(unitController.lootTile());
            else if(input.equals("unit found city")) System.out.println(unitController.foundCity());
            else if(input.equals("unit delete")) System.out.println(unitController.deleteSelectedUnit());
            else if(input.equals("unit sleep")) System.out.println(unitController.sleep());
            else if(input.equals("unit wake")) System.out.println(unitController.wake());
            else if(input.equals("unit fortify")) System.out.println(unitController.fortify());
            else if(input.equals("unit alert")) System.out.println(unitController.alert());
            else if(input.equals("unit garrison")) System.out.println(unitController.garrison());
            else if(input.equals("unit range attack setup")) System.out.println(unitController.rangeSetup());
            else if(input.equals("unit cancel action")) System.out.println(unitController.cancelActions());
            else if(input.equals("next turn")) System.out.println(civilizationController.nextTurn());
            else if(input.equals("menu show-current")) System.out.println("Game Menu");
            else if(input.equals("attack unit by unit")) attackUnit(scanner);
            else if(input.equals("attack city")) attackCity(scanner);
            else if(input.equals("attack unit by city")) attackUnitFromCity(scanner);
            else if(input.equals("technology menu")) chooseTechnologyMenu(civilizationController.getCurrentPlayer(),scanner);
            else if(input.equals("production menu")) chooseProductionMenu(civilizationController.getCurrentPlayer(),scanner);
            else if(input.equals("show units panel")) showUnitsInfo(civilizationController.showUnitsInfo());
            else if(input.equals("show military overview")) militaryOverview(civilizationController.showUnitsInfo());
            else if(input.equals("show cities panel")) showCitiesInfo(civilizationController.showCitiesInfo());
            else if(input.equals("show research panel")) showCurrentStudyInfo(civilizationController.showCurrentStudy());
            else if(input.equals("show demographic panel"))demographicPanel();
            else if(input.equals("purchase tile"))purchaseTile(scanner);
            else if(input.equals("menu exit")){
                System.out.println("exited game");
                break;
            }
            else System.out.println("invalid command");
        }
    }


    private void drawMap(){
        String[][] printableMap = new String[100][100];
        Tile[][] tiles = civilizationController.getTiles();
        int width = civilizationController.getMapWidth();
        int height = civilizationController.getMapHeight();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                ArrayList<String> infos = new ArrayList<>();
                City city = cityController.getCityAtCoordinate(i, j);
                if(city == null)infos.add("nl");
                else {
                    if(city.getCapital().getX() != i || city.getCapital().getY() != j)
                        infos.add("bg:" + cityController.getCityAtCoordinate(i, j).getId());
                    else infos.add("cp:" + cityController.getCityAtCoordinate(i, j).getId());
                }
                StringBuilder terrainFeatureName = new StringBuilder(tiles[i][j].getTerrain().getName().substring(0,3) + "-");
                if(tiles[i][j].getFeature() != null) terrainFeatureName.append(tiles[i][j].getFeature().getName().substring(0,3));
                infos.add(String.valueOf(terrainFeatureName));
                if(tiles[i][j].getResource() != null){
                    if(tiles[i][j].getResource().getNeededTechnology() == null)infos.add(tiles[i][j].getResource().getName());
                    else {
                        if(civilizationController.getCurrentPlayer().hasTechnology(tiles[i][j].getResource().getNeededTechnology()))
                            infos.add(tiles[i][j].getResource().getName());
                    }
                }

                StringBuilder unitsName = new StringBuilder("");
                if(unitController.getTileNonCombatUnit(i, j) != null)
                    unitsName.append(unitController.getTileNonCombatUnit(i, j).getName(), 0, 3).append("-");
                if(unitController.getTileCombatUnit(i, j) != null)unitsName.append(unitController.getTileCombatUnit(i, j).getName(), 0, 3);
                infos.add(String.valueOf(unitsName));

                String background = ANSI_CYAN_BACKGROUND;
                if(tiles[i][j].getVisibilityForUser(civilizationController.getTurn()).equals("visible"))background = ANSI_YELLOW_BACKGROUND;
                else if(tiles[i][j].getVisibilityForUser(civilizationController.getTurn()).equals("revealed")){
                    background = ANSI_BLUE_BACKGROUND;
                    infos = tiles[i][j].getOldInfoForUser(civilizationController.getTurn());
                }
                else infos.clear();

                addHexagonal(printableMap, i, j, background, tiles[i][j].getRivers(),infos);
            }
        }
        printMap(printableMap);
    }
    private void addHexagonal(String[][] printableMap, int x, int y, String backgroundColor, ArrayList<Integer> riverDirections, ArrayList<String> infos){
        int mapY, mapX;
        if(y%2 == 0) {
            mapY = (y  / 2) * 11 + (y / 2) * 5 + 3;
            mapX = x * 6;
        }
        else {
            mapY = ((y + 1) / 2) * 11 + (y / 2) * 5;
            mapX = x * 6 + 3;
        }
        String coordinates = x + "," + y;
        for (int i = 0; i < 3; i++) {
            printableMap[mapX + i][mapY - i] = "/";
            if(riverDirections.contains(6)) printableMap[mapX + i][mapY - i] = ANSI_BLUE_BACKGROUND + "/" + ANSI_RESET;
            for (int j = 1; j <= 5 + 2 * i; j++) {
                String c = " ";
                if (i != 2) {
                    if (!infos.isEmpty() && infos.get(0).length() >= j)
                        c = String.valueOf(infos.get(0).charAt(j - 1));
                }
                else if (coordinates.length() >= j) c = String.valueOf(coordinates.charAt(j - 1));

                printableMap[mapX + i][mapY - i + j] = backgroundColor + c + ANSI_RESET;
            }
            printableMap[mapX + i][mapY - i + 6 + 2 * i] = "\\";
            if(riverDirections.contains(2)) printableMap[mapX + i][mapY - i + 6 + 2 * i] = ANSI_BLUE_BACKGROUND + "\\" + ANSI_RESET;
            if(i != 2 && !infos.isEmpty())infos.remove(0);
        }
        for (int i = 0; i < 3; i++) {
            printableMap[mapX + 3 + i][mapY - 2 + i] = "\\";
            if(riverDirections.contains(5)) printableMap[mapX + 3 + i][mapY - 2 + i] = ANSI_BLUE_BACKGROUND + "\\" + ANSI_RESET;
            for (int j = 1; j <= 5 + 2 * (2 - i); j++) {
                String c = " ";
                if (infos.size() > 0 && infos.get(0).length() >= j)
                    c = String.valueOf(infos.get(0).charAt(j - 1));
                if(i == 2) {
                    if(riverDirections.contains(4))
                        printableMap[mapX + 3 + i][mapY - 2 + i + j] = ANSI_BLUE_BACKGROUND + "_" + ANSI_RESET;
                    else printableMap[mapX + 3 + i][mapY - 2 + i + j] = backgroundColor + "_" + ANSI_RESET;
                }
                else
                    printableMap[mapX + 3 + i][mapY - 2 + i + j] = backgroundColor + c + ANSI_RESET;
            }
            printableMap[mapX + 3 + i][mapY - 2 + i + 6 + 2 * (2 - i)] = "/";
            if(riverDirections.contains(3)) printableMap[mapX + 3 + i][mapY - 2 + i + 6 + 2 * (2 - i)] = ANSI_BLUE_BACKGROUND + "/" + ANSI_RESET;
            if(i != 2 && !infos.isEmpty())infos.remove(0);
        }

    }
    private void printMap(String[][] printableMap){
        for (int i = 0; i < 65; i++) {
            for (int j = 0; j < 100; j++) {
                if (printableMap[i][j] != null) System.out.print(printableMap[i][j]);
                else System.out.print(" ");
            }
            System.out.print("\n");
        }
    }

    private void showUnitsInfo(ArrayList<Unit> units){
        System.out.println("number of units : " + units.size());
        for(int i=0;i<units.size();i++){
            System.out.println((i+1)+"- name: "+units.get(i).getName()+" current tile: ("+units.get(i).getX()+","+units.get(i).getY()+") remaining move: "+units.get(i).getRemainingMoves()+" health: "+units.get(i).getHealth());
        }
    }
    private void militaryOverview(ArrayList<Unit> units){
        int id = 1;
        for(Unit unit : units){
            if(!(unit instanceof MilitaryUnit))continue;
            MilitaryUnit militaryUnit = (MilitaryUnit)unit;
            System.out.println((id)+"- name: "+militaryUnit.getName()+" current tile: ("+militaryUnit.getX()+","+militaryUnit.getY()+") remaining move: "+ militaryUnit.getRemainingMoves() + " combat type : " + militaryUnit.getCombatType() + " health : " + militaryUnit.getHealth());
            id++;
        }
        if(id == 1) System.out.println("you have no military units");

    }

    private void showCitiesInfo(ArrayList<City> cities){
        for(City city : cities){
            System.out.println("city id : " + city.getId() + " - capital: (" + city.getCapital().getX() + "," + city.getCapital().getY() + ") number of citizens: "+city.getCountOfCitizens()+" number of tiles: "+city.getTiles().size()+" remaining health: "+city.getHealth());
            System.out.println("city output for each turn - gold : " + city.gold() + " - food : " + city.totalFood() + " - production : " + city.production());
            System.out.println("number of unemployed citizens : " + (city.getCountOfCitizens() - city.getTilesWithCitizen().size()));
            System.out.println("tile coordinates : ");
            for(Tile tile : city.getTiles()){
                System.out.print("(" + tile.getX() + " , " + tile.getY() + ") - ");
            }
            System.out.println();
            System.out.println("tiles with citizen : ");
            for(Tile tile : city.getTilesWithCitizen()){
                System.out.print("(" + tile.getX() + " , " + tile.getY() + ") - ");
            }
            System.out.println();
            System.out.println("-------------------------------------------------------------------");
        }
    }

    private void demographicPanel(){
        int population = 0;
        int tileCount = 0;
        for(City city : civilizationController.showCitiesInfo()){
            population += city.getCountOfCitizens();
            tileCount += city.getTiles().size();
        }
        int happiness = civilizationController.getCurrentPlayer().getHappiness();
        int countOfMilitaryUnits = 0;
        for(Unit unit : civilizationController.showUnitsInfo()){
            if(unit instanceof MilitaryUnit) countOfMilitaryUnits++;
        }
        System.out.println("population : " + population + " tile count : " + tileCount + " happiness : " + happiness + " gold : " + civilizationController.getCurrentPlayer().getGold() + " count of military units : " + countOfMilitaryUnits);
    }
    private void purchaseTile(Scanner scanner){
        if(GameController.getSelectedCity() == null){
            System.out.println("no city selected");
            return;
        }
        ArrayList<Tile> possibleTiles = cityController.possibleTilesForPurchase(GameController.getSelectedCity());
        int id = 1;
        System.out.println("purchasable tiles : ");
        for(Tile tile : possibleTiles){
            System.out.println(id + "- ("  + tile.getX() + " , " + tile.getY() + ") price : " +tile.getPrice());
            id++;
        }
        String tileNumber = scanner.nextLine();
        if(tileNumber.equals("exit"))return;
        if((Commands.getCommandMatcher(tileNumber,Commands.WHICH_NUMBER))==null) {
            System.out.println("invalid command");
            return;
        }
        if(Integer.parseInt(tileNumber) > possibleTiles.size() || Integer.parseInt(tileNumber) < 1){
            System.out.println("invalid number");
        }
        System.out.println(cityController.purchaseTile(possibleTiles.get(Integer.parseInt(tileNumber) - 1)));
    }

    private void showCurrentStudyInfo(Technology technology) {
        if (technology != null) {
            int turnsLeft;
            if (civilizationController.getCurrentPlayer().getWaitedTechnologies().get(technology.getName()) % civilizationController.getCurrentPlayer().totalCup() == 0) {
                turnsLeft =  civilizationController.getCurrentPlayer().getWaitedTechnologies().get(technology.getName()) / civilizationController.getCurrentPlayer().totalCup();
            } else {
                turnsLeft = civilizationController.getCurrentPlayer().getWaitedTechnologies().get(technology.getName()) / civilizationController.getCurrentPlayer().totalCup() + 1;
            }
            System.out.println("you are currently studying " + technology.getName() + " and there is " + turnsLeft + " turns left to unlock");
            System.out.println("resources that need this technology to be discovered:");
            for (Resource resource : ResourceDatabase.getResources()) {
                if(resource.getNeededTechnology() == null)continue;
                if (resource.getNeededTechnology().equals(technology.getName())) {
                    System.out.println(resource.getName());
                }
            }
            System.out.println("units that need this technology to be build:");
            for (Unit unit : UnitsDatabase.getUnits()) {
                if(unit.getNeededTechnology() == null)continue;
                if (unit.getNeededTechnology().equals(technology.getName())) {
                    System.out.println(unit.getName());
                }
            }
            System.out.println("improvements that need this technology:");
            for (Improvement improvement : ImprovementDatabase.getImprovements()) {
                if(improvement.getNeededTechnology() == null)continue;
                if (improvement.getNeededTechnology().equals(technology.getName())) {
                    System.out.println(improvement.getName());
                }
            }
        }else {
            System.out.println("no current Studying");
        }
    }

    private void chooseTechnologyMenu(User user,Scanner scanner){
        if(civilizationController.getCurrentPlayer().getCities().isEmpty()){
            System.out.println("you need to build a city first");
            return;
        }
        System.out.println("you have earned these technologies:");
        for (Technology technology : user.getTechnologies()) {
            System.out.println(technology.getName());
        }
        System.out.println("you can choose one of these technology to study:");
        ArrayList<Technology> readyTechnologies=user.readyTechnologies();
        for(int i=0;i<readyTechnologies.size();i++){
            int turnsLeft;
            if(civilizationController.getCurrentPlayer().totalCup()==0){
                turnsLeft=-1;
            }else {
                if (readyTechnologies.get(i).getCost() % civilizationController.getCurrentPlayer().totalCup() == 0) {
                    turnsLeft = readyTechnologies.get(i).getCost() / civilizationController.getCurrentPlayer().totalCup();
                } else {
                    turnsLeft = readyTechnologies.get(i).getCost() / civilizationController.getCurrentPlayer().totalCup() + 1;
                }
            }
            System.out.println((i+1)+"- "+readyTechnologies.get(i).getName()+", it needs "+turnsLeft+" turns to unlock");
        }
        String whichTechnology=scanner.nextLine();
        if(!whichTechnology.equals("exit")){
            if((Commands.getCommandMatcher(whichTechnology,Commands.WHICH_NUMBER))==null) {
                System.out.println("invalid command");
                return;
            }
            if(Integer.parseInt(whichTechnology) > readyTechnologies.size() || Integer.parseInt(whichTechnology) < 1){
                System.out.println("invalid number");
            }
            civilizationController.studyTechnology(readyTechnologies.get(Integer.parseInt(whichTechnology)-1));
            System.out.println("technology will be studied!");
        }
    }

    private void chooseProductionMenu(User user,Scanner scanner){
        if(GameController.getSelectedCity() == null){
            System.out.println("no city selected");
            return;
        }
        System.out.println("you can choose one of these units to build:");
        ArrayList<Unit> units = unitController.getConstructableUnits();
        for(int i=0;i<units.size();i++){
            int turnsLeft;
            if(GameController.getSelectedCity().production()==0){
                turnsLeft=-1;
            }else {
                if (units.get(i).getCost() % GameController.getSelectedCity().production() == 0) {
                    turnsLeft =  units.get(i).getCost() / GameController.getSelectedCity().production();
                } else {
                    turnsLeft = units.get(i).getCost() / GameController.getSelectedCity().production() + 1;
                }
            }
            System.out.println((i+1)+"- "+units.get(i).getName()+", it needs "+turnsLeft+" turns to be built");
        }
        String whichProduction=scanner.nextLine();
        if(!whichProduction.equals("exit")){
            if((Commands.getCommandMatcher(whichProduction,Commands.WHICH_NUMBER))==null) {
                System.out.println("invalid command");
                return;
            }
            if(Integer.parseInt(whichProduction) > units.size() || Integer.parseInt(whichProduction) < 1){
                System.out.println("invalid number");
                return;
            }
            System.out.println("do you want to purchase this unit with gold");
            String answer = scanner.nextLine();
            if(answer.equals("no")) System.out.println(cityController.constructUnit(units.get(Integer.parseInt(whichProduction)-1).getName()));
            else if(answer.equals("yes")) System.out.println(cityController.purchaseUnitWithGold(units.get(Integer.parseInt(whichProduction)-1).getName()));
        }
    }
    public void unitBuild(String name){
        if(unitController.getSelectedUnit()==null) System.out.println("you need to choose a worker unit first!");
        if(!(unitController.getSelectedUnit() instanceof WorkerUnit)) System.out.println("this unit is not a worker unit");
        if (name.equals("Road")) System.out.println(unitController.buildRoad());
        else{
            for(Improvement improvement : ImprovementDatabase.getImprovements()){
                if(improvement.getName().equals(name)){
                    System.out.println(unitController.improveTile(improvement));
                    return;
                }
            }
            System.out.println("there is no such improvement");
        }
    }

    public void unitRemove(String name){
        if (name.equals("Road")) System.out.println(unitController.eliminateRoad());
        else System.out.println(unitController.eliminateFeature());
    }

    public void attackUnit(Scanner scanner){
        if(!unitController.checkSelectedUnit().equals("ok")){
            System.out.println(unitController.checkSelectedUnit());
        }
        else {
            if (!(unitController.getSelectedUnit() instanceof MilitaryUnit)) {
                System.out.println("this unit can't attack another unit");
            }else {
                if(unitController.getSelectedUnit().getRemainingMoves()==0){
                    System.out.println("this unit can't attack now!");
                }else {
                    ArrayList<Unit> reachableUnits = unitController.reachableUnits();
                    if (reachableUnits.size() > 0) {
                        System.out.println("units that you can reach to attack:");
                        for (int i = 0; i < reachableUnits.size(); i++) {
                            System.out.println((i + 1) + "- name: " + reachableUnits.get(i).getName() + " location: (" + reachableUnits.get(i).getX() + "," + reachableUnits.get(i).getY() + ") health: "+reachableUnits.get(i).getHealth());
                        }
                        String whichUnit = scanner.nextLine();
                        if (!whichUnit.equals("exit")) {
                            if((Commands.getCommandMatcher(whichUnit,Commands.WHICH_NUMBER))==null) {
                                System.out.println("invalid command");
                                return;
                            }
                            if(Integer.parseInt(whichUnit) > reachableUnits.size() || Integer.parseInt(whichUnit) < 1){
                                System.out.println("invalid number");
                                return;
                            }
                            System.out.println(unitController.attackUnit(reachableUnits.get(Integer.parseInt(whichUnit) - 1)));
                        }
                    } else {
                        System.out.println("you can't reach any unit to attack");
                    }
                }
            }
        }
    }

    public void attackCity(Scanner scanner){
        if(!unitController.checkSelectedUnit().equals("ok")){
            System.out.println(unitController.checkSelectedUnit());
        }else {
            if (!(unitController.getSelectedUnit() instanceof MilitaryUnit)) {
                System.out.println("this unit can't attack another unit");
            }else {
                if (unitController.getSelectedUnit().getRemainingMoves() == 0) {
                    System.out.println("this unit can't attack now!");
                }else {
                    ArrayList<City> reachableCities = unitController.reachableCities();
                    if (reachableCities.size() > 0) {
                        System.out.println("cities that you can reach to attack:");
                        for (int i = 0; i < reachableCities.size(); i++) {
                            System.out.println((i + 1) + "- id: "+reachableCities.get(i).getId()+" health:" + reachableCities.get(i).getHealth());
                        }
                        String whichCity = scanner.nextLine();
                        if (!whichCity.equals("exit")) {
                            if((Commands.getCommandMatcher(whichCity,Commands.WHICH_NUMBER))==null) {
                                System.out.println("invalid command");
                                return;
                            }
                            if(Integer.parseInt(whichCity) > reachableCities.size() || Integer.parseInt(whichCity) < 1){
                                System.out.println("invalid number");
                                return;
                            }
                            String output = unitController.attackCity(reachableCities.get(Integer.parseInt(whichCity) - 1));
                            if (!output.equals("dominated")) {
                                System.out.println(output);
                            } else {
                                System.out.println("you dominated this city, do you want to eliminate it or annex it?");
                                String which = scanner.nextLine();
                                if (which.equals("eliminate")) {
                                    cityController.getCityOwner(reachableCities.get(Integer.parseInt(whichCity) - 1)).getCities().remove(reachableCities.get(Integer.parseInt(whichCity) - 1));
                                    System.out.println("city eliminated!");
                                }
                                if (which.equals("annex")) {
                                    cityController.getCityOwner(reachableCities.get(Integer.parseInt(whichCity) - 1)).getCities().remove(reachableCities.get(Integer.parseInt(whichCity) - 1));
                                    reachableCities.get(Integer.parseInt(whichCity) - 1).setHealth(20);
                                    civilizationController.getCurrentPlayer().setHappiness(civilizationController.getCurrentPlayer().getHappiness() - 5);
                                    civilizationController.getCurrentPlayer().getCities().add(reachableCities.get(Integer.parseInt(whichCity) - 1));
                                    System.out.println("you annexed this city to your cities!");
                                }
                            }
                        }
                    } else {
                        System.out.println("you have move closer to a city to attack!");
                    }
                }
            }
        }
    }

    public void attackUnitFromCity(Scanner scanner){
        if(GameController.getSelectedCity()==null) System.out.println("select a city first!");
        else {
            ArrayList<Unit> reachableUnits = cityController.reachableUnits();
            if (reachableUnits.size() > 0) {
                System.out.println("units that you can reach to attack:");
                for (int i = 0; i < reachableUnits.size(); i++) {
                    System.out.println((i + 1) + "- name: " + reachableUnits.get(i).getName() + " location: (" + reachableUnits.get(i).getX() + "," + reachableUnits.get(i).getY() + ") health: "+reachableUnits.get(i).getHealth());
                }
                String whichUnit = scanner.nextLine();
                if (!whichUnit.equals("exit")) {
                    if((Commands.getCommandMatcher(whichUnit,Commands.WHICH_NUMBER))==null) {
                        System.out.println("invalid command");
                        return;
                    }
                    if(Integer.parseInt(whichUnit) > reachableUnits.size() || Integer.parseInt(whichUnit) < 1){
                        System.out.println("invalid number");
                        return;
                    }
                    System.out.println(cityController.attackUnit(reachableUnits.get(Integer.parseInt(whichUnit) - 1)));
                }
            } else {
                System.out.println("there is no units in your range!");
            }
        }
    }
}
