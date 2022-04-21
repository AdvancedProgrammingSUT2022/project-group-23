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

    static {
        terrains.add(new Terrain("dessert" , 0, 0, 0, 1, -33));
        terrains.add(new Terrain("grassland" , 0, 2, 0, 1, -33));
        terrains.add(new Terrain("hills" , 0, 0, 2, 2, 25));
        terrains.add(new Terrain("mountain" , 0, 0, 0, -1, 0));
        terrains.add(new Terrain("ocean" , 0, 0, 0, -1, 0));
        terrains.add(new Terrain("plain" , 0, 1, 1, 1, -33));
        terrains.add(new Terrain("snow" , 0, 0, 0, 1, -33));
        terrains.add(new Terrain("tundra" , 0, 1, 0, 1, -33));

        features.add(new Feature("flood plains", 0, 2 ,0, 1, -33));
        features.add(new Feature("forest", 0, 1 ,1, 2, 25));
        features.add(new Feature("ice", 0, 0 ,0, -1, 0));
        features.add(new Feature("jungle", 0, 1 ,-1, 2, 25));
        features.add(new Feature("marsh", 0, -1 ,0, 2, -33));
        features.add(new Feature("oasis", 1, 3 ,0, 1, -33));

        possibleFeaturesForTerrains.put("desert",new ArrayList<>(){{add("flood plains"); add("oasis");}});
        possibleFeaturesForTerrains.put("grassland",new ArrayList<>(){{add("forest"); add("marsh");}});
        possibleFeaturesForTerrains.put("hills",new ArrayList<>(){{add("forest"); add("jungle");}});
        possibleFeaturesForTerrains.put("plain",new ArrayList<>(){{add("forest"); add("jungle");}});
        possibleFeaturesForTerrains.put("tundra",new ArrayList<>(){{add("forest");}});

    }
    public static void addRandomTerrainAndFeatureToTile(Tile tile){
        Terrain terrain = null;
        Feature feature = null;
        if(random.nextInt(15) == 0){
            for(Feature tempFeature : features){
                if(tempFeature.getName().equals("ice")){
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
