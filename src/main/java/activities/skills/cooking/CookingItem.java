package activities.skills.cooking;

import util.item_requirement.ItemReq;

import java.util.Arrays;

public enum CookingItem {

    COOKED_BEEF("Raw meat", CookingType.MEAT, 1, new ItemReq("Raw beef", 1)),
    COOKED_RAT_MEAT("Raw meat", CookingType.MEAT, 1, new ItemReq("Raw rat meat", 1)),
    COOKED_BEAR_MEAT("Raw meat", CookingType.MEAT, 1, new ItemReq("Raw bear meat", 1)),
    COOKED_CHICKEN("Raw chicken", CookingType.MEAT, 1, new ItemReq("Raw chicken", 1)),
    COOKED_RABBIT("Raw rabbit", CookingType.MEAT, 1, new ItemReq("Raw rabbit", 1)),
    ROAST_RABBIT("Raw rabbit", CookingType.MEAT, 1, new ItemReq("Raw rabbit", 1)),
    //CRAB_MEAT(CookingType.MEAT, 1, new ItemReq(, 1)),

    SHRIMPS("Raw shrimps", CookingType.MEAT, 1, new ItemReq("Raw shrimps", 1)),
    KARAMBWANJI("Raw karambwanji", CookingType.MEAT, 1, new ItemReq("Raw karambwanji", 1)),
    SARDINE("Raw sardine", CookingType.MEAT, 1, new ItemReq("Raw sardine", 1)),
    ANCHOVIES("Raw anchovies", CookingType.MEAT, 1, new ItemReq("Raw anchovies", 1)),
    HERRING("Raw herring", CookingType.MEAT, 1, new ItemReq("Raw herring", 1)),
    MACKEREL("Raw mackerel", CookingType.MEAT, 1, new ItemReq("Raw mackerel", 1)),
    TROUT("Raw trout", CookingType.MEAT, 1, new ItemReq("Raw trout", 1)),
    COD("Raw cod", CookingType.MEAT, 1, new ItemReq("Raw cod", 1)),
    PIKE("Raw pike", CookingType.MEAT, 1, new ItemReq("Raw pike", 1)),
    SALMON("Raw salmon", CookingType.MEAT, 1, new ItemReq("Raw salmon", 1)),
    SLIMY_EEL("Raw slimy eel", CookingType.MEAT, 1, new ItemReq("Raw slimy eel", 1)),
    TUNA("Raw tuna", CookingType.MEAT, 1, new ItemReq("Raw tuna", 1)),
    KARAMBWAN("Raw karambwan", CookingType.MEAT, 1, new ItemReq("Raw karambwan", 1)),
    CAVE_EEL("Raw cave eel", CookingType.MEAT, 1, new ItemReq("Raw cave eel", 1)),
    LOBSTER("Raw lobster", CookingType.MEAT, 1, new ItemReq("Raw lobster", 1)),
    BASS("Raw bass", CookingType.MEAT, 1, new ItemReq("Raw bass", 1)),
    SWORDFISH("Raw swordfish", CookingType.MEAT, 1, new ItemReq("Raw swordfish", 1)),
    LAVA_EEL("Raw lava eel", CookingType.MEAT, 1, new ItemReq("Raw lava eel", 1)),
    MONKFISH("Raw monkfish", CookingType.MEAT, 1, new ItemReq("Raw monkfish", 1)),
    SACRED_EEL("Raw sacred eel", CookingType.MEAT, 1, new ItemReq("Raw sacred eel", 1)),
    SHARK("Raw shark", CookingType.MEAT, 1, new ItemReq("Raw shark", 1)),
    SEA_TURTLE("Raw sea turtle", CookingType.MEAT, 1, new ItemReq("Raw sea turtle", 1)),
    ANGLERFISH("Raw anglerfish", CookingType.MEAT, 1, new ItemReq("Raw anglerfish", 1)),
    DARK_CRAB("Raw dark crab", CookingType.MEAT, 1, new ItemReq("Raw dark crab", 1)),
    MANTA_RAY("Raw manta ray", CookingType.MEAT, 1, new ItemReq("Raw manta ray", 1));

    /*
    REDBERRY_PIE("Redberry pie", CookingType.PIE, 1, new ItemReq("Pie dish", 1), new ItemReq("Pastry dough", 1), new ItemReq("Redberries", 1)),
    MEAT_PIE("Meat pie", CookingType.PIE, 1, new ItemReq("Pie dish", 1), new ItemReq("Pastry dough", 1), new ItemReq("Cooked meat", 1)),
    MUD_PIE("Mud pie", CookingType.PIE, 1, new ItemReq("Pie dish", 1), new ItemReq("Pastry dough", 1), new ItemReq("Redberries", 1)),
    APPLE_PIE("Apple pie", CookingType.PIE, 1, new ItemReq("Pie dish", 1), new ItemReq("Pastry dough", 1), new ItemReq("Redberries", 1)),
    GARDEN_PIE("Garden pie", CookingType.PIE, 1, new ItemReq("Pie dish", 1), new ItemReq("Pastry dough", 1), new ItemReq("Redberries", 1)),
    FISH_PIE("Fish pie", CookingType.PIE, 1, new ItemReq("Pie dish", 1), new ItemReq("Pastry dough", 1), new ItemReq("Redberries", 1)),
    ADMIRAL_PIE("Admiral pie", CookingType.PIE, 1, new ItemReq("Pie dish", 1), new ItemReq("Pastry dough", 1), new ItemReq("Redberries", 1)),
    WILD_PIE("Wild pie", CookingType.PIE, 1, new ItemReq("Pie dish", 1), new ItemReq("Pastry dough", 1), new ItemReq("Redberries", 1)),
    SUMMER_PIE("Summer pie", CookingType.PIE, 1, new ItemReq("Pie dish", 1), new ItemReq("Pastry dough", 1), new ItemReq("Redberries", 1));

    PLAIN_PIZZA,
    MEAT_PIZZA,
    ANCHOVY_PIZZA,
    PINEAPPLE_PIZZA,

    COOKED_FISHCAKE,
    CAKE,
    CHOCOLATE_CAKE,

    JUG_OF_WINE,

    SWEETCORN;*/

    String name;
    CookingType cookingType;
    int cookingLvl;
    ItemReq[] itemReqs;
    private CookingItem base;

    CookingItem(final String name, final CookingType cookingType, final int cookingLvl, final ItemReq... itemReqs) {
        this.name = name;
        this.cookingType = cookingType;
        this.cookingLvl = cookingLvl;
        this.itemReqs = itemReqs;
    }

    CookingItem(final String name, final CookingType cookingType, final int cookingLvl, final CookingItem base, final ItemReq... itemReqs) {
        this.name = name;
        this.cookingType = cookingType;
        this.cookingLvl = cookingLvl;
        this.itemReqs = itemReqs;
        this.base = base;
    }

    public static CookingItem[] getAllWithType(final CookingType cookingType) {
        return Arrays.stream(values()).filter(item -> item.cookingType == cookingType).toArray(CookingItem[]::new);
    }

    @Override
    public String toString() {
        return name;
    }
}
