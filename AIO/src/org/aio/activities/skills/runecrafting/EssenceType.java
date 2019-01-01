package org.aio.activities.skills.runecrafting;

public enum EssenceType {
    RUNE("Rune essence"),
    PURE("Pure essence");

    private String name;

    EssenceType(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
