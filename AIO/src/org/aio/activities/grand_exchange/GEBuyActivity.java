package org.aio.activities.grand_exchange;

import org.aio.activities.banking.ItemReqBanking;
import org.aio.activities.grand_exchange.event.GrandExchangeBuyEvent;
import org.aio.util.item_requirement.ItemReq;

public class GEBuyActivity extends GEActivity {

    private final GEItem geItem;
    private final ItemReq coinReq;

    private ItemReqBanking itemReqBanking;

    public GEBuyActivity(final GEItem geItem) {
        this.geItem = geItem;

        int coinsRequired = geItem.getPrice() * geItem.getQuantity();

        coinReq = new ItemReq(
                "Coins",
                coinsRequired,
                coinsRequired
        ).setStackable();

        itemReqBanking = new ItemReqBanking(coinReq);
    }

    @Override
    public void onStart() {
        itemReqBanking.exchangeContext(getBot());
    }

    @Override
    public void runActivity() throws InterruptedException {
        if (box != null) {
            return;
        }

        if(!GRAND_EXCHANGE.contains(myPosition())){
            getWalking().webWalk(GRAND_EXCHANGE);
        } else if(!coinReq.hasRequirement(getInventory())) {
            itemReqBanking.run();
            if (itemReqBanking.hasFailed()) {
                setFailed();
            }
        } else {
            GrandExchangeBuyEvent buyEvent = new GrandExchangeBuyEvent(
                    geItem.getName(),
                    geItem.getPrice(),
                    geItem.getQuantity()
            );
            execute(buyEvent);
            if (buyEvent.hasFailed()) {
                setFailed();
            } else {
                box = buyEvent.getBoxUsed();
            }
        }
    }

    @Override
    public String toString() {
        return "Buying";
    }

    @Override
    public GEBuyActivity copy() {
        return new GEBuyActivity(geItem);
    }
}
