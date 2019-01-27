package org.aio.activities.grand_exchange;

import org.aio.activities.activity.Activity;
import org.osbot.rs07.api.GrandExchange;
import org.osbot.rs07.api.map.Area;

public abstract class GEActivity extends Activity {

    protected static final Area GRAND_EXCHANGE = new Area(3154, 3479, 3174, 3500);

    GrandExchange.Box box;

    GEActivity() {
        super(null);
    }

    public GrandExchange.Box getBox() {
        return box;
    }

    @Override
    public abstract GEActivity copy();
}
