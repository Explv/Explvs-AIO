package activities.banking;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import util.Location;

import java.util.Arrays;

public enum DepositBox {
    PORT_SARIM(new Location("Port Sarim", new Area(3044, 3237, 3052, 3234)));

    public Location location;

    public static Area[] AREAS = Arrays.stream(DepositBox.values()).map(depositBox -> depositBox.location.getArea()).toArray(Area[]::new);

    DepositBox(final Location location) {
        this.location = location;
    }

    public static boolean atAnyDepositBox(final Position position) {
        for (DepositBox depositBox : DepositBox.values()) {
            if (depositBox.location.getArea().contains(position)) return true;
        }
        return false;
    }


    @Override
    public String toString() {
        return location.toString();
    }


}
