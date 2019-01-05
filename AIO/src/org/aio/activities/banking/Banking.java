package org.aio.activities.banking;

import org.aio.util.Executable;
import org.aio.util.Sleep;
import org.osbot.rs07.api.map.Area;

public abstract class Banking extends Executable {

    public boolean succeeded;

    protected static final Area[] bankAreas = Bank.getAreas();

    @Override
    public void run() throws InterruptedException {
        if (!Bank.inAnyBank(myPosition())) {
            getWalking().webWalk(bankAreas);
        } else if (!getBank().isOpen()) {
            openBank();
        } else {
            succeeded = bank();
        }
    }

    void openBank() throws InterruptedException {
        if (getBank().open()) {
            Sleep.sleepUntil(() -> getBank().isOpen(), 5000);
        }
    }

    /**
     * Execute banking operation
     * @return whether the operation was a success or not
     */
    protected abstract boolean bank();

    @Override
    public void onEnd() {
        if (getBank().isOpen()) {
            closeBank();
        }
    }

    private void closeBank() {
        if (getBank().close()) {
            Sleep.sleepUntil(() -> !getBank().isOpen(), 2500);
        }
    }

    @Override
    public String toString() {
        return "ItemReqBanking";
    }
}

