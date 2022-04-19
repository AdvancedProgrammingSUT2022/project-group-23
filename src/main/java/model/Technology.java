package model;

public class Technology {
    private String name;
    private int cost;

    Technology(String name){
        this.name=name;
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
}
