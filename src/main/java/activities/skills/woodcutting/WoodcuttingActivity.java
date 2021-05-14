package activities.skills.woodcutting;

import activities.activity.Activity;
import activities.activity.ActivityType;
import activities.banking.DepositAllBanking;
import activities.banking.ToolUpgradeBanking;
import org.osbot.rs07.api.filter.AreaFilter;
import org.osbot.rs07.api.filter.NameFilter;
import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.model.GroundItem;
import org.osbot.rs07.api.ui.EquipmentSlot;
import util.Location;
import util.ResourceMode;
import util.Sleep;

import java.awt.*;
import java.util.Random;

public class WoodcuttingActivity extends Activity {

    private static final String BIRDS_NEST = "Bird's nest";
    private final Tree tree;
    private final Location treeLocation;
    private final ResourceMode resourceMode;
    private final Random random = new Random();
    private DepositAllBanking depositAllBanking;
    private ToolUpgradeBanking<Axe> axeBanking;
    private Entity targetTree;
    private Entity nextTree;
    private GroundItem birdsNest;

    public WoodcuttingActivity(final Tree tree, final Location treeLocation, final ResourceMode resourceMode) {
        super(ActivityType.WOODCUTTING);
        this.tree = tree;
        this.treeLocation = treeLocation;
        this.resourceMode = resourceMode;
    }

    @Override
    public void onStart() throws InterruptedException {
        depositAllBanking = new DepositAllBanking();

        axeBanking = new ToolUpgradeBanking<>(Axe.class);

        if (axeBanking.getCurrentTool() != null) {
            depositAllBanking.setExceptItems(axeBanking.getCurrentTool().name);
        }
    }

    @Override
    public void runActivity() throws InterruptedException {
        if (inventoryContainsNonWcItem() || (getInventory().isFull() && resourceMode == ResourceMode.BANK)) {
            execute(depositAllBanking);
        } else if (axeBanking.toolUpgradeAvailable()) {
            execute(axeBanking);
            if (axeBanking.getCurrentTool() != null) {
                depositAllBanking.setExceptItems(axeBanking.getCurrentTool().getName());
            }
        } else if (axeBanking.getCurrentTool().canEquip(getSkills()) &&
                !getEquipment().isWearingItem(EquipmentSlot.WEAPON, axeBanking.getCurrentTool().getName())) {
            getInventory().equip(axeBanking.getCurrentTool().getName());
        } else if (getInventory().isFull() && resourceMode == ResourceMode.DROP) {
            getInventory().dropAllExcept(axeBanking.getCurrentTool().getName());
        } else if (!treeLocation.getArea().contains(myPosition())) {
            getWalking().webWalk(treeLocation.getArea());
        } else if ((birdsNest = getGroundItems().closest(BIRDS_NEST)) != null) {
            getGroundItems().take(birdsNest);
        } else if (!myPlayer().isAnimating() || (targetTree != null && !targetTree.exists())) {
            chopTree();
        } else if (nextTree == null || nextTree == targetTree){
            nextTree = getNextTree();

            // Hover the next tree sometimes
            if (random.nextBoolean()) {
                nextTree.hover();
            }
        }
    }

    private boolean inventoryContainsNonWcItem() {
        return getInventory().contains(item -> {
            if (axeBanking.getCurrentTool() != null) {
                if (item.getName().equals(axeBanking.getCurrentTool().getName())) {
                    return false;
                }
            }
            if (item.getName().equals(tree.logsName)) {
                return false;
            }
            if (item.getName().equals(BIRDS_NEST)) {
                return false;
            }
            return true;
        });
    }

    private void chopTree() {
        targetTree = getObjects().closest(
                new AreaFilter<>(treeLocation.getArea()),
                new NameFilter<>(tree.toString())
        );
        if (targetTree != null && targetTree.interact("Chop down")) {
            Sleep.sleepUntil(() -> !targetTree.exists(), 5000);
        }
    }

    private Entity getNextTree() {
        return getObjects().closest(
                new AreaFilter<>(treeLocation.getArea()),
                new NameFilter<>(tree.toString()),
                obj -> obj != targetTree
        );
    }

    @Override
    public void onPaint(Graphics2D graphics) {
        Color prevColor = graphics.getColor();

        if (targetTree != null) {
            graphics.setColor(Color.GREEN);
            getGraphics().drawEntity(targetTree, graphics);
        }

        if (nextTree != null) {
            graphics.setColor(Color.ORANGE);
            getGraphics().drawEntity(nextTree, graphics);
        }

        graphics.setColor(prevColor);
    }

    @Override
    public WoodcuttingActivity copy() {
        return new WoodcuttingActivity(tree, treeLocation, resourceMode);
    }
}
