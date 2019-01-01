package org.aio.activities.grand_exchange;

import org.aio.util.Sleep;
import org.osbot.rs07.api.Bank;

public class GESellActivity extends GEActivity {

    private GrandExchangeHelper exchangeHelper;
    private final GEItem geItem;

    public GESellActivity(final GEItem geItem) {
        this.geItem = geItem;
    }

    @Override
    public void onStart() {
        this.exchangeHelper = new GrandExchangeHelper();
        this.exchangeHelper.exchangeContext(getBot());
    }

    @Override
    public void runActivity() throws InterruptedException {
        if (box != null) {
            return;
        } else if(!exchangeHelper.playerIsAtGE()){
            exchangeHelper.walkToGE();
        } else if(getInventory().getAmount(geItem.getName()) < geItem.getQuantity()) {
            getItemFromBank();
        } else {
            box = exchangeHelper.createSellOffer(geItem.getName(), geItem.getPrice(), geItem.getQuantity());
        }
    }

    private void getItemFromBank() throws InterruptedException {

        if (!getBank().isOpen()) openBank();
        else if (!getInventory().isEmpty()) getBank().depositAll();
        else if (geItem.getQuantity() > 28 && getBank().getWithdrawMode() != Bank.BankMode.WITHDRAW_NOTE)
            getBank().enableMode(Bank.BankMode.WITHDRAW_NOTE);
        else if (getBank().getAmount(geItem.getName()) >= geItem.getQuantity())
            getBank().withdraw(geItem.getName(), geItem.getQuantity());
        else setFailed();
    }

    private void openBank() throws InterruptedException {
        if (getBank().open()) {
            Sleep.sleepUntil(() -> getBank().isOpen(), 5000);
        }
    }

    @Override
    public String toString() {
        return "Selling";
    }
}
