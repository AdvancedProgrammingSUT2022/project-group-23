package model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.google.gson.graph.GraphAdapterBuilder;
import controller.CivilizationController;
import controller.GameController;
import database.SaveDatabase;
import database.TechnologyDatabase;
import database.UnitsDatabase;
import view_graphic.AutoSaveMenu;
import view_graphic.Game;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class User implements Comparable<User> {
    private String username;
    private String password;
    private String nickname;
    private int score;
    private int highScore;
    private int gold;
    private int happiness;
    private int isUnhappy;
    private boolean hasCapitalFallen;
    private static ArrayList<User> users = new ArrayList<>();
    private ArrayList<Unit> units;
    private ArrayList<City> cities;
    private Technology currentStudy;
    private ArrayList<Technology> technologies;
    private static User userLogged;
    private HashMap<String, Integer> waitedTechnologies;
    private ArrayList<String> luxuryResources;
    private HashMap<Tile, Integer> processingTiles;
    private HashMap<Tile, WorkerUnit> workingWorkers;
    private HashMap<Tile, Improvement> improvingTiles;
    private ArrayList<Tile> eliminatingFeatures;
    private String profilePictureURL;
    private String lastWin;
    private String lastOnline;
    private ArrayList<User> enemies;
    private ArrayList<User> confederate;

    private ArrayList<String> notifications;

    private HashMap<String, ArrayList<String>> messages;
    private HashMap<String,ArrayList<String>> giving;
    private HashMap<String,ArrayList<String>> receiving;

    public User(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.profilePictureURL = getClass().getResource("/images/profilePictures/" + new Random().nextInt(6) + ".png").toString();
        this.lastOnline = "";
        this.lastWin = "";
        users.add(this);
        User.updateUsersInfo();
    }

    public static void setUsers(ArrayList<User> users) {
        User.users = users;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getNickname() {
        return nickname;
    }

    public int getScore() {
        return score;
    }

    public int getGold() {
        return gold;
    }

    public int getHappiness() {
        return happiness;
    }

    public ArrayList<Unit> getUnits() {
        return units;
    }

    public void removeUnit(Unit unit) {
        units.remove(unit);
    }

    public ArrayList<City> getCities() {
        return cities;
    }

    public void addCity(City city) {
        cities.add(city);
    }

    public void addUnit(Unit unit) {
        units.add(unit);
    }

    public ArrayList<Technology> getTechnologies() {
        return technologies;
    }

    public void addTechnology(Technology technology) {
        technologies.add(technology);
    }

    public boolean hasTechnology(String technologyName) {
        for (Technology technology : technologies) {
            if (technology.getName().equals(technologyName)) return true;
        }
        return false;
    }

    public boolean hasResource(String resourceName) {
        for (City city : cities) {
            for (Tile tile : city.getTiles()) {
                if (tile.getResource() != null && tile.getResource().getName().equals(resourceName)) return true;
            }
        }
        return false;
    }

    public static ArrayList<User> getUsers() {
        return users;
    }

    public static void addUser(User user) {
        users.add(user);
    }

    public Technology getCurrentStudy() {
        return currentStudy;
    }

    public static User getUserLogged() {
        return userLogged;
    }

    public static void setUserLogged(User userLogged) {
        User.userLogged = userLogged;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public void newGame() {
        score = 0;
        gold = 0;
        isUnhappy = 0;
        happiness = 20;
        hasCapitalFallen = false;
        units = new ArrayList<>();
        cities = new ArrayList<>();
        technologies = new ArrayList<>();
        currentStudy = null;
        waitedTechnologies = new HashMap<>();
        luxuryResources = new ArrayList<>();
        processingTiles = new HashMap<>();
        workingWorkers = new HashMap<>();
        improvingTiles = new HashMap<>();
        eliminatingFeatures = new ArrayList<>();
        notifications = new ArrayList<>();
        enemies = new ArrayList<>();
        confederate = new ArrayList<>();
        messages = new HashMap<>();
        giving=new HashMap<>();
        receiving=new HashMap<>();
        updateUsersInfo();
    }

    public static void updateUsersInfo() {
        try {
            FileWriter writer = new FileWriter("src\\main\\resources\\saves\\userInfo\\UserInfo.json");
            writer.write(new Gson().toJson(User.getUsers()));
            writer.close();
        } catch (IOException e) {
            System.out.println("ERROR updating info");
        }
    }

    public static void loadGameInfo(String saveName) {
        try {
            String json = new String(Files.readAllBytes(Paths.get("src\\main\\resources\\saves\\gameSaves\\" + saveName + ".json")));
            Gson gson = getGson();
            SaveDatabase saveDatabase = gson.fromJson(json, SaveDatabase.class);
            GameController.setMapWidth(saveDatabase.getMapWidth());
            GameController.setMapHeight(saveDatabase.getMapHeight());
            GameController.setSelectedUnit(null);
            GameController.setSelectedCity(null);
            GameController.setCivilizationController(saveDatabase.getCivilizationController());
            GameController.setPlayers(saveDatabase.getPlayers());
            GameController.setTiles(saveDatabase.getTiles());
            GameController.setTurn(saveDatabase.getTurn());
            GameController.setCurrentPlayer(saveDatabase.getCurrentPlayer());
            GameController.setCurrentYear(saveDatabase.getCurrentYear());
            GameController.setLostPlayers(saveDatabase.getLostPlayers());
            GameController.setSaveNumber(saveDatabase.getSaveNumber());
            CivilizationController civilizationController = GameController.getCivilizationController();
            for (int j = 0; j < GameController.getPlayers().size(); j++) {
                User player = GameController.getPlayers().get(j);
                for (int i = 0; i < users.size(); i++) {
                    if (users.get(i).getUsername().equals(player.getUsername())) {
                        User user = users.get(i);
                        player.setPassword(user.getPassword());
                        player.setNickname(user.getNickname());
                        player.setHighScore(user.getHighScore());
                        player.setLastOnline(user.getLastOnline());
                        player.setLastWin(user.getLastWin());
                        player.setProfilePictureURL(user.getProfilePictureURL());
                        users.set(i, player);
                        break;
                    }
                }
            }
            for (User user : GameController.getPlayers()) {
                for (int i = 0; i < user.getUnits().size(); i++) {
                    Unit unit = user.getUnits().get(i);
                    if (user.getUnits().get(i).getName().equals("Settler")) {
                        SettlerUnit settlerUnit = new SettlerUnit(user.getUnits().get(i).getX(), user.getUnits().get(i).getY());
                        settlerUnit.setState(unit.getState());
                        settlerUnit.setRemainingMoves(unit.getRemainingMoves());
                        settlerUnit.setHealth(unit.getHealth());
                        settlerUnit.setMoves(unit.getMoves());
                        user.getUnits().set(i, settlerUnit);
                    } else if (user.getUnits().get(i).getName().equals("Worker")) {
                        WorkerUnit workerUnit = new WorkerUnit(user.getUnits().get(i).getX(), user.getUnits().get(i).getY());
                        workerUnit.setState(unit.getState());
                        workerUnit.setRemainingMoves(unit.getRemainingMoves());
                        workerUnit.setHealth(unit.getHealth());
                        workerUnit.setMoves(unit.getMoves());
                        user.getUnits().set(i, workerUnit);
                    } else {
                        ArrayList<Unit> allUnits = UnitsDatabase.getUnits();
                        for (Unit currentUnit : allUnits) {
                            if (currentUnit.getName().equals(unit.getName())) {
                                MilitaryUnit militaryUnit = ((MilitaryUnit) currentUnit).getCopy();
                                militaryUnit.setX(unit.getX());
                                militaryUnit.setY(unit.getY());
                                militaryUnit.setState(unit.getState());
                                militaryUnit.setRemainingMoves(unit.getRemainingMoves());
                                militaryUnit.setHealth(unit.getHealth());
                                militaryUnit.setMoves(unit.getMoves());
                                user.getUnits().set(i, militaryUnit);
                                break;
                            }
                        }
                    }
                }
            }
            Game.setCivilizationController(civilizationController);
        } catch (IOException e) {
            System.out.println("ERROR reading save file");
        }
    }

    public static void saveGame(String saveName) {
        try {
            FileWriter writer = new FileWriter("src\\main\\resources\\saves\\gameSaves\\" + saveName + ".json");
            SaveDatabase saveDatabase = new SaveDatabase();
            Gson gson = getGson();
            writer.write(gson.toJson(saveDatabase));
            writer.close();
        } catch (IOException e) {
            System.out.println("ERROR saving game info");
        }
    }

    public static void autoSave() {
        if (GameController.getSaveNumber() > AutoSaveMenu.getAutoSaveNumber())
            GameController.setSaveNumber(1);
        User.saveGame("AutoSave" + GameController.getSaveNumber());
        GameController.setSaveNumber(GameController.getSaveNumber() + 1);
    }

    private static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        new GraphAdapterBuilder().addType(SaveDatabase.class).addType(User.class).addType(Unit.class).addType(WorkerUnit.class).addType(SettlerUnit.class).addType(MilitaryUnit.class).addType(City.class).addType(Tile.class).addType(Building.class).addType(Technology.class).addType(Terrain.class).addType(Improvement.class).addType(River.class).addType(Resource.class).registerOn(gsonBuilder);
        return gsonBuilder.create();
    }

    public ArrayList<String> getNotifications() {
        return notifications;
    }

    public void addNotification(String notification) {
        notifications.add(notification);
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public void setHappiness(int happiness) {
        this.happiness = happiness;
        if (this.happiness < 0) setIsUnhappy(1);
        if (this.happiness >= 0) setIsUnhappy(0);
    }

    public int totalCup() {
        int cup = 0;
        for (City city : cities) {
            cup = +3;
            cup += city.getCountOfCitizens();
            for (Building building : city.getBuildings()) {
                cup += building.getCup();
            }
        }
        if (gold < 0) {
            cup += gold;
            if (cup < 0) cup = 0;
            gold = 0;
        }
        return cup;
    }

    public void setCurrentStudy(Technology currentStudy) {
        this.currentStudy = currentStudy;
    }

    public HashMap<String, Integer> getWaitedTechnologies() {
        return waitedTechnologies;
    }

    public ArrayList<Technology> readyTechnologies() {
        ArrayList<Technology> readyTechnology = new ArrayList<>();
        for (Technology technology : TechnologyDatabase.getTechnologies()) {
            ArrayList<String> prerequisiteTechnologies = technology.getPrerequisiteTechnologies();
            boolean hasAllPrerequisiteTechnologies = true;
            for (String technologyName : prerequisiteTechnologies) {
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
            if (hasAllPrerequisiteTechnologies && !technologies.contains(technology) && (currentStudy == null || !technology.getName().equals(currentStudy.getName()))) {
                readyTechnology.add(technology);
            }
        }
        return readyTechnology;
    }


    public int getIsUnhappy() {
        return isUnhappy;
    }

    public void setIsUnhappy(int isUnhappy) {
        this.isUnhappy = isUnhappy;
    }

    public ArrayList<String> getLuxuryResources() {
        return luxuryResources;
    }

    public void addLuxuryResource(String name) {
        luxuryResources.add(name);
    }

    public HashMap<Tile, Integer> getProcessingTiles() {
        return processingTiles;
    }

    public void setProcessingTiles(HashMap<Tile, Integer> processingTiles) {
        this.processingTiles = processingTiles;
    }

    public HashMap<Tile, WorkerUnit> getWorkingWorkers() {
        return workingWorkers;
    }

    public HashMap<Tile, Improvement> getImprovingTiles() {
        return improvingTiles;
    }

    public void setImprovingTiles(HashMap<Tile, Improvement> improvingTiles) {
        this.improvingTiles = improvingTiles;
    }

    public ArrayList<Tile> getEliminatingFeatures() {
        return eliminatingFeatures;
    }

    public String getProfilePictureURL() {
        return profilePictureURL;
    }

    public void setProfilePictureURL(String profilePictureURL) {
        this.profilePictureURL = profilePictureURL;
    }

    public String getLastWin() {
        return lastWin;
    }

    public void setLastWin(String lastWin) {
        this.lastWin = lastWin;
    }

    public String getLastOnline() {
        return lastOnline;
    }

    public void setLastOnline(String lastOnline) {
        this.lastOnline = lastOnline;
    }

    public HashMap<String, ArrayList<String>> getGiving () {
        return giving;
    }

    public HashMap<String, ArrayList<String>> getReceiving () {
        return receiving;
    }

    @Override
    public int compareTo(User o) {
        if (this.getHighScore() > o.getHighScore()) return 1;
        else if (this.getHighScore() < o.getHighScore()) return -1;
        else if (this.getLastWin().compareTo(o.lastWin) > 0) return 1;
        else if (this.getLastWin().compareTo(o.lastWin) < 0) return -1;
        else if (this.getNickname().compareTo(o.getNickname()) > 0) return 1;
        else if (this.getNickname().compareTo(o.getNickname()) < 0) return -1;
        return 0;
    }

    public static User getUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public ArrayList<User> getEnemies() {
        return enemies;
    }

    public ArrayList<User> getConfederate() {
        return confederate;
    }

    public void addEnemy(User user) {
        enemies.add(user);
    }

    public void addConfederate(User user) {
        confederate.add(user);
    }

    public boolean isHasCapitalFallen() {
        return hasCapitalFallen;
    }

    public void setHasCapitalFallen(boolean hasCapitalFallen) {
        this.hasCapitalFallen = hasCapitalFallen;
    }

    public HashMap<String, ArrayList<String>> getMessages () {
        return messages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return highScore == user.highScore && Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(nickname, user.nickname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, nickname, highScore);
    }
}

