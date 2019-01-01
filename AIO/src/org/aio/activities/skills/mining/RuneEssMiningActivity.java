package org.aio.activities.skills.mining;

import org.aio.util.Executable;
import org.aio.util.ResourceMode;
import org.aio.util.Sleep;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.Entity;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

public class RuneEssMiningActivity extends MiningActivity {

    private final Area auburyHouse = new Area(
            new int[][]{
                    { 3250, 3403 },
                    { 3250, 3401 },
                    { 3252, 3399 },
                    { 3254, 3399 },
                    { 3256, 3401 },
                    { 3256, 3403 },
                    { 3254, 3405 },
                    { 3252, 3405 },
                    { 3252, 3403 }
            }
    );

    private final int[][] gridPortalCoordinates = {
            { 37, 29 },
            { 33, 76 },
            { 72, 77 },
            { 74, 33 }
    };

    public RuneEssMiningActivity(final ResourceMode resourceMode) {
        super(null, Rock.RUNE_ESSENCE, resourceMode);
    }

    @Override
    public void onStart() {
        // On activity start get the best pickaxe the player can use, and has in their inventory or equipment
        getCurrentPickaxe();
        miningNode = new MiningNode();
        miningNode.exchangeContext(getBot());
        bankNode = new ItemReqBankingImpl();
        bankNode.exchangeContext(getBot());
    }

    @Override
    public boolean canExit() {
        return getObjects().closest("Rune Essence") == null;
    }

    @Override
    protected boolean inventoryContainsNonMiningItem() {
        return getInventory().contains(item ->
                        !item.getName().equals(currentPickaxe.name) &&
                        !item.getName().endsWith("essence")
        );
    }

    private class MiningNode extends Executable {

        @Override
        public void run() throws InterruptedException {
            if (myPlayer().isAnimating()) {
                return;
            } else if (getObjects().closest("Rune Essence") != null) {
                if (getObjects().closest("Rune Essence").interact("Mine")) {
                    Sleep.sleepUntil(() -> myPlayer().isAnimating(), 10_000);
                }
            } else if (auburyHouse.contains(myPosition())) {
                if (getNpcs().closest("Aubury").interact("Teleport")) {
                    Sleep.sleepUntil(() -> getNpcs().closest("Aubury") == null, 10_000);
                }
            } else {
                getWalking().webWalk(auburyHouse);
            }
        }
    }

    private class ItemReqBankingImpl extends MiningActivity.ItemReqBankingImpl {
        @Override
        public void run() throws InterruptedException {
            if (getObjects().closest("Rune Essence") != null) {
                Optional<Entity> portal = Stream.concat(getObjects().getAll().stream(), getNpcs().getAll().stream())
                                            .filter(entity -> entity.getName().contains("Portal"))
                                            .min(Comparator.comparingInt(p -> myPosition().distance(p.getPosition())));
                if (portal.isPresent()) {
                    if (portal.get().interact("Use", "Exit")) {
                        Sleep.sleepUntil(() -> auburyHouse.contains(myPosition()), 10_000);
                    }
                } else {
                    Position[] portalPositions = new Position[4];
                    for (int i = 0; i < gridPortalCoordinates.length; i++) {
                        portalPositions[i] = new Position(getMap().getBaseX() + gridPortalCoordinates[i][0], getMap().getBaseY() + gridPortalCoordinates[i][1], myPlayer().getZ());
                    }
                    Position closestPosition = Arrays.stream(portalPositions).min(Comparator.comparingInt(p -> myPosition().distance(p))).get();
                    getWalking().walk(closestPosition);
                }
            } else {
                super.run();
            }
        }
    }
}
