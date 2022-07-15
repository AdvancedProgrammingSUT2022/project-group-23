package database;

import model.Building;

import java.util.ArrayList;

public class BuildingDatabase {
    private static ArrayList<Building> buildings=new ArrayList<>();
    static {
        buildings.add(new Building("Palace",0,0,0,0,0,0,"none","none"));
        buildings.add(new Building("Barracks",80,0,0,0,0,1,null,"Bronze Working"));
        buildings.add(new Building("Granary",100,2,0,0,0,1,null,"Pottery"));
        buildings.add(new Building("Library",80,0,0,1,0,1,null,"Writing"));
        buildings.add(new Building("Monument",60,0,0,0,0,1,null,null));
        buildings.add(new Building("Walls",100,0,0,0,5,1,null,"Masonry"));
        buildings.add(new Building("Water Mill",120,2,0,0,0,2,null,"Wheel"));
        buildings.add(new Building("Armory",130,0,0,0,0,3,"Barracks","Iron Working"));
        buildings.add(new Building("Burial Tomb",120,0,2,0,0,0,null,"Philosophy"));
        buildings.add(new Building("Circus",150,0,3,0,0,3,null,"Horseback Riding"));
        buildings.add(new Building("Colosseum",150,0,4,0,0,3,null,"Construction"));
        buildings.add(new Building("Courthouse",200,0,0,0,0,5,null,"Mathematics"));
        buildings.add(new Building("Stable",100,0,0,0,0,1,null,"Horseback Riding"));
        buildings.add(new Building("Temple",120,0,0,0,0,2,"Monument","Philosophy"));
        buildings.add(new Building("Castle",200,0,0,0,7,3,"Walls","Chivalry"));
        buildings.add(new Building("Forge",150,0,0,0,0,2,null,"Metal Casting"));
        buildings.add(new Building("Garden",120,0,0,0,0,2,null,"Theology"));
        buildings.add(new Building("Market",120,0,0,0,0,0,null,"Currency"));
        buildings.add(new Building("Mint",120,0,0,0,0,0,null,"Currency"));
        buildings.add(new Building("Monastery",120,0,0,0,0,2,null,"Theology"));
        buildings.add(new Building("University",200,0,0,10,0,3,"Library","Education"));
        buildings.add(new Building("Workshop",100,0,0,0,0,2,null,"Metal Casting"));
        buildings.add(new Building("Bank",220,0,0,0,0,0,"Market","Banking"));
        buildings.add(new Building("Military Academy",350,0,0,0,0,3,"Barracks","Military Science"));
        buildings.add(new Building("Museum",350,0,0,0,0,3,"Opera House","Archaeology"));
        buildings.add(new Building("Opera House",220,0,0,0,0,3,"Temple","Acoustics"));
        buildings.add(new Building("Public School",350,0,0,10,0,3,"University","Scientific Theory"));
        buildings.add(new Building("Satrap’s Court",220,0,2,0,0,0,"Market","Banking"));
        buildings.add(new Building("Theater",300,0,4,0,0,5,"Colosseum","Printing Press"));
        buildings.add(new Building("Windmill",180,0,0,0,0,2,null,"Economics"));
        buildings.add(new Building("Arsenal",350,0,0,0,0,3,"Military Academy","Railroad"));
        buildings.add(new Building("Broadcast Tower",600,0,0,0,0,3,"Museum","Radio"));
        buildings.add(new Building("Factory",300,0,0,0,0,3,null,"Steam Power"));
        buildings.add(new Building("Hospital",400,0,0,0,0,2,null,"Biology"));
        buildings.add(new Building("Military Base",450,0,0,0,12,4,"Castle","Telegraph"));
        buildings.add(new Building("Stock Exchange",650,0,0,0,0,0,"Bank","Electricity"));
    }

    public static ArrayList<Building> getBuildings () {
        return buildings;
    }

    public static Building findBuilding(String name){
        for (Building building : buildings) {
            if(building.getName().equals(name)){
                return building;
            }
        }
        return null;
    }
}
