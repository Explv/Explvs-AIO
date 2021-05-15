package util.custom_method_provider;

import org.osbot.rs07.api.NPCS;
import org.osbot.rs07.api.model.NPC;

public class ExtendedNPCS extends NPCS {

    public boolean isTalkingToNPC(final String npcName) {
        NPC npc = getNpcs().closest(npcName);

        if (npc == null) {
            return false;
        }

        return getDialogues().inDialogue() && myPlayer().isInteracting(npc);
    }
}
