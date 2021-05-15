package activities.quests;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.event.WebWalkEvent;
import org.osbot.rs07.utility.Condition;
import util.Sleep;
import util.executable.Executable;
import util.executable.ExecutionFailedException;

import java.util.stream.Stream;

public class DialogueCompleter extends Executable {

    private final String npcName;
    private final String[] dialogueOptions;
    private Area area;

    private static final String[] BASE_DIALOGUE_OPTIONS = {
            "Yes",
            "Yes."
    };

    public DialogueCompleter(final String npcName, final Area area, final String... dialogueOptions) {
        this.npcName = npcName;
        this.area = area;
        this.dialogueOptions = buildDialogueOptions(dialogueOptions);
    }

    public DialogueCompleter(final String npcName, final String... dialogueOptions) {
        this.npcName = npcName;
        this.dialogueOptions = buildDialogueOptions(dialogueOptions);
    }

    private String[] buildDialogueOptions(final String[] dialogueOptions) {
        return Stream.concat(Stream.of(BASE_DIALOGUE_OPTIONS), Stream.of(dialogueOptions)).toArray(String[]::new);
    }

    @Override
    public void run() throws InterruptedException {
        NPC npc = getNpcs().closest(npcName);

        if (npc == null || !npc.isVisible()) {
            if (area != null && !area.contains(myPosition())) {
                WebWalkEvent webWalkEvent = new WebWalkEvent(area);
                webWalkEvent.setBreakCondition(new Condition() {
                    @Override
                    public boolean evaluate() {
                        NPC npc = getNpcs().closest(npcName);
                        return npc != null && npc.isVisible() && getMap().canReach(npc);
                    }
                });
                execute(webWalkEvent);
                return;
            } else {
                throw new ExecutionFailedException(String.format("Could not find NPC with name '%s'", npcName));
            }
        }

        if (!getMap().canReach(npc)) {
           getDoorHandler().handleNextObstacle(npc);
        } else if (!getDialogues().inDialogue() || !myPlayer().isInteracting(npc)) {
            if (npc.interact("Talk-to")) {
                Sleep.sleepUntil(() -> getDialogues().inDialogue() && myPlayer().isInteracting(npc), 5000);
            }
        } else {
            getDialogues().completeDialogue(dialogueOptions);
        }
    }
}
