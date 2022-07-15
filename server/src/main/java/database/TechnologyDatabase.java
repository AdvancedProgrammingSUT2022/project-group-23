package database;

import model.Technology;

import java.util.ArrayList;

public class TechnologyDatabase {
    private static ArrayList<Technology> technologies = new ArrayList<>();
    static {
        technologies.add(new Technology("Agriculture", 20, new ArrayList<>(),0));
        technologies.add(new Technology("Animal Husbandry", 35, new ArrayList<>(){{add("Agriculture");}},1));
        technologies.add(new Technology("Archery",35,new ArrayList<>(){{add("Agriculture");}},1));
        technologies.add(new Technology("Bronze Working",55,new ArrayList<>(){{add("Mining");}},2));
        technologies.add(new Technology("Calendar",70,new ArrayList<>(){{add("Pottery");}},2));
        technologies.add(new Technology("Masonry",55,new ArrayList<>(){{add("Mining");}},2));
        technologies.add(new Technology("Mining",35,new ArrayList<>(){{add("Agriculture");}},1));
        technologies.add(new Technology("Pottery",35,new ArrayList<>(){{add("Agriculture");}},1));
        technologies.add(new Technology("Wheel",55,new ArrayList<>(){{add("Animal Husbandry");}},2));
        technologies.add(new Technology("Trapping",55,new ArrayList<>(){{add("Animal Husbandry");}},2));
        technologies.add(new Technology("Writing",55,new ArrayList<>(){{add("Pottery");}},2));
        technologies.add(new Technology("Construction",100,new ArrayList<>(){{add("Masonry");}},3));
        technologies.add(new Technology("Horseback Riding",100,new ArrayList<>(){{add("Wheel");}},3));
        technologies.add(new Technology("Iron Working",150,new ArrayList<>(){{add("Bronze Working");}},4));
        technologies.add(new Technology("Mathematics",100,new ArrayList<>(){{add("Wheel");}{add("Archery");}},3));
        technologies.add(new Technology("Philosophy",100,new ArrayList<>(){{add("Writing");}},4));
        technologies.add(new Technology("Chivalry",440,new ArrayList<>(){{add("Civil Service");}{add("Horseback Riding");}{add("Currency");}},6));
        technologies.add(new Technology("Civil Service",400,new ArrayList<>(){{add("Philosophy");}{add("Trapping");}},5));
        technologies.add(new Technology("Currency",250,new ArrayList<>(){{add("Mathematics");}},4));
        technologies.add(new Technology("Education",440,new ArrayList<>(){{add("Theology");}},6));
        technologies.add(new Technology("Engineering",250,new ArrayList<>(){{add("Mathematics");}{add("Construction");}},4));
        technologies.add(new Technology("Machinery",440,new ArrayList<>(){{add("Engineering");}},6));
        technologies.add(new Technology("Metal Casting",240,new ArrayList<>(){{add("Iron Working");}},5));
        technologies.add(new Technology("Physics",240,new ArrayList<>(){{add("Engineering");}{add("Metal Casting");}},6));
        technologies.add(new Technology("Steel",440,new ArrayList<>(){{add("Metal Casting");}},6));
        technologies.add(new Technology("Theology",250,new ArrayList<>(){{add("Calendar");}{add("Philosophy");}},5));
        technologies.add(new Technology("Acoustics",650,new ArrayList<>(){{add("Education");}},7));
        technologies.add(new Technology("Archaeology",1300,new ArrayList<>(){{add("Acoustics");}},9));
        technologies.add(new Technology("Banking",650,new ArrayList<>(){{add("Education");}{add("Chivalry");}},7));
        technologies.add(new Technology("Chemistry",900,new ArrayList<>(){{add("Gunpowder");}},8));
        technologies.add(new Technology("Economics",900,new ArrayList<>(){{add("Banking");}{add("Printing Press");}},8));
        technologies.add(new Technology("Fertilizer",1300,new ArrayList<>(){{add("Chemistry");}},9));
        technologies.add(new Technology("Gunpowder",680,new ArrayList<>(){{add("Physics");}{add("Steel");}},7));
        technologies.add(new Technology("Metallurgy",900,new ArrayList<>(){{add("Gunpowder");}},8));
        technologies.add(new Technology("Military Science",1300,new ArrayList<>(){{add("Economics");}{add("Chemistry");}},9));
        technologies.add(new Technology("Printing Press",650,new ArrayList<>(){{add("Machinery");}{add("Physics");}},7));
        technologies.add(new Technology("Rifling",1425,new ArrayList<>(){{add("Metallurgy");}},9));
        technologies.add(new Technology("Scientific Theory",1300,new ArrayList<>(){{add("Acoustics");}},9));
        technologies.add(new Technology("Biology",1680,new ArrayList<>(){{add("Archaeology");}{add("Scientific Theory");}},10));
        technologies.add(new Technology("Combustion",2200,new ArrayList<>(){{add("Replaceable Parts");}{add("Railroad");}{add("Dynamite");}},12));
        technologies.add(new Technology("Dynamite",1900,new ArrayList<>(){{add("Fertilizer");}{add("Rifling");}},10));
        technologies.add(new Technology("Electricity",1900,new ArrayList<>(){{add("Biology");}{add("Steam Power");}},10));
        technologies.add(new Technology("Radio",2200,new ArrayList<>(){{add("Electricity");}},11));
        technologies.add(new Technology("Railroad",1900,new ArrayList<>(){{add("Steam Power");}},11));
        technologies.add(new Technology("Replaceable Parts",1900,new ArrayList<>(){{add("Steam Power");}},11));
        technologies.add(new Technology("Steam Power",1680,new ArrayList<>(){{add("Scientific Theory");}{add("Military Science");}},10));
    }

    public static ArrayList<Technology> getTechnologies() {
        return technologies;
    }

    public static Technology getTechnologyByName(String name){
        for (Technology technology : technologies) {
            if(technology.getName().equals(name)){
                return technology;
            }
        }
        return null;
    }
}
