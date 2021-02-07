package activities.quests;

import activities.activity.Activity;
import activities.banking.DepositAllBanking;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.GroundItem;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.Tab;
import util.MakeAllInterface;
import util.Sleep;

public class SheepShearer extends QuestActivity {

    private static final Area FARMER_AREA = new Area(3188, 3275, 3190, 3270);
    private static final Area SHEEP_AREA = new Area(3195, 3272, 3209, 3260);
    private static final Area SPINNER_AREA = new Area(3209, 3215, 3211, 3212).setPlane(1);

    private static final int INVENTORY_SLOTS_REQUIRED = 22;

    private static final String[] ITEMS_NEEDED = {
            "Ball of wool",
            "Wool",
            "Shears"
    };

    private final DialogueCompleter farmerDialogueCompleter = new DialogueCompleter(
            "Fred the Farmer",
            FARMER_AREA,
            "I'm looking for a quest.",
            "Yes, okay. I can do that.",
            "Of course!",
            "I'm something of an expert actually!"
    );

    private final DepositAllBanking depositAllBanking = new DepositAllBanking(ITEMS_NEEDED);
    private MakeAllInterface makeAllInterface;

    public SheepShearer() {
        super(Quest.SHEEP_SHEARER);
    }

    @Override
    public void onStart() {
        depositAllBanking.exchangeContext(getBot());

        makeAllInterface = new MakeAllInterface("Ball of Wool");
        makeAllInterface.exchangeContext(getBot());

        farmerDialogueCompleter.exchangeContext(getBot());
    }

    @Override
    public void runActivity() throws InterruptedException {
        if (getInventory().getEmptySlotCount() < INVENTORY_SLOTS_REQUIRED - getInventory().getAmount(ITEMS_NEEDED)) {
            depositAllBanking.run();
        } else if (getTabs().getOpen() != Tab.INVENTORY) {
            getTabs().open(Tab.INVENTORY);
        } else {
            switch (getProgress()) {
                case 0:
                    farmerDialogueCompleter.run();
                    break;
                case 1:
                    if (hasRequiredItems()) {
                        farmerDialogueCompleter.run();
                    } else {
                        getItemsNeeded();
                    }
                    break;
                case 21:
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
        return getInventory().getAmount("Ball of wool") > 19;
    }

    private void getItemsNeeded() {
        if (!getInventory().contains("Shears")) {
            pickupShears();
        } else if (!getInventory().contains("Ball of wool") && getInventory().getAmount("Wool") < 20) {
            shearSheep();
        } else if (getInventory().getAmount("Ball of wool") < 20) {
            spinWool();
        }
    }

    private void shearSheep() {
        if (SheepShearer.SHEEP_AREA.contains(myPlayer())) {
            NPC npc = getNpcs().closest(n -> !n.hasAction("Talk-to") && n.hasAction("Shear") && SheepShearer.SHEEP_AREA.contains(n));
            if (npc != null && npc.interact("Shear")) {
                Sleep.sleepUntil(() -> myPlayer().getAnimation() == 893, 15000);
                Sleep.sleepUntil(() -> !myPlayer().isAnimating(), 10000);
            }
        } else {
            getWalking().webWalk(SheepShearer.SHEEP_AREA);
        }
    }

    private void spinWool() {
        if (SheepShearer.SPINNER_AREA.contains(myPlayer())) {
            RS2Object object = getObjects().closest(n -> n.hasAction("Spin"));
            if (object != null && object.interact("Spin")) {
                Sleep.sleepUntil(() -> getWidgets().getWidgetContainingText("What would you like to spin?") != null, 8000);

                if (makeAllInterface.isMakeAllScreenOpen() && makeAllInterface.makeAll()) {
                    Sleep.sleepUntil(() -> !getInventory().contains("Wool"), 180000, 1000);
                }
            }
        } else {
            getWalking().webWalk(SheepShearer.SPINNER_AREA);
        }
    }

    private void pickupShears() {
        if (SheepShearer.FARMER_AREA.contains(myPosition())) {
            GroundItem itemToGet = getGroundItems().closest("Shears");
            if (itemToGet != null && itemToGet.interact("Take")) {
                Sleep.sleepUntil(() -> getInventory().contains("Shears"), 8000);
            }
        } else {
            getWalking().webWalk(SheepShearer.FARMER_AREA);
        }
    }

    @Override
    public Activity copy() {
        return new SheepShearer();
    }
}