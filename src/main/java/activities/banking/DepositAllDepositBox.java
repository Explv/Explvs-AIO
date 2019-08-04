package activities.banking;

import util.Sleep;

public class DepositAllDepositBox extends DepositAll {
	
	public DepositAllDepositBox(final String... exceptItems) {
		super(exceptItems);
	}

	@Override
	protected boolean bank() {
		if (exceptItems == null) {
			return getDepositBox().depositAll();
		}
		return getDepositBox().depositAllExcept(exceptItems);
	}

	protected void openDeposit() throws InterruptedException {
		if (getDepositBox().open()) {
			Sleep.sleepUntil(() -> getDepositBox().isOpen(), 5000);
		}
	}

	@Override
	public void run() throws InterruptedException {
		if (!Deposit.inAnyDeposit(myPosition())) {
			getWalking().webWalk(depositAreas);
		} else if (!getDepositBox().isOpen()) {
			openDepositBox();
		} else {
			succeeded = bank();
		}
	}

	void openDepositBox() throws InterruptedException {
		if (getDepositBox().open()) {
			Sleep.sleepUntil(() -> getDepositBox().isOpen(), 5000);
		}
	}

}
