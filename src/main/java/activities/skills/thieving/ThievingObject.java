package activities.skills.thieving;

import org.osbot.rs07.api.map.Area;
import util.Location;

import java.util.Arrays;

public enum ThievingObject {

    MAN("Man", ThievingType.NPC, new Location("Lumbridge 1", new Area(3217, 3209, 3225, 3229)), new Location("Lumbridge 2", new Area(3207, 3209, 3216, 3227)), new Location("Lumbridge 3", new Area(3232, 3210, 3240, 3223)), new Location("Lumbridge 4", new Area(3234, 3234, 3216, 3247)), new Location("Edgeville", new Area(3105, 3504, 3087, 3516))),
    FARMER("Farmer", ThievingType.NPC, new Location("Lumbridge 1", new Area(3236, 3300, 3225, 3287)), new Location("Lumbridge 2", new Area(3232, 3302, 3222, 3311)), new Location("Ardougne", new Area(2634, 3374, 2655, 3355))),
    HAM_MEMBER("H.A.M. Member", ThievingType.NPC, new Location("H.A.M. Hideout", new Area(3174, 9640, 3160, 9623))),
    WARRIOR_WOMAN("Warrior woman", ThievingType.NPC, new Location("Varrock 1", new Area(3210, 3481, 3201, 3493)), new Location("Ardougne", new Area(2654, 3326, 2661, 3336))),
    AL_KHARID_WARRIOR("Al-Kharid warrior", ThievingType.NPC, new Location("Al-Kharid", new Area(3282, 3160, 3303, 3177))),
    ROGUE("Rogue", ThievingType.NPC, new Location("Wilderness", new Area(3085, 3904, 3072, 3918)), new Location("Rogue's Castle", new Area(3297, 3946, 3278, 3926))),
    CAVE_GOBLIN("Cave goblin", ThievingType.NPC, new Location("", null)), // need quest
    MASTER_FARMER("Master Farmer", ThievingType.NPC, new Location("Ardougne", new Area(2645, 3365, 2635, 3353)), new Location("Draynor Village", new Area(3086, 3255, 3072, 3245))),
    GUARD("Guard", ThievingType.NPC, new Location("Falador 1", new Area(2952, 3372, 2940, 3384)), new Location("Falador 2", new Area(2958, 3376, 2969, 3401)), new Location("Falador 3", new Area(3011, 3330, 3002, 3315)), new Location("Varrock 1", new Area(3165, 3438, 3184, 3416)), new Location("Varrock 2", new Area(3205, 3375, 3218, 3392)), new Location("Ardougne 1", new Area(2655, 3296, 2667, 3317)), new Location("Ardougne 2", new Area(2639, 3334, 2630, 3345))),
    //FREMMENIK_CITIZEN ("Sassilik", ThievingType.NPC, new Location("Rellekka 1", new Area(2630, 3655, 2626, 3651)), "Jennella", new Location("Rellekka 2", new Area(2644, 3653, 2639, 3649)), "Lanzig", new Location("Rellekka 3", new Area(2672, 3667, 2677, 3662))),
    BEARDED_POLLNIVNIAN_BANDIT("Bearded pollnivnian bandit", ThievingType.NPC, new Location("", null)), // blackhack?
    DESERT_BANDIT("Bandit", ThievingType.NPC, new Location("Bandit Camp 1", new Area(3183, 2987, 3188, 2980)), new Location("Bandit Camp 2", new Area(3176, 2976, 3169, 2970)), new Location("Bandit Camp 3", new Area(3157, 2989, 3164, 2976))),
    KNIGHT("Knight of Ardougne", ThievingType.NPC, new Location("Ardougne", new Area(2648, 3324, 2675, 3288))),
    YANILLE_WATCHMAN("Watchman", ThievingType.NPC, new Location("Yanille", new Area(2543, 3118, 2550, 3111).setPlane(1))),
    MENAPHITE_THUG("Menaphite thug", ThievingType.NPC, new Location("", null)), // blackjack?
    PALADIN("Paladin", ThievingType.NPC, new Location("Ardougne", new Area(2648, 3324, 2675, 3288))),
    GNOME("Gnome", ThievingType.NPC, new Location("Tree Gnome Village", new Area(2465, 3410, 2428, 3446)), new Location("Tree Gnome Village", new Area(2426, 3454, 2394, 3420))),
    HERO("Hero", ThievingType.NPC, new Location("Ardougne", new Area(2677, 3305, 2650, 3323))),
    ELF("Elf", ThievingType.NPC, new Location("Elf Camp", null)), // Add Elf Camp area

    BAKERS_STALL("Baker's stall", ThievingType.STALL, new Location("Ardougne", new Area(2668, 3312, 2668, 3312))),
    SILK_STALL("Silk stall", ThievingType.STALL, new Location("Ardougne", new Area(2663, 3316, 2662, 3316))),
    TEA_STALL("Tea stall", ThievingType.STALL, new Location("Varrock", new Area(3271, 3412, 3268, 3409))),
    //WINE_STALL ("Wine Stall", ThievingType.STALL, new Location("Draynor Village", new Area(3084, 3253, 3083, 3253))),
    SEED_STALL("Seed Stall", ThievingType.STALL, new Location("Draynor Village", new Area(3074, 3250, 3074, 3249)));


    //VEGETABLE_STALL ("Vegetable Stall", ThievingType.STALL, new Location("", null)), // Add Miscellania + Etceteria area
   /* CRAFTING_STALL ("Crafting Stall", ThievingType.STALL, new Location("", new Area(2780, 2795, 2782, 2793))), // Add Keldagrim area
    FOOD_STALL ("Food Stall", ThievingType.STALL, new Location("", new Area(2769, 2790, 2767, 2788))),
    GENERAL_STALL ("General Stall", ThievingType.STALL, new Location("Ape Atoll", new Area(2754, 2776, 2752, 2774))),
    FUR_STALL ("Fur stall", ThievingType.STALL, new Location("Ardougne", new Area(2664, 3298, 2662, 3295)), new Location("Rellekka 1", new Area(2646, 3682, 2649, 3679)), new Location("Rellekka 2", new Area(2640, 3678, 2637, 3675))),
    FISH_STALL ("Fish stall", ThievingType.STALL, new Location("Rellekka 1", new Area(2646, 3678, 2649, 3675)), new Location("Rellekka 2", new Area(2640, 3671, 2637, 3674)), new Location ("Rellekka 3", new Area(2640, 3679, 2637, 3682))), // Add Miscellenia + Etceteria area
    CROSSBOW_STALL ("Crossbow Stall", ThievingType.STALL, new Location("", null)), // Add Keldagrim area
    SILVER_STALL ("Silver stall", ThievingType.STALL, new Location("Ardougne", new Area(2658, 3313, 2656, 3316))), // Add Keldagrim area
    SPICE_STALL ("Spice stall", ThievingType.STALL, new Location("Ardougne", new Area(2659, 3299, 2657, 3296))),
    MAGIC_STALL ("Magic Stall", ThievingType.STALL, new Location("Ape Atoll", new Area(2757, 2768, 2758, 2770))),
    SCIMITAR_STALL ("Scimitar Stall", ThievingType.STALL, new Location("Ape Atoll", new Area(2760, 2773, 2758, 2775))),
    GEM_STALL ("Gem stall", ThievingType.STALL, new Location("Ardougne", new Area(2666, 3303, 2669, 3305))), //Add Keldagrim area*/

    public Location[] locations;
    String name;
    ThievingType type;

    ThievingObject(final String name, final ThievingType type, final Location... locations) {
        this.name = name;
        this.type = type;
        this.locations = locations;
    }

    public static ThievingObject[] getAllObjectsWithType(ThievingType thievingType) {
        return Arrays.stream(values()).filter(obj -> obj.type == thievingType).toArray(ThievingObject[]::new);
    }

    @Override
    public String toString() {
        return name;
    }
}
