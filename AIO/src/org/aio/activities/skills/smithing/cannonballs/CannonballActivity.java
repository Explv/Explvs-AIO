package org.aio.activities.skills.smithing.cannonballs;

import org.aio.activities.activity.Activity;
import org.aio.activities.activity.ActivityType;
import org.aio.activities.banking.ItemReqBanking;
import org.aio.activities.skills.smithing.smelting.SmeltLocation;
import org.aio.util.Executable;
import org.aio.util.MakeAllInterface;
import org.aio.util.Sleep;
import org.aio.util.item_requirement.ItemReq;

public class CannonballActivity extends Activity {

    private final SmeltLocation smeltLocation;
    private final ItemReq[] itemReqs = {
            new ItemReq("Ammo mould"),
            new ItemReq("Steel bar", 1)
    };
    private Executable bankNode;
    private MakeAllInterface makeAllInterface;

    public CannonballActivity(final SmeltLocation smeltLocation) {
        super(ActivityType.SMITHING);
        this.smeltLocation = smeltLocation;
    }

    @Override
    public void onStart() {
        makeAllInterface = new MakeAllInterface(1);
        makeAllInterface.exchangeContext(getBot());

        bankNode = new ItemReqBanking(itemReqs);
        bankNode.exchangeContext(getBot());
    }

    @Override
    public void runActivity() throws InterruptedException {
        if (canMakeCannonballs()) {
            if (getBank() != null && getBank().isOpen()) {
                getBank().close();
            } else if (!smeltLocation.location.getArea().contains(myPosition())) {
                getWalking().webWalk(smeltLocation.location.getArea());
            } else {
                makeCannonballs();
            }
        } else {
            bankNode.run();
            if (bankNode.hasFailed()) {
                setFailed();
            }
        }
    }

    private boolean canMakeCannonballs(){
        return ItemReq.hasItemRequirements(itemReqs, getInventory());
    }

    private void makeCannonballs() {
        if (!makeAllInterface.isMakeAllScreenOpen()) {
            useFurance();
        } else if (makeAllInterface.makeAll()) {
            Sleep.sleepUntil(() -> !canMakeCannonballs() || getDialogues().isPendingContinuation(), 150_000);
        }
    }
    private void useFurance(){
        if (getObjects().closest("Furnace").interact("Smelt")) {
            Sleep.sleepUntil(() -> makeAllInterface.isMakeAllScreenOpen(), 5000);
        }
    }

    @Override
    public CannonballActivity copy() {
        return new CannonballActivity(smeltLocation);
    }
}
