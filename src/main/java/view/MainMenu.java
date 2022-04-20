package view;

import controller.MainMenuController;
import enums.Commands;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;

public class MainMenu {
    private static MainMenu instance;
    private MainMenuController mainMenuController;
    private MainMenu(){
        this.mainMenuController = MainMenuController.getInstance();
    }
    public static MainMenu getInstance(){
        if(instance == null){
            instance = new MainMenu();
        }
        return instance;
    }
    public void run(Scanner scanner){
        String input;
        while (true){
            input = scanner.nextLine();
            Matcher matcher;
            if((matcher = Commands.getCommandMatcher(input, Commands.ENTER_MENU)) != null){
                String menuName = matcher.group("menuName");
                if(menuName.toLowerCase().matches("(profile)|(profile menu)")){
                    ProfileMenu.getInstance().run(scanner);
                }
            }else if(input.startsWith("play game")){
                matcher = Commands.getCommandMatcher(input, Commands.PLAYER);
                ArrayList<String> usernames = new ArrayList<>();
                while(matcher.find()){
                    String username = matcher.group("username");
                    if(!mainMenuController.hasUser(username)){
                        System.out.println("some player doesn't exist!");
                    }
                    usernames.add(username);
                }
                if(usernames.size() < 2) System.out.println("not enough players");
                else {
                    System.out.println("game started");
                    GameView gameView = new GameView(usernames);
                    gameView.run(scanner);
                }
            }
            else if(input.equals("menu show-current")) System.out.println("Main Menu");
            else if(input.equals("user logout")){
                System.out.println(mainMenuController.logout());
                break;
            } else if(input.equals("menu exit"))break;
            else System.out.println("invalid command");
        }
    }
}
