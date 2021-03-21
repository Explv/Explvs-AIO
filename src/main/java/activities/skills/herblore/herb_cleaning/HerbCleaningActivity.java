package activities.skills.herblore.herb_cleaning;

import activities.activity.Activity;
import activities.activity.ActivityType;
import activities.banking.ItemReqBanking;
import util.Executable;
import util.Sleep;
import util.item_requirement.ItemReq;

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
            Sleep.sleepUntil(() -> getInventory().getAmount(herb.grimyName) < herbCount, 700);
        }
    }

    @Override
    public HerbCleaningActivity copy() {
        return new HerbCleaningActivity(herb);
    }
}
