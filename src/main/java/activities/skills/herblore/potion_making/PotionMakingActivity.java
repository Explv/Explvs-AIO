package activities.skills.herblore.potion_making;

import activities.activity.Activity;
import activities.activity.ActivityType;
import activities.banking.ItemReqBanking;
import util.Sleep;
import util.executable.Executable;
import util.item_requirement.ItemReq;


public class PotionMakingActivity extends Activity {

    private final Potion potion;
    private final Executable bankNode;

    public PotionMakingActivity(final Potion potion) {
        super(ActivityType.HERBLORE);
        this.potion = potion;
        bankNode = new ItemReqBanking(potion.itemReqs);
    }

    @Override
    public void runActivity() throws InterruptedException {
        if (ItemReq.hasItemRequirements(potion.itemReqs, getInventory())) {
            makePotion();
        } else {
            execute(bankNode);
        }
    }

    private void makePotion() throws InterruptedException {
        if (getMakeAllInterface().isOpen()) {
            getMakeAllInterface().makeAll(1);
            Sleep.sleepUntil(() -> !ItemReq.hasItemRequirements(potion.itemReqs, getInventory()) || getDialogues().isPendingContinuation(), 30_000);
        } else if (potion.itemReqs[1].toString().equals(getInventory().getSelectedItemName())) {
            if (getInventory().getItem(potion.itemReqs[0].toString()).interact()) {
                Sleep.sleepUntil(() -> getMakeAllInterface().isOpen(), 3000);
            }
        } else {
            getInventory().use(potion.itemReqs[1].toString());
        }
    }

    @Override
    public PotionMakingActivity copy() {
        return new PotionMakingActivity(potion);
    }
}
