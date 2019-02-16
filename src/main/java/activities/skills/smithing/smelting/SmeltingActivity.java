package activities.skills.smithing.smelting;

import activities.activity.Activity;
import activities.activity.ActivityType;
import activities.banking.ItemReqBanking;
import activities.skills.smithing.Bar;
import util.Executable;
import util.MakeAllInterface;
import util.Sleep;
import util.item_requirement.ItemReq;

public class SmeltingActivity extends Activity {

    private final Bar bar;
    private final SmeltLocation smeltLocation;
    private final String widgetText;
    private Executable bankNode;
    private MakeAllInterface makeAllInterface;

    public SmeltingActivity(final Bar bar, final SmeltLocation smeltLocation) {
        super(ActivityType.SMITHING);
        this.bar = bar;
        this.smeltLocation = smeltLocation;
        widgetText = bar.toString().substring(0, bar.toString().indexOf(" "));
    }

    @Override
    public void onStart() {
        makeAllInterface = new MakeAllInterface(bar.toString());
        makeAllInterface.exchangeContext(getBot());

        bankNode = new ItemReqBanking(bar.oresRequired);
        bankNode.exchangeContext(getBot());
    }

    @Override
    public void runActivity() throws InterruptedException {
        if (canSmeltBar()) {
            if (getBank() != null && getBank().isOpen()) {
                getBank().close();
            } else if (!smeltLocation.location.getArea().contains(myPosition())) {
                getWalking().webWalk(smeltLocation.location.getArea());
            } else {
                smeltBars();
            }
        } else {
            bankNode.run();
            if (bankNode.hasFailed()) {
                setFailed();
            }
        }
    }

    private boolean canSmeltBar() {
        return ItemReq.hasItemRequirements(bar.oresRequired, getInventory());
    }

    private void smeltBars() {
        if (!makeAllInterface.isMakeAllScreenOpen()) {
            useFurance();
        } else if (makeAllInterface.makeAll()) {
            Sleep.sleepUntil(() -> !canSmeltBar() || getDialogues().isPendingContinuation(), 60_000);
        }
    }

    private void useFurance() {
        if (getObjects().closest("Furnace").interact("Smelt")) {
            Sleep.sleepUntil(() -> makeAllInterface.isMakeAllScreenOpen(), 5000);
        }
    }

    @Override
    public SmeltingActivity copy() {
        return new SmeltingActivity(bar, smeltLocation);
    }
}
