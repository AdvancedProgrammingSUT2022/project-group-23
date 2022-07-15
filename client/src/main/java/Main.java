
import controller.NetworkController;
import view_graphic.App;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        NetworkController.connectToServer();
        App.main(args);
        System.exit(0);
    }
}
