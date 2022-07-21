package model;

import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class User implements Comparable<User> {
    private String username;
    private String password;
    private String nickname;
    private int highScore;
    private String lastWin;
    private String lastOnline;
    private String profilePictureURL;
    private ArrayList<String> friends;
    ArrayList<Pair<String, String>> friendRequests;
    private static ArrayList<User> users = new ArrayList<>();

    public User(String username, String password, String nickname, String profilePictureURL) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.profilePictureURL = profilePictureURL;
        this.lastOnline = "";
        this.lastWin = "No wins";
        this.highScore = 0;
        this.friends = new ArrayList<>();
        friendRequests = new ArrayList<>();
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

    public static ArrayList<User> getUsers() {
        return users;
    }

    public static void addUser(User user) {
        users.add(user);
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


    public static void updateUsersInfo() {
        try {
            FileWriter writer = new FileWriter("src\\main\\resources\\saves\\userInfo\\UserInfo.json");
            writer.write(new Gson().toJson(User.getUsers()));
            writer.close();
        } catch (IOException e) {
            System.out.println("ERROR updating info");
        }
    }


    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
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

    public ArrayList<Pair<String, String>> getFriendRequests() {
        return friendRequests;
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

