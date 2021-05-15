package activities.skills.smithing.item_making;

import activities.activity.Activity;
import activities.activity.ActivityType;
import activities.banking.ItemReqBanking;
import activities.skills.smithing.Bar;
import org.osbot.rs07.api.ui.RS2Widget;
import util.Sleep;
import util.executable.Executable;
import util.item_requirement.ItemReq;
import util.widget.CachedWidget;

import java.util.ArrayList;
import java.util.List;

public class SmithItemMakingActivity extends Activity {

    private final Bar bar;
    private final SmithItem smithItem;
    private final SmithLocation smithLocation;
    private final CachedWidget smithItemWidget;
    private final ItemReq[] itemReqs;

    private Executable bankNode;

    public SmithItemMakingActivity(final Bar bar, final SmithItem smithItem, final SmithLocation smithLocation) {
        super(ActivityType.SMITHING);
        this.bar = bar;
        this.smithItem = smithItem;
        this.smithLocation = smithLocation;
        smithItemWidget = new CachedWidget(smithItem.name);

        List<ItemReq> itemReqs = new ArrayList<>();
        itemReqs.add(new ItemReq(bar.toString(), smithItem.barsRequired));
        itemReqs.add(new ItemReq("Hammer"));
        this.itemReqs = itemReqs.toArray(new ItemReq[0]);
        bankNode = new ItemReqBanking(this.itemReqs);
    }

    @Override
    public void runActivity() throws InterruptedException {
        if (canSmithItem()) {
            if (!smithLocation.location.getArea().contains(myPosition())) {
                getWalking().webWalk(smithLocation.location.getArea());
            } else if (smithItemWidget.getParent(getWidgets()).map(RS2Widget::isVisible).isPresent()) {
                smithAll();
            } else {
                useAnvil();
            }
        } else {
            execute(bankNode);
        }
    }

    private boolean canSmithItem() {
        return getInventory().contains("Hammer") &&
                getInventory().getAmount(bar.toString()) >= smithItem.barsRequired;
    }

    private void smithAll() {
        if (smithItemWidget.getParent(getWidgets()).get().interact("Smith")) {
            Sleep.sleepUntil(() -> !canSmithItem() || getDialogues().isPendingContinuation(), 100_000);
        }
    }

    private void useAnvil() {
        if (getObjects().closest("Anvil").interact("Smith")) {
            Sleep.sleepUntil(() -> smithItemWidget.getParent(getWidgets()).map(RS2Widget::isVisible).isPresent(), 5000);
        }
    }

    @Override
    public SmithItemMakingActivity copy() {
        return new SmithItemMakingActivity(bar, smithItem, smithLocation);
    }
}
