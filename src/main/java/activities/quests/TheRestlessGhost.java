package activities.quests;

import activities.activity.Activity;
import activities.banking.DepositAllBanking;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.EquipmentSlot;
import org.osbot.rs07.api.ui.Tab;
import util.Sleep;
import util.executable.ExecutionFailedException;

public class TheRestlessGhost extends QuestActivity {

    private static final Area CHURCH = new Area(3241, 3210, 3246, 3207);;
    private static final Area URHNEY = new Area(3144, 3173, 3151, 3177);
    private static final Area COFFIN = new Area(3247, 3195, 3252, 3190);
    private static final Area WIZARD = new Area(3116, 9564, 3121, 9569);

    private final DialogueCompleter aereckDialogueCompleter = new DialogueCompleter(
            "Father Aereck",
            CHURCH,
            "I'm looking for a quest!",
            "Ok, let me help then."
    );

    private final DialogueCompleter urhneyDialogueCompleter = new DialogueCompleter(
            "Father Urhney",
            URHNEY,
            "Father Aereck sent me to talk to you.",
            "He's got a ghost haunting his graveyard."
    );

    private final DialogueCompleter ghostDialogueCompleter = new DialogueCompleter(
            "Restless ghost",
            "Yep, now tell me what the problem is."
    );

    private static final int INVENTORY_SLOTS_REQUIRED = 4;
    private static final String[] ITEMS_NEEDED = {
            "Ghost's skull",
            "Ghostspeak amulet",
    };

    private static boolean shouldExit = true;
    private final DepositAllBanking depositAllBanking = new DepositAllBanking(ITEMS_NEEDED);

    public TheRestlessGhost() {
        super(Quest.THE_RESTLESS_GHOST);
    }

    @Override
    public boolean canExit() {
        return shouldExit;
    }

    @Override
    public void runActivity() throws InterruptedException {
        if (!getInventory().contains(ITEMS_NEEDED) && getInventory().getEmptySlotCount() < INVENTORY_SLOTS_REQUIRED) {
            execute(depositAllBanking);
        } else if (getTabs().getOpen() != Tab.INVENTORY) {
            getTabs().open(Tab.INVENTORY);
        } else {
            switch (getProgress()) {
                case 0:
                    execute(aereckDialogueCompleter);
                    break;
                case 1:
                    execute(urhneyDialogueCompleter);
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
                    // Make sure we are not in the cut scene
                    if (!Tab.INVENTORY.isDisabled(bot)) {
                        Sleep.sleepUntil(() -> getWidgets().getWidgetContainingText("You have completed the Restless Ghost Quest!").isVisible(), 15000, 1000);

                        log("Quest is complete");
                        shouldExit = true;
                        isComplete = true;
                    }
                    break;
                default:
                    throw new ExecutionFailedException("Unknown progress config value: " + getProgress());
            }
        }
    }

    private void placeSkull() throws InterruptedException {
        RS2Object coffin = getObjects().closest("Coffin");

        if (coffin == null || !getMap().canReach(coffin)) {
            getWalking().webWalk(COFFIN);
        } else if (coffin.hasAction("Close") && getInventory().contains("Ghost's skull")) {
            useSkull();
        } else if (coffin.hasAction("Open")) {
            openCoffin();
        }
    }

    private void useSkull() throws InterruptedException {
        if ("Ghost's skull".equals(getInventory().getSelectedItemName())) {
            shouldExit = false;
            if (getObjects().closest("Coffin").interact("Use")) {
                Sleep.sleepUntil(() -> Tab.INVENTORY.isDisabled(bot), 15000, 1500);
            }
        } else {
            if (getInventory().use("Ghost's skull")) {
                Sleep.sleepUntil(() -> "Ghost's skull".equals(getInventory().getSelectedItemName()), 5000);
            }
        }
    }

    private void talkToGhost() throws InterruptedException {
        RS2Object coffin = getObjects().closest("Coffin");

        if (coffin == null || !getMap().canReach(coffin)) {
            getWalking().webWalk(COFFIN);
            return;
        }

        NPC ghost = getNpcs().closest("Restless ghost");

        if (ghost == null) {
            openCoffin();
        } else if (!getEquipment().isWearingItem(EquipmentSlot.AMULET, "Ghostspeak amulet")) {
            if (getInventory().interact("Wear", "Ghostspeak amulet")) {
                Sleep.sleepUntil(() -> getEquipment().isWearingItem(EquipmentSlot.AMULET, "Ghostspeak amulet"), 2000);
            }
        } else {
            execute(ghostDialogueCompleter);
        }
    }

    private void openCoffin() {
        RS2Object coffin = getObjects().closest("Coffin");

        if (coffin != null && (coffin.interact("Search", "Open"))) {
            Sleep.sleepUntil(() -> getNpcs().closest("Restless ghost") != null, 10000, 1500);
        }
    }

    private void searchAlter() {
        RS2Object alter = getObjects().closest("Altar");

        if (!WIZARD.contains(myPosition())) {
            getWalking().webWalk(WIZARD);
        } else if (alter != null && alter.interact("Search")) {
            Sleep.sleepUntil(() -> getInventory().contains("Ghost's skull"), 5000);
        }
    }

    @Override
    public Activity copy() {
        return new TheRestlessGhost();
    }
}