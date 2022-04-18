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
                HashMap<String, String> options = getOptions(matcher, 3);
                if(!options.containsKey("username") || !options.containsKey("nickname") ||
                        !options.containsKey("password"))
                    System.out.println("invalid command");
                else System.out.println(registerController.addUser(options.get("username"), options.get("nickname"), options.get("password")));
            } else if((matcher = Commands.getCommandMatcher(input, Commands.LOGIN_USER)) != null){
                HashMap<String, String> options = getOptions(matcher, 2);
                if(!options.containsKey("username") || !options.containsKey("password"))
                    System.out.println("invalid command");
                else System.out.println(registerController.login(options.get("username"), options.get("password")));
            }
            else if(input.equals("exit"))break;
        }
    }

    private HashMap<String, String> getOptions(Matcher matcher, int countOfOptions) {
        HashMap<String, String> options = new HashMap<>();
        for (int i = 1; i <= countOfOptions; i++) {
            options.put(matcher.group("option" + i), matcher.group("value" + i));

        }
        return options;
    }
}
