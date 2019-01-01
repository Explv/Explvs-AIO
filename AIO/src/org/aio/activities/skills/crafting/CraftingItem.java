package org.aio.activities.skills.crafting;

import org.aio.util.item_requirement.ItemReq;

import java.util.Arrays;

public enum CraftingItem {
    BALL_OF_WOOL ("Ball of Wool", "Ball of Wool", CraftingType.SPINNING, 1, 2.5f, new ItemReq("Wool", 1)),
    BOW_STRING ("Bow String", "Bow String", CraftingType.SPINNING, 10, 15, new ItemReq("Flax", 1)),
    CROSSBOW_STRING ("Crossbow String", "Sinew", CraftingType.SPINNING, 10, 15, new ItemReq("Sinew", 1)),
    CROSSBOW_STRING_ROOTS ("Crossbow String Roots", "Tree Roots", CraftingType.SPINNING, 10, 15, new ItemReq("Roots", 1)),
    MAGIC_STRING ("Magic String", "Magic Roots", CraftingType.SPINNING, 19, 30, new ItemReq("Magic roots", 1)),
    ROPE ("Rope", "Rope", CraftingType.SPINNING, 30, 25, new ItemReq("Yak hair", 1)),

    STRIP_OF_CLOTH ("Strip of cloth", "Cloth", CraftingType.WEAVING, 10, 12, new ItemReq("Ball of wool", 4)),
    EMPTY_SACK ("Empty sack", "Sack", CraftingType.WEAVING, 21, 38, new ItemReq("Jute fibre", 4)),
    BASKET ("Basket", "Basket", CraftingType.WEAVING, 36, 56, new ItemReq("Willow branch", 6)),

    POT ("Pot", "Pot", CraftingType.POTTERY, 1, 12.6f, new ItemReq("Soft clay", 1)),
    PIE_DISH ("Pie Dish", "Pie Dish", CraftingType.POTTERY, 7, 25, new ItemReq("Soft clay", 1)),
    BOWL ("Bowl", "Bowl", CraftingType.POTTERY, 8, 33, new ItemReq("Soft clay", 1)),
    PLANT_POT ("Plantpot", "Plantpot", CraftingType.POTTERY, 19, 37.5f, new ItemReq("Soft clay", 1)),
    POT_LID ("Pot lid", "Pot Lid", CraftingType.POTTERY, 25, 40, new ItemReq("Soft clay", 1)),

    LEATHER_GLOVES ("Leather gloves", "Leather gloves", CraftingType.ARMOUR, 1, 13.8f, new ItemReq("Needle"), new ItemReq("Leather", 1), new ItemReq("Thread", 1).setStackable()),
    LEATHER_BOOTS ("Leather boots", "Leather boots", CraftingType.ARMOUR, 7, 16.25f, new ItemReq("Needle"), new ItemReq("Leather", 1), new ItemReq("Thread", 1).setStackable()),
    COWL ("Leather cowl", "Leather cowl", CraftingType.ARMOUR, 9, 18.5f, new ItemReq("Needle"), new ItemReq("Leather", 1), new ItemReq("Thread", 1).setStackable()),
    LEATHER_VAMBRACES ("Leather vambraces", "Leather vambraces", CraftingType.ARMOUR, 11, 22, new ItemReq("Needle"), new ItemReq("Leather", 1), new ItemReq("Thread", 1).setStackable()),
    LEATHER_BODY ("Leather body", "Leather body", CraftingType.ARMOUR, 14, 25, new ItemReq("Needle"), new ItemReq("Leather", 1), new ItemReq("Thread", 1).setStackable()),
    LEATHER_CHAPS ("Leather chaps", "Leather chaps", CraftingType.ARMOUR, 18, 27, new ItemReq("Needle"), new ItemReq("Leather", 1), new ItemReq("Thread", 1).setStackable()),
    HARD_LEATHER_BODY ("Hard leather body", "Hardleather body", CraftingType.ARMOUR, 28, 35, new ItemReq("Needle"), new ItemReq("Hard Leather", 1), new ItemReq("Thread", 1).setStackable()),
    SPIKY_VAMBRACES ("Spiky vambraces", "Spiky vambraces", CraftingType.ARMOUR, 32, 6, new ItemReq("Leather vambraces", 1), new ItemReq("Kebbit claws", 1)),
    COIF ("Coifs", "Coif", CraftingType.ARMOUR, 38, 37, new ItemReq("Needle"), new ItemReq("Leather", 1), new ItemReq("Thread", 1).setStackable()),
    STUDDED_BODY ("Studded body", "Studded body", CraftingType.ARMOUR, 41, 40, new ItemReq("Leather body", 1), new ItemReq("Steel studs", 1)),
    STUDDED_CHAPS ("Studded chaps", "Studded chaps", CraftingType.ARMOUR, 44, 42, new ItemReq("Leather chaps", 1), new ItemReq("Steel studs", 1)),

    GREEN_DHIDE_VAMBRACES ("Green d'hide vambraces", "Green dragonhide vambraces", CraftingType.ARMOUR, 57, 62, new ItemReq("Needle"), new ItemReq("Green dragon leather", 1), new ItemReq("Thread", 1).setStackable()),
    GREEN_DHIDE_CHAPS ("Green d'hide chaps", "Green dragonhide chaps", CraftingType.ARMOUR, 60, 124, new ItemReq("Needle"), new ItemReq("Green dragon leather", 2), new ItemReq("Thread", 1).setStackable()),
    GREEN_DHIDE_BODY ("Green d'hide body", "Green dragonhide body", CraftingType.ARMOUR, 63, 186, new ItemReq("Needle"), new ItemReq("Green dragon leather", 3), new ItemReq("Thread", 1).setStackable()),
    BLUE_DHIDE_VAMBRACES ("Blue d'hide vambraces", "Blue dragonhide vambraces", CraftingType.ARMOUR, 66, 70, new ItemReq("Needle"), new ItemReq("Blue dragon leather", 1), new ItemReq("Thread", 1).setStackable()),
    BLUE_DHIDE_CHAPS ("Blue d'hide chaps", "Blue dragonhide chaps", CraftingType.ARMOUR, 68, 140, new ItemReq("Needle"), new ItemReq("Blue dragon leather", 2), new ItemReq("Thread", 1).setStackable()),
    BLUE_DHIDE_BODY ("Blue d'hide body", "Blue dragonhide body", CraftingType.ARMOUR, 71, 210, new ItemReq("Needle"), new ItemReq("Blue dragon leather", 3), new ItemReq("Thread", 1).setStackable()),
    RED_DHIDE_VAMBRACES ("Red d'hide vambraces", "Red dragonhide vambraces", CraftingType.ARMOUR, 73, 78, new ItemReq("Needle"), new ItemReq("Red dragon leather", 1), new ItemReq("Thread", 1).setStackable()),
    RED_DHIDE_CHAPS ("Red d'hide chaps", "Red dragonhide chaps", CraftingType.ARMOUR, 75, 156, new ItemReq("Needle"), new ItemReq("Red dragon leather", 2), new ItemReq("Thread", 1).setStackable()),
    RED_DHIDE_BODY ("Red d'hide body", "Red dragonhide body", CraftingType.ARMOUR, 77, 234, new ItemReq("Needle"), new ItemReq("Red dragon leather", 3), new ItemReq("Thread", 1).setStackable()),
    BLACK_DHIDE_VAMBRACES ("Black d'hide vambraces", "Black dragonhide vambraces", CraftingType.ARMOUR, 79, 86, new ItemReq("Needle"), new ItemReq("Black dragon leather", 1), new ItemReq("Thread", 1).setStackable()),
    BLACK_DHIDE_CHAPS ("Black d'hide chaps", "Black dragonhide chaps", CraftingType.ARMOUR, 82, 172, new ItemReq("Needle"), new ItemReq("Black dragon leather", 2), new ItemReq("Thread", 1).setStackable()),
    BLACK_DHIDE_BODY ("Black d'hide body", "Black dragonhide body", CraftingType.ARMOUR, 84, 258, new ItemReq("Needle"), new ItemReq("Black dragon leather", 3), new ItemReq("Thread", 1).setStackable()),

    SNAKESKIN_BOOTS ("Snakeskin boots", "Snakeskin boots", CraftingType.ARMOUR, 45, 30, new ItemReq("Needle"), new ItemReq("Snakeskin", 6), new ItemReq("Thread", 1).setStackable()),
    SNAKESKIN_VAMBRACES ("Snakeskin vambraces", "Snakeskin vambraces", CraftingType.ARMOUR, 47, 35, new ItemReq("Needle"), new ItemReq("Snakeskin", 8), new ItemReq("Thread", 1).setStackable()),
    SNAKESKIN_BANDANA ("Snakeskin bandana", "Snakeskin bandana", CraftingType.ARMOUR, 48, 45, new ItemReq("Needle"), new ItemReq("Snakeskin", 5), new ItemReq("Thread", 1).setStackable()),
    SNAKESKIN_CHAPS ("Snakeskin chaps", "Snakeskin chaps", CraftingType.ARMOUR, 51, 50, new ItemReq("Needle"), new ItemReq("Snakeskin", 12), new ItemReq("Thread", 1).setStackable()),
    SNAKESKIN_BODY ("Snakeskin body", "Snakeskin body", CraftingType.ARMOUR, 53, 55, new ItemReq("Needle"), new ItemReq("Snakeskin", 15), new ItemReq("Thread", 1).setStackable()),

    YAK_HIDE_LEGS ("Yak-hide legs", "Yak hide leg armour", CraftingType.ARMOUR, 43, 32, new ItemReq("Needle"), new ItemReq("Cured yak-hide", 1), new ItemReq("Thread", 1).setStackable()),
    YAK_HIDE_BODY ("Yak-hide body", "Yak hide body armour", CraftingType.ARMOUR, 46, 32, new ItemReq("Needle"), new ItemReq("Cured yak-hide", 2), new ItemReq("Thread", 1).setStackable()),

    BLOOD_N_TAR_SNELM ("Blood'n'tar snelm", "Blood'n'tar snelm", CraftingType.SNELM, 15, 32.5f, new ItemReq("Chisel"), new ItemReq("Blamish red shell", 1)),
    BROKEN_BARK_SNELM ("Broken bark snelm", "Broken bark snelm", CraftingType.SNELM, 15, 32.5f, new ItemReq("Chisel"), new ItemReq("Blamish bark shell", 1)),
    BRUISE_BLUE_SNELM ("Bruise blue snelm", "Bruise blue snelm", CraftingType.SNELM, 15, 32.5f, new ItemReq("Chisel"), new ItemReq("Blamish blue shell", 1)),
    MYRE_SNELM ("Myre snelm", "Myre snelm", CraftingType.SNELM, 15, 32.5f, new ItemReq("Chisel"), new ItemReq("Blamish myre shell", 1)),
    OCHRE_SNELM ("Ochre snelm", "Ochre snelm", CraftingType.SNELM, 15, 32.5f, new ItemReq("Chisel"), new ItemReq("Blamish ochre shell", 1)),

    MOLTEN_GLASS ("Molten glass", "Molten glass", CraftingType.MOLTEN_GLASS, 1, 20, new ItemReq("Bucket of sand", 1), new ItemReq("Soda ash", 1)),
    
    BEER_GLASS ("Beer Glass", "Beer Glass", CraftingType.GLASS, 1, 17.5f, new ItemReq("Glassblowing pipe"), new ItemReq("Molten glass", 1)),
    CANDLE_LANTERN ("Candle Lantern", "Candle Lantern", CraftingType.GLASS, 4, 19, new ItemReq("Glassblowing pipe"), new ItemReq("Molten glass", 1)),
    OIL_LAMP ("Oil lamp", "Oil lamp", CraftingType.GLASS, 12, 25, new ItemReq("Glassblowing pipe"), new ItemReq("Molten glass", 1)),
    VIAL ("Vial", "Vial", CraftingType.GLASS, 33, 35, new ItemReq("Glassblowing pipe"), new ItemReq("Molten glass", 1)),
    FISH_BOWL ("Fish Bowl", "Fish Bowl", CraftingType.GLASS, 42, 42.5f, new ItemReq("Glassblowing pipe"), new ItemReq("Molten glass", 1)),
    UNPOWERED_ORB ("Unpowered Orb", "Orb", CraftingType.GLASS, 46, 52.5f, new ItemReq("Glassblowing pipe"), new ItemReq("Molten glass", 1)),
    LANTERN_LENS ("Lantern Lens", "Lantern Lens", CraftingType.GLASS, 49, 55, new ItemReq("Glassblowing pipe"), new ItemReq("Molten glass", 1)),
    DORGESHUUN_LIGHT_ORB ("Dorgeshuun Light Orb", "Dorgeshuun Light Orb", CraftingType.GLASS, 87, 70, new ItemReq("Glassblowing pipe"),  new ItemReq("Molten glass", 1)),

    OPAL ("Opal", "Uncut opal", CraftingType.JEM_CUTTING, 1, 15, new ItemReq("Chisel"), new ItemReq("Uncut opal", 1)),
    JADE ("Jade", "Uncut jade", CraftingType.JEM_CUTTING, 13, 20, new ItemReq("Chisel"), new ItemReq("Uncut jade", 1)),
    RED_TOPAZ ("Red topaz", "Uncut red topaz", CraftingType.JEM_CUTTING, 16, 25, new ItemReq("Chisel"), new ItemReq("Uncut red topaz", 1)),
    SAPPHIRE ("Sapphire", "Uncut sapphire", CraftingType.JEM_CUTTING, 20, 50, new ItemReq("Chisel"), new ItemReq("Uncut sapphire", 1)),
    EMERALD ("Emerald", "Uncut emerald", CraftingType.JEM_CUTTING, 27, 67.5f, new ItemReq("Chisel"), new ItemReq("Uncut emerald", 1)),
    RUBY ("Ruby", "Uncut ruby", CraftingType.JEM_CUTTING, 34, 85, new ItemReq("Chisel"), new ItemReq("Uncut ruby", 1)),
    DIAMOND ("Diamond", "Uncut diamond", CraftingType.JEM_CUTTING, 43, 107.5f, new ItemReq("Chisel"), new ItemReq("Uncut diamond", 1)),
    DRAGONSTONE ("Dragonstone", "Uncut dragonstone", CraftingType.JEM_CUTTING, 55, 137.5f, new ItemReq("Chisel"), new ItemReq("Uncut dragonstone", 1)),
    ONYX ("Onyx", "Uncut onyx", CraftingType.JEM_CUTTING, 67, 167.5f, new ItemReq("Chisel"), new ItemReq("Uncut onyx", 1)),

    GOLD_RING ("Gold ring", "Rings", 1, CraftingType.JEWELLERY, 5, 15, new ItemReq("Ring mould"), CraftingConstants.GOLD_BAR),
    SAPPHIRE_RING ("Sapphire ring", "Rings", 2, CraftingType.JEWELLERY, 20, 40, new ItemReq("Ring mould"), CraftingConstants.GOLD_BAR, new ItemReq("Sapphire", 1)),
    EMERALD_RING ("Emerald ring", "Rings", 3, CraftingType.JEWELLERY, 27, 55, new ItemReq("Ring mould"), CraftingConstants.GOLD_BAR, new ItemReq("Emerald", 1)),
    RUBY_RING ("Ruby ring", "Rings", 4, CraftingType.JEWELLERY, 34, 70, new ItemReq("Ring mould"), CraftingConstants.GOLD_BAR, new ItemReq("Ruby", 1)),
    DIAMOND_RING ("Diamond ring", "Rings", 5, CraftingType.JEWELLERY, 43, 85, new ItemReq("Ring mould"), CraftingConstants.GOLD_BAR, new ItemReq("Diamond", 1)),
    DRAGONSTONE_RING ("Dragonstone ring", "Rings", 6, CraftingType.JEWELLERY, 55, 100, new ItemReq("Ring mould"), CraftingConstants.GOLD_BAR, new ItemReq("Dragonstone", 1)),
    ONYX_RING ("Onyx ring", "Rings", 7, CraftingType.JEWELLERY, 67, 115, new ItemReq("Ring mould"), CraftingConstants.GOLD_BAR, new ItemReq("Onyx", 1)),

    GOLD_NECKLACE ("Gold necklace", "Necklaces", 1, CraftingType.JEWELLERY, 6, 20, new ItemReq("Necklace mould"), CraftingConstants.GOLD_BAR),
    SAPPHIRE_NECKLACE ("Sapphire necklace", "Necklaces", 2, CraftingType.JEWELLERY, 22, 55, new ItemReq("Necklace mould"), CraftingConstants.GOLD_BAR, new ItemReq("Sapphire", 1)),
    EMERALD_NECKLACE ("Emerald necklace", "Necklaces", 3, CraftingType.JEWELLERY, 29, 60, new ItemReq("Necklace mould"), CraftingConstants.GOLD_BAR, new ItemReq("Emerald", 1)),
    RUBY_NECKLACE ("Ruby necklace", "Necklaces", 4, CraftingType.JEWELLERY, 40, 75, new ItemReq("Necklace mould"), CraftingConstants.GOLD_BAR, new ItemReq("Ruby", 1)),
    DIAMOND_NECKLACE ("Diamond necklace", "Necklaces", 5, CraftingType.JEWELLERY, 56, 90, new ItemReq("Necklace mould"), CraftingConstants.GOLD_BAR, new ItemReq("Diamond", 1)),
    DRAGONSTONE_NECKLACE ("Dragonstone necklace", "Necklaces", 6, CraftingType.JEWELLERY, 72, 105, new ItemReq("Necklace mould"), CraftingConstants.GOLD_BAR, new ItemReq("Dragonstone", 1)),
    ONYX_NECKLACE ("Onyx necklace", "Necklaces", 7, CraftingType.JEWELLERY, 82, 120, new ItemReq("Necklace mould"), CraftingConstants.GOLD_BAR, new ItemReq("Onyx", 1)),

    GOLD_BRACELET ("Gold bracelet", "Bracelets", 1, CraftingType.JEWELLERY, 7, 25, CraftingConstants.BRACELET_MOULD, CraftingConstants.GOLD_BAR),
    SAPPHIRE_BRACELET ("Sapphire bracelet", "Bracelets", 2, CraftingType.JEWELLERY, 23, 60, CraftingConstants.BRACELET_MOULD, CraftingConstants.GOLD_BAR, new ItemReq("Sapphire", 1)),
    EMERALD_BRACELET ("Emerald bracelet", "Bracelets", 3, CraftingType.JEWELLERY, 30, 65, CraftingConstants.BRACELET_MOULD, CraftingConstants.GOLD_BAR, new ItemReq("Emerald", 1)),
    RUBY_BRACELET ("Ruby bracelet", "Bracelets", 4, CraftingType.JEWELLERY, 42, 80, CraftingConstants.BRACELET_MOULD, CraftingConstants.GOLD_BAR, new ItemReq("Ruby", 1)),
    DIAMOND_BRACELET ("Diamond bracelet", "Bracelets", 5, CraftingType.JEWELLERY, 58, 95, CraftingConstants.BRACELET_MOULD, CraftingConstants.GOLD_BAR, new ItemReq("Diamond", 1)),
    DRAGONSTONE_BRACELET ("Dragonstone bracelet", "Bracelets", 6, CraftingType.JEWELLERY, 74, 110, CraftingConstants.BRACELET_MOULD, CraftingConstants.GOLD_BAR, new ItemReq("Dragonstone", 1)),
    ONYX_BRACELET ("Onyx bracelet", "Bracelets", 7, CraftingType.JEWELLERY, 84, 125, CraftingConstants.BRACELET_MOULD, CraftingConstants.GOLD_BAR, new ItemReq("Onyx", 1)),

    GOLD_AMULET ("Gold amulet", "Amulets", 1, CraftingType.JEWELLERY, 8, 30, CraftingConstants.AMULET_MOULD, CraftingConstants.GOLD_BAR),
    SAPPHIRE_AMULET ("Sapphire amulet", "Amulets", 2, CraftingType.JEWELLERY, 24, 65, CraftingConstants.AMULET_MOULD, CraftingConstants.GOLD_BAR, new ItemReq("Sapphire", 1)),
    EMERALD_AMULET ("Emerald amulet", "Amulets", 3, CraftingType.JEWELLERY, 31, 70, CraftingConstants.AMULET_MOULD, CraftingConstants.GOLD_BAR, new ItemReq("Emerald", 1)),
    RUBY_AMULET ("Ruby amulet", "Amulets", 4, CraftingType.JEWELLERY, 50, 85, CraftingConstants.AMULET_MOULD, CraftingConstants.GOLD_BAR, new ItemReq("Ruby", 1)),
    DIAMOND_AMULET ("Diamond amulet", "Amulets", 5, CraftingType.JEWELLERY, 70, 100, CraftingConstants.AMULET_MOULD, CraftingConstants.GOLD_BAR, new ItemReq("Diamond", 1)),
    DRAGONSTONE_AMULET ("Dragonstone amulet", "Amulets", 6, CraftingType.JEWELLERY, 80, 150, CraftingConstants.AMULET_MOULD, CraftingConstants.GOLD_BAR, new ItemReq("Dragonstone", 1)),
    ONYX_AMULET ("Onyx amulet", "Amulets",7, CraftingType.JEWELLERY, 90, 165, CraftingConstants.AMULET_MOULD, CraftingConstants.GOLD_BAR, new ItemReq("Onyx", 1)),

    HOLY_SYMBOL ("Holy symbol", "Saradomin", CraftingType.JEWELLERY, 16, 50, new ItemReq("Holy mould"), new ItemReq("Silver bar", 1)),
    UNHOLY_SYMBOL ("Unholy symbol", "Unholy", CraftingType.JEWELLERY, 17, 50, new ItemReq("Unholy mould"), new ItemReq("Silver bar", 1)),
    TIARA ("Tiara", "Tiara", CraftingType.JEWELLERY, 23, 52.5f, new ItemReq("Tiara mould"), new ItemReq("Silver bar", 1)),

    SILVER_BOLTS ("Silver bolts", "Crossbow", CraftingType.WEAPONRY, 22, 50, new ItemReq("Bolt mould"),  new ItemReq("Silver bar", 1), new ItemReq("Feather", 1)),
    WATER_BATTLESTAFF ("Water battlestaff", "Water battlestaff", CraftingType.WEAPONRY, 54, 100, new ItemReq("Battlestaff", 1), new ItemReq("Water orb", 1)),
    EARTH_BATTLESTAFF ("Earth battlestaff", "Earth battlestaff", CraftingType.WEAPONRY, 58, 112.5f, new ItemReq("Battlestaff", 1), new ItemReq("Earth orb", 1)),
    FIRE_BATTLESTAFF ("Fire battlestaff", "Fire battlestaff", CraftingType.WEAPONRY, 62, 125, new ItemReq("Battlestaff", 1), new ItemReq("Fire orb", 1)),
    AIR_BATTLESTAFF ("Air battlestaff", "Air battlestaff", CraftingType.WEAPONRY, 66, 137.5f, new ItemReq("Battlestaff", 1), new ItemReq("Air orb", 1)),
    SILVER_SICKLE ("Silver Sickle", "Sickle", CraftingType.WEAPONRY, 0, 0, new ItemReq("Sickle mould", 1), new ItemReq("Silver bar", 1));

    private interface CraftingConstants {
        ItemReq AMULET_MOULD = new ItemReq("Amulet mould");
        ItemReq BRACELET_MOULD = new ItemReq("Bracelet mould");
        ItemReq GOLD_BAR = new ItemReq("Gold bar", 1);
    }
    
    String name;
    String widgetText;
    int childIndex;
    String widgetTitle;
    CraftingType type;
    int levelRequired;
    float xpGained;
    ItemReq[] itemReqs;

    CraftingItem(final String name, final String widgetText, final CraftingType type, final int levelRequired, final float xpGained, final ItemReq... itemReqs){
        this.name = name;
        this.widgetText = widgetText;
        this.type = type;
        this.levelRequired = levelRequired;
        this.xpGained = xpGained;
        this.itemReqs = itemReqs;
    }

    CraftingItem(final String name, final String widgetTitle, final int childIndex, final CraftingType type, final int levelRequired, final float xpGained, final ItemReq... itemReqs){
        this.name = name;
        this.widgetTitle = widgetTitle;
        this.childIndex = childIndex;
        this.type = type;
        this.levelRequired = levelRequired;
        this.xpGained = xpGained;
        this.itemReqs = itemReqs;
    }

    public static CraftingItem[] getItemsWithType(CraftingType type){
        return Arrays.stream(values()).filter(item -> item.type == type).toArray(CraftingItem[]::new);
    }

    @Override
    public String toString(){
        return name;
    }
}
