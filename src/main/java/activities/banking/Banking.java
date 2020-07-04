package activities.banking;

import org.osbot.rs07.api.map.Area;
import util.Executable;
import util.Sleep;

import java.util.stream.Stream;

public abstract class Banking extends Executable {

    private static final Area[] ALL_BANK_AND_DEPOSIT_BOX_AREAS = Stream.concat(Stream.of(Bank.AREAS), Stream.of(DepositBox.AREAS)).toArray(Area[]::new);

    private final boolean useDepositBoxes;

    protected enum BankType {
        BANK,
        DEPOSIT_BOX
    }

    private BankType currentBankType;
    public boolean succeeded;

    public Banking() {
        this.useDepositBoxes = false;
    }

    public Banking(final boolean useDepositBoxes) {
        this.useDepositBoxes = useDepositBoxes;
    }

    @Override
    public void run() throws InterruptedException {
        if (!playerInBank()) {
            walkToBank();
        } else if (!isBankOpen()) {
            currentBankType = openBank();
        } else {
            succeeded = bank(currentBankType);
        }
    }

    private boolean playerInBank() {
        if (useDepositBoxes) {
            return Stream.of(ALL_BANK_AND_DEPOSIT_BOX_AREAS).anyMatch(area -> area.contains(myPosition()));
        }
        return Stream.of(Bank.AREAS).anyMatch(area -> area.contains(myPosition()));
    }

    private boolean walkToBank() {
        if (useDepositBoxes) {
            return getWalking().webWalk(ALL_BANK_AND_DEPOSIT_BOX_AREAS);
        }
        return getWalking().webWalk(Bank.AREAS);
    }

    boolean isBankOpen() {
        if (useDepositBoxes) {
            return getDepositBox().isOpen();
        }
        return getBank().isOpen();
    }

    BankType openBank() throws InterruptedException {
        if (useDepositBoxes && getDepositBox() != null) {
            if (getDepositBox().open()) {
                Sleep.sleepUntil(() -> getDepositBox().isOpen(), 5000);
            }
            return BankType.DEPOSIT_BOX;
        }

        if (getBank().open()) {
            Sleep.sleepUntil(() -> getBank().isOpen(), 5000);
        }
        return BankType.BANK;
    }

    /**
     * Execute banking operation
     *
     * @return whether the operation was a success or not
     */
    protected abstract boolean bank(final BankType currentBankType);

    @Override
    public void onEnd() {
        closeBank();
    }

    private void closeBank() {
        if (currentBankType == BankType.DEPOSIT_BOX) {
            if (getDepositBox().isOpen() && getDepositBox().close()) {
                Sleep.sleepUntil(() -> !getDepositBox().isOpen(), 2500);
            }
            return;
        }

        if (getBank().isOpen() && getBank().close()) {
            Sleep.sleepUntil(() -> !getBank().isOpen(), 2500);
        }
    }

    @Override
    public String toString() {
        return "ItemReqBanking";
    }
}

