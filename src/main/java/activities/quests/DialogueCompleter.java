package activities.quests;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.event.WebWalkEvent;
import org.osbot.rs07.utility.Condition;
import util.Executable;
import util.Sleep;

public class DialogueCompleter extends Executable {

    private final String npcName;
    private final String[] dialogueOptions;
    private Area area;

    public DialogueCompleter(final String npcName, final Area area, final String... dialogueOptions) {
        this.npcName = npcName;
        this.area = area;
        this.dialogueOptions = dialogueOptions;
    }

    public DialogueCompleter(final String npcName, final String... dialogueOptions) {
        this.npcName = npcName;
        this.dialogueOptions = dialogueOptions;
    }

    @Override
    public void run() throws InterruptedException {
        NPC npc = getNpcs().closest(npcName);

        if (npc == null || !npc.isOnScreen() || !getMap().canReach(npc)) {
            if (area != null && !area.contains(myPosition())) {
                WebWalkEvent webWalkEvent = new WebWalkEvent(area);
                webWalkEvent.setBreakCondition(new Condition() {
                    @Override
                    public boolean evaluate() {
                        NPC npc = getNpcs().closest(npcName);
                        return npc != null && npc.isOnScreen() && getMap().canReach(npc);
                    }
                });
                execute(webWalkEvent);
                return;
            } else {
                log(String.format("Could not find NPC with name '%s'", npcName));
                setFailed();
                return;
            }
        }

        if (!getDialogues().inDialogue() || !myPlayer().isInteracting(npc)) {
            if (npc.interact("Talk-to")) {
                Sleep.sleepUntil(() -> getDialogues().inDialogue() && myPlayer().isInteracting(npc), 5000);
            }
        } else {
            getDialogues().completeDialogue(dialogueOptions);
        }
    }
}
