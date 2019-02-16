package activities.skills.runecrafting;

import org.osbot.rs07.api.map.Area;

public enum Altar {

    AIR("Air altar", "Air talisman", "Air tiara", false, new Area(2981, 3289, 2989, 3295)),
    MIND("Mind altar", "Mind talisman", "Mind tiara", false, new Area(2978, 3508, 2987, 3517)),
    WATER("Water altar", "Water talisman", "Water tiara", false, new Area(3180, 3161, 3187, 3167)),
    EARTH("Earth altar", "Earth talisman", "Earth tiara", false, new Area(3300, 3471, 3310, 3479)),
    FIRE("Fire altar", "Fire talisman", "Fire tiara", false, new Area(3311, 3255, 3319, 3264)),
    BODY("Body altar", "Body talisman", "Body tiara", false, new Area(3049, 3437, 3058, 3448)),
    CHAOS("Chaos altar", "Chaos talisman", "Chaos tiara", true, new Area(3058, 3588, 3069, 3598)),
    ASTRAL("Astral altar", "", "", true, new Area(2151, 3859, 2164, 3871)),
    NATURE("Nature altar", "Nature talisman", "Nature tiara", true, new Area(2860, 3017, 2868, 3024)),
    LAW("Law altar", "Law talisman", "Law tiara", true, new Area(2855, 3371, 2865, 3382));
    //BLOOD ("Blood altar", "", "", true, new Area(1713, 3825, 1721, 3833)),
    //SOUL ("Soul altar", "", "", true, new Area(1812, 3851, 1818, 3857));

    String name;
    String talisman;
    String tiara;
    boolean pureEss;
    Area area;

    Altar(final String name, final String talisman, final String tiara, final boolean pureEss, final Area area) {
        this.name = name;
        this.area = area;
        this.talisman = talisman;
        this.tiara = tiara;
    }

    @Override
    public String toString() {
        return name;
    }
}
