package org.aio.activities.skills.smithing.item_making;

import org.aio.activities.activity.Activity;
import org.aio.activities.activity.ActivityType;
import org.aio.activities.banking.ItemReqBanking;
import org.aio.activities.skills.smithing.Bar;
import org.aio.util.CachedWidget;
import org.aio.util.Executable;
import org.aio.util.Sleep;
import org.aio.util.item_requirement.ItemReq;
import org.osbot.rs07.api.ui.RS2Widget;

import java.util.ArrayList;
import java.util.Collections;
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
        Collections.addAll(itemReqs, bar.oresRequired);
        itemReqs.add(new ItemReq("Hammer"));
        this.itemReqs = itemReqs.toArray(new ItemReq[0]);
    }

    @Override
    public void onStart() {
        bankNode = new ItemReqBanking(itemReqs);
        bankNode.exchangeContext(getBot());
    }

    @Override
    public void runActivity() throws InterruptedException {
        if (canSmithItem()) {
            if (getBank() != null && getBank().isOpen()) {
                getBank().close();
            } else if(!smithLocation.location.getArea().contains(myPosition())){
                getWalking().webWalk(smithLocation.location.getArea());
            } else if(smithItemWidget.getParent(getWidgets()).map(RS2Widget::isVisible).isPresent()){
                smithAll();
            } else {
                useAnvil();
            }
        } else {
            bankNode.run();
            if (bankNode.hasFailed()) {
                setFailed();
            }
        }
    }

    private boolean canSmithItem(){
        return getInventory().contains("Hammer") &&
                getInventory().getAmount(bar.toString()) >= smithItem.barsRequired;
    }

    private void smithAll(){
        if (smithItemWidget.getParent(getWidgets()).get().interact("Smith All", "Smith All sets")) {
            Sleep.sleepUntil(() -> !canSmithItem() || getDialogues().isPendingContinuation(), 100_000);
        }
    }

    private void useAnvil(){
        if (getObjects().closest("Anvil").interact("Smith")) {
            Sleep.sleepUntil(() -> smithItemWidget.getParent(getWidgets()).map(RS2Widget::isVisible).isPresent(), 5000);
        }
    }
}
