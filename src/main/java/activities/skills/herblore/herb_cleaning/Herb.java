package activities.skills.herblore.herb_cleaning;

public enum Herb {

    GUAM("Guam leaf", "Grimy guam leaf"),
    ROGUES_PURSE("Rogue's purse", "Grimy rogue's purse"),
    MARRENTILL("Marrentill", "Grimy marrentill"),
    TARROMIN("Tarromin", ("Grimy tarromin")),
    HARRALANDER("Harralander", ("Grimy harralander")),
    RANARR_WEED("Ranarr weed", "Grimy ranarr weed"),
    TOADFLAX("Toadflax", "Grimy toadflax"),
    IRIT_LEAF("Irit leaf", "Grimy irit leaf"),
    AVANTOE("Avantoe", "Grimy avantoe"),
    KWUARM("Kwuarm", "Grimy kwuarm"),
    SNAPDRAGON("Snapdragon", "Grimy snapdragon"),
    CADANTINE("Cadantine", "Grimy cadantine"),
    LANTADYME("Lantadyme", "Grimy lantadyme"),
    DWARF_WEED("Dwarf weed", "Grimy dwarf weed"),
    TORSTOL("Torstol", "Grimy torstol");

    public String grimyName;
    String name;

    Herb(final String name, final String grimyName) {
        this.name = name;
        this.grimyName = grimyName;
    }

    @Override
    public String toString() {
        return name;
    }
}
