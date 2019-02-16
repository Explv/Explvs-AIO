package activities.skills.smithing.smelting;

import org.osbot.rs07.api.map.Area;
import util.Location;

public enum SmeltLocation {

    AL_KHARID(new Location("Al-Kharid", new Area(3272, 3184, 3279, 3188))),
    EDGEVILLE(new Location("Edgeville", new Area(3105, 3496, 3110, 3501))),
    FALADOR(new Location("Falador", new Area(
            new int[][]{
                    {2970, 3368},
                    {2970, 3377},
                    {2974, 3377},
                    {2974, 3375},
                    {2978, 3375},
                    {2978, 3373},
                    {2976, 3373},
                    {2976, 3368}
            }
    )));

    public Location location;

    SmeltLocation(final Location location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return location.toString();
    }
}
