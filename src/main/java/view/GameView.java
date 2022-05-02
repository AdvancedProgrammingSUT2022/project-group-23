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
            else if((matcher = Commands.getCommandMatcher(input, Commands.UNIT_BUILD)) != null)
                unitBuild(matcher.group("name"));
            else if((matcher = Commands.getCommandMatcher(input, Commands.UNIT_REMOVE)) != null)
                unitRemove(matcher.group("name"));
            else if(input.equals("unit repair")) System.out.println(unitController.healTile());
            else if(input.equals("unit found city")) System.out.println(unitController.foundCity());
            else if(input.equals("next turn")) System.out.println(civilizationController.nextTurn());
            else if(input.equals("menu show-current")) System.out.println("Game Menu");
            else if(input.equals("attack unit")) attackUnit(scanner);
            else if(input.equals("technology menu")) chooseTechnologyMenu(civilizationController.getCurrentPlayer(),scanner);
            else if(input.equals("show units info")) showUnitsInfo(civilizationController.showUnitsInfo());
            else if(input.equals("show cities info")) showCitiesInfo(civilizationController.showCitiesInfo());
            else if(input.equals("show research info")) showCurrentStudyInfo(civilizationController.showCurrentStudy());
            else if(input.equals("menu exit"))break;
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
                if(tiles[i][j].getTerrain() != null)infos.add(tiles[i][j].getTerrain().getName());
                if(tiles[i][j].getFeature() != null)infos.add(tiles[i][j].getFeature().getName());
                if(tiles[i][j].getResource() != null){
                    if(tiles[i][j].getResource().getNeededTechnology() == null)infos.add(tiles[i][j].getResource().getName());
                    else {
                        if(civilizationController.getCurrentPlayer().hasTechnology(tiles[i][j].getResource().getNeededTechnology()))
                            infos.add(tiles[i][j].getResource().getName());
                    }
                }
                if(unitController.getTileNonCombatUnit(i, j) != null)infos.add(unitController.getTileNonCombatUnit(i, j).getName());
                if(unitController.getTileCombatUnit(i, j) != null)infos.add(unitController.getTileCombatUnit(i, j).getName());

                String background = ANSI_CYAN_BACKGROUND;
                if(tiles[i][j].getVisibilityForUser(civilizationController.getTurn()).equals("visible"))background = ANSI_YELLOW_BACKGROUND;
                else if(tiles[i][j].getVisibilityForUser(civilizationController.getTurn()).equals("revealed"))background = ANSI_BLUE_BACKGROUND;
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
        for(int i=0;i<units.size();i++){
            System.out.println((i+1)+"- name: "+units.get(i).getName()+" current tile: ("+units.get(i).getX()+","+units.get(i).getY()+") remaining move: "+units.get(i).getRemainingMoves());
        }
    }

    private void showCitiesInfo(ArrayList<City> cities){
        for(City city : cities){
            System.out.println("city id : " + city.getId() + " - capital: (" + city.getCapital().getX() + "," + city.getCapital().getY() + ") number of citizens: "+city.getCountOfCitizens()+" number of tiles: "+city.getTiles().size());
            System.out.println("city output for each turn - gold : " + city.gold() + " - food : " + city.totalFood() + " - production : " + city.production());
            System.out.println("tile coordinates : ");
            for(Tile tile : city.getTiles()){
                System.out.print("(" + tile.getX() + " , " + tile.getY() + ") - ");
            }
            System.out.println("tiles with citizen : ");
            for(Tile tile : city.getTilesWithCitizen()){
                System.out.print("(" + tile.getX() + " , " + tile.getY() + ") - ");
            }
            System.out.println("-------------------------------------------------------------------");
        }
    }

    private void showCurrentStudyInfo(Technology technology) {
        if (technology != null) {
            int turnsLeft;
            if (technology.getCost() % civilizationController.getCurrentPlayer().totalCup() == 0) {
                turnsLeft =  technology.getCost() / civilizationController.getCurrentPlayer().totalCup();
            } else {
                turnsLeft = technology.getCost() / civilizationController.getCurrentPlayer().totalCup() + 1;
            }
            System.out.println("you are currently studying " + technology.getName() + " and there is " + turnsLeft + " turns left to unlock");
            System.out.println("resources that need this technology to be discovered:");
            for (Resource resource : ResourceDatabase.getResources()) {
                if (resource.getNeededTechnology().equals(technology.getName())) {
                    System.out.println(resource.getName());
                }
            }
            System.out.println("units that need this technology to be build:");
            for (Unit unit : UnitsDatabase.getUnits()) {
                if (unit.getNeededTechnology().equals(technology.getName())) {
                    System.out.println(unit.getName());
                }
            }
            System.out.println("improvements that need this technology:");
            for (Improvement improvement : ImprovementDatabase.getImprovements()) {
                if (improvement.getNeededTechnology().equals(technology.getName())) {
                    System.out.println(improvement.getName());
                }
            }
        }else {
            System.out.println("no current Studying");
        }
    }

    private void chooseTechnologyMenu(User user,Scanner scanner){
        System.out.println("you have earned these technologies:");
        for (Technology technology : user.getTechnologies()) {
            System.out.println(technology.getName());
        }
        System.out.println("you can choose one of these technology to study:");
        ArrayList<Technology> readyTechnologies=user.readyTechnologies();
        for(int i=0;i<readyTechnologies.size();i++){
            int turnsLeft;
            if(civilizationController.getCurrentPlayer().totalCup() == 0)turnsLeft = -1;
            else if (readyTechnologies.get(i).getCost() % civilizationController.getCurrentPlayer().totalCup() == 0) {
                turnsLeft =  readyTechnologies.get(i).getCost() / civilizationController.getCurrentPlayer().totalCup();
            } else {
                turnsLeft = readyTechnologies.get(i).getCost() / civilizationController.getCurrentPlayer().totalCup() + 1;
            }
            System.out.println((i+1)+"- "+readyTechnologies.get(i).getName()+", it needs "+turnsLeft+" turns to unlock");
        }
        String whichTechnology=scanner.nextLine();
        if(!whichTechnology.equals("exit")){
            civilizationController.studyTechnology(readyTechnologies.get(Integer.parseInt(whichTechnology)-1));
            System.out.println("technology will be studied!");
        }
    }
    public void unitBuild(String name){
        //TODO railroad building
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
        //TODO railroad removing
        if (name.equals("Road")) System.out.println(unitController.eliminateRoad());
        else System.out.println(unitController.eliminateFeature());
    }

    public void attackUnit(Scanner scanner){
        if(!unitController.checkSelectedUnit().equals("ok")){
            System.out.println(unitController.checkSelectedUnit());
        }
        if(!(unitController.getSelectedUnit() instanceof MilitaryUnit)){
            System.out.println("this unit can't attack another unit");
        }
        ArrayList<Unit> reachableUnits=unitController.reachableUnits();
        if(reachableUnits.size()>0) {
            System.out.println("units that you can reach to attack:");
            for (int i = 0; i < reachableUnits.size(); i++) {
                System.out.println((i + 1) + "- name: " + reachableUnits.get(i).getName() + " location: (" + reachableUnits.get(i).getX() + "," + reachableUnits.get(i).getY() + ")");
            }
            String whichUnit=scanner.nextLine();
            if(!whichUnit.equals("exit")){
                System.out.println(unitController.attackUnit(reachableUnits.get(Integer.parseInt(whichUnit)-1)));
            }
        }else {
            System.out.println("you can't reach any unit to attack");
        }
    }
}
