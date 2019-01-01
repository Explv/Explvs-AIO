package org.aio.activities.skills.runecrafting;

public enum RunecraftingType {
    RUNES("Runes"),
    TIARAS("Tiaras");

    private String name;

    RunecraftingType(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
