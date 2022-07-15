package database;

import model.MilitaryUnit;
import model.Unit;

import java.util.ArrayList;

public class UnitsDatabase {
    private static ArrayList<Unit> units = new ArrayList<>();
    static {
        units.add(new MilitaryUnit("Archer", 70, "Archery", 2, 4, 6, 2, "Archery", null));
        units.add(new MilitaryUnit("Chariot Archer", 60, "Mounted", 4, 3, 6, 2, "The Wheel", "Horses"));
        units.add(new MilitaryUnit("Scout", 25, "Recon", 2, 4, -1, -1, null, null));
        units.add(new MilitaryUnit("Spearman", 50, "Melee", 2, 7, -1, -1, "Bronze Working", null));
        units.add(new MilitaryUnit("Warrior", 40, "Melee", 2, 6, -1, -1, null, null));
        units.add(new MilitaryUnit("Catapult", 100, "Siege", 2, 4, 14, 2, "Mathematics", "Iron"));
        units.add(new MilitaryUnit("Horseman", 80,"Mounted", 4, 12, -1, -1, "Horseback Riding", "Horses"));
        units.add(new MilitaryUnit("Swordsman", 80,"Melee", 2, 11, -1, -1, "Iron Working", "Iron"));
        units.add(new MilitaryUnit("Crossbowman", 120,"Archery", 2, 6, 12, 2, "Machinery", null));
        units.add(new MilitaryUnit("Knight", 150, "Mounted", 3, 18, -1, -1, "Chivalry", "Horses"));
        units.add(new MilitaryUnit("Longswordsman", 150, "Melee", 3, 18, -1, -1, "Steel", "Iron"));
        units.add(new MilitaryUnit("Pikeman", 100, "Melee", 2, 10, -1, -1, "Civil Service", null));
        units.add(new MilitaryUnit("Trebuchet", 170, "Siege", 2, 6, 20, 2, "Physics", "Iron"));
        units.add(new MilitaryUnit("Canon", 250, "Siege", 2, 10, 26, 2, "Chemistry", null));
        units.add(new MilitaryUnit("Cavalry", 260, "Mounted", 3, 25, -1, -1, "Military Science", "Horses"));
        units.add(new MilitaryUnit("Lancer", 220, "Mounted", 4, 22, -1, -1, "Metallurgy", "Horses"));
        units.add(new MilitaryUnit("Musketman", 120, "Gunpowder", 2, 16, -1, -1, "Gunpowder", null));
        units.add(new MilitaryUnit("Rifleman", 200, "Gunpowder", 2, 25, 6, 2, "Rifling", null));
        units.add(new MilitaryUnit("Anti-Tank Gun", 300, "Gunpowder", 2, 32, -1, -1, "Replaceable Parts", null));
        units.add(new MilitaryUnit("Artillery", 420, "Siege", 2, 16, 32, 3, "Dynamite", null));
        units.add(new MilitaryUnit("Infantry", 300, "Gunpowder", 2, 36, -1, -1, "Replaceable Parts", null));
        units.add(new MilitaryUnit("Panzer", 450, "Armored", 5, 60, -1, -1, "Combustion", null));
        units.add(new MilitaryUnit("Tank", 450, "Armored",4, 50, -1, -1, "Combustion", null));
    }

    public static ArrayList<Unit> getUnits() {
        return units;
    }
}
