package org.aio.activities.skills.smithing;

import org.aio.util.item_requirement.ItemReq;

public enum Bar {

    BRONZE("Bronze bar", 1, true, new ItemReq("Copper ore", 1), new ItemReq("Tin ore", 1)),
    IRON("Iron bar", 15, true, new ItemReq("Iron ore", 1)),
    SILVER ("Silver bar", 20, false, new ItemReq("Silver ore", 1)),
    STEEL("Steel bar", 30, true, new ItemReq("Coal", 2), new ItemReq("Iron ore", 1)),
    GOLD ("Gold bar", 40, false, new ItemReq("Gold ore", 1)),
    MITHRIL("Mithril bar", 50, true, new ItemReq("Coal", 4), new ItemReq("Mithril ore", 1)),
    ADAMANTITE("Adamantite bar", 70, true, new ItemReq("Coal", 6), new ItemReq("Adamantite ore", 1)),
    RUNITE("Runite bar", 85, true, new ItemReq("Coal", 8), new ItemReq("Runite ore", 1));

    String name;
    int levelRequired;
    public boolean smithable;
    public ItemReq[] oresRequired;

    Bar(final String name, final int levelRequired, final boolean smithable, final ItemReq... oresRequired){
        this.name = name;
        this.smithable = smithable;
        this.levelRequired = levelRequired;
        this.oresRequired = oresRequired;
    }

    @Override
    public String toString(){
        return name;
    }
}
