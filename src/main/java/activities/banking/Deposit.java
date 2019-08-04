package activities.banking;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import util.Location;

import java.util.Arrays;

// TODO: Check these areas
public enum Deposit {

	RIMMINGTON(new Location("Rimmington Depositbox", new Area(3050, 3233, 3042, 3237)));

	public Location location;

	Deposit(final Location location) {
		this.location = location;
	}

	public static boolean inAnyDeposit(final Position position) {
		for (Deposit deposit : Deposit.values()) {
			if (deposit.location.getArea().contains(position))
				return true;
		}
		return false;
	}

	public static Area[] getAreas() {
		return Arrays.stream(Deposit.values()).map(deposit -> deposit.location.getArea()).toArray(Area[]::new);
	}

	@Override
	public String toString() {
		return location.toString();
	}
}
