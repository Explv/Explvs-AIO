package util.item_requirement;

import org.osbot.rs07.api.Bank;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.api.util.ItemContainer;

import java.util.stream.Stream;

public class ItemReq {

    public static final int QUANTITY_ALL = Integer.MIN_VALUE;

    private final String name;
    private final int minQuantity;
    private final int maxQuantity;

    private boolean noted;
    private boolean stackable;
    private boolean equiable;

    // Used for "tools", where the player should only ever have 1 of the item
    // For example, axes when woodcutting, or pickaxes when mining
    public ItemReq(final String name) {
        this.name = name;
        this.minQuantity = 1;
        this.maxQuantity = 1;
    }

    // Used when the player should have at least "minQuantity" of the item to perform the task
    // But should preferably have as many of the item as possible
    public ItemReq(final String name, final int minQuantity) {
        this.name = name;
        this.minQuantity = minQuantity;
        this.maxQuantity = QUANTITY_ALL;
    }

    // Used when the player should have at least "minQuantity" of the item, and at most
    // "maxQuantity" of the item. For example, you may want the player to have at least
    // 60 coins to charter a ship, but at most 5k coins so that the full cash stack isn't used
    public ItemReq(final String name, final int minQuantity, final int maxQuantity) {
        this.name = name;
        this.minQuantity = minQuantity;
        this.maxQuantity = maxQuantity;
    }

    public static boolean isRequirementItem(final ItemReq[] itemReqs, final Item item) {
        for (ItemReq itemReq : itemReqs) {
            if (itemReq.isRequirementItem(item)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasItemRequirements(final ItemReq[] itemReqs, final ItemContainer... itemContainers) {
        for (ItemReq req : itemReqs) {
            if (!req.hasRequirement(itemContainers)) {
                return false;
            }
        }
        return true;
    }

    public final String getName() {
        return name;
    }

    public final int getMinQuantity() {
        return minQuantity;
    }

    public final int getMaxQuantity() {
        return maxQuantity;
    }

    public final boolean isNoted() {
        return noted;
    }

    public final boolean isStackable() {
        return stackable;
    }

    public final boolean isEquipable() {
        return equiable;
    }

    public final int getMinNumSlots() {
        if (isStackable()) {
            return 1;
        }
        return getMinQuantity();
    }

    public final int getMaxNumSlots() {
        if (isStackable()) {
            return 1;
        }
        return getMaxQuantity();
    }

    public final ItemReq setNoted() {
        noted = true;
        stackable = true;
        return this;
    }

    public final ItemReq setStackable() {
        stackable = true;
        return this;
    }

    public final ItemReq setEquipable() {
        equiable = true;
        return this;
    }

    public final boolean isRequirementItem(final Item item) {
        return item.getName().equals(getName()) &&
                (item.isNote() == isNoted()) || (isStackable() && item.getDefinition().getNotedId() == -1);
    }

    public final boolean hasRequirement(final ItemContainer... itemContainers) {
        return hasRequirement(getAmount(itemContainers));
    }

    public final boolean hasRequirement(final long amount) {
        return amount >= getMinQuantity() && (getMaxQuantity() == ItemReq.QUANTITY_ALL || amount <= getMaxQuantity());
    }

    public final long getAmount(final ItemContainer... itemContainers) {
        return Stream.of(itemContainers)
                .mapToLong(itemContainer ->
                        itemContainer.getAmount(item ->
                                item.getName().equals(getName()) &&
                                        (
                                                itemContainer instanceof Bank ||
                                                        item.isNote() == isNoted() ||
                                                        (isStackable() && item.getDefinition().getNotedId() == -1) // If an item is stackable, it's noted ID will be -1
                                        )
                        )
                ).sum();
    }

    @Override
    public String toString() {
        return name;
    }
}
