package util.custom_method_provider;

import org.osbot.rs07.api.Inventory;
import org.osbot.rs07.script.MethodProvider;
import util.Sleep;

public class ExtendedInventory extends Inventory {

    public boolean use(final String itemName) {
        if (itemName.equals(getSelectedItemName())) {
            return true;
        }
        if (getInventory().interact("Use", itemName)) {
            Sleep.sleepUntil(() -> itemName.equals(getSelectedItemName()), 1000);
            return true;
        }
        return false;
    }

    public boolean equip(final String itemName) {
        return getInventory().getItem(itemName).interact("Wield", "Equip");
    }
}
