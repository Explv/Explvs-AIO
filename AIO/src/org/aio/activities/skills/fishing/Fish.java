package org.aio.activities.skills.fishing;

public enum Fish {

    SHRIMP ("Shrimps", FishingMethod.NET, 1, FishingLocation.smallNetBaitLocations),
    SARDINE ("Sardine", FishingMethod.BAIT, 5, FishingLocation.smallNetBaitLocations),
    //KARAMBWANJI ("Karambwanji", FishingMethod.NET, 5),
    HERRING ("Herring", FishingMethod.BAIT, 10, FishingLocation.smallNetBaitLocations),
    ANCHOVIES ("Anchovies", FishingMethod.NET, 15, FishingLocation.smallNetBaitLocations),
    MACKEREL ("Mackerel", FishingMethod.BIG_NET, 16, FishingLocation.bigNetHarpoonLocations),
    OYSTER ("Oyster", FishingMethod.BIG_NET, 16, FishingLocation.bigNetHarpoonLocations),
    CASKETS ("Casket", FishingMethod.BIG_NET, 16, FishingLocation.bigNetHarpoonLocations),
    TROUT ("Trout", FishingMethod.LURE, 20, FishingLocation.lureBaitLocations),
    COD ("Cod", FishingMethod.BIG_NET, 23, FishingLocation.bigNetHarpoonLocations),
    PIKE ("Pike", FishingMethod.BAIT, 25, FishingLocation.lureBaitLocations),
    //SLIMY_EEL ("Slimy eel", FishingMethod.BAIT, 28),
    SALMON ("Salmon", FishingMethod.LURE, 30, FishingLocation.lureBaitLocations),
    //GIANT_FROGSPAWN ("Giant frogspawn", FishingMethod.NET, 33),
    TUNA ("Tuna", FishingMethod.HARPOON, 35, FishingLocation.tunaSwordfishLocations),
    RAINBOW_FISH ("Rainbow fish", FishingMethod.STRIPY_LURE, 38, FishingLocation.lureBaitLocations),
    //CAVE_EEL ("Cave eel", FishingMethod.BAIT, 38),
    LOBSTER ("Lobster", FishingMethod.CAGE, 40, FishingLocation.cageHarpoonLocations),
    BASS ("Bass", FishingMethod.BIG_NET, 46, FishingLocation.bigNetHarpoonLocations),
    SWORDFISH ("Swordfish", FishingMethod.HARPOON, 50, FishingLocation.tunaSwordfishLocations),
    //LAVA_EEL ("Lava eel", FishingMethod.OILY_BAIT, 53),
    MONKFISH ("Monkfish", FishingMethod.NET, 62, FishingLocation.PISCATORIS_FISHING_COLONY),
    //KARAMBWAN ("Karambwan", FishingMethod.VESSEL, 65),
    SHARK ("Shark", FishingMethod.HARPOON, 76, FishingLocation.bigNetHarpoonLocations);
    //SEA_TURTLE ("Sea turtle", FishingMethod.TRAWLER, 79),
    //MANTA_RAY ("Manta ray", FishingMethod.TRAWLER, 81),
    //ANGLERFISH ("Anglerfish", FishingMethod.SANDWORM_BAIT, 82),
    //DARK_CRAB ("Dark crab", FishingMethod.BAIT_POT, 85),
    //SACRED_EEL ("Sacred eel", FishingMethod.SWAMP_BAIT, 87);

    String name;
    public FishingMethod fishingMethod;
    int lvlRequirement;
    public FishingLocation[] locations;

    Fish(final String name, final FishingMethod fishingMethod, final int lvlRequirement, final FishingLocation... locations){
        this.name = name;
        this.fishingMethod = fishingMethod;
        this.lvlRequirement = lvlRequirement;
        this.locations = locations;
    }

    @Override
    public String toString(){
        return name;
    }
}
