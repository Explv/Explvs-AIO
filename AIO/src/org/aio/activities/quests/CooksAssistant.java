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

import java.util.stream.Stream;

public class CooksAssistant extends QuestActivity {

    private final Area cookRoom = new Area(3205, 3215, 3212, 3212);
    private final Area basement = new Area(3214, 9625, 3216, 9623);
    private final Area cow = new Area(3253, 3270, 3255, 3275);
    private final Area chicken = new Area(3235, 3295, 3226, 3300);
    private final Area wheat = new Area(3162, 3295, 3157, 3298);
    private final Area upper = new Area(new Position(3168, 3305, 2), new Position(3165, 3308, 2));
    private final Area bin = new Area(3165, 3305, 3168, 3308);

    private boolean operated = false;
    private boolean put = false;

    private final String[] cookOptions = {
            "What's wrong?",
            "I'm always happy to help a cook in distress.",
            "Actually, I know where to find this stuff."
    };

    private final String[] itemsNeeded = {
            "Pot of flour",
            "Bucket of milk",
            "Egg"
    };

    private final DepositAllBanking depositAllBanking = new DepositAllBanking(itemsNeeded);

    public CooksAssistant() {
        super(Quest.COOKS_ASSISTANT);
    }

    @Override
    public void onStart() {
        depositAllBanking.exchangeContext(getBot());
    }

    @Override
    public void runActivity() throws InterruptedException {
        if (!getInventory().contains(itemsNeeded) && getInventory().isFull()) {
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
        return Stream.of(itemsNeeded).allMatch(item -> getInventory().contains(item));
    }

    private void getItemsNeeded() throws InterruptedException {
        if (!getInventory().contains("Pot", "Pot of flour", "Bucket of milk")) {
            getGroundItem(cookRoom, "Pot");
        } else if (!getInventory().contains("Bucket", "Bucket of milk")) {
            getGroundItem(basement, "Bucket");
        } else if (getInventory().contains("Bucket") && !getInventory().contains("Bucket of milk")) {
            getItemFromObject(cow, "Bucket of milk", "Dairy cow", "Milk");
        } else if (!getInventory().contains("Egg")) {
            getGroundItem(chicken, "Egg");
        } else if (!getInventory().contains("Pot of flour")) {

            // Get grain
            if (!put && !getInventory().contains("Grain")) {
                getItemFromObject(wheat, "Grain", "Wheat", "Pick");
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
                getItemFromObject(bin, "Pot of flour", "Flour bin", "Empty");
            }
        }
    }

    private void fillHopper() {
        if (!upper.contains(myPosition())) {
            getWalking().webWalk(upper);
        } else if (getInventory().getSelectedItemName() != null && getInventory().getSelectedItemName().equals("Grain")) {

            RS2Object hopper = getObjects().closest(n -> n.getName().equals("Hopper"));

            if (hopper != null && hopper.interact("Use")) {
                Sleep.sleepUntil(() -> {
                    return getChatbox().contains(MessageType.GAME, "There is already grain in the hopper.") ||
                            (!myPlayer().isAnimating() && !getInventory().contains("Grain"));
                }, 15000);
            }
            if (getChatbox().contains(MessageType.GAME, "There is already grain in the hopper.") || !getInventory().contains("Grain")) {
                put = true;
            }
        } else if (getInventory().interact("Use", "Grain")) {
            Sleep.sleepUntil(() -> getInventory().getSelectedItemName() != null && getInventory().getSelectedItemName().equals("Grain"), 5000);
        }
    }

    private void operateHopper() {
        if (!upper.contains(myPosition())) {
            getWalking().webWalk(upper);
        } else {
            RS2Object controls = getObjects().closest("Hopper controls");

            if (controls == null) {
                log("Could not find object 'Hopper controls'");
                setFailed();
                return;
            }

            if (controls.interact("Operate")) {
                Sleep.sleepUntil(
                        () -> getChatbox().contains(
                                MessageType.GAME,
                                "You operate the hopper. The grain slides down the chute."
                        ),
                        10000);
            }

            if (getChatbox().contains(MessageType.GAME, "You operate the hopper. The grain slides down the chute.")) {
                operated = true;
            }
        }
    }

    private void getItemFromObject(Area place, String itemName, String objectName, String interaction) throws InterruptedException {
        if (place.contains(myPlayer())) {
            RS2Object object = getObjects().closest(n -> n.getName().equals(objectName));
            if (object != null && object.interact(interaction)) {
                Sleep.sleepUntil(() -> getInventory().contains(itemName) && !myPlayer().isAnimating(), 15000);
            }
        } else {
            getWalking().webWalk(place);
        }
    }

    private void getGroundItem(Area place, String itemName) throws InterruptedException {
        if (place.contains(myPosition())) {
            GroundItem itemToGet = groundItems.closest(itemName);
            if (itemToGet != null && itemToGet.interact("Take")) {
                Sleep.sleepUntil(() -> getInventory().contains(itemName), 8000);
            }
        } else {
            getWalking().webWalk(place);
        }
    }

    private void talkToCook() throws InterruptedException {
        NPC cook = getNpcs().closest("Cook");

        if (!cookRoom.contains(myPosition())) {
            getWalking().webWalk(cookRoom);
        } else if (!getDialogues().inDialogue() || !myPlayer().isInteracting(cook)) {
            if (cook.interact("Talk-to")) {
                Sleep.sleepUntil(() -> getDialogues().inDialogue() && myPlayer().isInteracting(cook), 5000);
            }
        } else {
            getDialogues().completeDialogue(cookOptions);
        }
    }

    @Override
    public Activity copy() {
        return new CooksAssistant();
    }
}