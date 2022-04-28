package database;

import model.Feature;
import model.Resource;
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
        terrains.add(new Terrain("Desert" , 0, 0, 0, 1, -33,30));
        terrains.add(new Terrain("Grassland" , 0, 2, 0, 1, -33,40));
        terrains.add(new Terrain("Hill" , 0, 0, 2, 2, 25,35));
        terrains.add(new Terrain("Mountain" , 0, 0, 0, -1, 0,35));
        terrains.add(new Terrain("Ocean" , 0, 0, 0, -1, 0,30));
        terrains.add(new Terrain("Plain" , 0, 1, 1, 1, -33,35));
        terrains.add(new Terrain("Snow" , 0, 0, 0, 1, -33,30));
        terrains.add(new Terrain("Tundra" , 0, 1, 0, 1, -33,35));

        features.add(new Feature("Flood Plain", 0, 2 ,0, 1, -33,10));
        features.add(new Feature("Forest", 0, 1 ,1, 2, 25,10));
        features.add(new Feature("Ice", 0, 0 ,0, -1, 0,-10));
        features.add(new Feature("Jungle", 0, 1 ,-1, 2, 25,-10));
        features.add(new Feature("Marsh", 0, -1 ,0, 2, -33,0));
        features.add(new Feature("Oasis", 1, 3 ,0, 1, -33,10));

        possibleFeaturesForTerrains.put("Desert",new ArrayList<>(){{add("Flood Plain"); add("Oasis");}});
        possibleFeaturesForTerrains.put("Grassland",new ArrayList<>(){{add("Forest"); add("Marsh");}});
        possibleFeaturesForTerrains.put("Hill",new ArrayList<>(){{add("Forest"); add("Jungle");}});
        possibleFeaturesForTerrains.put("Plain",new ArrayList<>(){{add("Forest"); add("Jungle");}});
        possibleFeaturesForTerrains.put("Tundra",new ArrayList<>(){{add("Forest");}});


        possibleResourcesForTerrains.put("Desert",new ArrayList<>(){{add("Sheep"); add("Iron"); add("Cotton"); add("Gems");add("Gold");add("Incense"); add("Marble");add("Silver");}});
        possibleResourcesForTerrains.put("Grassland",new ArrayList<>(){{add("Cattle"); add("Sheep"); add("Coal"); add("Horses");add("Iron");add("Cotton"); add("Gems");add("Gold");add("Marble");}});
        possibleResourcesForTerrains.put("Hill",new ArrayList<>(){{add("Deer"); add("Sheep"); add("Coal"); add("Iron");add("Gems");add("Gold"); add("Marble");}});
        possibleResourcesForTerrains.put("Mountain",new ArrayList<>(){});
        possibleResourcesForTerrains.put("Ocean",new ArrayList<>(){});
        possibleResourcesForTerrains.put("Plain",new ArrayList<>(){{add("Sheep"); add("Wheat"); add("Coal"); add("Horses");add("Iron");add("Cotton"); add("Gems");add("Gold");add("Incense");add("Ivory");add("Marble");}});
        possibleResourcesForTerrains.put("Snow",new ArrayList<>(){{add("Iron");}});
        possibleResourcesForTerrains.put("Tundra",new ArrayList<>(){{add("Deer"); add("Horses"); add("Iron"); add("Furs");add("Gems");add("Marble"); add("Silver");}});


        possibleResourcesForFeatures.put("Flood Plain",new ArrayList<>(){{add("Wheat");add("Sugar");}});
        possibleResourcesForFeatures.put("Forest",new ArrayList<>(){{add("Deer");add("Dyes");add("Furs");add("Gems");add("Silk");}});
        possibleResourcesForFeatures.put("Ice",new ArrayList<>(){});
        possibleResourcesForFeatures.put("Jungle",new ArrayList<>(){{add("Banana");add("Dyes");}});
        possibleResourcesForFeatures.put("Marsh",new ArrayList<>(){{add("Sugar");}});
        possibleResourcesForFeatures.put("Oasis",new ArrayList<>(){});



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
    public static void addRandomResourceToTile(Tile tile){
        Terrain terrain = tile.getTerrain();
        Feature feature = tile.getFeature();
        ArrayList<String> possibleResources = new ArrayList<>();
        if(terrain != null){
            possibleResources.addAll(possibleResourcesForTerrains.get(terrain.getName()));
        }
        if (feature != null){
            possibleResources.addAll(possibleResourcesForFeatures.get(feature.getName()));
        }
        if(possibleResources.isEmpty())return;
        if(random.nextInt(3) == 0) {
            String resourceName = possibleResources.get(random.nextInt(possibleResources.size()));
            for(Resource resource : ResourceDatabase.getResources()){
                if (resource.getName().equals(resourceName)){
                    tile.setResource(resource);
                    return;
                }
            }
        }
    }
}
