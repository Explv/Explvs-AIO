package activities.quests;

import activities.activity.Activity;
import activities.banking.Bank;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.ui.Tab;
import util.Sleep;

public class RuneMysteries extends QuestActivity {
    private final DialogueCompleter dukeHoracioDialogueCompleter = new DialogueCompleter(
            "Duke Horacio",
            new Area(new Position(3208, 3218, 1), new Position(3213, 3225, 1)),
            "Duke Horacio", "Have you any quests for me?", "Yes"
    );

    private final DialogueCompleter sedridorDialogueCompleter = new DialogueCompleter(
            "Sedridor",
            new Area(3105, 9570, 3103, 9572),
            "I'm looking for the head wizard.", "Ok, here you are.", "Yes, certainly."
    );

    private final DialogueCompleter auburyDialogueCompleter = new DialogueCompleter(
            "Aubury",
            new Area(3251, 3400, 3254, 3402),
            "I have been sent here with a package for you."
    );

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
                getWalking().webWalk(Bank.AREAS);
            }
        } else {

            if (getTabs().getOpen() != Tab.INVENTORY) {
                getTabs().open(Tab.INVENTORY);
            }

            switch (getProgress()) {
                case 0:
                    execute(dukeHoracioDialogueCompleter);
                    break;
                case 1:
                    if (!getInventory().contains("Air talisman")) {
                        execute(dukeHoracioDialogueCompleter);
                    } else {
                        execute(sedridorDialogueCompleter);
                    }
                    break;
                case 2:
                    execute(sedridorDialogueCompleter);
                    break;
                case 3:
                    if (!getInventory().contains("Research package")) {
                        execute(sedridorDialogueCompleter);
                    } else {
                       execute(auburyDialogueCompleter);
                    }
                    break;
                case 4:
                    execute(auburyDialogueCompleter);
                    break;
                case 5:
                    execute(sedridorDialogueCompleter);
                    break;

            }
        }
    }

    @Override
    public Activity copy() {
        return new RuneMysteries();
    }
}