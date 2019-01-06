package org.aio.activities.grand_exchange;

import org.aio.activities.activity.Activity;
import org.osbot.GE;
import org.osbot.rs07.api.GrandExchange;

public abstract class GEActivity extends Activity {

    protected GrandExchange.Box box;

    public GEActivity() {
        super(null);
    }

    public GrandExchange.Box getBox() {
        return box;
    }

    @Override
    public abstract GEActivity copy();
}
