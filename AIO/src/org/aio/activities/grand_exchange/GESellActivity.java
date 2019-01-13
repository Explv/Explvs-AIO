package org.aio.activities.grand_exchange;

import org.aio.activities.banking.DepositAllBanking;
import org.aio.activities.banking.ItemReqBanking;
import org.aio.util.item_requirement.ItemReq;

public class GESellActivity extends GEActivity {

    private GrandExchangeHelper exchangeHelper;
    private final GEItem geItem;
    private DepositAllBanking depositAllBanking;
    private ItemReqBanking itemReqBanking;
    private boolean checkedBank;

    public GESellActivity(final GEItem geItem) {
        this.geItem = geItem;
        depositAllBanking = new DepositAllBanking();
        itemReqBanking = new ItemReqBanking(new ItemReq(geItem.getName(), 1, geItem.getQuantity()).setNoted());
    }

    @Override
    public void onStart() {
        this.exchangeHelper = new GrandExchangeHelper();
        this.exchangeHelper.exchangeContext(getBot());
        itemReqBanking.exchangeContext(getBot());
        depositAllBanking.exchangeContext(getBot());
    }

    @Override
    public void runActivity() throws InterruptedException {
        if (box != null) {
            return;
        } else if(!exchangeHelper.playerIsAtGE()){
            exchangeHelper.walkToGE();
        } else if(!checkedBank && getInventory().getAmount(geItem.getName()) < geItem.getQuantity()) {
            if (!getInventory().isEmpty()) {
                depositAllBanking.run();
            } else {
                itemReqBanking.run();
                if (itemReqBanking.hasFailed()) {
                    setFailed();
                } else {
                    checkedBank = true;
                }
            }
        } else {
            box = exchangeHelper.createSellOffer(geItem.getName(), geItem.getPrice(), geItem.getQuantity());
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
