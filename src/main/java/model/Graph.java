package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class Graph {
    private int V;

    private final int mapWidth;

    private final int mapHeight;
    private LinkedList<Integer> adj[];

    private Tile[][] tiles;

    public Graph(int mapHeight, int mapWidth, Tile[][] tiles) {
        V = mapHeight * mapWidth;
        this.mapHeight = mapHeight;
        this.mapWidth = mapWidth;
        adj = new LinkedList[V];
        for (int i = 0; i < V; ++i)
            adj[i] = new LinkedList();
        this.tiles = tiles;
    }

    public void addEdge(int v, int w) {
        adj[v].add(w);
    }


    public ArrayList<Tile> getVisibleTiles(int s) {
        ArrayList<Tile> visibleTiles = new ArrayList<>();

        int distance[] = new int[V];
        for (int i = 0; i < V; i++) {
            distance[i] = 100000000;
        }

        LinkedList<Integer> queue = new LinkedList();

        distance[s] = 0;
        queue.add(s);

        while (queue.size() != 0) {
            s = queue.poll();
            if(distance[s] > 2)continue;
            visibleTiles.add(tiles[s / mapWidth][s % mapWidth]);
            if(distance[s] != 0 && tiles[s / mapWidth][s % mapWidth].getTerrain() != null){
                String terrainName = tiles[s / mapWidth][s % mapWidth].getTerrain().getName();
                if(terrainName.equals("Hill") || terrainName.equals("Mountain"))continue;
            } else if(distance[s] != 0 && tiles[s / mapWidth][s % mapWidth].getFeature() != null){
                String featureName = tiles[s / mapWidth][s % mapWidth].getFeature().getName();
                if(featureName.equals("Forest") || featureName.equals("Jungle"))continue;
            }

            Iterator<Integer> i = adj[s].listIterator();
            while (i.hasNext()) {
                int n = i.next();
                if (distance[s] + 1 < distance[n]) {
                    distance[n] = distance[s] + 1;
                    queue.add(n);
                }
            }
        }
        return visibleTiles;
    }
}