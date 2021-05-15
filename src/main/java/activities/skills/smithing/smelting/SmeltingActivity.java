package activities.skills.smithing.smelting;

import activities.activity.Activity;
import activities.activity.ActivityType;
import activities.banking.ItemReqBanking;
import activities.skills.smithing.Bar;
import util.Sleep;
import util.executable.Executable;
import util.item_requirement.ItemReq;

public class SmeltingActivity extends Activity {

    private final Bar bar;
    private final SmeltLocation smeltLocation;
    private final Executable bankNode;

    public SmeltingActivity(final Bar bar, final SmeltLocation smeltLocation) {
        super(ActivityType.SMITHING);
        this.bar = bar;
        this.smeltLocation = smeltLocation;
        bankNode = new ItemReqBanking(bar.oresRequired);
    }

    @Override
    public void runActivity() throws InterruptedException {
        if (canSmeltBar()) {
            if (!smeltLocation.location.getArea().contains(myPosition())) {
                getWalking().webWalk(smeltLocation.location.getArea());
            } else {
                smeltBars();
            }
        } else {
            execute(bankNode);
        }
    }

    private boolean canSmeltBar() {
        return ItemReq.hasItemRequirements(bar.oresRequired, getInventory());
    }

    private void smeltBars() throws InterruptedException {
        if (!getMakeAllInterface().isOpen()) {
            useFurance();
        } else {
            getMakeAllInterface().makeAll(bar.toString());
            Sleep.sleepUntil(() -> !canSmeltBar() || getDialogues().isPendingContinuation(), 60_000);
        }
    }

    private void useFurance() {
        if (getObjects().closest("Furnace").interact("Smelt")) {
            Sleep.sleepUntil(() -> getMakeAllInterface().isOpen(), 5000);
        }
    }

    @Override
    public SmeltingActivity copy() {
        return new SmeltingActivity(bar, smeltLocation);
    }
}
