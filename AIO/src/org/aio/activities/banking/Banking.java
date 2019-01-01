package org.aio.activities.banking;

import org.aio.util.Executable;
import org.aio.util.Sleep;
import org.osbot.rs07.api.map.Area;

public abstract class Banking extends Executable {

    protected static final Area[] bankAreas = Bank.getAreas();

    @Override
    public void run() throws InterruptedException {
        if (!Bank.inAnyBank(myPosition())) {
            getWalking().webWalk(bankAreas);
        } else if (!getBank().isOpen()) {
            openBank();
        } else {
            bank();
        }
    }

    void openBank() throws InterruptedException {
        if (getBank().open()) {
            Sleep.sleepUntil(() -> getBank().isOpen(), 5000);
        }
    }

    protected abstract void bank();

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

