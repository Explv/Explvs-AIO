package activities.banking;

import org.osbot.rs07.api.map.Area;
import util.Sleep;
import util.executable.BlockingExecutable;

import java.util.stream.Stream;

public abstract class Banking extends BlockingExecutable {

    private static final Area[] ALL_BANK_AND_DEPOSIT_BOX_AREAS = Stream.concat(Stream.of(Bank.AREAS), Stream.of(DepositBox.AREAS)).toArray(Area[]::new);

    private final boolean useDepositBoxes;

    protected enum BankType {
        BANK,
        DEPOSIT_BOX
    }

    private BankType currentBankType;
    private boolean finishedBanking;

    public Banking() {
        this.useDepositBoxes = false;
    }

    public Banking(final boolean useDepositBoxes) {
        this.useDepositBoxes = useDepositBoxes;
    }

    /**
     * Override BlockingExecutable::setFinished
     * to prevent finishing immediately upon a super class
     * setFinished() call. We need to ensure that the bank is closed
     * before we end the executable.
     */
    @Override
    public void setFinished() {
        finishedBanking = true;
    }

    @Override
    public void blockingRun() throws InterruptedException {
        if (finishedBanking) {
            if (isBankOpen()) {
                closeBank();
            } else {
                super.setFinished();
            }
        } else if (!playerInBank()) {
            walkToBank();
        } else if (getInventory().contains("Coin pouch")) {
            getInventory().getItem("Coin pouch").interact();
        } else if (!isBankOpen()) {
            openBank();
        } else {
            if (getBank() != null && getBank().isOpen()) {
                currentBankType = BankType.BANK;
            } else {
                currentBankType = BankType.DEPOSIT_BOX;
            }

            bank(currentBankType);
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
        if (useDepositBoxes && getBank() != null && getDepositBox() != null) {
            return getBank().isOpen() || getDepositBox().isOpen();
        }

        if (getBank() != null) {
            return getBank().isOpen();
        }

        if (useDepositBoxes && getDepositBox() != null) {
            return getDepositBox().isOpen();
        }

        return false;
    }

    private void openBank() throws InterruptedException {
        if (getBank() != null && getBank().open()) {
            Sleep.sleepUntil(() -> getBank().isOpen(), 5000);
            return;
        }

        if (useDepositBoxes && getDepositBox() != null) {
            if (getDepositBox().open()) {
                Sleep.sleepUntil(() -> getDepositBox().isOpen(), 5000);
            }
        }

        throw new IllegalStateException("Cannot open bank, no bank or deposit box found.");
    }

    /**
     * Execute banking operation
     */
    protected abstract void bank(final BankType currentBankType) throws InterruptedException;

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

