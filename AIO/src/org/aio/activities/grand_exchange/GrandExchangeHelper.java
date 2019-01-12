package org.aio.activities.grand_exchange;

import org.aio.util.Sleep;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.osbot.rs07.api.GrandExchange;
import org.osbot.rs07.api.comparator.WidgetComparator;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.event.Event;
import org.osbot.rs07.script.MethodProvider;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

public class GrandExchangeHelper extends MethodProvider {

    private static final Map<String, Integer> allGEItems = new HashMap<>();

    private static final Area GRAND_EXCHANGE = new Area(3154, 3479, 3174, 3500);
    private static final int ITEM_ID_CONFIG = 1151;

    public static Map<String, Integer> getAllGEItems() {
        if (allGEItems.isEmpty()) {

            File summaryFile = Paths.get(System.getProperty("user.home"), "OSBot", "Data", "explv_aio_rsbuddy_summary.json").toFile();

            try {
                if (!summaryFile.exists()) {
                    System.out.println("Downloading summary JSON from RSBuddy");
                    URL website = new URL("https://rsbuddy.com/exchange/summary.json");
                    ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                    FileOutputStream fos = new FileOutputStream(summaryFile);
                    fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                }

                try (FileReader fileReader = new FileReader(summaryFile)) {
                    JSONObject jsonObject = (JSONObject) (new JSONParser().parse(fileReader));
                    for (Object value : jsonObject.values()) {
                        JSONObject item = (JSONObject) value;
                        allGEItems.put((String) item.get("name"), new Long((long) item.get("id")).intValue());
                    }
                }
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }
        return allGEItems;
    }

    public static Optional<Integer> getBuyPrice(final int itemID) {
        return getRSBuddyPrice(itemID, "buying");
    }

    public static Optional<Integer> getSellPrice(final int itemID) {
        return getRSBuddyPrice(itemID, "selling");
    }

    private static Optional<Integer> getRSBuddyPrice(final int itemID, final String type) {
        try {
            URL url = new URL("http://api.rsbuddy.com/grandExchange?a=guidePrice&i=" + itemID);
            URLConnection con = url.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36");

            try (InputStreamReader inputStreamReader = new InputStreamReader(con.getInputStream());
                 BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
                JSONParser jsonParser = new JSONParser();
                JSONObject itemJSON = (JSONObject) jsonParser.parse(bufferedReader);

                int price = new Long((long) itemJSON.get(type)).intValue();

                System.out.println("Got RSBuddy price");
                return Optional.of(price);
            }
        } catch (Exception e) {
            return getOSRSExchangePrice(itemID);
        }
    }


    private static Optional<Integer> getOSRSExchangePrice(final int itemID) {
        try {
            URL url = new URL("http://services.runescape.com/m=itemdb_oldschool/api/catalogue/detail.json?item=" + itemID);
            URLConnection con = url.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36");

            try (InputStreamReader inputStreamReader = new InputStreamReader(con.getInputStream());
                 BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
                JSONParser jsonParser = new JSONParser();
                JSONObject json = (JSONObject) jsonParser.parse(bufferedReader);

                JSONObject itemJSON = (JSONObject) json.get("item");
                JSONObject currentPriceData = (JSONObject) itemJSON.get("current");
                int currentPrice = new Long((long) currentPriceData.get("price")).intValue();

                return Optional.of(currentPrice);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public boolean playerIsAtGE() {
        return GRAND_EXCHANGE.contains(myPosition());
    }

    public void walkToGE() {
        getWalking().webWalk(GRAND_EXCHANGE);
    }

    private Optional<GrandExchange.Box> getEmptyBox() {
        GrandExchange.Box[] boxes;
        if (getWorlds().isMembersWorld()) {
            boxes = GrandExchange.Box.values();
        } else {
            boxes = new GrandExchange.Box[]{GrandExchange.Box.BOX_1, GrandExchange.Box.BOX_2, GrandExchange.Box.BOX_3};
        }
        return Arrays.stream(boxes).filter(this::isBoxEmpty).findAny();
    }

    private boolean isBoxEmpty(final GrandExchange.Box box) {
        return getGrandExchange().getStatus(box) == GrandExchange.Status.EMPTY;
    }

    private void open() {
        NPC exchangeWorker = getNpcs().closest("Grand Exchange Clerk");
        if (exchangeWorker != null && exchangeWorker.interact("Exchange")) {
            Sleep.sleepUntil(() -> getGrandExchange().isOpen(), 3000);
        }
    }

    public GrandExchange.Box createBuyOffer(final String itemName, final int price, final int quantity) {
        GrandExchangeEvent grandExchangeEvent = new GrandExchangeEvent() {
            @Override
            public int execute() throws InterruptedException {
                if (!getGrandExchange().isOpen()) {
                    open();
                } else if (boxUsed != null && getGrandExchange().getStatus(boxUsed) != GrandExchange.Status.EMPTY) {
                    setFinished();
                } else if (!getGrandExchange().isBuyOfferOpen()) {
                    Optional<GrandExchange.Box> emptyBox = getEmptyBox();
                    if (emptyBox.isPresent()) {
                        if (getGrandExchange().buyItems(emptyBox.get())) {
                            this.boxUsed = emptyBox.get();
                        }
                    } else if (finishedBoxExists()) {
                        getGrandExchange().collect(true);
                    } else {
                        RS2Widget box1Widget = getBoxWidget(0);
                        if (box1Widget != null && box1Widget.interact("Abort offer")) {
                            Sleep.sleepUntil(() -> isBoxEmpty(GrandExchange.Box.BOX_1), 5000);
                        }
                    }
                } else if (getConfigs().get(ITEM_ID_CONFIG) == -1) {
                    getSearchText().ifPresent(text -> {
                        if (!itemName.equals(text)) {
                            if (!text.isEmpty()) {
                                for (int i = 0; i < text.length(); i++) {
                                    getKeyboard().typeString("\b");
                                }
                            }
                            getKeyboard().typeString(itemName, false);
                        } else {
                            selectItemFromList(itemName);
                        }
                    });
                } else if (getGrandExchange().getOfferPrice() != price) {
                    if (getGrandExchange().setOfferPrice(price)) {
                        Sleep.sleepUntil(() -> getGrandExchange().getOfferPrice() == price, 3000);
                    }
                } else if (getGrandExchange().getOfferQuantity() != quantity) {
                    if (getGrandExchange().setOfferQuantity(quantity)) {
                        Sleep.sleepUntil(() -> getGrandExchange().getOfferQuantity() == quantity, 3000);
                    }
                } else if (getGrandExchange().confirm()) {
                    setFinished();
                }
                return random(100, 150);
            }
        };
        execute(grandExchangeEvent);
        return grandExchangeEvent.boxUsed;
    }

    public GrandExchange.Box createSellOffer(final String itemName, final int price, final int quantity) {
        GrandExchangeEvent grandExchangeEvent = new GrandExchangeEvent() {
            @Override
            public int execute() throws InterruptedException {
                if (!getGrandExchange().isOpen()) {
                    open();
                } else if (boxUsed != null && getGrandExchange().getStatus(boxUsed) != GrandExchange.Status.EMPTY) {
                    setFinished();
                } else if (!getGrandExchange().isSellOfferOpen()) {
                    Optional<GrandExchange.Box> emptyBox = getEmptyBox();
                    if (emptyBox.isPresent()) {
                        if (getGrandExchange().sellItems(emptyBox.get())) {
                            this.boxUsed = emptyBox.get();
                        }
                    } else if (finishedBoxExists()) {
                        getGrandExchange().collect(true);
                    } else {
                        RS2Widget box1Widget = getBoxWidget(0);
                        if (box1Widget != null && box1Widget.interact("Abort offer")) {
                            Sleep.sleepUntil(() -> isBoxEmpty(GrandExchange.Box.BOX_1), 5000);
                        }
                    }
                } else if (getConfigs().get(ITEM_ID_CONFIG) == -1) {
                    if (getInventory().getItem(itemName).interact()) {
                        Sleep.sleepUntil(() -> getConfigs().get(ITEM_ID_CONFIG) != -1, 5000);
                    }
                } else if (getGrandExchange().getOfferPrice() != price) {
                    if (getGrandExchange().setOfferPrice(price)) {
                        Sleep.sleepUntil(() -> getGrandExchange().getOfferPrice() == price, 3000);
                    }
                } else if (getGrandExchange().getOfferQuantity() != quantity && getInventory().getAmount(itemName) == quantity) {
                    if (getGrandExchange().setOfferQuantity(quantity)) {
                        Sleep.sleepUntil(() -> getGrandExchange().getOfferQuantity() == quantity, 3000);
                    }
                } else if (getGrandExchange().getOfferQuantity() < getInventory().getAmount(itemName)) {
                    if (getGrandExchange().setOfferQuantity((int)getInventory().getAmount(itemName))) {
                        Sleep.sleepUntil(() -> getGrandExchange().getOfferQuantity() == (int)getInventory().getAmount(itemName), 3000);
                    }
                } else if (getGrandExchange().confirm()) {
                    setFinished();
                }
                return random(100, 150);
            }
        };
        execute(grandExchangeEvent);
        return grandExchangeEvent.getBoxUsed();
    }

    private RS2Widget getBoxWidget(int boxIndex) {
        if (!getGrandExchange().isOpen()) {
            return null;
        }
        if (getGrandExchange().isOfferScreenOpen()) {
            return null;
        }

        List<RS2Widget> boxWidgets = new ArrayList<>();

        for (final RS2Widget widget : getWidgets().getWidgets(465)) {
            Rectangle widgetRect = widget.getBounds();
            if (widgetRect.getWidth() == 115 && widgetRect.getHeight() == 110) {
                boxWidgets.add(widget);
            }
        }

        if (boxWidgets.size() == GrandExchange.Box.values().length) {
            boxWidgets.sort(WidgetComparator.yComparator);
            List<RS2Widget> widgetsInRow = boxIndex < 4 ? boxWidgets.subList(0, 4) : boxWidgets.subList(4, 8);
            widgetsInRow.sort(WidgetComparator.xComparator);
            return widgetsInRow.get(boxIndex < 4 ? boxIndex : boxIndex - 4);
        }

        return null;
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

    private Optional<String> getSearchText() {
        Optional<RS2Widget> searchTextWidget = getWidgets().getAll().stream().filter(widget -> widget != null && widget.getRootId() == 162 && widget.getMessage().startsWith("<col=000000>What would you like to buy?")).findAny();
        return searchTextWidget
                .map(RS2Widget::getMessage)
                .map(message -> message.replaceAll("<col=000000>What would you like to buy\\?</col> ", "")
                        .replaceAll("\\*", ""));
    }

    private void selectItemFromList(final String itemName) {
        Optional<RS2Widget> itemWidget = getWidgets().getAll().stream().filter(widget -> widget != null && widget.getRootId() == 162 && widget.getMessage().equals(itemName)).findAny();
        itemWidget.ifPresent(widget -> {
            final RS2Widget resultsBoxWidget = getWidgets().get(widget.getRootId(), widget.getSecondLevelId());
            final RS2Widget scrollDownWidget = getWidgets().filter(162, w -> w.getWidth() == 16 && w.getHeight() == 16).stream().max(WidgetComparator.yComparator).get();
            execute(new Event() {
                @Override
                public int execute() throws InterruptedException {
                    if (widget.isVisible() && resultsBoxWidget.getBounds().contains(widget.getBounds())) {
                        if (widget.interact()) {
                            Sleep.sleepUntil(() -> getConfigs().get(ITEM_ID_CONFIG) != -1, 5000);
                            setFinished();
                        }
                    } else {
                        scrollDownWidget.interact();
                    }
                    return 0;
                }
            });
        });
    }

    abstract class GrandExchangeEvent extends Event {
        protected GrandExchange.Box boxUsed;

        public GrandExchange.Box getBoxUsed() {
            return boxUsed;
        }
    }
}
