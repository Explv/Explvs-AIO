package activities.money_making.flax_picking;

import activities.activity.Activity;
import activities.activity.ActivityType;
import activities.banking.Banking;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.utility.ConditionalSleep;
import util.executable.Executable;

public class FlaxPickingActivity extends Activity {

    private final Area flaxArea = new Area(
            new int[][]{
                    {2739, 3437},
                    {2737, 3439},
                    {2737, 3445},
                    {2736, 3446},
                    {2736, 3450},
                    {2738, 3452},
                    {2739, 3452},
                    {2741, 3454},
                    {2743, 3454},
                    {2744, 3453},
                    {2745, 3453},
                    {2746, 3452},
                    {2752, 3452},
                    {2752, 3437},
                    {2747, 3437},
                    {2746, 3436},
                    {2742, 3436},
                    {2741, 3437}
            }
    );

    private Entity flax;
    private Executable flaxBankNode = new FlaxBank();

    public FlaxPickingActivity() {
        super(ActivityType.MONEY_MAKING);
    }

    @Override
    public void runActivity() throws InterruptedException {
        if (!getInventory().isFull() && getEquipment().isEmpty()) {
            if (!flaxArea.contains(myPosition())) {
                getWalking().webWalk(flaxArea);
            } else {
                if (flax == null || !flax.exists()) flax = getObjects().closest(true, "Flax");
                if (flax != null) {
                    long flaxInvAmount = getInventory().getAmount("Flax");
                    flax.interact("Pick");
                    new ConditionalSleep(5000) {
                        @Override
                        public boolean condition() throws InterruptedException {
                            return getInventory().getAmount("Flax") > flaxInvAmount || !flax.exists();
                        }
                    }.sleep();
                }
            }
        } else {
            execute(flaxBankNode);
        }
    }

    @Override
    public Activity copy() {
        return new FlaxPickingActivity();
    }

    private class FlaxBank extends Banking {

        @Override
        public void bank(final BankType currentBankType) {
            if (!getInventory().isEmpty()) {
                getBank().depositAll();
            } else if (!getEquipment().isEmpty()) {
                getBank().depositWornItems();
            } else {
                setFinished();
            }
        }
    }
}
