package activities.skills.ranged;

import activities.activity.Activity;
import activities.activity.ActivityType;
import activities.banking.ItemReqBanking;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.EquipmentSlot;
import util.Sleep;
import util.executable.Executable;
import util.item_requirement.ItemReq;

public class RangeGuildActivity extends Activity {

    private final Bow bow;
    private final int progressConfig = 156;
    private final Area miniGameArea = new Area(
            new int[][]{
                    {2672, 3414},
                    {2668, 3418},
                    {2671, 3421},
                    {2675, 3417}
            }
    );
    private final ItemReq[] itemReqs;
    private final Executable bankNode;

    public RangeGuildActivity(final Bow bow) {
        super(ActivityType.RANGED);
        this.bow = bow;
        itemReqs = new ItemReq[]{new ItemReq("Coins", 200, 200_000).setStackable(), new ItemReq(bow.toString(), 1).setEquipable()};
        bankNode = new ItemReqBanking(itemReqs);
    }

    @Override
    public void runActivity() throws InterruptedException {
        if (ItemReq.hasItemRequirements(itemReqs, getInventory(), getEquipment())) {
            doRangeGuild();
        } else {
            execute(bankNode);
        }
    }

    private void doRangeGuild() throws InterruptedException {
        if (!miniGameArea.contains(myPosition())) {
            getWalking().webWalk(miniGameArea);
        } else if (getWidgets().getWidgetContainingText("View") != null) {
            getWidgets().get(325, 88).interact();
        } else if (!getEquipment().isWieldingWeapon(bow.toString())) {
            wieldItem(bow.toString());
        } else if (!getEquipment().isWearingItem(EquipmentSlot.ARROWS, "Bronze arrow")) {
            wieldItem("Bronze arrow");
        } else if (getConfigs().get(progressConfig) == 0) {
            completeDialog("Competition Judge", "Sure, I'll give it a go.");
        } else {
            fireAtTarget();
        }
    }

    private void fireAtTarget() {
        RS2Object target = getObjects().closest("Target");
        if (target != null) {
            int progress = getConfigs().get(progressConfig);
            target.interact("Fire-at");
            Sleep.sleepUntil(() -> getConfigs().get(progressConfig) > progress || getDialogues().isPendingContinuation(), 4000);
        }
    }

    private void wieldItem(String itemName) {
        getInventory().equip(itemName);
        Sleep.sleepUntil(() -> getEquipment().isWieldingWeapon(bow.toString()), 2500);
    }

    private void completeDialog(String npcName, String... options) throws InterruptedException {
        if (!getDialogues().inDialogue()) {
            talkTo(npcName);
        } else if (getDialogues().isPendingContinuation()) {
            getDialogues().clickContinue();
        } else if (options.length > 0 && getDialogues().isPendingOption()) {
            getDialogues().completeDialogue(options);
        }
    }

    private void talkTo(String npcName) {
        NPC npc = getNpcs().closest(npcName);
        if (npc != null) {
            npc.interact("Talk-to");
            Sleep.sleepUntil(() -> getDialogues().inDialogue(), 5000);
        }
    }

    @Override
    public RangeGuildActivity copy() {
        return new RangeGuildActivity(bow);
    }
}
