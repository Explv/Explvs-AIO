package util.custom_method_provider;

import org.osbot.rs07.api.GroundItems;
import org.osbot.rs07.api.model.GroundItem;
import org.osbot.rs07.event.InteractionEvent;
import org.osbot.rs07.script.MethodProvider;
import util.Sleep;

public class ExtendedGroundItems extends GroundItems {

    public boolean canTakeStackableItem(final String itemName) {
        return getInventory().contains(itemName) || !getInventory().isFull();
    }

    public boolean take(final String itemName) {
        return take(getGroundItems().closest(itemName));
    }

    public boolean take(final String itemName, final boolean useCamera) {
        return take(getGroundItems().closest(itemName), useCamera);
    }

    public boolean take(final GroundItem groundItem) {
       return take(groundItem, true);
    }

    public boolean take(final GroundItem groundItem, final boolean useCamera) {
        long invAmount = getInventory().getAmount(groundItem.getName());
        if (execute(new InteractionEvent(groundItem, "Take").setOperateCamera(useCamera)).hasFinished()) {
            Sleep.sleepUntil(() -> {
                return getInventory().getAmount(groundItem.getName()) > invAmount ||
                       !groundItem.exists();
            }, 5000);
        }
        return getInventory().getAmount(groundItem.getName()) > invAmount;
    }
}
