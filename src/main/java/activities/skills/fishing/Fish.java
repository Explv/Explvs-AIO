package activities.skills.fishing;

import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.model.Item;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Fish {

    SHRIMP("Shrimps", FishingMethod.NET, 1, FishingLocation.smallNetBaitLocations),
    SARDINE("Sardine", FishingMethod.BAIT, 5, FishingLocation.smallNetBaitLocations),
    //KARAMBWANJI ("Karambwanji", FishingMethod.NET, 5),
    HERRING("Herring", FishingMethod.BAIT, 10, FishingLocation.smallNetBaitLocations),
    ANCHOVIES("Anchovies", FishingMethod.NET, 15, FishingLocation.smallNetBaitLocations),
    MACKEREL("Mackerel", FishingMethod.BIG_NET, 16, FishingLocation.bigNetHarpoonLocations),
    OYSTER("Oyster", FishingMethod.BIG_NET, 16, FishingLocation.bigNetHarpoonLocations),
    CASKETS("Casket", FishingMethod.BIG_NET, 16, FishingLocation.bigNetHarpoonLocations),
    TROUT("Trout", FishingMethod.LURE, 20, FishingLocation.lureBaitLocations),
    COD("Cod", FishingMethod.BIG_NET, 23, FishingLocation.bigNetHarpoonLocations),
    PIKE("Pike", FishingMethod.BAIT, 25, FishingLocation.lureBaitLocations),
    //SLIMY_EEL ("Slimy eel", FishingMethod.BAIT, 28),
    SALMON("Salmon", FishingMethod.LURE, 30, FishingLocation.lureBaitLocations),
    //GIANT_FROGSPAWN ("Giant frogspawn", FishingMethod.NET, 33),
    TUNA("Tuna", FishingMethod.HARPOON, 35, FishingLocation.tunaSwordfishLocations),
    RAINBOW_FISH("Rainbow fish", FishingMethod.STRIPY_LURE, 38, FishingLocation.lureBaitLocations),
    //CAVE_EEL ("Cave eel", FishingMethod.BAIT, 38),
    LOBSTER("Lobster", FishingMethod.CAGE, 40, FishingLocation.cageHarpoonLocations),
    BASS("Bass", FishingMethod.BIG_NET, 46, FishingLocation.bigNetHarpoonLocations),
    SWORDFISH("Swordfish", FishingMethod.HARPOON, 50, FishingLocation.tunaSwordfishLocations),
    //LAVA_EEL ("Lava eel", FishingMethod.OILY_BAIT, 53),
    MONKFISH("Monkfish", FishingMethod.NET, 62, FishingLocation.PISCATORIS_FISHING_COLONY),
    //KARAMBWAN ("Karambwan", FishingMethod.VESSEL, 65),
    SHARK("Shark", FishingMethod.HARPOON, 76, FishingLocation.bigNetHarpoonLocations);
    //SEA_TURTLE ("Sea turtle", FishingMethod.TRAWLER, 79),
    //MANTA_RAY ("Manta ray", FishingMethod.TRAWLER, 81),
    //ANGLERFISH ("Anglerfish", FishingMethod.SANDWORM_BAIT, 82),
    //DARK_CRAB ("Dark crab", FishingMethod.BAIT_POT, 85),
    //SACRED_EEL ("Sacred eel", FishingMethod.SWAMP_BAIT, 87);

    public FishingMethod fishingMethod;
    public FishingLocation[] locations;
    String name;
    int lvlRequirement;

    public static final Set<String> COOKED_FISH_NAMES = Stream.of(Fish.values()).map(fish -> fish.name).collect(Collectors.toSet());
    public static final Set<String> RAW_FISH_NAMES = Stream.of(Fish.values()).map(Fish::getRawFishName).collect(Collectors.toSet());

    public static final Filter<Item> COOKED_FISH_FILTER = item -> COOKED_FISH_NAMES.contains(item.getName());
    public static final Filter<Item> RAW_FISH_FILTER = item -> RAW_FISH_NAMES.contains(item.getName());

    Fish(final String name, final FishingMethod fishingMethod, final int lvlRequirement, final FishingLocation... locations) {
        this.name = name;
        this.fishingMethod = fishingMethod;
        this.lvlRequirement = lvlRequirement;
        this.locations = locations;
    }

    public String getRawFishName() {
        return "Raw " + name.toLowerCase();
    }

    @Override
    public String toString() {
        return name;
    }
}
