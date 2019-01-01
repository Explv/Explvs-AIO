package org.aio.activities.skills.fishing;

import org.aio.util.item_requirement.ItemReq;

public enum FishingMethod {

    NET ("Fishing spot", "Small Net", new ItemReq("Small fishing net")),
    BIG_NET ("Fishing spot", "Big Net", new ItemReq("Big fishing net")),
    BAIT("Fishing spot", "Bait", new ItemReq("Fishing rod"), new ItemReq("Fishing bait", 1).setStackable()),
    OILY_BAIT ("Rod Fishing spot", "Bait", new ItemReq("Oily fishing rod"), new ItemReq("Fishing bait", 1).setStackable()),
    LURE ("Rod Fishing spot", "Lure", new ItemReq("Fly fishing rod"), new ItemReq("Feather", 1).setStackable()),
    STRIPY_LURE ("Rod Fishing spot", "Lure", new ItemReq("Fly fishing rod"), new ItemReq("Stripy Feather", 1).setStackable()),
    CAGE ("Fishing spot", "Cage", new ItemReq("Lobster pot")),
    HARPOON ("Fishing spot", "Harpoon", new ItemReq("Harpoon")),
    VESSEL ("Fishing spot", "Vessel", new ItemReq("Karambwan vessel")),
    TRAWLER ("Fishing spot", "Trawler", null),
    SANDWORM_BAIT ("Fishing spot", "", null),
    BAIT_POT ("Fishing spot", "", null),
    SWAMP_BAIT ("Fishing spot", "", null);

    String spotName;
    String action;
    ItemReq[] itemReqs;

    FishingMethod(final String spotName, final String action, final ItemReq... itemReqs){
        this.spotName = spotName;
        this.action = action;
        this.itemReqs = itemReqs;
    }
}
