package database;

import model.Building;

import java.util.ArrayList;

public class BuildingDataBase {
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
