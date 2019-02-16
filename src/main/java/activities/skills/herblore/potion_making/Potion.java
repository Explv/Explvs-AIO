package activities.skills.herblore.potion_making;

import util.item_requirement.ItemReq;

import static activities.skills.herblore.potion_making.Potion.Constants.VIAL_OF_WATER;

public enum Potion {

    GUAM_POTION("Guam potion (unf)", VIAL_OF_WATER, new ItemReq("Guam leaf", 1)),
    ATTACK("Attack potion(3)", new ItemReq("Guam potion (unf)", 1), new ItemReq("Eye of newt", 1)),
    GUAM_TAR("Guam tar", new ItemReq("Guam potion (unf)", 1), new ItemReq("Swamp tar", 15)),

    MARRENTIL_POTION("Marrentill potion (unf)", VIAL_OF_WATER, new ItemReq("Marrentill", 1)),
    ANTIPOISION("Antipoison", new ItemReq("Marrentill potion (unf)", 1), new ItemReq("Unicorn horn dust", 1)),
    MARRENTILL_TAR("Marrentill tar", new ItemReq("Marrentill potion (unf)", 1), new ItemReq("Swamp tar", 15)),

    ROGUES_PURSE_POTION("Rogue's purse potion (unf)", VIAL_OF_WATER, new ItemReq("Rogue's purse", 1)),
    RELICYMS_BALM("Relicym's balm", new ItemReq("Rogue's purse potion (unf)", 1), new ItemReq("Snake weed", 1)),

    TARROMIN_POTION("Tarromin potion (unf)", VIAL_OF_WATER, new ItemReq("Tarromin", 1)),
    STRENGTH("Strength potion", new ItemReq("Tarromin potion (unf)", 1), new ItemReq("Limpwurt root", 1)),
    SERUM_207("Serum 207", new ItemReq("Tarromin potion (unf)", 1), new ItemReq("Ashes", 1)),
    TARROMIN("Tarromin tar", new ItemReq("Tarromin potion (unf)", 1), new ItemReq("Swamp tar", 15)),
    SHRINK_ME_QUICK("Shrink-me-quick", new ItemReq("Tarromin potion (unf)", 1), new ItemReq("Shrunk ogleroot", 1)),

    HARRALANDER_POTION("Harralander potion (unf)", VIAL_OF_WATER, new ItemReq("Harralander", 1)),
    RESTORE("Restore potion", new ItemReq("Harralander potion (unf)", 1), new ItemReq("Red spiders' eggs", 1)),
    GUTHIX_BALANCE("Guthix balance", new ItemReq("Harralander potion (unf)", 1), new ItemReq("Red spiders' eggs", 1), new ItemReq("Garlic", 1), new ItemReq("Silver dust", 1)),
    BLAMISH_OIL("Blamish oil", new ItemReq("Harralander potion (unf)", 1), new ItemReq("Blamish snail slime", 1)),
    ENERGY("Energy potion", new ItemReq("Harralander potion (unf)", 1), new ItemReq("Chocolate dust", 1)),
    COMBAT("Combat potion", new ItemReq("Harralander potion (unf)", 1), new ItemReq("Goat horn dust", 1)),
    HARRALANDER("Harralander tar", new ItemReq("Harralander potion (unf)", 1), new ItemReq("Swamp tar", 15)),


    RANARR_POTION("Ranarr potion (unf)", VIAL_OF_WATER, new ItemReq("Ranarr weed", 1)),
    DEFENCE("Defence potion", new ItemReq("Ranarr potion (unf)", 1), new ItemReq("White berries", 1)),
    PRAYER("Prayer potion", new ItemReq("Ranarr potion (unf)", 1), new ItemReq("Snape grass", 1)),

    TOADFLAX_POTION("Toadflax potion (unf)", VIAL_OF_WATER, new ItemReq("Toadflax", 1)),
    AGILITY("Agility potion", new ItemReq("Toadflax potion (unf)", 1), new ItemReq("Toad's legs", 1)),
    ANTIDOTE_PLUS("Antidote+", new ItemReq("Toadflax potion (unf)", 1), new ItemReq("Yew roots", 1)),
    SARADOMIN_BREW("Saradomin brew", new ItemReq("Toadflax potion (unf)", 1), new ItemReq("Crushed birds nest", 1)),

    IRIT_POTION("Irit potion (unf)", VIAL_OF_WATER, new ItemReq("Irit leaf", 1)),
    SUPER_ATTACK("Super attack", new ItemReq("Irit potion (unf)", 1), new ItemReq("Eye of newt", 1)),
    SUPER_ANTIPOISON("Super antipoison", new ItemReq("Irit potion (unf)", 1), new ItemReq("Unicorn horn dust", 1)),

    AVANTOE_POTION("Avantoe potion (unf)", VIAL_OF_WATER, new ItemReq("Avantoe", 1)),

    FISHING("Fishing potion", new ItemReq("Avantoe potion (unf)", 1), new ItemReq("Snape grass", 1)),
    SUPER_ENERGY("Super energy potion", new ItemReq("Avantoe potion (unf)", 1), new ItemReq("Mort myre fungi", 1)),
    HUNTER("Hunter potion", new ItemReq("Avantoe potion (unf)", 1), new ItemReq("Kebbit teeth dust", 1)),

    KWUARM_POTION("Kwuarm potion (unf)", VIAL_OF_WATER, new ItemReq("Kwuarm", 1)),
    SUPER_STRENGTH("Super strength", new ItemReq("Kwuarm potion (unf)", 1), new ItemReq("Limpwurt root", 1)),
    WEAPON_POISON("Weapon poison", new ItemReq("Kwuarm potion (unf)", 1), new ItemReq("Blue dragon scale", 1)),

    SNAPDRAGON_POTION("Snapdragon potion (unf)", VIAL_OF_WATER, new ItemReq("Snapdragon", 1)),
    SUPER_RESTORE("Super restore", new ItemReq("Snapdragon potion (unf)", 1), new ItemReq("Red spiders' eggs", 1)),
    SANFEW_SERUM("Sanfew serum", new ItemReq("Snapdragon potion (unf)", 1), new ItemReq("Red spiders' eggs", 1), new ItemReq("Unicorn horn dust", 1), new ItemReq("Snake weed", 1), new ItemReq("Nail beast nails", 1)),

    CADANTINE_POTION("Cadantine potion (unf)", VIAL_OF_WATER, new ItemReq("Cadantine", 1)),
    SUPER_DEFENCE("Super defence", new ItemReq("Cadantine potion (unf)", 1), new ItemReq("White berries", 1)),

    LANTADYME_POTION("Lantadyme potion (unf)", VIAL_OF_WATER, new ItemReq("Lantadyme", 1)),
    ANTI_FIRE("Anti-fire potion", new ItemReq("Lantadyme potion (unf)", 1), new ItemReq("Blue dragon scale", 1)),
    MAGIC("Magic potion", new ItemReq("Lantadyme potion (unf)", 1), new ItemReq("Potato cactus", 1)),

    DWARF_WEED_POTION("Dwarf weed potion (unf)", VIAL_OF_WATER, new ItemReq("Dwarf weed", 1)),
    RANGING("Ranging potion", new ItemReq("Dwarf weed potion (unf)", 1), new ItemReq("Wine of Zamorak", 1)),

    TOSRTOL_WEED_POTION("Torstol weed potion (unf)", VIAL_OF_WATER, new ItemReq("Torstol", 1)),
    ZAMORAK_BREW("Zamorak brew", new ItemReq("Torstol weed potion (unf)", 1), new ItemReq("Jangerberries", 1)),

    ANTIDOTE_PLUS_PLUS_UNF("Antidote++ (unf)", new ItemReq("Coconut milk", 1), new ItemReq("Irit leaf", 1)),
    ANTIDOTE_PLUS_PLUS("Antidote++", new ItemReq("Antidote++ (unf)", 1), new ItemReq("Magic roots", 1)),

    WEAPON_POISON_PLUS_UNF("Weapon poison+ (unf)", new ItemReq("Coconut milk", 1), new ItemReq("Cactus spine", 1)),
    WEAPON_POISION_PLUS("Weapon poison+", new ItemReq("Weapon poison+ (unf)", 1), new ItemReq("Red spiders' eggs", 1)),

    WEAPON_POISON_PLUS_PLUS_UNF("Weapon poision++ (unf)", new ItemReq("Coconut milk", 1), new ItemReq("Nightshade", 1)),
    WEAPON_POISON_PLUS_PLUS("Weapon poison++", new ItemReq("Weapon poision++ (unf)", 1), new ItemReq("Poison ivy berries", 1)),

    STAMINA("Stamina potion", new ItemReq("Super energy (3)", 1), new ItemReq("Amylase crystal", 1)),
    EXTENDED_ANTIFIRE("Extended antifire", new ItemReq("Antifire potion (4)", 1), new ItemReq("Lava scale shard", 1)),
    ANTI_VENOM("Anti-venom", new ItemReq("Antidote++ (3)", 1), new ItemReq("Zulrah's scales", 1)),
    SUPER_COMBAT("Super combat potion", new ItemReq("Torstol", 1), new ItemReq("Super attack", 1), new ItemReq("Super strength", 1), new ItemReq("Super defence", 1)),
    ANTI_VENOM_PLUS("Anti-venom+", new ItemReq("Anti-venom (4)", 1), new ItemReq("Torstol", 1));

    public String name;
    public ItemReq[] itemReqs;
    Potion(final String name, final ItemReq... itemReqs) {
        this.name = name;
        this.itemReqs = itemReqs;
    }

    @Override
    public String toString() {
        return name;
    }

    protected interface Constants {
        ItemReq VIAL_OF_WATER = new ItemReq("Vial of water", 1);
    }
}
