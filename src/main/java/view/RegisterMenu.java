package view;

import controller.RegisterController;
import enums.Commands;

import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;

public class RegisterMenu {
    private static RegisterMenu instance;
    private RegisterController registerController;
    private RegisterMenu(){
        this.registerController = RegisterController.getInstance();
    }
    public static RegisterMenu getInstance(){
        if(instance == null){
            instance = new RegisterMenu();
        }
        return instance;
    }
    public void run(Scanner scanner){
        String input;
        while (true){
            input = scanner.nextLine();
            Matcher matcher;
            if((matcher = Commands.getCommandMatcher(input, Commands.CREATE_USER)) != null){
                HashMap<String, String> options = new HashMap<>();
                options.put(matcher.group("firstOption"), matcher.group("firstValue"));
                options.put(matcher.group("secondOption"), matcher.group("secondValue"));
                options.put(matcher.group("thirdOption"), matcher.group("thirdValue"));
                if(!options.containsKey("username") || !options.containsKey("nickname") ||
                        !options.containsKey("password"))
                    System.out.println("invalid command");
                else System.out.println(registerController.addUser(options.get("username"), options.get("nickname"), options.get("password")));
            }
            if(input.equals("exit"))break;
        }
    }
}
