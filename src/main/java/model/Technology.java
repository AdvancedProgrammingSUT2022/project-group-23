package model;

import java.util.ArrayList;

public class Technology {
    private String name;
    private int cost;
    private int treePlace;

    private ArrayList<String> prerequisiteTechnologies;

    public Technology(String name, int cost, ArrayList<String> prerequisiteTechnologies,int treePlace) {
        this.name = name;
        this.cost = cost;
        this.prerequisiteTechnologies = prerequisiteTechnologies;
        this.treePlace=treePlace;
    }

    public String getName () {
        return name;
    }

    public int getCost () {
        return cost;
    }

    public void setName (String name) {
        this.name = name;
    }

    public void setCost (int cost) {
        this.cost = cost;
    }

    public ArrayList<String> getPrerequisiteTechnologies () {
        return prerequisiteTechnologies;
    }

    public int getTreePlace () {
        return treePlace;
    }
}
