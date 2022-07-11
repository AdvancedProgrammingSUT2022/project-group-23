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
                HashMap<String, String> options = Commands.getOptions(matcher, 3);
                if(!options.containsKey("username") || !options.containsKey("nickname") ||
                        !options.containsKey("password"))
                    System.out.println("invalid command");
                else System.out.println(registerController.addUser(options.get("username"), options.get("nickname"), options.get("password")));
            } else if((matcher = Commands.getCommandMatcher(input, Commands.LOGIN_USER)) != null){
                HashMap<String, String> options = Commands.getOptions(matcher, 2);
                String message = "";
                if(!options.containsKey("username") || !options.containsKey("password"))
                    System.out.println("invalid command");
                else System.out.println(message = registerController.login(options.get("username"), options.get("password")));
                if(message.equals("user logged in successfully!")){
                    MainMenu.getInstance().run(scanner);
                }
            }
            else if(input.equals("menu show-current")) System.out.println("Login Menu");
            else if(input.equals("menu exit"))break;
            else if(input.equals("user logout")) System.out.println("please login first");
            else if(input.startsWith("menu enter")) System.out.println("please login first");
            else System.out.println("invalid command");
        }
    }


}
