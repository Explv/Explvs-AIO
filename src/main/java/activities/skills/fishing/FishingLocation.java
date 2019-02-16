package activities.skills.fishing;

import org.osbot.rs07.api.map.Area;
import util.Location;

public enum FishingLocation {

    DRAYNOR_VILLAGE(new Location("Draynor Village", new Area(3085, 3226, 3088, 3230))),
    LUMBRIDGE_SWAMP(new Location("Lumbridge Swamp", new Area(3238, 3144, 3247, 3160))),
    MUSA_POINT(new Location("Musa Point", new Area(2923, 3176, 2926, 3183))),
    PORT_SARIM(new Location("Port Sarim", new Area(2985, 3174, 2988, 3178))),
    AL_KHARID(new Location("Al Kharid", new Area(
            new int[][]{
                    {3265, 3151},
                    {3269, 3151},
                    {3279, 3140},
                    {3276, 3137},
                    {3263, 3148}
            }
    ))),
    CATHERBY(new Location("Catherby", new Area(
            new int[][]{
                    {2835, 3434},
                    {2845, 3433},
                    {2848, 3431},
                    {2852, 3426},
                    {2856, 3426},
                    {2858, 3429},
                    {2863, 3429},
                    {2863, 3425},
                    {2858, 3424},
                    {2850, 3423},
                    {2846, 3428},
                    {2833, 3431}
            }
    ))),
    BARBARIAN_VILLAGE(new Location("Barbarian Village", new Area(
            new int[][]{
                    {3102, 3422},
                    {3102, 3428},
                    {3106, 3432},
                    {3106, 3436},
                    {3111, 3436},
                    {3111, 3430},
                    {3106, 3425},
                    {3106, 3422}
            }
    ))),
    LUMBRIDGE(new Location("Lumbridge", new Area(3238, 3239, 3241, 3255))),
    SHILO_VILLAGE(new Location("Shilo Village", new Area(2857, 2970, 2861, 2973))),
    FISHING_GUILD_NORTH(new Location("Fishing Guild North", new Area(2599, 3419, 2606, 3426))),
    FISHING_GUILD_SOUTH(new Location("Fishing Guild South", new Area(2603, 3410, 2614, 3417))),
    PISCATORIS_FISHING_COLONY(new Location("Piscatoris Fishing Colony", new Area(2307, 3697, 2312, 3703)));

    static FishingLocation[] smallNetBaitLocations = {
            DRAYNOR_VILLAGE,
            LUMBRIDGE_SWAMP,
            MUSA_POINT,
            PORT_SARIM,
            AL_KHARID,
            CATHERBY
    };

    static FishingLocation[] lureBaitLocations = {
            BARBARIAN_VILLAGE,
            LUMBRIDGE,
            SHILO_VILLAGE
    };

    static FishingLocation[] cageHarpoonLocations = {
            MUSA_POINT,
            CATHERBY,
            FISHING_GUILD_NORTH,
            FISHING_GUILD_SOUTH
    };

    static FishingLocation[] bigNetHarpoonLocations = {
            CATHERBY,
            FISHING_GUILD_NORTH,
            FISHING_GUILD_SOUTH
    };

    static FishingLocation[] tunaSwordfishLocations = {
            MUSA_POINT,
            CATHERBY,
            FISHING_GUILD_SOUTH,
            FISHING_GUILD_NORTH,
            PISCATORIS_FISHING_COLONY
    };

    public Location location;

    FishingLocation(final Location location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return location.toString();
    }
}
