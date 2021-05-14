package activities.skills.runecrafting;

import activities.banking.ItemReqBanking;
import util.executable.Executable;
import util.item_requirement.ItemReq;


public class TiaraMakingActivity extends RunecraftingBase {

    private final ItemReq[] itemReqs;
    private final Executable bankNode;

    public TiaraMakingActivity(final Altar altar) {
        super(altar);
        itemReqs = new ItemReq[]{new ItemReq("Tiara", 1), new ItemReq(altar.talisman, 1)};
        bankNode = new ItemReqBanking(itemReqs);
    }

    @Override
    public void runActivity() throws InterruptedException {
        if (ItemReq.hasItemRequirements(itemReqs, getInventory())) {
            if (getAltar() != null) {
                makeTiaras();
            } else if (getObjects().closest("Mysterious ruins") != null && altar.area.contains(myPosition())) {
                enterAltar();
            } else {
                walkToAltar();
            }
        } else if (getAltar() != null) {
            leaveAltar();
        } else {
            execute(bankNode);
        }
    }

    private void makeTiaras() throws InterruptedException {
        if (!"Tiara".equals(getInventory().getSelectedItemName())) {
            if (getInventory().isItemSelected()) {
                getInventory().deselectItem();
            } else {
                getInventory().interact("Use", "Tiara");
            }
        } else {
            getAltar().interact("Use");
        }
    }

    @Override
    public TiaraMakingActivity copy() {
        return new TiaraMakingActivity(altar);
    }
}
