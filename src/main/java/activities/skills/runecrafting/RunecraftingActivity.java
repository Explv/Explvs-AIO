package activities.skills.runecrafting;

import activities.banking.Banking;
import activities.banking.ItemReqBanking;
import org.osbot.rs07.api.ui.EquipmentSlot;
import util.executable.Executable;
import util.Sleep;
import util.item_requirement.ItemReq;

public class RunecraftingActivity extends RunecraftingBase {

    protected final EssenceType essenceType;
    private final ItemReq essenceReq;
    protected Executable banking;
    private ItemReq talismanReq;
    private Executable talismanBanking = new TalismanBanking();

    public RunecraftingActivity(final Altar altar, final EssenceType essenceType) {
        super(altar);
        this.essenceType = essenceType;
        this.essenceReq = new ItemReq(essenceType.toString(), 1);
    }

    @Override
    public void onStart() {
        if (getInventory().contains(altar.tiara) || getEquipment().isWearingItem(EquipmentSlot.HAT, altar.tiara)) {
            talismanReq = new ItemReq(altar.tiara).setEquipable();
        } else if (getInventory().contains(altar.talisman)) {
            talismanReq = new ItemReq(altar.talisman);
        }

        if (talismanReq != null) {
            banking = new ItemReqBanking(talismanReq, essenceReq);
        }
    }

    @Override
    public void runActivity() throws InterruptedException {
        if (talismanReq == null) {
            execute(talismanBanking);
        } else if (banking == null) {
            banking = new ItemReqBanking(talismanReq, essenceReq);
        } else if (!ItemReq.hasItemRequirements(new ItemReq[]{talismanReq, essenceReq}, getInventory(), getEquipment())) {
            if (getAltar() != null) {
                leaveAltar();
            } else {
                execute(banking);
            }
        } else if (getInventory().contains(altar.tiara) && !getEquipment().isWearingItem(EquipmentSlot.HAT, altar.tiara)) {
            equipTiara();
        } else if (getAltar() != null) {
            craftRunes();
        } else if (altar.area.contains(myPosition()) && getObjects().closest("Mysterious ruins") != null) {
            enterAltar();
        } else {
            walkToAltar();
        }
    }

    private void equipTiara() {
        if (getInventory().getItem(altar.tiara).interact("Wear")) {
            Sleep.sleepUntil(() -> getEquipment().contains(altar.tiara), 2000);
        }
    }

    private void craftRunes() {
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
        protected void bank(final BankType currentBankType) {
            if (getBank().contains(altar.tiara)) {
                talismanReq = new ItemReq(altar.tiara).setEquipable();
            } else {
                talismanReq = new ItemReq(altar.talisman);
            }
            setFinished();
        }
    }
}
