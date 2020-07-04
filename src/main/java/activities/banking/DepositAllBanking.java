package activities.banking;

import util.item_requirement.ItemReq;

import java.util.stream.Stream;

public class DepositAllBanking extends Banking {

    private String[] exceptItems;

    public DepositAllBanking(final String... exceptItems) {
        super(true);
        this.exceptItems = exceptItems;
    }

    public DepositAllBanking() {
        this(new String[]{});
    }

    public DepositAllBanking(final ItemReq... exceptItems) {
        this(Stream.of(exceptItems).map(ItemReq::getName).toArray(String[]::new));
    }

    public void setExceptItems(final String... exceptItems) {
        this.exceptItems = exceptItems;
    }

    @Override
    protected boolean bank(final BankType currentBankType) {
        if (exceptItems == null) {
            if (currentBankType == BankType.DEPOSIT_BOX) {
                return getDepositBox().depositAll();
            } else {
                return getBank().depositAll();
            }
        }

        if (currentBankType == BankType.DEPOSIT_BOX) {
            return getDepositBox().depositAllExcept(exceptItems);
        } else {
            return getBank().depositAllExcept(exceptItems);
        }
    }
}
