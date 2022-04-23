
import view.RegisterMenu;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        RegisterMenu registerMenu = RegisterMenu.getInstance();
        registerMenu.run(scanner);
    }
}
