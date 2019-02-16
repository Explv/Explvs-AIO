package activities.skills.fletching;

public enum FletchItemType {

    ARROW_SHAFT("Arrow shaft"),
    JAVELIN_SHAFT("Javelin shaft"),
    HEADLESS_ARROW("Headless arrow"),
    ARROW("Arrow"),
    BOW_U("Bow (u)"),
    BOW("Bow"),
    CROSSBOW_STOCK("Crossbow stock"),
    CROSSBOW_U("Crossbow (u)"),
    CROSSBOW("Crossbow"),
    DART("Dart"),
    SHIELD("Shield");

    String name;

    FletchItemType(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
