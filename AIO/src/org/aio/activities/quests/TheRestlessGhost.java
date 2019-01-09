package org.aio.activities.quests;

import org.aio.activities.activity.Activity;
import org.aio.activities.banking.DepositAllBanking;
import org.aio.util.Sleep;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.GroundItem;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.EquipmentSlot;
import org.osbot.rs07.api.ui.Message;
import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.listener.MessageListener;

import java.util.stream.Stream;

public class TheRestlessGhost extends QuestActivity {

    private static final Area CHURCH = new Area(3240, 3204, 3247, 3215);
    private static final Area URHNEY = new Area(3144, 3173, 3151, 3177);
    private static final Area COFFIN = new Area(3247, 3195, 3252, 3190);
    private static final Area WIZARD = new Area(3116, 9564, 3121, 9569);

    private static final String[] AERECT_OPTIONS = {
            "I'm looking for a quest!",
            "Ok, let me help then."
    };

    private static final String[] URHNEY_OPTIONS = {
            "Father Aereck sent me to talk to you.",
            "He's got a ghost haunting his graveyard."
    };

    private static final int INVENTORY_SLOTS_REQUIRED = 4;

    private static final String[] ITEMS_NEEDED = {
            "Ghost's skull",
            "Ghostspeak amulet",
    };

    private final DepositAllBanking depositAllBanking = new DepositAllBanking(ITEMS_NEEDED);

    public TheRestlessGhost() {
        super(Quest.THE_RESTLESS_GHOST);
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
                    talkToAereck();
                    break;
                case 1:
                    talkToUrhney();
                    break;
                case 2:
                    talkToGhost();
                    break;
                case 3:
                    searchAlter();
                    break;
                case 4:
                    placeSkull();
                    break;
                case 5:
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

    private void placeSkull() throws InterruptedException {
        RS2Object coffin = getObjects().closest("Coffin");

        if (!COFFIN.contains(myPosition())) {
            getWalking().webWalk(COFFIN);
        } else if(coffin != null && coffin.hasAction("Close") && getInventory().contains("Ghost's skull")) {
            useSkull();
        } else if (coffin != null && coffin.hasAction("Open")) {
            openCoffin();
        }
    }

    private void useSkull() throws InterruptedException {
        if(getInventory().isItemSelected()) {
            if(getObjects().closest("Coffin").interact("Use")){
                Sleep.sleepUntil(() -> !getInventory().isItemSelected() && !myPlayer().isAnimating(), 8000, 1500);
            }
        } else {
            if(getInventory().interact("Use", "Ghost's skull")) {
                Sleep.sleepUntil(() -> getInventory().isItemSelected(), 5000);
            }
        }
    }

    private void talkToGhost() throws InterruptedException {
        NPC ghost = getNpcs().closest("Restless ghost");

        if (!COFFIN.contains(myPosition())) {
            getWalking().webWalk(COFFIN);
        } else if (ghost == null) {
            openCoffin();
        } else if (!getDialogues().inDialogue() && canTalkToGhost() || !myPlayer().isInteracting(ghost)) {
            if (ghost.interact("Talk-to")) {
                Sleep.sleepUntil(() -> getDialogues().inDialogue() && myPlayer().isInteracting(ghost), 5000);
            }
        } else {
            getDialogues().completeDialogue(AERECT_OPTIONS);
        }
    }

    private void openCoffin() throws InterruptedException {
        RS2Object coffin = getObjects().closest("Coffin");

        if (coffin != null && coffin.interact("Open")) {
            Sleep.sleepUntil(() -> coffin.hasAction("Close"), 8000);
        } else if (coffin != null && coffin.interact("Search")) {
            Sleep.sleepUntil(() -> getNpcs().closest("Restless ghost") != null, 10000, 1500);
        }
    }

    private void searchAlter() throws InterruptedException {
        RS2Object alter = getObjects().closest("Altar");

        if (!WIZARD.contains(myPosition())) {
            getWalking().webWalk(WIZARD);
        } else if(alter != null && alter.interact("Search")) {
            Sleep.sleepUntil(() -> getInventory().contains("Ghost's skull"), 5000);
        }
    }

    private boolean canTalkToGhost() {
        if(getInventory().contains("Ghostspeak amulet")) {
            if(getInventory().interact("Wear", "Ghostspeak amulet")) {
                Sleep.sleepUntil(() -> !getInventory().contains("Ghostspeak amulet"), 5000);
            }
        }
        return getEquipment().getItemInSlot(EquipmentSlot.AMULET.slot).equals("Ghostspeak amulet");
    }

    private void talkToAereck() throws InterruptedException {
        NPC aereck = getNpcs().closest("Father Aereck");

        if (!CHURCH.contains(myPosition())) {
            getWalking().webWalk(CHURCH);
        } else if (!getDialogues().inDialogue() || !myPlayer().isInteracting(aereck)) {
            if (aereck.interact("Talk-to")) {
                Sleep.sleepUntil(() -> getDialogues().inDialogue() && myPlayer().isInteracting(aereck), 5000);
            }
        } else {
            getDialogues().completeDialogue(AERECT_OPTIONS);
        }
    }

    private void talkToUrhney() throws InterruptedException {
        NPC urhney = getNpcs().closest("Father Urhney");

        if (!URHNEY.contains(myPosition())) {
            getWalking().webWalk(URHNEY);
        } else if (!getDialogues().inDialogue() || !myPlayer().isInteracting(urhney)) {
            if (urhney.interact("Talk-to")) {
                Sleep.sleepUntil(() -> getDialogues().inDialogue() && myPlayer().isInteracting(urhney), 5000);
            }
        } else {
            getDialogues().completeDialogue(URHNEY_OPTIONS);
        }
    }

    @Override
    public Activity copy() {
        return new TheRestlessGhost();
    }
}