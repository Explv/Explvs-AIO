package org.aio.activities.skills.magic;

public enum Rune {
    AIR ("Air rune"),
    MIND ("Mind rune"),
    WATER ("Water rune"),
    EARTH ("Earth rune"),
    FIRE ("Fire rune"),
    BODY ("Body rune"),
    COSMIC ("Cosmic rune"),
    CHAOS ("Chaos rune"),
    NATURE ("Nature rune"),
    LAW ("Law rune"),
    DEATH ("Death rune"),
    ASTRAL ("Astral rune"),
    BLOOD ("Blood rune"),
    SOUL ("Soul rune"),
    WRATH ("Wrath rune"),
    MIST ("Mist rune"),
    DUST ("Dust rune"),
    MUD ("Mud rune"),
    SMOKE ("Smoke rune"),
    STEAM ("Steam rune"),
    LAVA ("Lava rune");

    private String name;

    Rune(final String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }
}
