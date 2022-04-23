package database;

import model.Resource;

import java.util.ArrayList;

public class ResourceDatabase {
    private static ArrayList<Resource> resources = new ArrayList<>();
    static {
        resources.add(new Resource("banana", 0, 1, 0, "Plantation",null));
    }

    public static ArrayList<Resource> getResources() {
        return resources;
    }
}
