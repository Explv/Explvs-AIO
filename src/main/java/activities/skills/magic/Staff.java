package activities.skills.magic;

public enum Staff {
    STAFF_OF_AIR("Staff of air", Rune.AIR),
    STAFF_OF_WATER("Staff of water", Rune.WATER),
    STAFF_OF_EARTH("Staff of earth", Rune.EARTH),
    STAFF_OF_FIRE("Staff of fire", Rune.FIRE),

    AIR_BATTLESTAFF("Air battlestaff", Rune.AIR),
    WATER_BATTLESTAFF("Water battlestaff", Rune.WATER),
    EARTH_BATTLESTAFF("Earth battlestaff", Rune.EARTH),
    FIRE_BATTLESTAFF("Fire battlestaff", Rune.FIRE),
    LAVA_BATTLESTAFF("Lava battlestaff", Rune.EARTH, Rune.FIRE),
    MUD_BATTLESTAFF("Mud battlestaff", Rune.EARTH, Rune.WATER),
    STEAM_BATTLESTAFF("Steam battlestaff", Rune.FIRE, Rune.WATER),
    SMOKE_BATTLESTAFF("Smoke battlestaff", Rune.FIRE, Rune.AIR),
    MIST_BATTLESTAFF("Mist battlestaff", Rune.WATER, Rune.AIR),
    DUST_BATTLESTAFF("Dust battlestaff", Rune.EARTH, Rune.AIR);

    private String name;
    private Rune[] runes;

    Staff(final String name, final Rune... runes) {
        this.name = name;
        this.runes = runes;
    }

    public String getName() {
        return name;
    }

    public Rune[] getRunes() {
        return runes;
    }

    @Override
    public String toString() {
        return getName();
    }
}
