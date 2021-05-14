package activities.skills.mining;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.event.WalkingEvent;
import util.executable.Executable;
import util.ResourceMode;
import util.Sleep;

import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

public class RuneEssMiningActivity extends MiningActivity {

    private static final Area AUBURY_HOUSE = new Area(
            new int[][]{
                    {3250, 3403},
                    {3250, 3401},
                    {3252, 3399},
                    {3254, 3399},
                    {3256, 3401},
                    {3256, 3403},
                    {3254, 3405},
                    {3252, 3405},
                    {3252, 3403}
            }
    );

    public RuneEssMiningActivity(final ResourceMode resourceMode) {
        super(null, Rock.RUNE_ESSENCE, resourceMode);
        miningNode = new MiningNode();
    }

    @Override
    public void runActivity() throws InterruptedException {
        if ((shouldBank() || pickaxeBanking.toolUpgradeAvailable()) && inEssenceMine()) {
            leaveEssenceMine();
        } else {
            super.runActivity();
        }
    }

    @Override
    protected boolean inventoryContainsNonMiningItem() {
        return getInventory().contains(item -> {
            if (pickaxeBanking.getCurrentTool() != null) {
                if (item.getName().equals(pickaxeBanking.getCurrentTool().getName())) {
                    return false;
                }
            }
            return !item.getName().endsWith("essence");
        });
    }

    private boolean inEssenceMine() {
        return getClosestRuneEssence() != null;
    }

    private RS2Object getClosestRuneEssence() {
        return getObjects().closest("Rune Essence");
    }

    private void leaveEssenceMine() {
        Optional<Entity> portal = getClosestPortal();

        if (!portal.isPresent()) {
            getWalking().walk(getClosestRuneEssence());
        } else if (myPosition().distance(portal.get().getPosition()) > 3) {
            WalkingEvent walkingEvent = new WalkingEvent(portal.get());
            walkingEvent.setMinDistanceThreshold(2);
            execute(walkingEvent);
        } else if (portal.get().interact("Use", "Exit")) {
            Sleep.sleepUntil(() -> AUBURY_HOUSE.contains(myPosition()), 10_000);
        }
    }

    private Optional<Entity> getClosestPortal() {
        // For some reason portals can be both Objects and NPCs
        return Stream.concat(
                getObjects().getAll().stream(),
                getNpcs().getAll().stream()
        )
                .filter(entity -> entity.getName().contains("Portal"))
                .min(Comparator.comparingInt(p -> myPosition().distance(p.getPosition())));
    }

    @Override
    public boolean canExit() {
        return !inEssenceMine();
    }

    @Override
    public RuneEssMiningActivity copy() {
        return new RuneEssMiningActivity(resourceMode);
    }

    private class MiningNode extends Executable {

        @Override
        public void run() throws InterruptedException {
            if (myPlayer().isAnimating()) {
                return;
            }

            if (inEssenceMine()) {
                RS2Object essence = getClosestRuneEssence();
                if (myPosition().distance(essence) > 5) {
                    getWalking().walk(essence);
                } else if (essence.interact("Mine")) {
                    Sleep.sleepUntil(() -> myPlayer().isAnimating(), 10_000);
                }
            } else if (AUBURY_HOUSE.contains(myPosition())) {
                if (getNpcs().closest("Aubury").interact("Teleport")) {
                    Sleep.sleepUntil(() -> getNpcs().closest("Aubury") == null, 10_000);
                }
            } else {
                getWalking().webWalk(AUBURY_HOUSE);
            }
        }
    }
}
