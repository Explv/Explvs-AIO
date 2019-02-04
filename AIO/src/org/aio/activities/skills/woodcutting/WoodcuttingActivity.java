package org.aio.activities.skills.woodcutting;

import org.aio.activities.activity.Activity;
import org.aio.activities.activity.ActivityType;
import org.aio.activities.banking.DepositAllBanking;
import org.aio.activities.banking.ToolUpgradeBanking;
import org.aio.util.Location;
import org.aio.util.ResourceMode;
import org.aio.util.Sleep;
import org.osbot.rs07.api.filter.AreaFilter;
import org.osbot.rs07.api.filter.NameFilter;
import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.ui.EquipmentSlot;

public class WoodcuttingActivity extends Activity {

    private final Tree tree;
    private final Location treeLocation;
    private final ResourceMode resourceMode;
    private DepositAllBanking depositAllBanking;
    private ToolUpgradeBanking<Axe> axeBanking;
    private Entity targetTree;

    public WoodcuttingActivity(final Tree tree, final Location treeLocation, final ResourceMode resourceMode) {
        super(ActivityType.WOODCUTTING);
        this.tree = tree;
        this.treeLocation = treeLocation;
        this.resourceMode = resourceMode;
    }

    @Override
    public void onStart() throws InterruptedException {
        depositAllBanking = new DepositAllBanking();
        depositAllBanking.exchangeContext(getBot());

        axeBanking = new ToolUpgradeBanking<>(Axe.class);
        axeBanking.exchangeContext(getBot());
        axeBanking.onStart();

        if (axeBanking.getCurrentTool() != null) {
            depositAllBanking.setExceptItems(axeBanking.getCurrentTool().name);
        }
    }

    @Override
    public void runActivity() throws InterruptedException {
        if (inventoryContainsNonWcItem() || (getInventory().isFull() && resourceMode == ResourceMode.BANK)) {
            depositAllBanking.run();
        } else if (axeBanking.toolUpgradeAvailable()) {
            axeBanking.run();
            if (axeBanking.hasFailed()) {
                setFailed();
            } else if (axeBanking.getCurrentTool() != null) {
                depositAllBanking.setExceptItems(axeBanking.getCurrentTool().getName());
            }
        } else if (getBank() != null && getBank().isOpen()) {
            getBank().close();
        } else if (axeBanking.getCurrentTool().canEquip(getSkills()) &&
                   !getEquipment().isWearingItem(EquipmentSlot.WEAPON, axeBanking.getCurrentTool().getName())) {
            if (getInventory().getItem(axeBanking.getCurrentTool().getName()).interact("Wield")) {
                Sleep.sleepUntil(() -> getEquipment().isWearingItem(EquipmentSlot.WEAPON, axeBanking.getCurrentTool().getName()), 2000);
            }
        } else if (getInventory().isFull() && resourceMode == ResourceMode.DROP) {
            getInventory().dropAll(item -> !item.getName().equals(axeBanking.getCurrentTool().getName()));
        } else if (!treeLocation.getArea().contains(myPosition())) {
            getWalking().webWalk(treeLocation.getArea());
        } else if (getGroundItems().closest("Bird's nest") != null) {
            pickUpBirdsNest();
        } else if (!myPlayer().isAnimating() || (targetTree != null && !targetTree.exists())) {
            chopTree();
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
            if (item.getName().equals("Bird's nest")) {
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
            Sleep.sleepUntil(() -> myPlayer().isAnimating() || !targetTree.exists(), 5000);
        }
    }

    private void pickUpBirdsNest() {
        int emptySlots = getInventory().getEmptySlots();
        if (getGroundItems().closest("Bird's nest").interact("Take")) {
            Sleep.sleepUntil(() -> getInventory().getEmptySlots() < emptySlots || getGroundItems().closest("Bird's nest") == null, 5000);
        }
    }

    @Override
    public WoodcuttingActivity copy() {
        return new WoodcuttingActivity(tree, treeLocation, resourceMode);
    }
}
