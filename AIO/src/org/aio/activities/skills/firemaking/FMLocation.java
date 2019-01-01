package org.aio.activities.skills.firemaking;

import org.aio.util.Location;
import org.osbot.rs07.api.map.Area;

public enum FMLocation {

    GRAND_EXCHANGE (new Location("Grand Exchange", new Area(3146, 3473, 3185, 3487))),
    LUMBRIDGE (new Location("Lumbridge", new Area(
            new int[][]{
                    {3217, 3216},
                    {3217, 3211},
                    {3215, 3209},
                    {3207, 3209},
                    {3207, 3203},
                    {3221, 3203},
                    {3221, 3216}
            }
    ))),
    VARROCK_WEST (new Location("Varrock West", new Area(3200, 3428, 3165, 3432))),
    FALADOR_EAST (new Location("Falador East",new Area(2996, 3359, 3033, 3366))),
    DRAYNOR (new Location("Draynor", new Area(3097, 3247, 3074, 3250))),
    AL_KHARID (new Location("Al Kharid", new Area(3304, 3157, 3265, 3154))),
    EDGEVILLE (new Location("Edgeville", new Area(3073, 3501, 3104, 3506))),
    SEERS_VILLAGE (new Location("Seers' Village", new Area(2703, 3483, 2737, 3486))),
    VARROCK_EAST (new Location("Varrock East", new Area(3228, 3428, 3273, 3430))),
    YANILLE (new Location("Yanille", new Area(2608, 3096, 2575, 3098)));

    public Location LOCATION;

    FMLocation(final Location LOCATION){
        this.LOCATION = LOCATION;
    }

    @Override
    public String toString(){
        return LOCATION.toString();
    }
}
