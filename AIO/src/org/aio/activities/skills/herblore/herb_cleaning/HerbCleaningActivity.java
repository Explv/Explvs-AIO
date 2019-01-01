package org.aio.activities.skills.herblore.herb_cleaning;

import org.aio.activities.activity.Activity;
import org.aio.activities.activity.ActivityType;
import org.aio.activities.banking.ItemReqBanking;
import org.aio.util.Executable;
import org.aio.util.Sleep;
import org.aio.util.item_requirement.ItemReq;

public class HerbCleaningActivity extends Activity {

    private final Herb herb;
    private Executable bankNode;

    public HerbCleaningActivity(final Herb herb) {
        super(ActivityType.HERBLORE);
        this.herb = herb;
    }

    @Override
    public void onStart() {
        bankNode = new ItemReqBanking(new ItemReq(herb.grimyName, 1));
        bankNode.exchangeContext(getBot());
    }

    @Override
    public void runActivity() throws InterruptedException {
        if (getInventory().contains(herb.grimyName)) {
            if (getBank() != null && getBank().isOpen()) {
                getBank().close();
            } else {
                cleanHerbs();
            }
        } else {
            bankNode.run();
            if (bankNode.hasFailed()) {
                setFailed();
            }
        }
    }

    private void cleanHerbs() {
        long herbCount = getInventory().getAmount(herb.grimyName);
        if (getInventory().getItem(herb.grimyName).interact("Clean")) {
            Sleep.sleepUntil(() -> getInventory().getAmount(herb.grimyName) < herbCount, 2000);
        }
    }
}
