package org.aio.activities.grand_exchange;

import org.aio.util.Sleep;

public class GEBuyActivity extends GEActivity {

    private GrandExchangeHelper exchangeHelper;
    private final GEItem geItem;

    public GEBuyActivity(final GEItem geItem) {
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
        } else if(getInventory().getAmount("Coins") < (geItem.getPrice() * geItem.getQuantity())) {
            getCoins();
        } else {
            box = exchangeHelper.createBuyOffer(geItem.getName(), geItem.getPrice(), geItem.getQuantity());
        }
    }

    private void getCoins() throws InterruptedException {
        if (!getBank().isOpen()) {
            if (getBank().open()) {
                Sleep.sleepUntil(() -> getBank().isOpen(), 5000);
            }
        } else if (!getInventory().isEmpty()) {
            getBank().depositAll();
        } else if (getBank().getAmount("Coins") + getInventory().getAmount("Coins") >= (geItem.getPrice() * geItem.getQuantity())) {
            withdrawRemaining("Coins", (geItem.getPrice() * geItem.getQuantity()));
        } else{
            setFailed();
        }
    }

    private boolean withdrawRemaining(String itemName, int quantity) {
        long invAmount = getInventory().getAmount(itemName);
        return invAmount < quantity && getBank().withdraw(itemName, quantity - (int) invAmount);
    }

    @Override
    public String toString() {
        return "Buying";
    }
}
