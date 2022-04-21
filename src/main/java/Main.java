import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.User;
import view.RegisterMenu;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        try {
            String json= new String(Files.readAllBytes(Paths.get("src\\main\\resources\\UsersInfo.json")));
            User.setUsers(new Gson().fromJson(json, new TypeToken<List<User>>(){}.getType()));
        } catch (IOException e) {
            System.out.println("ERROR");
        }
        Scanner scanner = new Scanner(System.in);
        RegisterMenu registerMenu = RegisterMenu.getInstance();
        registerMenu.run(scanner);
    }
}
