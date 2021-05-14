package activities.skills.runecrafting;

import activities.banking.Banking;
import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.api.ui.EquipmentSlot;
import util.Sleep;
import util.executable.ExecutionFailedException;

public class FireAltarRunecraftingActivity extends RunecraftingActivity {

    private static final Area CASTLE_WARS_AREA = new Area(2432, 3138, 2494, 3066);
    private final Area duelArenaEntrance = new Area(3312, 3225, 3325, 3246);
    private final Area dontTeleportArea = new Area(3301, 3267, 3326, 3217);
    private final boolean useRingOfDueling;
    private final Filter<Item> ringOfDuelingFilter = item -> item.getName().startsWith("Ring of dueling(");

    private final RingOfDuelingBanking ringOfDuelingBanking = new RingOfDuelingBanking();

    public FireAltarRunecraftingActivity(final Altar altar, final EssenceType essenceType, final boolean useRingOfDueling) {
        super(altar, essenceType);
        this.useRingOfDueling = useRingOfDueling;
    }

    @Override
    public void runActivity() throws InterruptedException {
        if (getInventory().contains(ringOfDuelingFilter) && !isWearingRingOfDueling()) {
            if (getInventory().interact("Wear", ringOfDuelingFilter)) {
                Sleep.sleepUntil(FireAltarRunecraftingActivity.this::isWearingRingOfDueling, 3000);
            }
        } else if (!isWearingRingOfDueling() && !getInventory().contains(ringOfDuelingFilter) && useRingOfDueling) {
            execute(ringOfDuelingBanking);
        } else {
            super.runActivity();
        }
    }

    @Override
    public void walkToAltar() {
        if (!dontTeleportArea.contains(myPosition()) && useRingOfDueling && isWearingRingOfDueling()) {
            if (getEquipment().interact(EquipmentSlot.RING, "Duel Arena")) {
                Sleep.sleepUntil(() -> duelArenaEntrance.contains(myPosition()), 5000);
            }
        } else {
            super.walkToAltar();
        }
    }

    @Override
    public void leaveAltar() {
        if (useRingOfDueling && isWearingRingOfDueling()) {
            if (getEquipment().interact(EquipmentSlot.RING, "Castle Wars")) {
                Sleep.sleepUntil(() -> CASTLE_WARS_AREA.contains(myPosition()), 5000);
            }
        } else {
            leaveAltar();
        }
    }

    private boolean isWearingRingOfDueling() {
        return getEquipment().isWearingItem(EquipmentSlot.RING, ringOfDuelingFilter);
    }

    @Override
    public FireAltarRunecraftingActivity copy() {
        return new FireAltarRunecraftingActivity(altar, essenceType, useRingOfDueling);
    }

    private class RingOfDuelingBanking extends Banking {
        @Override
        public void bank(final BankType currentBankType) {
            if (!getBank().contains(ringOfDuelingFilter)) {
                throw new ExecutionFailedException("No ring of dueling found");
            } else {
                getBank().withdraw(ringOfDuelingFilter, 1);
            }
        }
    }
}
