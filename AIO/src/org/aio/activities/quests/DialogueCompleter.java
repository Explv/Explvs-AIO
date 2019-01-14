package org.aio.activities.quests;

import org.aio.util.Executable;
import org.aio.util.Sleep;
import org.osbot.rs07.api.Walking;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.NPC;

import java.util.List;

public class DialogueCompleter extends Executable {

    private final String npcName;
    private final String[] dialogueOptions;
    private Area area;
    private List<Position> path;

    public DialogueCompleter(final String npcName, final String... dialogueOptions) {
        this.npcName = npcName;
        this.dialogueOptions = dialogueOptions;
    }

    public DialogueCompleter(final String npcName, final Area area, final String... dialogueOptions) {
        this(npcName, dialogueOptions);
        this.area = area;
    }

    public DialogueCompleter(final String npcName, final Area area, final List<Position> path, final String... dialogueOptions) {
        this(npcName, area, dialogueOptions);
        this.path = path;
    }

    @Override
    public void run() throws InterruptedException {
        run(WalkType.PATH);
    }

    public void run(WalkType usePreferredWalkType) throws InterruptedException{
        NPC npc = getNpcs().closest(npcName);

        if (npc == null) {
            if (area != null && !area.contains(myPosition())) {
                if(path != null && usePreferredWalkType == WalkType.PATH){
                    getWalking().walkPath(path);
                }else{
                    getWalking().webWalk(area);
                }
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

    enum WalkType {
        PATH,
        WEBWALK
    }
}
