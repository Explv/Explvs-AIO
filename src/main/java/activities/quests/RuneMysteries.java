package activities.quests;

import activities.activity.Activity;
import activities.banking.Bank;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.ui.Tab;
import util.Sleep;

public class RuneMysteries extends QuestActivity {

    private final Area dukeHoraciosRoom = new Area(new Position(3208, 3218, 1), new Position(3213, 3225, 1));
    private final Area wizardsTower = new Area(
            new int[][]{
                    {3103, 3159},
                    {3103, 3162},
                    {3102, 3163},
                    {3102, 3165},
                    {3103, 3166},
                    {3105, 3166},
                    {3106, 3165},
                    {3106, 3164},
                    {3109, 3161},
                    {3107, 3159}
            }
    );
    private final Area auburysShop = new Area(3251, 3400, 3254, 3402);

    public RuneMysteries() {
        super(Quest.RUNE_MYSTERIES);
    }

    @Override
    public void runActivity() throws InterruptedException {
        if (!getInventory().contains("Air talisman", "Research package") && getInventory().isFull()) {
            if (Bank.inAnyBank(myPosition())) {
                if (!getBank().isOpen()) {
                    if (getBank().open()) {
                        Sleep.sleepUntil(() -> getBank().isOpen(), 5000);
                    }
                } else getBank().depositAllExcept("Air talisman", "Research package");
            } else {
                getWalking().webWalk(Bank.getAreas());
            }
        } else {

            if (getTabs().getOpen() != Tab.INVENTORY) {
                getTabs().open(Tab.INVENTORY);
            }

            switch (getProgress()) {
                case 0:
                    talkToDukeHoracio();
                    break;
                case 1:
                    if (!getInventory().contains("Air talisman")) {
                        talkToDukeHoracio();
                    } else {
                        talkToSedridor();
                    }
                    break;
                case 2:
                    talkToSedridor();
                    break;
                case 3:
                    if (!getInventory().contains("Research package")) {
                        talkToSedridor();
                    } else {
                        talkToAubury();
                    }
                    break;
                case 4:
                    talkToAubury();
                    break;
                case 5:
                    talkToSedridor();
                    break;

            }
        }
    }

    private void talkToDukeHoracio() throws InterruptedException {

        NPC dukeHoracio = getNpcs().closest("Duke Horacio");

        if (dukeHoracio == null) {
            getWalking().webWalk(dukeHoraciosRoom);
        } else {
            completeDialog("Duke Horacio", "Have you any quests for me?", "Sure, no problem.");
        }
    }

    private void talkToSedridor() throws InterruptedException {

        NPC sedridor = getNpcs().closest("Sedridor");
        if (sedridor != null) {
            if (getMap().canReach(sedridor)) {
                completeDialog("Sedridor", "I'm looking for the head wizard.", "Ok, here you are.", "Yes, certainly.");
            } else {
                getWalking().webWalk(new Area(3105, 9570, 3103, 9572));
            }
        } else if (wizardsTower.contains(myPosition())) {
            if (getObjects().closest("Ladder").interact("Climb-down")) {
                Sleep.sleepUntil(() -> !wizardsTower.contains(myPosition()), 5000);
            }
        } else {
            getWalking().webWalk(wizardsTower);
        }
    }

    private void talkToAubury() throws InterruptedException {

        NPC aubury = getNpcs().closest("Aubury");
        if (aubury != null) {
            if (getMap().canReach(aubury)) {
                completeDialog("Aubury", "I have been sent here with a package for you.");
            } else {
                if (getDoorHandler().handleNextObstacle(aubury)) {
                    Sleep.sleepUntil(() -> getMap().canReach(aubury), 5000);
                }
            }
        } else {
            getWalking().webWalk(auburysShop);
        }
    }

    private void completeDialog(String npcName, String... options) throws InterruptedException {
        if (!getDialogues().inDialogue()) {
            talkTo(npcName);
        } else if (getDialogues().isPendingContinuation()) {
            getDialogues().clickContinue();
        } else if (options.length > 0 && getDialogues().isPendingOption()) {
            getDialogues().completeDialogue(options);
        }
    }

    private void talkTo(String npcName) {
        NPC npc = getNpcs().closest(npcName);
        if (npc != null) {
            npc.interact("Talk-to");
            Sleep.sleepUntil(() -> getDialogues().inDialogue(), 5000);
        }
    }

    @Override
    public Activity copy() {
        return new RuneMysteries();
    }
}