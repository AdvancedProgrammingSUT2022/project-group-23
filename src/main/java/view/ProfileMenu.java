package view;

import controller.ProfileController;
import enums.Commands;

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
                if(menuName.matches("([mM]ain)|([mM]enu [mM]enu)")){
                    break;
                }
            }
            else if(input.equals("menu show-current")) System.out.println("Profile Menu");
            else if(input.equals("menu exit"))break;
            else System.out.println("invalid command");
        }
    }
}
