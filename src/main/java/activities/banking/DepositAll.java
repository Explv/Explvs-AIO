package activities.banking;

import org.osbot.rs07.api.map.Area;

import util.Executable;
import util.Sleep;

public abstract class DepositAll extends Executable {
	
	protected String[] exceptItems;

	public DepositAll(final String... exceptItems) {
		this.exceptItems = exceptItems;
	}

	public void setExceptItems(final String... exceptItems) {
		this.exceptItems = exceptItems;
	}

	protected static final Area[] bankAreas = Bank.getAreas(), depositAreas = Deposit.getAreas();
	
	public boolean succeeded;

	protected abstract void openDeposit() throws InterruptedException;

/*	void openBank() throws InterruptedException {
		if (getBank().open()) {
			Sleep.sleepUntil(() -> getBank().isOpen(), 5000);
		}
	}*/

	/**
	 * Execute banking operation
	 *
	 * @return whether the operation was a success or not
	 */
	protected abstract boolean bank();

	@Override
	public void onEnd() {
		if (getBank().isOpen()) {
			closeBank();
		}
		if (getDepositBox().isOpen()) {
			closeDeposit();
		}
	}

	private void closeBank() {
		if (getBank().close()) {
			Sleep.sleepUntil(() -> !getBank().isOpen(), 2500);
		}
	}

	private void closeDeposit() {
		if (getDepositBox().close()) {
			Sleep.sleepUntil(() -> !getDepositBox().isOpen(), 2500);
		}
	}

	@Override
	public String toString() {
		return "Deposit";
	}

}
