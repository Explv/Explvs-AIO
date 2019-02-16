package activities.skills.cooking;

import activities.activity.Activity;
import activities.activity.ActivityType;
import activities.banking.ItemReqBanking;
import util.Executable;
import util.MakeAllInterface;
import util.Sleep;
import util.item_requirement.ItemReq;

public class CookingActivity extends Activity {

    private final CookingItem cookingItem;
    private final CookingLocation cookingLocation;
    private MakeAllInterface makeAllInterface;
    private Executable bankNode;

    public CookingActivity(final CookingItem cookingItem, final CookingLocation cookingLocation) {
        super(ActivityType.COOKING);
        this.cookingItem = cookingItem;
        this.cookingLocation = cookingLocation;
    }

    @Override
    public void onStart() {
        bankNode = new ItemReqBanking(cookingItem.itemReqs);
        bankNode.exchangeContext(getBot());

        makeAllInterface = new MakeAllInterface(1);
        makeAllInterface.exchangeContext(getBot());
    }

    @Override
    public void runActivity() throws InterruptedException {
        if (ItemReq.hasItemRequirements(cookingItem.itemReqs, getInventory())) {
            if (getBank() != null && getBank().isOpen()) {
                getBank().close();
            } else if (!cookingLocation.location.getArea().contains(myPosition())) {
                getWalking().webWalk(cookingLocation.location.getArea());
            } else {
                cook();
            }
        } else {
            bankNode.run();
            if (bankNode.hasFailed()) {
                setFailed();
            }
        }
    }

    private void cook() {
        if (makeAllInterface.isMakeAllScreenOpen()) {
            if (makeAllInterface.makeAll()) {
                Sleep.sleepUntil(() -> getDialogues().isPendingContinuation() || !ItemReq.hasItemRequirements(cookingItem.itemReqs, getInventory()), 90_000);
            }
        } else if (!cookingItem.toString().equals(getInventory().getSelectedItemName())) {
            getInventory().getItem(cookingItem.toString()).interact("Use");
        } else if (getObjects().closest(cookingLocation.cookingObject.toString()).interact("Use")) {
            Sleep.sleepUntil(() -> makeAllInterface.isMakeAllScreenOpen(), 2000);
        }
    }

    @Override
    public CookingActivity copy() {
        return new CookingActivity(cookingItem, cookingLocation);
    }
}
