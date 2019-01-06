package org.aio.activities.skills.runecrafting;

import org.aio.activities.activity.Activity;
import org.aio.activities.banking.Banking;
import org.aio.activities.banking.ItemReqBanking;
import org.aio.activities.skills.mining.RuneEssMiningActivity;
import org.aio.util.Executable;
import org.aio.util.Sleep;
import org.aio.util.item_requirement.ItemReq;
import org.osbot.rs07.api.ui.EquipmentSlot;

public class RunecraftingActivity extends RunecraftingBase {

    protected final EssenceType essenceType;
    private final ItemReq essenceReq;
    private ItemReq talismanReq;
    protected Executable banking;
    private Executable talismanBanking = new TalismanBanking();

    public RunecraftingActivity(final Altar altar, final EssenceType essenceType) {
        super(altar);
        this.essenceType = essenceType;
        this.essenceReq = new ItemReq(essenceType.toString(), 1);
    }

    @Override
    public void onStart() {
        talismanBanking.exchangeContext(getBot());

        if (getInventory().contains(altar.tiara) || getEquipment().isWearingItem(EquipmentSlot.HAT, altar.tiara)) {
            talismanReq = new ItemReq(altar.tiara);
        } else if (getInventory().contains(altar.talisman)) {
            talismanReq = new ItemReq(altar.talisman);
        }

        if (talismanReq != null) {
            banking = new ItemReqBanking(talismanReq, essenceReq);
            banking.exchangeContext(getBot());
        }
    }

    @Override
    public void runActivity() throws InterruptedException {
        if (talismanReq == null) {
            talismanBanking.run();
            if (talismanBanking.hasFailed()) {
                setFailed();
            }
        } else if (banking == null) {
            banking = new ItemReqBanking(talismanReq, essenceReq);
            banking.exchangeContext(getBot());
        } else if (!ItemReq.hasItemRequirements(new ItemReq[] { talismanReq, essenceReq }, getInventory(), getEquipment())) {
            if (getAltar() != null) {
                leaveAltar();
            } else {
                banking.run();
                if (banking.hasFailed()) {
                    setFailed();
                }
            }
        } else if (getBank() != null && getBank().isOpen()) {
            getBank().close();
        } else if(getInventory().contains(altar.tiara) && !getEquipment().isWearingItem(EquipmentSlot.HAT, altar.tiara)){
            equipTiara();
        } else if(getAltar() != null){
            craftRunes();
        } else if(altar.area.contains(myPosition()) && getObjects().closest("Mysterious ruins") != null){
            enterAltar();
        } else {
            walkToAltar();
        }
    }

    private void equipTiara(){
        if (getInventory().getItem(altar.tiara).interact("Wear")) {
            Sleep.sleepUntil(() -> getEquipment().contains(altar.tiara), 2000);
        }
    }

    private void craftRunes(){
        if (getAltar().interact("Craft-rune")) {
            Sleep.sleepUntil(() -> getDialogues().isPendingContinuation() || (!myPlayer().isAnimating() && !getInventory().contains("Rune essence", "Pure essence")), 10_000);
        }
    }

    @Override
    public RunecraftingActivity copy() {
        return new RunecraftingActivity(altar, essenceType);
    }

    private class TalismanBanking extends Banking {
        @Override
        protected boolean bank() {
            if (getBank().contains(altar.tiara)) {
                talismanReq = new ItemReq(altar.tiara).setEquipable();
            } else {
                talismanReq = new ItemReq(altar.talisman);
            }

            return true;
        }
    }
}
