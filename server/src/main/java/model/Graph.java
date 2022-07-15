package model;

import controller.GameController;
import controller.UnitController;

import java.util.*;

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


    public ArrayList<Tile> getVisibleTiles(int s, int range) {
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
            if(distance[s] > range)continue;
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
    public ArrayList<Tile> getTilesAtDistance(int s, int range){
        ArrayList<Tile> tilesAtDistnace = new ArrayList<>();

        int distance[] = new int[V];
        for (int i = 0; i < V; i++) {
            distance[i] = 100000000;
        }

        LinkedList<Integer> queue = new LinkedList();

        distance[s] = 0;
        queue.add(s);

        while (queue.size() != 0) {
            s = queue.poll();
            if(distance[s] == range){
                tilesAtDistnace.add(tiles[s / mapWidth][s % mapWidth]);
                continue;
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
        return tilesAtDistnace;
    }
    public ArrayList<Integer> getShortestPath(int s, int f, int userId, UnitController unitController){
        int distance[] = new int[V];
        int parent[] = new int[V];
        for (int i = 0; i < V; i++) {
            distance[i] = 100000000;
        }

        LinkedList<Integer> queue = new LinkedList();

        distance[s] = 0;
        parent[s] = s;
        queue.add(s);

        while (queue.size() != 0) {
            s = queue.poll();
            if(s == f)break;
            Iterator<Integer> i = adj[s].listIterator();
            while (i.hasNext()) {
                int n = i.next();
                if (distance[s] + 1 < distance[n] && tiles[n / mapWidth][n % mapWidth].getMovementCost() != -1 ){//&&
                      //  !tiles[n / mapWidth][n % mapWidth].getVisibilityForUser(userId).equals("fog of war")) {

                    if(GameController.getSelectedUnit() instanceof MilitaryUnit && unitController.getTileCombatUnit(n / mapWidth,n % mapWidth) != null)continue;
                    if(!(GameController.getSelectedUnit() instanceof MilitaryUnit) && unitController.getTileNonCombatUnit(n / mapWidth,n % mapWidth) != null)continue;
                    distance[n] = distance[s] + 1;
                    parent[n] = s;
                    queue.add(n);
                }
            }
        }
        ArrayList<Integer> path = new ArrayList<>();
        int vertex = f;
        if(parent[vertex] == 0) return null;
        while (parent[vertex] != vertex){
            path.add(vertex);
            vertex = parent[vertex];
        }
        //path.add(vertex);
        Collections.reverse(path);
        return path;
    }
}