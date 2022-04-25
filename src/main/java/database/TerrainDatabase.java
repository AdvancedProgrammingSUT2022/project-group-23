package database;

import model.Feature;
import model.Terrain;
import model.Tile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class TerrainDatabase {
    private static Random random = new Random();
    private static ArrayList<Terrain> terrains = new ArrayList<>();
    private static ArrayList<Feature> features = new ArrayList<>();
    private static HashMap<String, ArrayList<String>> possibleFeaturesForTerrains = new HashMap<>();
    private static HashMap<String, ArrayList<String>> possibleResourcesForTerrains = new HashMap<>();
    private static HashMap<String, ArrayList<String>> possibleResourcesForFeatures = new HashMap<>();

    static {
        terrains.add(new Terrain("Dessert" , 0, 0, 0, 1, -33));
        terrains.add(new Terrain("Grassland" , 0, 2, 0, 1, -33));
        terrains.add(new Terrain("Hill" , 0, 0, 2, 2, 25));
        terrains.add(new Terrain("Mountain" , 0, 0, 0, -1, 0));
        terrains.add(new Terrain("Ocean" , 0, 0, 0, -1, 0));
        terrains.add(new Terrain("Plain" , 0, 1, 1, 1, -33));
        terrains.add(new Terrain("Snow" , 0, 0, 0, 1, -33));
        terrains.add(new Terrain("Tundra" , 0, 1, 0, 1, -33));

        features.add(new Feature("Flood Plain", 0, 2 ,0, 1, -33));
        features.add(new Feature("Forest", 0, 1 ,1, 2, 25));
        features.add(new Feature("Ice", 0, 0 ,0, -1, 0));
        features.add(new Feature("Jungle", 0, 1 ,-1, 2, 25));
        features.add(new Feature("Marsh", 0, -1 ,0, 2, -33));
        features.add(new Feature("Oasis", 1, 3 ,0, 1, -33));

        possibleFeaturesForTerrains.put("Desert",new ArrayList<>(){{add("Flood Plain"); add("Oasis");}});
        possibleFeaturesForTerrains.put("Grassland",new ArrayList<>(){{add("Forest"); add("Marsh");}});
        possibleFeaturesForTerrains.put("Hill",new ArrayList<>(){{add("Forest"); add("Jungle");}});
        possibleFeaturesForTerrains.put("Plain",new ArrayList<>(){{add("Forest"); add("Jungle");}});
        possibleFeaturesForTerrains.put("Tundra",new ArrayList<>(){{add("Forest");}});


        possibleResourcesForTerrains.put("Desert",new ArrayList<>(){{add("Sheep"); add("Iron"); add("Cotton"); add("Gems");add("Gold");add("Incense"); add("Marble");add("Silver");}});
        //TODO fill the rest of hash map

        possibleResourcesForFeatures.put("Flood Plain",new ArrayList<>(){{add("Wheat");add("Sugar");}});
        //TODO fill the rest of hash map



    }
    public static void addRandomTerrainAndFeatureToTile(Tile tile){
        Terrain terrain = null;
        Feature feature = null;
        if(random.nextInt(15) == 0){
            for(Feature tempFeature : features){
                if(tempFeature.getName().equals("Ice")){
                    feature = tempFeature;
                    break;
                }
            }
        }
        else {
            terrain = terrains.get(random.nextInt(terrains.size()));
            if (possibleFeaturesForTerrains.containsKey(terrain.getName())) {
                ArrayList<String> terrainPossibleFeatures = possibleFeaturesForTerrains.get(terrain.getName());
                String featureName = terrainPossibleFeatures.get(random.nextInt(terrainPossibleFeatures.size()));
                for (Feature tempFeature : features) {
                    if (tempFeature.getName().equals(featureName)) {
                        feature = tempFeature;
                        break;
                    }
                }
            }
        }
        tile.setTerrain(terrain);
        tile.setFeature(feature);
    }
}
