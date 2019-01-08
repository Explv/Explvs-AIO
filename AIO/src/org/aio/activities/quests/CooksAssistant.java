package org.aio.activities.quests;

import org.aio.activities.activity.Activity;
import org.aio.activities.banking.DepositAllBanking;
import org.aio.util.Sleep;
import org.osbot.rs07.api.Chatbox.MessageType;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.GroundItem;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.Tab;

import java.util.function.BooleanSupplier;
import java.util.stream.Stream;

public class CooksAssistant extends QuestActivity {

    private static final Area COOK_ROOM = new Area(3205, 3215, 3212, 3212);
    private static final Area BASEMENT = new Area(3214, 9625, 3216, 9623);
    private static final Area COW = new Area(3253, 3270, 3255, 3275);
    private static final Area CHICKEN = new Area(3235, 3295, 3226, 3300);
    private static final Area WHEAT = new Area(3162, 3295, 3157, 3298);
    private static final Area UPPER = new Area(new Position(3168, 3305, 2), new Position(3165, 3308, 2));
    private static final Area BIN = new Area(3165, 3305, 3168, 3308);

    private static final String[] COOK_OPTIONS = {
            "What's wrong?",
            "I'm always happy to help a cook in distress.",
            "Actually, I know where to find this stuff."
    };

    private static final int INVENTORY_SLOTS_REQUIRED = 7;

    private static final String[] ITEMS_NEEDED = {
            "Pot of flour",
            "Bucket of milk",
            "Egg"
    };

    private boolean operated = false;
    private boolean put = false;

    private final DepositAllBanking depositAllBanking = new DepositAllBanking(ITEMS_NEEDED);

    public CooksAssistant() {
        super(Quest.COOKS_ASSISTANT);
    }

    @Override
    public void onStart() {
        depositAllBanking.exchangeContext(getBot());
    }

    @Override
    public void runActivity() throws InterruptedException {
        if (!getInventory().contains(ITEMS_NEEDED) && getInventory().getEmptySlotCount() < INVENTORY_SLOTS_REQUIRED) {
            depositAllBanking.run();
        } else if (getTabs().getOpen() != Tab.INVENTORY) {
            getTabs().open(Tab.INVENTORY);
        } else {
            switch (getProgress()) {
                case 0:
                    talkToCook();
                    break;
                case 1:
                    if (hasRequiredItems()) {
                        talkToCook();
                    } else {
                        getItemsNeeded();
                    }
                    break;
                case 2:
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
        return Stream.of(ITEMS_NEEDED).allMatch(item -> getInventory().contains(item));
    }

    private void getItemsNeeded() throws InterruptedException {
        if (!getInventory().contains("Pot", "Pot of flour", "Bucket of milk")) {
            getGroundItem(COOK_ROOM, "Pot");
        } else if (!getInventory().contains("Bucket", "Bucket of milk")) {
            getGroundItem(BASEMENT, "Bucket");
        } else if (getInventory().contains("Bucket") && !getInventory().contains("Bucket of milk")) {
            getItemFromObject(COW, "Bucket of milk", "Dairy COW", "Milk");
        } else if (!getInventory().contains("Egg")) {
            getGroundItem(CHICKEN, "Egg");
        } else if (!getInventory().contains("Pot of flour")) {

            // Get grain
            if (!put && !getInventory().contains("Grain")) {
                getItemFromObject(WHEAT, "Grain", "Wheat", "Pick");
            }

            // Put grain
            if (!put && !operated && getInventory().contains("Grain")) {
                fillHopper();
            }

            // Operate machine
            if (!operated && put) {
                operateHopper();
            }

            // Get flour
            if (operated && put) {
                getItemFromObject(BIN, "Pot of flour", "Flour BIN", "Empty");
            }
        }
    }

    private void fillHopper() {
        if (!UPPER.contains(myPosition())) {
            getWalking().webWalk(UPPER);
        } else if (!"Grain".equals(getInventory().getSelectedItemName())) {
            getInventory().interact("Use", "Grain");
        } else {
            RS2Object hopper = getObjects().closest("Hopper");

            if (hopper == null) {
                log("Could not find object 'Hopper'");
                setFailed();
                return;
            }

            if (hopper.interact("Use")) {
                BooleanSupplier chatboxContainsSuccessMessage = () -> getChatbox().contains(
                        MessageType.GAME,
                        "There is already grain in the hopper.",
                        "You put grain into the hopper"
                );

                Sleep.sleepUntil(chatboxContainsSuccessMessage, 15000);

                if (chatboxContainsSuccessMessage.getAsBoolean()) {
                    put = true;
                }
            }
        }
    }

    private void operateHopper() {
        if (!UPPER.contains(myPosition())) {
            getWalking().webWalk(UPPER);
        } else {
            RS2Object controls = getObjects().closest("Hopper controls");

            if (controls == null) {
                log("Could not find object 'Hopper controls'");
                setFailed();
                return;
            }

            if (controls.interact("Operate")) {
                BooleanSupplier chatboxContainsSuccessMessage = () -> getChatbox().contains(
                        MessageType.GAME,
                        "You operate the hopper. The grain slides down the chute."
                );

                Sleep.sleepUntil(chatboxContainsSuccessMessage, 10000);

                if (chatboxContainsSuccessMessage.getAsBoolean()) {
                    operated = true;
                }
            }
        }
    }

    private void getItemFromObject(Area place, String itemName, String objectName, String interaction) throws InterruptedException {
        if (place.contains(myPlayer())) {
            RS2Object object = getObjects().closest(objectName);
            if (object != null && object.interact(interaction)) {
                Sleep.sleepUntil(() -> getInventory().contains(itemName) && !myPlayer().isAnimating(), 15000);
            }
        } else {
            getWalking().webWalk(place);
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

    private void talkToCook() throws InterruptedException {
        NPC cook = getNpcs().closest("Cook");

        if (!COOK_ROOM.contains(myPosition())) {
            getWalking().webWalk(COOK_ROOM);
        } else if (!getDialogues().inDialogue() || !myPlayer().isInteracting(cook)) {
            if (cook.interact("Talk-to")) {
                Sleep.sleepUntil(() -> getDialogues().inDialogue() && myPlayer().isInteracting(cook), 5000);
            }
        } else {
            getDialogues().completeDialogue(COOK_OPTIONS);
        }
    }

    @Override
    public Activity copy() {
        return new CooksAssistant();
    }
}