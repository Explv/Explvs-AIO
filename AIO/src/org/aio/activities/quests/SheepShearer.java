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

    private static final Area FARMER = new Area(3188, 3275, 3190, 3270);
    private static final Area SHEEP = new Area(3195, 3272, 3209, 3260);
    private static final Area SPINNER = new Area(3209, 3215, 3211, 3212).setPlane(1);

    private static final String[] FARMER_OPTIONS = {
            "I'm looking for a quest.",
            "Yes okay. I can do that.",
            "Of course!",
            "I'm something of an expert actually!"
    };

    private static final int INVENTORY_SLOTS_REQUIRED = 22;

    private static final String[] ITEMS_NEEDED = {
            "Ball of wool",
            "Wool",
            "Shears"
    };

    private final DepositAllBanking depositAllBanking = new DepositAllBanking(ITEMS_NEEDED);

    public SheepShearer() {
        super(Quest.SHHEP_SHEARER);
    }

    @Override
    public void onStart() {
        depositAllBanking.exchangeContext(getBot());

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
                    talkToFarmer();
                    break;
                case 1:
                    if (hasRequiredItems()) {
                        talkToFarmer();
                    } else {
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
            getGroundItem(FARMER, "Shears");
        } else if (!getInventory().contains("Ball of wool") && getInventory().getAmount("Wool") < 20) {
            getItemFromNPC(SHEEP, "Wool", "Shear");
        } else if (getInventory().getAmount("Ball of wool") < 20) {
            useObject(SPINNER, "Spin", "Ball of Wool");
        }
    }

    private void getItemFromNPC(Area place, String itemName, String interaction) throws InterruptedException {
        if (place.contains(myPlayer())) {
            NPC npc = getNpcs().closest(n -> !n.hasAction("Talk-to") && n.hasAction(interaction) && place.contains(n));
            if (npc != null && npc.interact(interaction)) {
                Sleep.sleepUntil(() -> myPlayer().getAnimation() == 893, 15000);
                Sleep.sleepUntil(() -> !myPlayer().isAnimating(), 10000);
            }
        } else {
            getWalking().webWalk(place);
        }
    }

    private void useObject(Area place, String objectAction, String menuOption) throws InterruptedException {
        if (place.contains(myPlayer())) {
            RS2Object object = getObjects().closest(n -> n.hasAction(objectAction));
            if (object != null && object.interact(objectAction)) {
                Sleep.sleepUntil(() -> getWidgets().getWidgetContainingText("What would you like to spin?") != null, 8000);

                if (makeAllInterface.isMakeAllScreenOpen()) {
                    if (makeAllInterface.makeAll()) {
                        Sleep.sleepUntil(() -> !getInventory().contains("Wool"), 180000, 1000);
                    }
                }
            }
        } else {
            getWalking().webWalk(place);
        }
    }

    private void talkToFarmer() throws InterruptedException {
        NPC farmer = getNpcs().closest("Fred the Farmer");

        if (!FARMER.contains(myPosition())) {
            getWalking().webWalk(FARMER);
        } else if (!getDialogues().inDialogue() || !myPlayer().isInteracting(farmer)) {
            if (farmer.interact("Talk-to")) {
                Sleep.sleepUntil(() -> getDialogues().inDialogue() && myPlayer().isInteracting(farmer), 5000);
            }
        } else {
            getDialogues().completeDialogue(FARMER_OPTIONS);
        }
    }

    private void getGroundItem(Area place, String itemName) throws InterruptedException {
        if (place.contains(myPosition())) {
            GroundItem itemToGet = getGroundItems().closest(itemName);
            if (itemToGet != null && itemToGet.interact("Take")) {
                Sleep.sleepUntil(() -> getInventory().contains(itemName), 8000);
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