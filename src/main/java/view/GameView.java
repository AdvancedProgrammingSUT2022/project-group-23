package view;

import controller.CivilizationController;
import enums.Commands;
import model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;

public class GameView {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";
    private String[][] printableMap = new String[100][100];
    CivilizationController civilizationController;

    public GameView(ArrayList<String> usernames){
        System.out.println(usernames);

        ArrayList<User> players = new ArrayList<>();
        ArrayList<User> allUsers = User.getUsers();
        for(String username : usernames){
            for(User user : allUsers){
                if(user.getUsername().equals(username)){
                    players.add(user);
                    break;
                }
            }
        }
        civilizationController = new CivilizationController(players);
    }
    public void run(Scanner scanner){
        String input;
        while (true){
            input = scanner.nextLine();
            Matcher matcher;
            if(input.equals("menu show-current")) System.out.println("Game Menu");
            else if(input.equals("menu exit"))break;
            else System.out.println("invalid command");
        }
    }


    public void addHexagonal(int x, int y, String backgroundColor, ArrayList<Integer> riverDirections, ArrayList<String> infos){
        int mapY, mapX;
        if(y%2 == 0) {
            mapY = (y  / 2) * 11 + (y / 2) * 5 + 3;
            mapX = x * 6;
        }
        else {
            mapY = ((y + 1) / 2) * 11 + (y / 2) * 5;
            mapX = x * 6 + 3;
        }
        String coordinates = x + "," + y;
        for (int i = 0; i < 3; i++) {
            printableMap[mapX + i][mapY - i] = "/";
            if(riverDirections.contains(1)) printableMap[mapX + i][mapY - i] = ANSI_BLUE_BACKGROUND + "/" + ANSI_RESET;
            for (int j = 1; j <= 5 + 2 * i; j++) {
                String c = " ";
                if (i != 2) {
                    if (!infos.isEmpty() && infos.get(0).length() >= j)
                        c = String.valueOf(infos.get(0).charAt(j - 1));
                }
                else if (coordinates.length() >= j) c = String.valueOf(coordinates.charAt(j - 1));

                printableMap[mapX + i][mapY - i + j] = backgroundColor + c + ANSI_RESET;
            }
            printableMap[mapX + i][mapY - i + 6 + 2 * i] = "\\";
            if(riverDirections.contains(2)) printableMap[mapX + i][mapY - i + 6 + 2 * i] = ANSI_BLUE_BACKGROUND + "\\" + ANSI_RESET;
            if(i != 2 && !infos.isEmpty())infos.remove(0);
        }
        for (int i = 0; i < 3; i++) {
            printableMap[mapX + 3 + i][mapY - 2 + i] = "\\";
            if(riverDirections.contains(3)) printableMap[mapX + 3 + i][mapY - 2 + i] = ANSI_BLUE_BACKGROUND + "\\" + ANSI_RESET;
            for (int j = 1; j <= 5 + 2 * (2 - i); j++) {
                String c = " ";
                if (infos.size() > 0 && infos.get(0).length() >= j)
                    c = String.valueOf(infos.get(0).charAt(j - 1));
                if(i == 2)
                    printableMap[mapX + 3 + i][mapY - 2 + i + j] = backgroundColor + "_" + ANSI_RESET;
                else
                    printableMap[mapX + 3 + i][mapY - 2 + i + j] = backgroundColor + c + ANSI_RESET;
            }
            printableMap[mapX + 3 + i][mapY - 2 + i + 6 + 2 * (2 - i)] = "/";
            if(riverDirections.contains(4)) printableMap[mapX + 3 + i][mapY - 2 + i + 6 + 2 * (2 - i)] = ANSI_BLUE_BACKGROUND + "/" + ANSI_RESET;
            if(i != 2 && !infos.isEmpty())infos.remove(0);
        }

    }
    public void printMap(){
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                if (printableMap[i][j] != null) System.out.print(printableMap[i][j]);
                else System.out.print(" ");
            }
            System.out.print("\n");
        }
    }
}
