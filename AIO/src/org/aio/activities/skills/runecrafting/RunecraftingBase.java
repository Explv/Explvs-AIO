package org.aio.activities.skills.runecrafting;

import org.aio.activities.activity.Activity;
import org.aio.activities.activity.ActivityType;
import org.aio.activities.skills.mining.MiningActivity;
import org.aio.util.Sleep;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.event.WebWalkEvent;
import org.osbot.rs07.utility.Condition;

abstract class RunecraftingBase extends Activity {

    final Altar altar;

    RunecraftingBase(final Altar altar) {
        super(ActivityType.RUNECRAFTING);
        this.altar = altar;
    }

    void walkToAltar() {
        WebWalkEvent webWalkEvent = new WebWalkEvent(altar.area);
        webWalkEvent.setBreakCondition(new Condition() {
            @Override
            public boolean evaluate() {
                return altar.area.contains(myPosition()) && getObjects().closest("Mysterious ruins") != null;
            }
        });
        execute(webWalkEvent);
    }

    RS2Object getAltar() {
        return getObjects().closest(obj -> obj.getName().equals("Altar") && obj.hasAction("Craft-rune"));
    }

    void enterAltar(){
        if (getInventory().contains(altar.talisman)) {
            enterAltarUsingTalisman();
        } else {
            enterAltarUsingTiara();
        }
    }

    void enterAltarUsingTalisman() {
        if (!altar.talisman.equals(getInventory().getSelectedItemName())) {
            if (getInventory().isItemSelected()) {
                getInventory().deselectItem();
            } else {
                getInventory().interact("Use", altar.talisman);
            }
        } else if (getObjects().closest("Mysterious ruins").interact("Use")) {
            Sleep.sleepUntil(() -> getAltar() != null, 5000);
        }
    }

    void enterAltarUsingTiara() {
        if (getObjects().closest("Mysterious ruins").interact("Enter")) {
            Sleep.sleepUntil(() -> getAltar() != null, 10000);
        }
    }

    void leaveAltar() {
        if (getObjects().closest("Portal").interact("Use")) {
            Sleep.sleepUntil(() -> getObjects().closest("Altar") == null, 10_000);
        }
    }

    @Override
    public boolean canExit() {
        return getAltar() == null;
    }
}
