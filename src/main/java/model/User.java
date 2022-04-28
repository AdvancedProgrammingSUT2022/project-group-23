package model;

import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class User {
    private String username;
    private String password;
    private String nickname;
    private int score;
    private int cup;
    private int gold;
    private int happiness;
    private static ArrayList<User> users = new ArrayList<>();
    private ArrayList<Unit> units;
    private ArrayList<City> cities;
    private Technology currentStudy;
    private ArrayList<Technology> technologies;
    private static User userLogged;

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

    public int getCup () {
        return cup;
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
        cup = 0;
        gold = 0;
        happiness = 0;
        units = new ArrayList<>();
        cities = new ArrayList<>();
        technologies = new ArrayList<>();
        currentStudy = null;
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
            cup++;
            cup += city.getCountOfCitizens();
        }
        return cup;
    }
}
