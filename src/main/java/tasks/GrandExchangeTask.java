package tasks;

import activities.grand_exchange.GEActivity;
import activities.grand_exchange.GEItem;
import activities.grand_exchange.GEMode;
import org.osbot.rs07.api.GrandExchange;
import util.RSUnits;

public class GrandExchangeTask extends Task {

    private final GEActivity geActivity;
    private final GEMode geMode;
    private final GEItem geItem;
    private final boolean waitForCompletion;
    private boolean completed;

    public GrandExchangeTask(final GEActivity geActivity, final GEMode geMode, final GEItem geItem, final boolean waitForCompletion) {
        super(TaskType.GRAND_EXCHANGE, geActivity);
        this.geActivity = geActivity;
        this.geMode = geMode;
        this.geItem = geItem;
        this.waitForCompletion = waitForCompletion;
    }

    @Override
    public boolean isComplete() {
        if (completed) {
            return true;
        }

        GEActivity activity = (GEActivity) getActivity();

        if (activity.getBox() == null) {
            return false;
        }

        if (activity.getBox() != null && !waitForCompletion) {
            completed = true;
            return true;
        }

        if (geMode == GEMode.BUY && getGrandExchange().getStatus(activity.getBox()) == GrandExchange.Status.FINISHED_BUY) {
            if (getGrandExchange().collect(true)) {
                completed = true;
                return true;
            } else {
                return false;
            }
        }

        if (geMode == GEMode.SELL && getGrandExchange().getStatus(activity.getBox()) == GrandExchange.Status.FINISHED_SALE) {
            if (getGrandExchange().collect(true)) {
                completed = true;
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format(
                "Grand Exchange task: %s %s %s for %s each",
                geMode.toString(),
                RSUnits.valueToFormatted(geItem.getQuantity()),
                geItem.getName(),
                RSUnits.valueToFormatted(geItem.getPrice())
        );
    }

    @Override
    public Task copy() {
        return new GrandExchangeTask(geActivity.copy(), geMode, geItem, waitForCompletion);
    }
}
