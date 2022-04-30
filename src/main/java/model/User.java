package model;

import com.google.gson.Gson;
import database.TechnologyDatabase;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class User {
    private String username;
    private String password;
    private String nickname;
    private int score;
    private int gold;
    private int happiness;
    private int isUnhappy=0;
    private static ArrayList<User> users = new ArrayList<>();
    private ArrayList<Unit> units;
    private ArrayList<City> cities;
    private Technology currentStudy;
    private ArrayList<Technology> technologies;
    private static User userLogged;
    private HashMap<String ,Integer> waitedTechnologies;
    private ArrayList<String> luxuryResources;
    private HashMap<Tile,Integer> processingRoads;
    private HashMap<Tile,WorkerUnit> workingWorkers;

    public User(String username,String password,String nickname)
    {
        this.username=username;
        this.password=password;
        this.nickname=nickname;
        users.add(this);
    }

    public static void setUsers (ArrayList<User> users) {
        User.users = users;
    }

    public String getUsername () {
        return username;
    }

    public String getPassword () {
        return password;
    }

    public String getNickname () {
        return nickname;
    }

    public int getScore () {
        return score;
    }

    public int getGold () {
        return gold;
    }

    public int getHappiness () {
        return happiness;
    }

    public ArrayList<Unit> getUnits () {
        return units;
    }
    public void removeUnit(Unit unit){
        units.remove(unit);
    }

    public ArrayList<City> getCities () {
        return cities;
    }

    public void addCity(City city)
    {
        cities.add(city);
    }

    public void addUnit(Unit unit)
    {
        units.add(unit);
    }

    public ArrayList<Technology> getTechnologies () {
        return technologies;
    }

    public void addTechnology(Technology technology)
    {
        technologies.add(technology);
    }

    public boolean hasTechnology(String technologyName){
        for(Technology technology : technologies){
            if(technology.getName().equals(technologyName)) return true;
        }
        return false;
    }

    public static ArrayList<User> getUsers () {
        return users;
    }

    public static void addUser(User user)
    {
        users.add(user);
    }

    public Technology getCurrentStudy () {
        return currentStudy;
    }

    public static User getUserLogged () {
        return userLogged;
    }

    public static void setUserLogged (User userLogged) {
        User.userLogged = userLogged;
    }

    public void setPassword (String password) {
        this.password = password;
    }

    public void setNickname (String nickname) {
        this.nickname = nickname;
    }

    public void newGame(){
        gold = 0;
        happiness = 20;
        units = new ArrayList<>();
        cities = new ArrayList<>();
        technologies = new ArrayList<>();
        currentStudy = null;
        waitedTechnologies = new HashMap<>();
        luxuryResources= new ArrayList<>();
        processingRoads = new HashMap<>();
        workingWorkers = new HashMap<>();
        updateUsersInfo();
    }

    public static void updateUsersInfo()
    {
        try {
            FileWriter writer=new FileWriter("src\\main\\resources\\UsersInfo.json");
            writer.write(new Gson().toJson(User.getUsers()));
            writer.close();
        } catch (IOException e) {
            System.out.println("ERROR");
        }
    }

    public void setScore (int score) {
        this.score = score;
    }

    public void setGold (int gold) {
        this.gold = gold;
    }

    public void setHappiness (int happiness) {
        this.happiness = happiness;
    }

    public int totalCup(){
        int cup=0;
        for (City city : cities) {
            cup=+3;
            cup += city.getCountOfCitizens();
        }
        return cup;
    }

    public void setCurrentStudy (Technology currentStudy) {
        this.currentStudy = currentStudy;
    }

    public HashMap<String, Integer> getWaitedTechnologies () {
        return waitedTechnologies;
    }

    public ArrayList<Technology> readyTechnologies(){
        ArrayList<Technology> readyTechnology=new ArrayList<>();
        for (Technology technology : TechnologyDatabase.getTechnologies()) {
            ArrayList<String> prerequisiteTechnologies = technology.getPrerequisiteTechnologies();
            boolean hasAllPrerequisiteTechnologies = true;
            for(String technologyName : prerequisiteTechnologies) {
                boolean hasTechnology = false;
                for (Technology technology1 : technologies) {
                    if (technology1.getName().equals(technologyName)) {
                        hasTechnology = true;
                        break;
                    }
                }
                if (!hasTechnology) {
                    hasAllPrerequisiteTechnologies = false;
                    break;
                }
            }
            if(hasAllPrerequisiteTechnologies && !technologies.contains(technology) && !technology.getName().equals(currentStudy.getName())){
                readyTechnology.add(technology);
            }
        }
        return readyTechnology;
    }

    public int getIsUnhappy () {
        return isUnhappy;
    }

    public void setIsUnhappy (int isUnhappy) {
        this.isUnhappy = isUnhappy;
    }

    public ArrayList<String> getLuxuryResources () {
        return luxuryResources;
    }

    public void addLuxuryResource(String name){
        luxuryResources.add(name);
    }

    public HashMap<Tile, Integer> getProcessingRoads () {
        return processingRoads;
    }

    public void setProcessingRoads (HashMap<Tile, Integer> processingRoads) {
        this.processingRoads = processingRoads;
    }

    public HashMap<Tile, WorkerUnit> getWorkingWorkers () {
        return workingWorkers;
    }

    public void setWorkingWorkers (HashMap<Tile, WorkerUnit> workingWorkers) {
        this.workingWorkers = workingWorkers;
    }
}

