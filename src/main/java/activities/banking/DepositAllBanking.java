package activities.banking;

import util.Sleep;

public class DepositAllBanking extends DepositAll {

	public DepositAllBanking(final String... exceptItems) {
		super(exceptItems);
	}

	@Override
	protected boolean bank() {
		if (exceptItems == null) {
			return getBank().depositAll();
		}
		return getBank().depositAllExcept(exceptItems);
	}

	protected void openDeposit() throws InterruptedException {
		if (getBank().open()) {
			Sleep.sleepUntil(() -> getBank().isOpen(), 5000);
		}
	}

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

}
