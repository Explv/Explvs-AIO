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
    protected void bank(final BankType currentBankType) {
        boolean success;

        if (exceptItems == null) {
            if (currentBankType == BankType.DEPOSIT_BOX) {
                success = getDepositBox().depositAll();
            } else {
                success = getBank().depositAll();
            }
        } else if (currentBankType == BankType.DEPOSIT_BOX) {
            success = getDepositBox().depositAllExcept(exceptItems);
        } else {
            success = getBank().depositAllExcept(exceptItems);
        }

        if (success) {
            setFinished();
        }
    }
}
