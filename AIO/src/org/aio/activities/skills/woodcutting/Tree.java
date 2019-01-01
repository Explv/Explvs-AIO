package org.aio.activities.skills.woodcutting;

import org.aio.util.Location;
import org.osbot.rs07.api.map.Area;

public enum Tree {

    NORMAL("Tree",
            "Logs",
            new Location("Lumbridge", new Area(3192, 3239, 3202, 3249)),
            new Location("North Seers' Village", new Area(2706, 3499, 2717, 3504)),
            new Location("Draynor Village", new Area(3103, 3226, 3108, 3232))
    ),
    ACHEY ("Achey", null),
    OAK("Oak",
            "Oak logs",
            new Location("Lumbridge", new Area(3202, 3237, 3206, 3247)),
            new Location("West Varrock", new Area(3160, 3410, 3171, 3423)),
            new Location("Seers' Village Bank", new Area(2731, 3490, 2734, 3494))
    ),
    WILLOW("Willow",
            "Willow logs",
            new Location("Draynor Village", new Area(3080, 3238, 3091, 3224)),
            new Location("North Seers' Village", new Area(2707, 3506, 2714, 3514))
    ),
    TEAK ("Teak", "Teak logs", new Location("Castle Wars", new Area(2332, 3046, 2336, 3050))),
    MAPLE ("Maple", "Maple logs",
            new Location("South Seers' Village Bank", new Area(2728, 3480, 2731, 3482)),
            new Location("North Seers' Village Bank", new Area(2720, 3498, 2734, 3502))),
    HOLLOW ("Hollow", null),
    MAHOGANY ("Magohany", null),
    ARCTIC_PINE ("Arctic pine", null),
    YEW("Yew",
            "Yew logs",
            new Location("Grand Exchange", new Area(
                    new int[][]{
                            {3201, 3501},
                            {3201, 3507},
                            {3226, 3507},
                            {3226, 3498},
                            {3207, 3498},
                            {3207, 3499},
                            {3206, 3499},
                            {3206, 3500},
                            {3205, 3500},
                            {3205, 3501}
                    }
            )),
            new Location("Falador", new Area(2994, 3309, 2998, 3314)),
            new Location("Port Sarim", new Area(3050, 3268, 3056, 3273)),
            new Location("Varrock South", new Area(3250, 3361, 3254, 3365)),
            new Location("Varrock Church", new Area(3247, 3470, 3251, 3475)),
            new Location("Edgeville", new Area(
                    new int[][]{
                            {3085, 3468},
                            {3085, 3483},
                            {3090, 3483},
                            {3090, 3474},
                            {3092, 3474},
                            {3092, 3468}
                    })),
            new Location("Seers' Village", new Area(
                    new int[][]{
                            {2704, 3457},
                            {2704, 3467},
                            {2708, 3467},
                            {2708, 3461},
                            {2718, 3461},
                            {2718, 3457}
                    }
            )),
            new Location("Catherby", new Area(new int[][]{
                    {2761, 3425},
                    {2761, 3428},
                    {2762, 3428},
                    {2762, 3433},
                    {2759, 3433},
                    {2759, 3435},
                    {2752, 3435},
                    {2752, 3427},
                    {2757, 3427},
                    {2757, 3425}
            }))
    ),
    MAGIC("Magic",
            "Magic logs",
            new Location("Sorcerer's Tower", new Area(2698, 3395, 2707, 3400))
    );

    public String name;
    public String logsName;
    public Location[] locations;

    Tree(final String name, final String logsName, final Location... locations) {
        this.name = name;
        this.logsName = logsName;
        this.locations = locations;
    }

    @Override
    public String toString(){
        return name;
    }
}

