package org.aio.activities.grand_exchange.event;

import org.aio.util.Sleep;
import org.aio.util.widget.CachedWidget;
import org.aio.util.widget.filters.WidgetDimensionFilter;
import org.osbot.rs07.api.GrandExchange;
import org.osbot.rs07.api.comparator.WidgetComparator;
import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.event.Event;

import java.util.Optional;

public class GrandExchangeBuyEvent extends GrandExchangeEvent {
    private static final int CHATBOX_INTERFACE_ID = 162;

    private static final CachedWidget SEARCH_TEXT_WIDGET = new CachedWidget(
            CHATBOX_INTERFACE_ID,
            w -> w.getMessage().startsWith("<col=000000>What would you like to buy?")
    );

    public GrandExchangeBuyEvent(final String itemName, final int price, final int quantity) {
        super(itemName, price, quantity);
    }

    @Override
    boolean isOfferScreenOpen() {
        return getGrandExchange().isBuyOfferOpen();
    }

    @Override
    boolean openOfferScreen(final GrandExchange.Box box) {
        return getGrandExchange().buyItems(box);
    }

    @Override
    boolean setOfferItem() {
        Optional<String> itemSearchTextOpt = getSearchText();

        if (!itemSearchTextOpt.isPresent()) {
            return false;
        }

        String itemSearchText = itemSearchTextOpt.get();

        if (!itemName.equals(itemSearchText)) {
            if (!itemSearchText.isEmpty()) {
                for (int i = 0; i < itemSearchText.length(); i++) {
                    getKeyboard().typeString("\b");
                }
            }
            getKeyboard().typeString(itemName, false);
        } else if (selectItemFromList(itemName)) {
            return true;
        }
        return false;
    }

    private Optional<String> getSearchText() {
        return SEARCH_TEXT_WIDGET.get(getWidgets())
                .map(RS2Widget::getMessage)
                .map(message -> message.replaceAll("<col=000000>What would you like to buy\\?</col> ", "")
                        .replaceAll("\\*", ""));
    }

    private boolean selectItemFromList(final String itemName) {
        RS2Widget itemWidget = getWidgets().singleFilter(CHATBOX_INTERFACE_ID, w -> w.getMessage().equals(itemName));

        if (itemWidget == null) {
            return false;
        }

        final RS2Widget resultsBoxWidget = getWidgets().get(itemWidget.getRootId(), itemWidget.getSecondLevelId());
        final RS2Widget scrollDownWidget = getWidgets().filter(CHATBOX_INTERFACE_ID, new WidgetDimensionFilter(16, 16)).stream().max(WidgetComparator.yComparator).get();

        Event selectItemWidgetEvent = new Event() {
            @Override
            public int execute() throws InterruptedException {
                if (itemWidget.isVisible() && resultsBoxWidget.getBounds().contains(itemWidget.getBounds())) {
                    if (itemWidget.interact()) {
                        Sleep.sleepUntil(() -> getOfferedItemID() != -1, 5000);
                        setFinished();
                    }
                } else {
                    scrollDownWidget.interact();
                }
                return 0;
            }
        };

        return execute(selectItemWidgetEvent).hasFinished();
    }
}
