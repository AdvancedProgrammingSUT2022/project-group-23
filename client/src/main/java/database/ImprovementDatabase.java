package database;

import model.Improvement;

import java.util.ArrayList;

public class ImprovementDatabase {
    private static ArrayList<Improvement> improvements = new ArrayList<>();
    static {
        improvements.add(new Improvement("Camp", 0, 0, 0, "Trapping", new ArrayList<>(){{add("Forest");add("Tundra");add("Plain");add("Hill");}}));
        improvements.add(new Improvement("Farm", 0, 1, 0, "Agriculture", new ArrayList<>(){{add("Grassland");add("Plain");add("Dessert");}}));
        improvements.add(new Improvement("Lumber Mill", 0, 0, 1, "Engineering", new ArrayList<>(){{add("Forest");}}));
        improvements.add(new Improvement("Mine", 0, 0, 1, "Mining", new ArrayList<>(){{add("Grassland");add("Plain");add("Desert");add("Tundra");add("Jungle");add("Snow");add("Hill");}}));
        improvements.add(new Improvement("Pasture", 0, 0, 0, "Animal Husbandry", new ArrayList<>(){{add("Grassland");add("Plain");add("Desert");add("Tundra");add("Hill");}}));
        improvements.add(new Improvement("Plantation", 0, 0, 0, "Calendar", new ArrayList<>(){{add("Grassland");add("Plain");add("Desert");add("Forest");add("Marsh");add("Flood Plain");add("Jungle");}}));
        improvements.add(new Improvement("Quarry", 0, 0, 0, "Masonry", new ArrayList<>(){{add("Grassland");add("Plain");add("Desert");add("Tundra");add("Hill");}}));
        improvements.add(new Improvement("Trading Post", 2, 0, 0, "Trapping", new ArrayList<>(){{add("Grassland");add("Plain");add("Desert");add("Tundra");}}));
        improvements.add(new Improvement("Manufactory", 0, 0, 2, "Engineering", new ArrayList<>(){{add("Grassland");add("Plain");add("Desert");add("Tundra");add("Snow");}}));
    }

    public static ArrayList<Improvement> getImprovements() {
        return improvements;
    }
}
