package org.aio.activities.quests;

import org.aio.util.Executable;
import org.aio.util.Sleep;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.NPC;

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

        if (npc == null) {
            if (area != null && !area.contains(myPosition())) {
                getWalking().webWalk(area);
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
