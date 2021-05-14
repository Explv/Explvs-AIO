package activities.skills.herblore.herb_cleaning;

import activities.activity.Activity;
import activities.activity.ActivityType;
import activities.banking.ItemReqBanking;
import util.executable.Executable;
import util.Sleep;
import util.item_requirement.ItemReq;

public class HerbCleaningActivity extends Activity {

    private final Herb herb;
    private final ItemReq herbReq;
    private final Executable bankNode;

    public HerbCleaningActivity(final Herb herb) {
        super(ActivityType.HERBLORE);
        this.herb = herb;
        this.herbReq = new ItemReq(herb.grimyName, 1);
        bankNode = new ItemReqBanking(herbReq);
    }

    @Override
    public void runActivity() throws InterruptedException {
        if (ItemReq.hasItemRequirements(new ItemReq[]{ herbReq }, getInventory())) {
            cleanHerbs();
        } else {
            execute(bankNode);
        }
    }

    private void cleanHerbs() {
        if (getInventory().interact("Clean", herb.grimyName)) {
            Sleep.sleepUntil(() -> {
                return !getInventory().contains(herb.grimyName) ||
                        getDialogues().isPendingContinuation();
            }, 30_000);
        }
    }

    @Override
    public HerbCleaningActivity copy() {
        return new HerbCleaningActivity(herb);
    }
}
