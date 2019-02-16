package activities.skills.smithing.item_making;

import org.osbot.rs07.api.map.Area;
import util.Location;

public enum SmithLocation {

    VARROCK(new Location("Varrock", new Area(3185, 3420, 3190, 3427))),
    SEERS_VILLAGE(new Location("Seers' Village", new Area(
            new int[][]{
                    {2706, 3487},
                    {2706, 3492},
                    {2704, 3492},
                    {2704, 3499},
                    {2706, 3499},
                    {2706, 3497},
                    {2709, 3497},
                    {2709, 3496},
                    {2715, 3496},
                    {2715, 3492},
                    {2712, 3492},
                    {2712, 3487}
            }
    )));

    Location location;

    SmithLocation(final Location location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return location.toString();
    }
}
