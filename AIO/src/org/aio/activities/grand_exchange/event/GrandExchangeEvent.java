package org.aio.activities.grand_exchange.event;

import org.aio.util.Sleep;
import org.aio.util.widget.CachedWidget;
import org.aio.util.widget.filters.WidgetActionFilter;
import org.aio.util.widget.filters.WidgetDimensionFilter;
import org.osbot.rs07.api.GrandExchange;
import org.osbot.rs07.api.comparator.WidgetComparator;
import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.event.Event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

abstract class GrandExchangeEvent extends Event {
    private static final int GRAND_EXCHANGE_INTERFACE_ID = 465;

    private static final int ITEM_ID_CONFIG = 1151;

    private static final CachedWidget GRAND_EXCHANGE_INTERFACE_WIDGET = new CachedWidget(
            GRAND_EXCHANGE_INTERFACE_ID,
            "Grand Exchange"
    );

    private static final CachedWidget COLLECT_TO_BANK_WIDGET = new CachedWidget(
            GRAND_EXCHANGE_INTERFACE_ID,
            new WidgetActionFilter("Collect to bank")
    );

    private static final CachedWidget COLLECT_TO_INVENTORY_WIDGET = new CachedWidget(
            GRAND_EXCHANGE_INTERFACE_ID,
            new WidgetActionFilter("Collect to inventory")
    );

    private static final GrandExchange.Box[] F2P_BOXES = new GrandExchange.Box[]{
            GrandExchange.Box.BOX_1, GrandExchange.Box.BOX_2, GrandExchange.Box.BOX_3
    };
    private static final GrandExchange.Box[] MEMBERS_BOXES = GrandExchange.Box.values();
    private static final ConcurrentHashMap<Integer, CachedWidget> BOX_WIDGETS = new ConcurrentHashMap<>();
    private static final Filter<RS2Widget> BOX_FILTER = new WidgetDimensionFilter(115, 110);

    final String itemName;
    private final int price;
    int quantity;

    private GrandExchange.Box boxUsed;

    GrandExchangeEvent(final String itemName, final int price, final int quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }

    public GrandExchange.Box getBoxUsed() {
        return boxUsed;
    }

    @Override
    public int execute() throws InterruptedException {
        if (!isGrandExchangeOpen()) {
            openGrandExchange();
        } else if (boxUsed != null && getGrandExchange().getStatus(boxUsed) != GrandExchange.Status.EMPTY) {
            setFinished();
        } else if (!isOfferScreenOpen()) {
            Optional<GrandExchange.Box> emptyBox = getEmptyBox();
            if (emptyBox.isPresent()) {
                if (openOfferScreen(emptyBox.get())) {
                    this.boxUsed = emptyBox.get();
                }
            } else if (finishedBoxExists()) {
                collectToBank();
            } else {
                abortOffer();
            }
        } else if (getConfigs().get(ITEM_ID_CONFIG) == -1) {
            setOfferItem();
        } else if (getGrandExchange().getOfferPrice() != price) {
            setOfferPrice(price);
        } else if (getGrandExchange().getOfferQuantity() != quantity) {
            setOfferQuantity(quantity);
        } else if (getGrandExchange().confirm()) {
            setFinished();
        }
        return random(100, 150);
    }

    private boolean isGrandExchangeOpen() {
        return GRAND_EXCHANGE_INTERFACE_WIDGET.isVisible(getWidgets());
    }

    private boolean openGrandExchange() {
        NPC exchangeWorker = getNpcs().closest("Grand Exchange Clerk");
        if (exchangeWorker != null && exchangeWorker.interact("Exchange")) {
            Sleep.sleepUntil(this::isGrandExchangeOpen, 3000, 600);
            return true;
        }
        return false;
    }

    int getOfferedItemID() {
        return getConfigs().get(ITEM_ID_CONFIG);
    }

    abstract boolean isOfferScreenOpen();

    abstract boolean openOfferScreen(final GrandExchange.Box box);

    abstract boolean setOfferItem();

    private Optional<GrandExchange.Box> getEmptyBox() {
        GrandExchange.Box[] boxes = getWorlds().isMembersWorld() ? MEMBERS_BOXES : F2P_BOXES;
        return Arrays.stream(boxes).filter(this::isBoxEmpty).findAny();
    }

    private boolean isBoxEmpty(final GrandExchange.Box box) {
        return getGrandExchange().getStatus(box) == GrandExchange.Status.EMPTY;
    }

    private Optional<RS2Widget> getBoxWidget(int boxIndex) {
        if (!getGrandExchange().isOpen()) {
            return Optional.empty();
        }

        if (getGrandExchange().isOfferScreenOpen()) {
            return Optional.empty();
        }

        if (BOX_WIDGETS.containsKey(boxIndex)) {
            return BOX_WIDGETS.get(boxIndex).get(getWidgets());
        }

        List<RS2Widget> boxWidgets = new ArrayList<>();

        for (final RS2Widget widget : getWidgets().getWidgets(GRAND_EXCHANGE_INTERFACE_ID)) {
            if (BOX_FILTER.match(widget)) {
                boxWidgets.add(widget);
            }
        }

        if (boxWidgets.size() != GrandExchange.Box.values().length) {
            return Optional.empty();
        }

        boxWidgets.sort(WidgetComparator.yComparator);

        List<RS2Widget> topRowWidgets = boxWidgets.subList(0, 4);
        topRowWidgets.sort(WidgetComparator.xComparator);

        List<RS2Widget> bottomRowWidgets = boxWidgets.subList(4, 8);
        bottomRowWidgets.sort(WidgetComparator.xComparator);

        for (int i = 0; i < 4; i++) {
            BOX_WIDGETS.put(i, new CachedWidget(topRowWidgets.get(i)));
        }

        for (int i = 4; i < 8; i++) {
            BOX_WIDGETS.put(i, new CachedWidget(bottomRowWidgets.get(i)));
        }

        return BOX_WIDGETS.get(boxIndex).get(getWidgets());
    }

    private boolean finishedBoxExists() {
        for (GrandExchange.Box box : GrandExchange.Box.values()) {
            GrandExchange.Status status = getGrandExchange().getStatus(box);
            if (status == GrandExchange.Status.FINISHED_BUY || status == GrandExchange.Status.FINISHED_SALE) {
                return true;
            }
        }
        return false;
    }

    private boolean abortOffer() {
        Optional<RS2Widget> boxWidget = getBoxWidget(0);
        if (boxWidget.isPresent() && boxWidget.get().interact("Abort offer")) {
            Sleep.sleepUntil(() -> isBoxEmpty(GrandExchange.Box.BOX_1), 5000);
            return true;
        }
        return false;
    }

    private boolean setOfferQuantity(final int quantity) {
        if (getGrandExchange().setOfferQuantity(quantity)) {
            Sleep.sleepUntil(() -> getGrandExchange().getOfferQuantity() == quantity, 3000);
            return true;
        }
        return false;
    }

    private boolean setOfferPrice(final int price) {
        if (getGrandExchange().setOfferPrice(price)) {
            Sleep.sleepUntil(() -> getGrandExchange().getOfferPrice() == price, 3000);
            return true;
        }
        return false;
    }

    private boolean collectToBank() {
        if (COLLECT_TO_BANK_WIDGET.interact(getWidgets(), "Collect to bank")) {
            Sleep.sleepUntil(() -> !COLLECT_TO_BANK_WIDGET.isVisible(getWidgets()), 2400, 600);
            return true;
        }
        return false;
    }

    private boolean collectToInventory() {
        if (COLLECT_TO_INVENTORY_WIDGET.interact(getWidgets(), "Collect to inventory")) {
            Sleep.sleepUntil(() -> !COLLECT_TO_INVENTORY_WIDGET.isVisible(getWidgets()), 2400, 600);
            return true;
        }
        return false;
    }
}