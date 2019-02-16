package activities.grand_exchange.event;

import org.osbot.rs07.api.GrandExchange;
import util.Sleep;

public class GrandExchangeSellEvent extends GrandExchangeEvent {

    public GrandExchangeSellEvent(final String itemName, final int price, final int quantity) {
        super(itemName, price, quantity);
    }

    @Override
    public void onStart() {
        quantity = (int) Math.min(quantity, getInventory().getAmount(itemName));
    }

    @Override
    boolean isOfferScreenOpen() {
        return getGrandExchange().isSellOfferOpen();
    }

    @Override
    boolean openOfferScreen(final GrandExchange.Box box) {
        return getGrandExchange().sellItems(box);
    }

    @Override
    boolean setOfferItem() {
        if (getInventory().getItem(itemName).interact()) {
            Sleep.sleepUntil(() -> getOfferedItemID() != -1, 5000);
            return true;
        }
        return false;
    }
}
