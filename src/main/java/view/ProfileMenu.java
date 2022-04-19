package view;

import controller.ProfileController;
import enums.Commands;

import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;

public class ProfileMenu {
    private static ProfileMenu instance;
    public ProfileController profileController;
    private ProfileMenu(){
        profileController = ProfileController.getInstance();
    }
    public static ProfileMenu getInstance(){
        if(instance == null){
            instance = new ProfileMenu();
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
                if(menuName.toLowerCase().matches("(main)|(main menu)"))
                    break;
            } else if((matcher = Commands.getCommandMatcher(input, Commands.CHANGE_NICKNAME)) != null){
                System.out.println(profileController.changeNickname(matcher.group("nickname")));
            }else if((matcher = Commands.getCommandMatcher(input, Commands.CHANGE_PASSWORD)) != null){
                HashMap<String, String> options = Commands.getOptions(matcher, 2);
                System.out.println(profileController.changePassword(options.get("current"), options.get("new")));
            }
            else if(input.equals("menu show-current")) System.out.println("Profile Menu");
            else if(input.equals("menu exit"))break;
            else System.out.println("invalid command");
        }
    }
}
