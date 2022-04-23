package database;

import model.Technology;

import java.util.ArrayList;

public class TechnologyDatabase {
    private static ArrayList<Technology> technologies = new ArrayList<>();
    static {
        technologies.add(new Technology("Agriculture", 20, new ArrayList<>()));
        technologies.add(new Technology("Animal Husbandry", 35, new ArrayList<>(){{add("Agriculture");}}));
        //TODO fill in the rest of technologies
    }
}
