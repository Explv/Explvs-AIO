package org.aio.activities.skills.runecrafting;

import org.aio.activities.banking.ItemReqBanking;
import org.aio.util.Executable;
import org.aio.util.item_requirement.ItemReq;

public class TiaraMakingActivity extends RunecraftingBase {

    private final ItemReq[] itemReqs;
    private final Executable bankNode;

    public TiaraMakingActivity(final Altar altar) {
        super(altar);
        itemReqs = new ItemReq[]{ new ItemReq("Tiara", 1), new ItemReq(altar.talisman, 1) };
        bankNode = new ItemReqBanking(itemReqs);
    }

    @Override
    public void onStart() {
        bankNode.exchangeContext(getBot());
    }

    @Override
    public void runActivity() throws InterruptedException {
        if (ItemReq.hasItemRequirements(itemReqs, getInventory())) {
            if (getBank() != null && getBank().isOpen()) {
                getBank().close();
            } else if (getAltar() != null) {
                makeTiaras();
            } else if(getObjects().closest("Mysterious ruins") != null && altar.area.contains(myPosition())) {
                enterAltar();
            } else {
                walkToAltar();
            }
        } else if (getAltar() != null) {
            leaveAltar();
        } else {
            bankNode.run();
            if (bankNode.hasFailed()) {
                setFailed();
            }
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
}
