package database;

import model.MilitaryUnit;
import model.Unit;

import java.util.ArrayList;

public class UnitsDatabase {
    private static ArrayList<Unit> units = new ArrayList<>();
    static {
        units.add(new MilitaryUnit("Archer", 70, 2, 4, 6, 2, "Archery", null));
    }

    public static ArrayList<Unit> getUnits() {
        return units;
    }
}
