package activities.skills.fletching;

import util.item_requirement.ItemReq;

import java.util.Arrays;

public enum FletchItem {

    ARROW_SHAFTS("15 arrow shafts", "15 arrow shafts", FletchItemType.ARROW_SHAFT, new ItemReq("Knife"), new ItemReq("Logs", 1)),
    ARROW_SHAFTS_30("30 arrow shafts", "30 arrow shafts", FletchItemType.ARROW_SHAFT, new ItemReq("Knife"), new ItemReq("Oak logs", 1)),
    ARROW_SHAFTS_45("45 arrow shafts", "45 arrow shafts", FletchItemType.ARROW_SHAFT, new ItemReq("Knife"), new ItemReq("Willow logs", 1)),
    ARROW_SHAFTS_60("60 arrow shafts", "60 arrow shafts", FletchItemType.ARROW_SHAFT, new ItemReq("Knife"), new ItemReq("Maple logs", 1)),
    ARROW_SHAFTS_75("75 arrow shafts", "75 arrow shafts", FletchItemType.ARROW_SHAFT, new ItemReq("Knife"), new ItemReq("Yew logs", 1)),
    ARROW_SHAFTS_90("90 arrow shafts", "90 arrow shafts", FletchItemType.ARROW_SHAFT, new ItemReq("Knife"), new ItemReq("Magic logs", 1)),
    ARROW_SHAFTS_105("105 arrow shafts", "105 arrow shafts", FletchItemType.ARROW_SHAFT, new ItemReq("Knife"), new ItemReq("Redwood logs", 1)),

    OAK_SHIELD("Oak shield", "Oak shield", FletchItemType.SHIELD, new ItemReq("Knife"), new ItemReq("Oak logs", 1)),
    WILLOW_SHIELD("Willow shield", "Willow shield", FletchItemType.SHIELD, new ItemReq("Knife"), new ItemReq("Willow logs", 1)),
    MAPLE_SHIELD("Maple shield", "Maple shield", FletchItemType.SHIELD, new ItemReq("Knife"), new ItemReq("Maple logs", 1)),
    YEW_SHIELD("Yew shield", "Yew shield", FletchItemType.SHIELD, new ItemReq("Knife"), new ItemReq("Yew logs", 1)),
    MAGIC_SHIELD("Magic shield", "Magic shield", FletchItemType.SHIELD, new ItemReq("Knife"), new ItemReq("Magic logs", 1)),
    REDWOOD_SHIELD("Redwood shield", "Redwood shield", FletchItemType.SHIELD, new ItemReq("Knife"), new ItemReq("Redwood logs", 1)),

    JAVELIN_SHAFTS("Javelin Shafts", "15 javelin Shafts", FletchItemType.JAVELIN_SHAFT, new ItemReq("Knife"), new ItemReq("Logs", 1)),

    HEADLESS_ARROWS("Headless arrow", "Headless arrow", FletchItemType.HEADLESS_ARROW, new ItemReq("Arrow shaft", 1).setStackable(), new ItemReq("Feather", 1).setStackable()),

    BRONZE_ARROW("Bronze arrow", "Bronze arrow", FletchItemType.ARROW, new ItemReq("Bronze arrowtips", 1).setStackable(), new ItemReq("Headless arrow", 1).setStackable()),
    IRON_ARROW("Iron arrow", "Iron arrow", FletchItemType.ARROW, new ItemReq("Iron arrowtips", 1).setStackable(), new ItemReq("Headless arrow", 1).setStackable()),
    STEEL_ARROW("Steel arrow", "Steel arrow", FletchItemType.ARROW, new ItemReq("Steel arrowtips", 1).setStackable(), new ItemReq("Headless arrow", 1).setStackable()),
    MITHRIL_ARROW("Mithril arrow", "Mithril arrow", FletchItemType.ARROW, new ItemReq("Mithril arrowtips", 1).setStackable(), new ItemReq("Headless arrow", 1).setStackable()),
    ADAMANT_ARROW("Adamant arrow", "Adamant arrow", FletchItemType.ARROW, new ItemReq("Adamant arrowtips", 1).setStackable(), new ItemReq("Headless arrow", 1).setStackable()),
    RUNE_ARROW("Rune arrow", "Rune arrow", FletchItemType.ARROW, new ItemReq("Rune arrowtips", 1).setStackable(), new ItemReq("Headless arrow", 1).setStackable()),
    DRAGON_ARROW("Dragon arrow", "Dragon arrow", FletchItemType.ARROW, new ItemReq("Dragon arrowtips", 1).setStackable(), new ItemReq("Headless arrow", 1).setStackable()),

    SHORT_BOW_U("Shortbow (u)", "Shortbow", FletchItemType.BOW_U, new ItemReq("Knife"), new ItemReq("Logs", 1)),
    LONG_BOW_U("Longbow (u)", "Longbow", FletchItemType.BOW_U, new ItemReq("Knife"), new ItemReq("Logs", 1)),
    OAK_SHORT_BOW_U("Oak shortbow (u)", "Oak shortbow", FletchItemType.BOW_U, new ItemReq("Knife"), new ItemReq("Oak logs", 1)),
    OAK_LONG_BOW_U("Oak longbow (u)", "Oak longbow", FletchItemType.BOW_U, new ItemReq("Knife"), new ItemReq("Oak logs", 1)),
    //OGRE_COMPOSITE_BOW_U ("Comp ogre bow (u)", FletchItemType.BOW_U, new ItemReq("Knife"), new ItemReq("", 1)),
    WILLOW_SHORTBOW_U("Willow shortbow (u)", "Willow shortbow", FletchItemType.BOW_U, new ItemReq("Knife"), new ItemReq("Willow logs", 1)),
    WILLOW_LONGBOW_U("Willow longbow (u)", "Willow longbow", FletchItemType.BOW_U, new ItemReq("Knife"), new ItemReq("Willow logs", 1)),
    MAPLE_SHORTBOW_U("Maple shortbow (u)", "Maple shortbow", FletchItemType.BOW_U, new ItemReq("Knife"), new ItemReq("Maple logs", 1)),
    MAPLE_LONGBOW_U("Maple longbow (u)", "Maple longbow", FletchItemType.BOW_U, new ItemReq("Knife"), new ItemReq("Maple logs", 1)),
    YEW_SHORTBOW_U("Yew shortbow (u)", "Yew shortbow", FletchItemType.BOW_U, new ItemReq("Knife"), new ItemReq("Yew logs", 1)),
    YEW_LONGBOW_U("Yew longbow (u)", "Yew longbow", FletchItemType.BOW_U, new ItemReq("Knife"), new ItemReq("Yew logs", 1)),
    MAGIC_SHORTBOW_U("Magic shortbow (u)", "Magic shortbow", FletchItemType.BOW_U, new ItemReq("Knife"), new ItemReq("Magic logs", 1)),
    MAGIC_LONGBOW_U("Magic longbow (u)", "Magic longbow", FletchItemType.BOW_U, new ItemReq("Knife"), new ItemReq("Magic logs", 1)),

    SHORT_BOW("Shortbow", "Shortbow", FletchItemType.BOW, new ItemReq("Shortbow (u)", 1), new ItemReq("Bow string", 1)),
    LONG_BOW("Longbow", "Longbow", FletchItemType.BOW, new ItemReq("Longbow (u)", 1), new ItemReq("Bow string", 1)),
    OAK_SHORT_BOW("Oak shortbow", "Oak shortbow", FletchItemType.BOW, new ItemReq("Oak shortbow (u)", 1), new ItemReq("Bow string", 1)),
    OAK_LONG_BOW("Oak longbow", "Oak longbow", FletchItemType.BOW, new ItemReq("Oak longbow (u)", 1), new ItemReq("Bow string", 1)),
    OGRE_COMPOSITE_BOW("Comp ogre bow", "Comp ogre bow", FletchItemType.BOW, new ItemReq("Ogre composite bow (u)", 1), new ItemReq("Bow string", 1)),
    WILLOW_SHORTBOW("Willow shortbow", "Willow shortbow", FletchItemType.BOW, new ItemReq("Willow shortbow (u)", 1), new ItemReq("Bow string", 1)),
    WILLOW_LONGBOW("Willow longbow", "Willow longbow", FletchItemType.BOW, new ItemReq("Willow longbow (u)", 1), new ItemReq("Bow string", 1)),
    MAPLE_SHORTBOW("Maple shortbow", "Maple shortbow", FletchItemType.BOW, new ItemReq("Maple shortbow (u)", 1), new ItemReq("Bow string", 1)),
    MAPLE_LONGBOW("Maple longbow", "Maple longbow", FletchItemType.BOW, new ItemReq("Maple longbow (u)", 1), new ItemReq("Bow string", 1)),
    YEW_SHORTBOW("Yew shortbow", "Yew shortbow", FletchItemType.BOW, new ItemReq("Yew shortbow (u", 1), new ItemReq("Bow string", 1)),
    YEW_LONGBOW("Yew longbow", "Yew longbow", FletchItemType.BOW, new ItemReq("Yew longbow (u)", 1), new ItemReq("Bow string", 1)),
    MAGIC_SHORTBOW("Magic shortbow", "Magic shortbow", FletchItemType.BOW, new ItemReq("Magic shortbow (u)", 1), new ItemReq("Bow string", 1)),
    MAGIC_LONGBOW("Magic longbow", "Magic longbow", FletchItemType.BOW, new ItemReq("Magic longbow (u)", 1), new ItemReq("Bow string", 1)),

    CROSSBOW_STOCK("Wooden stock", "Crossbow stock", FletchItemType.CROSSBOW_STOCK, new ItemReq("Knife"), new ItemReq("Logs", 1)),
    OAK_CROSSBOW_STOCK("Oak stock", "Crossbow stock", FletchItemType.CROSSBOW_STOCK, new ItemReq("Knife"), new ItemReq("Oak logs", 1)),
    WILLOW_CROSSBOW_STOCK("Willow stock", "Crossbow stock", FletchItemType.CROSSBOW_STOCK, new ItemReq("Knife"), new ItemReq("Willow logs", 1)),
    TEAK_CROSSBOW_STOCK("Teak stock", "Crossbow stock", FletchItemType.CROSSBOW_STOCK, new ItemReq("Knife"), new ItemReq("Teak logs", 1)),
    MAPLE_CROSSBOW_STOCK("Maple stock", "Crossbow stock", FletchItemType.CROSSBOW_STOCK, new ItemReq("Knife"), new ItemReq("Maple logs", 1)),
    MAHOGANY_CROSSBOW_STOCK("Mahogany stock", "Crossbow stock", FletchItemType.CROSSBOW_STOCK, new ItemReq("Knife"), new ItemReq("Mahogany logs", 1)),
    YEW_CROSSBOW_STOCK("Yew stock", "Crossbow stock", FletchItemType.CROSSBOW_STOCK, new ItemReq("Knife"), new ItemReq("Yew logs", 1)),

    BRONZE_CROSSBOW_U("Bronze crossbow (u)", "Bronze crossbow (u)", FletchItemType.CROSSBOW_U, new ItemReq("Bronze limbs", 1), new ItemReq("Wooden stock", 1), new ItemReq("Hammer")),
    IRON_CROSSBOW_U("Iron crossbow (u)", "Iron crossbow (u)", FletchItemType.CROSSBOW_U, new ItemReq("Iron limbs", 1), new ItemReq("Willow stock", 1), new ItemReq("Hammer")),
    STEEL_CROSSBOW_U("Steel crossbow (u)", "Steel crossbow (u)", FletchItemType.CROSSBOW_U, new ItemReq("Steel limbs", 1), new ItemReq("Teak stock", 1), new ItemReq("Hammer")),
    MITHRIL_CROSSBOW_U("Mithril crossbow (u)", "Mithril crossbow (u)", FletchItemType.CROSSBOW_U, new ItemReq("Mithril limbs", 1), new ItemReq("Maple stock", 1), new ItemReq("Hammer")),
    ADAMANTITE_CROSSBOW_U("Adamant crossbow (u)", "Adamant crossbow (u)", FletchItemType.CROSSBOW_U, new ItemReq("Adamantite limbs", 1), new ItemReq("Mahogany stock", 1), new ItemReq("Hammer")),
    RUNITE_CROSSBOW_U("Runite crossbow (u)", "Rune crossbow (u)", FletchItemType.CROSSBOW_U, new ItemReq("Runite limbs", 1), new ItemReq("Yew stock", 1), new ItemReq("Hammer")),

    BRONZE_CROSSBOW("Bronze crossbow", "Bronze crossbow", FletchItemType.CROSSBOW, new ItemReq("Bronze crossbow (u)", 1), new ItemReq("Crossbow string", 1)),
    IRON_CROSSBOW("Iron crossbow", "Iron crossbow", FletchItemType.CROSSBOW, new ItemReq("Iron crossbow (u)", 1), new ItemReq("Crossbow string", 1)),
    STEEL_CROSSBOW("Steel crossbow", "Steel crossbow", FletchItemType.CROSSBOW, new ItemReq("Steel crossbow (u)", 1), new ItemReq("Crossbow string", 1)),
    MITHRIL_CROSSBOW("Mithril crossbow", "Mithril crossbow", FletchItemType.CROSSBOW, new ItemReq("Mithril crossbow (u)", 1), new ItemReq("Crossbow string", 1)),
    ADAMANT_CROSSBOW("Adamant crossbow", "Adamant crossbow", FletchItemType.CROSSBOW, new ItemReq("Adamant crossbow (u)", 1), new ItemReq("Crosbow string", 1)),
    RUNE_CROSSBOW("Rune crossbow", "Rune crossbow", FletchItemType.CROSSBOW, new ItemReq("Runite crossbow (u)", 1), new ItemReq("Crossbow string", 1)),

    BRONZE_DART("Bronze dart", null, FletchItemType.DART, new ItemReq("Bronze dart tip", 1).setStackable(), new ItemReq("Feather", 1).setStackable()),
    IRON_DART("Iron dart", null, FletchItemType.DART, new ItemReq("Iron dart tip", 1).setStackable(), new ItemReq("Feather", 1).setStackable()),
    STEEL_DART("Steel dart", null, FletchItemType.DART, new ItemReq("Steel dart tip", 1).setStackable(), new ItemReq("Feather", 1).setStackable()),
    MITHRIL_DART("Mithril dart", null, FletchItemType.DART, new ItemReq("Mithril dart tip", 1).setStackable(), new ItemReq("Feather", 1).setStackable()),
    ADAMANT_DART("Adamant dart", null, FletchItemType.DART, new ItemReq("Adamant dart tip", 1).setStackable(), new ItemReq("Feather", 1).setStackable()),
    RUNE_DART("Rune dart", null, FletchItemType.DART, new ItemReq("Rune dart tip", 1).setStackable(), new ItemReq("Feather", 1).setStackable()),
    DRAGON_DART("Dragon dart", null, FletchItemType.DART, new ItemReq("Dragon dart tip", 1).setStackable(), new ItemReq("Feather", 1).setStackable());

    String name;
    String widgetText;
    FletchItemType type;
    ItemReq[] itemReqs;

    FletchItem(final String name, final String widgetText, final FletchItemType type, final ItemReq... itemReqs) {
        this.name = name;
        this.widgetText = widgetText;
        this.type = type;
        this.itemReqs = itemReqs;
    }

    public static FletchItem[] getAllWithType(FletchItemType fletchItemType) {
        return Arrays.stream(values()).filter(item -> item.type == fletchItemType).toArray(FletchItem[]::new);
    }

    @Override
    public String toString() {
        return name;
    }
}
