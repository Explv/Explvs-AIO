package activities.skills.crafting;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import util.Location;

public enum CraftingType {
    SPINNING(new Location("Lumbridge Castle", new Area(new Position(3213, 3217, 1), new Position(3208, 3212, 1))),
            new Location("Seers' Village", new Area(new Position(2710, 3470, 1), new Position(2716, 3473, 1)))
    ),
    WEAVING(new Location("Falador", new Area(3038, 3285, 3040, 3289))),
    POTTERY(new Location("Barbarian Village", new Area(3082, 3407, 3087, 3411))),
    ARMOUR(CraftingLocations.BANK_LOCATION),
    SNELM(CraftingLocations.BANK_LOCATION),
    MOLTEN_GLASS(CraftingLocations.FURNACE_LOCATIONS),
    GLASS(CraftingLocations.BANK_LOCATION),
    JEM_CUTTING(CraftingLocations.BANK_LOCATION),
    JEWELLERY(CraftingLocations.FURNACE_LOCATIONS),
    WEAPONRY(CraftingLocations.BANK_LOCATION);

    public Location[] locations;

    CraftingType(final Location... locations) {
        this.locations = locations;
    }

    @Override
    public String toString() {
        char[] name = name().toLowerCase().toCharArray();
        name[0] = Character.toUpperCase(name[0]);
        return new String(name);
    }

    private interface CraftingLocations {
        Location BANK_LOCATION = new Location("Bank", null);
        Location[] FURNACE_LOCATIONS = {
                new Location("Al-Kharid", new Area(3272, 3184, 3279, 3188)),
                new Location("Edgeville", new Area(3105, 3496, 3110, 3501)),
                new Location("Falador", new Area(
                        new int[][]{
                                {2970, 3368},
                                {2970, 3377},
                                {2974, 3377},
                                {2974, 3375},
                                {2978, 3375},
                                {2978, 3373},
                                {2976, 3373},
                                {2976, 3368}
                        }
                ))
        };
    }
}
