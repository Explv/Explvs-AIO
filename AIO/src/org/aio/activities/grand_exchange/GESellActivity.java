package org.aio.activities.grand_exchange;

import org.aio.activities.banking.DepositAllBanking;
import org.aio.activities.banking.ItemReqBanking;
import org.aio.activities.grand_exchange.event.GrandExchangeSellEvent;
import org.aio.util.item_requirement.ItemReq;

public class GESellActivity extends GEActivity {

    private final GEItem geItem;
    private DepositAllBanking depositAllBanking;
    private ItemReqBanking itemReqBanking;
    private boolean checkedBank;

    public GESellActivity(final GEItem geItem) {
        this.geItem = geItem;
        depositAllBanking = new DepositAllBanking();
        itemReqBanking = new ItemReqBanking(
                new ItemReq(geItem.getName(), 1, geItem.getQuantity()).setStackable().setNoted()
        );
    }

    @Override
    public void onStart() {
        itemReqBanking.exchangeContext(getBot());
        depositAllBanking.exchangeContext(getBot());
    }

    @Override
    public void runActivity() throws InterruptedException {
        if (box != null) {
            return;
        }

        if (!GRAND_EXCHANGE.contains(myPosition())) {
            getWalking().webWalk(GRAND_EXCHANGE);
        } else if (!checkedBank && getInventory().getAmount(geItem.getName()) < geItem.getQuantity()) {
            if (!getInventory().isEmpty()) {
                depositAllBanking.run();
            } else {
                itemReqBanking.run();
                if (itemReqBanking.hasFailed()) {
                    setFailed();
                } else if (getBank() != null && getBank().isOpen()) {
                    checkedBank = true;
                }
            }
        } else {
            GrandExchangeSellEvent sellEvent = new GrandExchangeSellEvent(
                    geItem.getName(), geItem.getPrice(), geItem.getQuantity()
            );
            execute(sellEvent);
            box = sellEvent.getBoxUsed();
        }
    }

    @Override
    public String toString() {
        return "Selling";
    }

    @Override
    public GESellActivity copy() {
        return new GESellActivity(geItem);
    }
}
