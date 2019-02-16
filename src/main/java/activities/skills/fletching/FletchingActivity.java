package activities.skills.fletching;

import activities.activity.Activity;
import activities.activity.ActivityType;
import activities.banking.ItemReqBanking;
import util.Executable;
import util.MakeAllInterface;
import util.Sleep;
import util.item_requirement.ItemReq;


public class FletchingActivity extends Activity {

    private final FletchItem fletchItem;
    private Executable bankNode;
    private MakeAllInterface makeAllInterface;

    public FletchingActivity(final FletchItem fletchItem) {
        super(ActivityType.FLETCHING);
        this.fletchItem = fletchItem;
    }

    @Override
    public void onStart() {
        bankNode = new ItemReqBanking(fletchItem.itemReqs);
        bankNode.exchangeContext(getBot());

        makeAllInterface = new MakeAllInterface(fletchItem.widgetText);
        makeAllInterface.exchangeContext(getBot());
    }

    @Override
    public void runActivity() throws InterruptedException {
        if (canMake()) {
            if (getBank() != null && getBank().isOpen()) {
                getBank().close();
            } else {
                makeAllItem(fletchItem.itemReqs[1].toString(), fletchItem.itemReqs[0].toString());
            }
        } else {
            bankNode.run();
            if (bankNode.hasFailed()) {
                setFailed();
            }
        }
    }

    private boolean canMake() {
        return ItemReq.hasItemRequirements(fletchItem.itemReqs, getInventory());
    }

    private void makeAllItem(String useItem, String interactItem) throws InterruptedException {
        if (makeAllInterface.isMakeAllScreenOpen()) {
            if (makeAllInterface.makeAll()) {
                Sleep.sleepUntil(() -> !canMake() || getDialogues().isPendingContinuation(), 60_000);
            }
        } else if (!useItem.equals(getInventory().getSelectedItemName())) {
            getInventory().getItem(useItem).interact("Use");
        } else if (getInventory().getItem(interactItem).interact()) {
            if (fletchItem.type != FletchItemType.DART) {
                Sleep.sleepUntil(() -> makeAllInterface.isMakeAllScreenOpen(), 3000);
            }
        }
    }

    @Override
    public FletchingActivity copy() {
        return new FletchingActivity(fletchItem);
    }
}
