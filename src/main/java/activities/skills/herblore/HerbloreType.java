package activities.skills.herblore;

public enum HerbloreType {

    HERB_CLEANING("Herb cleaning"),
    POTION("Potion");

    String name;

    HerbloreType(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
