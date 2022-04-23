package database;

import model.Improvement;

import java.util.ArrayList;

public class ImprovementDatabase {
    private static ArrayList<Improvement> improvements = new ArrayList<>();
    static {
        improvements.add(new Improvement("Camp", 0, 0, 0, new ArrayList<>(){{add("forest");add("tundra");add("plain");add("hills");}}));

    }

    public static ArrayList<Improvement> getImprovements() {
        return improvements;
    }
}
