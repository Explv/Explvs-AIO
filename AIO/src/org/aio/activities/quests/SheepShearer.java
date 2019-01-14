package org.aio.activities.quests;

import org.aio.activities.activity.Activity;
import org.aio.activities.banking.DepositAllBanking;
import org.aio.util.MakeAllInterface;
import org.aio.util.Sleep;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.GroundItem;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.Tab;

public class SheepShearer extends QuestActivity {

    private MakeAllInterface makeAllInterface;

    private static final Area FARMER_AREA = new Area(3188, 3275, 3190, 3270);
    private static final Area SHEEP_AREA = new Area(3195, 3272, 3209, 3260);
    private static final Area SPINNER_AREA = new Area(3209, 3215, 3211, 3212).setPlane(1);

    private static final String[] FARMER_OPTIONS = {
    };

    private static final int INVENTORY_SLOTS_REQUIRED = 22;

    private static final String[] ITEMS_NEEDED = {
            "Ball of wool",
            "Wool",
            "Shears"
    };

    private final DialogueCompleter farmerDialogueCompleter = new DialogueCompleter(
            "Fred the Farmer",
            FARMER_AREA,
            "I'm looking for a quest.",
            "Yes okay. I can do that.",
            "Of course!",
            "I'm something of an expert actually!"
    );

    private final DialogueCompleter farmerBackDialogueCompleter = new DialogueCompleter(
            "Fred the Farmer",
            FARMER_AREA,
            "I'm looking for a quest.",
            "Yes okay. I can do that.",
            "Of course!",
            "I'm something of an expert actually!"
    );
    private final ItemCompleter shearsItemCompleter = new ItemCompleter("Shears", FARMER_AREA);

    private final ItemCompleter woolItemCompleter = new ItemCompleter(
            "Sheep",
            "Wool",
            "Shear",
            SHEEP_AREA
    );


    private final DepositAllBanking depositAllBanking = new DepositAllBanking(ITEMS_NEEDED);

    public SheepShearer() {
        super(Quest.SHEEP_SHEARER);
    }

    @Override
    public void onStart() {
        depositAllBanking.exchangeContext(getBot());
        farmerDialogueCompleter.exchangeContext(getBot());
        shearsItemCompleter.exchangeContext(getBot());
        woolItemCompleter.exchangeContext(getBot());
        makeAllInterface = new MakeAllInterface("Ball of Wool");
        makeAllInterface.exchangeContext(getBot());
    }

    @Override
    public void runActivity() throws InterruptedException {
        if (getInventory().getEmptySlotCount() < INVENTORY_SLOTS_REQUIRED - getInventory().getAmount(ITEMS_NEEDED)) {
            depositAllBanking.run();
        } else if (getTabs().getOpen() != Tab.INVENTORY) {
            getTabs().open(Tab.INVENTORY);
        } else {
            switch (getProgress()) {
                case 0:
                    farmerDialogueCompleter.run();
                    break;
                case 1:
                    if (hasRequiredItems()) {
                        farmerDialogueCompleter.run();
                    } else {
                        log("fuck");
                        getItemsNeeded();
                    }
                    break;
                case 21:
                    log("Quest is complete");
                    isComplete = true;
                    break;
                default:
                    log("Unknown progress config value: " + getProgress());
                    setFailed();
                    break;
            }
        }
    }

    private boolean hasRequiredItems() {
        return getInventory().getAmount("Ball of wool") > 19;
    }

    private void getItemsNeeded() throws InterruptedException {
        if (!getInventory().contains("Shears")) {
            shearsItemCompleter.run();
        } else if (!getInventory().contains("Ball of wool") && getInventory().getAmount("Wool") < 20) {
            woolItemCompleter.run();
        } else if (getInventory().getAmount("Ball of wool") < 20) {
            useObject(SPINNER_AREA, "Spin");
        }
    }

    private void useObject(Area place, String objectAction) throws InterruptedException {
        if (place.contains(myPlayer())) {
            RS2Object object = getObjects().closest(n -> n.hasAction(objectAction));
            if (object != null && object.interact(objectAction)) {
                Sleep.sleepUntil(() -> getWidgets().getWidgetContainingText("What would you like to spin?") != null, 8000);

                if (makeAllInterface.isMakeAllScreenOpen() && makeAllInterface.makeAll()) {
                    Sleep.sleepUntil(() -> !getInventory().contains("Wool"), 180000, 1000);
                }
            }
        } else {
            getWalking().webWalk(place);
        }
    }

    @Override
    public Activity copy() {
        return new SheepShearer();
    }
}