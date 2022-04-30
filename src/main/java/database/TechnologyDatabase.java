package database;

import model.Technology;

import java.util.ArrayList;

public class TechnologyDatabase {
    private static ArrayList<Technology> technologies = new ArrayList<>();
    static {
        technologies.add(new Technology("Agriculture", 20, new ArrayList<>()));
        technologies.add(new Technology("Animal Husbandry", 35, new ArrayList<>(){{add("Agriculture");}}));
        technologies.add(new Technology("Archery",35,new ArrayList<>(){{add("Agriculture");}}));
        technologies.add(new Technology("Bronze Working",55,new ArrayList<>(){{add("Mining");}}));
        technologies.add(new Technology("Calendar",70,new ArrayList<>(){{add("Pottery");}}));
        technologies.add(new Technology("Masonry",55,new ArrayList<>(){{add("Mining");}}));
        technologies.add(new Technology("Mining",35,new ArrayList<>(){{add("Agriculture");}}));
        technologies.add(new Technology("Pottery",35,new ArrayList<>(){{add("Agriculture");}}));
        technologies.add(new Technology("Wheel",55,new ArrayList<>(){{add("Animal Husbandry");}}));
        technologies.add(new Technology("Trapping",55,new ArrayList<>(){{add("Animal Husbandry");}}));
        technologies.add(new Technology("Writing",55,new ArrayList<>(){{add("Pottery");}}));
        technologies.add(new Technology("Construction",100,new ArrayList<>(){{add("Masonry");}}));
        technologies.add(new Technology("Horseback Riding",100,new ArrayList<>(){{add("Wheel");}}));
        technologies.add(new Technology("Iron Working",150,new ArrayList<>(){{add("Bronze Working");}}));
        technologies.add(new Technology("Mathematics",100,new ArrayList<>(){{add("Wheel");}{add("Archery");}}));
        technologies.add(new Technology("Philosophy",100,new ArrayList<>(){{add("Writing");}}));
        technologies.add(new Technology("Chivalry",440,new ArrayList<>(){{add("Civil Service");}{add("Horseback Riding");}{add("Currency");}}));
        technologies.add(new Technology("Civil Service",400,new ArrayList<>(){{add("Philosophy");}{add("Trapping");}}));
        technologies.add(new Technology("Currency",250,new ArrayList<>(){{add("Mathematics");}}));
        technologies.add(new Technology("Education",440,new ArrayList<>(){{add("Theology");}}));
        technologies.add(new Technology("Engineering",250,new ArrayList<>(){{add("Mathematics");}{add("Construction");}}));
        technologies.add(new Technology("Machinery",440,new ArrayList<>(){{add("Engineering");}}));
        technologies.add(new Technology("Metal Casting",240,new ArrayList<>(){{add("Iron Working");}}));
        technologies.add(new Technology("Physics",240,new ArrayList<>(){{add("Engineering");}{add("Metal Casting");}}));
        technologies.add(new Technology("Steel",440,new ArrayList<>(){{add("Metal Casting");}}));
        technologies.add(new Technology("Theology",250,new ArrayList<>(){{add("Calendar");}{add("Philosophy");}}));
        technologies.add(new Technology("Acoustics",650,new ArrayList<>(){{add("Education");}}));
        technologies.add(new Technology("Archaeology",1300,new ArrayList<>(){{add("Acoustics");}}));
        technologies.add(new Technology("Banking",650,new ArrayList<>(){{add("Education");}{add("Chivalry");}}));
        technologies.add(new Technology("Chemistry",900,new ArrayList<>(){{add("Gunpowder");}}));
        technologies.add(new Technology("Economics",900,new ArrayList<>(){{add("Banking");}{add("Printing Press");}}));
        technologies.add(new Technology("Fertilizer",1300,new ArrayList<>(){{add("Chemistry");}}));
        technologies.add(new Technology("Gunpowder",680,new ArrayList<>(){{add("Physics");}{add("Steel");}}));
        technologies.add(new Technology("Metallurgy",900,new ArrayList<>(){{add("Gunpowder");}}));
        technologies.add(new Technology("Military Science",1300,new ArrayList<>(){{add("Economics");}{add("Chemistry");}}));
        technologies.add(new Technology("Printing Press",650,new ArrayList<>(){{add("Machinery");}{add("Physics");}}));
        technologies.add(new Technology("Rifling",1425,new ArrayList<>(){{add("Metallurgy");}}));
        technologies.add(new Technology("Scientific Theory",1300,new ArrayList<>(){{add("Acoustics");}}));
        technologies.add(new Technology("Biology",1680,new ArrayList<>(){{add("Archaeology");}{add("Scientific Theory");}}));
        technologies.add(new Technology("Combustion",2200,new ArrayList<>(){{add("Replaceable Parts");}{add("Railroad");}{add("Dynamite");}}));
        technologies.add(new Technology("Dynamite",1900,new ArrayList<>(){{add("Fertilizer");}{add("Rifling");}}));
        technologies.add(new Technology("Electricity",1900,new ArrayList<>(){{add("Biology");}{add("Steam Power");}}));
        technologies.add(new Technology("Radio",2200,new ArrayList<>(){{add("Electricity");}}));
        technologies.add(new Technology("Railroad",1900,new ArrayList<>(){{add("Steam Power");}}));
        technologies.add(new Technology("Replaceable Parts",1900,new ArrayList<>(){{add("Steam Power");}}));
        technologies.add(new Technology("Steam Power",1680,new ArrayList<>(){{add("Scientific Theory");}{add("Military Science");}}));
        technologies.add(new Technology("Telegraph",2200,new ArrayList<>(){{add("Electricity");}}));
    }

    public static ArrayList<Technology> getTechnologies() {
        return technologies;
    }
}
