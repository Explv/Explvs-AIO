package org.aio.activities.skills.woodcutting;

import org.aio.activities.activity.Activity;
import org.aio.activities.activity.ActivityType;
import org.aio.activities.banking.Banking;
import org.aio.util.Executable;
import org.aio.util.Location;
import org.aio.util.ResourceMode;
import org.aio.util.Sleep;
import org.osbot.rs07.api.filter.AreaFilter;
import org.osbot.rs07.api.filter.NameFilter;
import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.ui.EquipmentSlot;

import java.util.Arrays;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.Optional;

public class WoodcuttingActivity extends Activity {

    private final Tree tree;
    private final Location treeLocation;
    private final ResourceMode resourceMode;
    private final EnumSet<Axe> axesInBank = EnumSet.noneOf(Axe.class);
    private boolean checkedBankForAxes;
    private Axe currentAxe;
    private Executable bankNode;
    private Entity targetTree;

    public WoodcuttingActivity(final Tree tree, final Location treeLocation, final ResourceMode resourceMode) {
        super(ActivityType.WOODCUTTING);
        this.tree = tree;
        this.treeLocation = treeLocation;
        this.resourceMode = resourceMode;
    }

    @Override
    public void onStart() {
        // On activity start get the best axe the player can use, and has in their inventory or equipment
        currentAxe = Arrays.stream(Axe.values())
                    .filter(axe -> axe.canUse(getSkills()))
                    .filter(axe -> getInventory().contains(axe.name) || getEquipment().isWearingItem(EquipmentSlot.WEAPON, axe.name))
                    .max(Comparator.comparingInt(Axe::getLevelRequired))
                    .orElse(null);
        bankNode = new WCBanking();
        bankNode.exchangeContext(getBot());
    }

    @Override
    public void runActivity() throws InterruptedException {
        if (currentAxe == null || !checkedBankForAxes || bankContainsBetterAxe() || inventoryContainsNonWcItem() || (getInventory().isFull() && resourceMode == ResourceMode.BANK)) {
            bankNode.run();
            if (bankNode.hasFailed()) {
                setFailed();
            }
        } else if (getBank() != null && getBank().isOpen()) {
            getBank().close();
        } else if (currentAxe.canEquip(getSkills()) && !getEquipment().isWearingItem(EquipmentSlot.WEAPON, currentAxe.name)) {
            if (getInventory().getItem(currentAxe.name).interact("Wield")) {
                Sleep.sleepUntil(() -> getEquipment().isWearingItem(EquipmentSlot.WEAPON, currentAxe.name), 2000);
            }
        } else if (getInventory().isFull() && resourceMode == ResourceMode.DROP) {
            getInventory().dropAll(item -> !item.getName().equals(currentAxe.name));
        } else if (!treeLocation.getArea().contains(myPosition())) {
            getWalking().webWalk(treeLocation.getArea());
        } else if (getGroundItems().closest("Bird's nest") != null) {
            pickUpBirdsNest();
        } else if (!myPlayer().isAnimating() || (targetTree != null && !targetTree.exists())) {
            chopTree();
        }
    }

    private boolean bankContainsBetterAxe() {
        return axesInBank.stream().anyMatch(axe -> axe.canUse(getSkills()) && axe.getLevelRequired() > currentAxe.getLevelRequired());
    }

    private boolean inventoryContainsNonWcItem() {
        return getInventory().contains(item ->
                !item.getName().equals(currentAxe.name) &&
                        !item.getName().equals(tree.logsName) &&
                        !item.getName().equals("Bird's nest"));
    }

    private void chopTree() {
        targetTree = getObjects().closest(new AreaFilter<>(treeLocation.getArea()), new NameFilter<>(tree.toString()),
                i -> i.getDefinition().getModifiedModelColors() != null);
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

    private class WCBanking extends Banking {

        @Override
        public boolean bank() {
            if (axesInBank.isEmpty()) {
                for (Axe axe : Axe.values()) {
                    if (getBank().contains(axe.name)) {
                        axesInBank.add(axe);
                    }
                }
                checkedBankForAxes = true;
            }

            if (currentAxe == null || bankContainsBetterAxe()) {
                if (axesInBank.isEmpty()) {
                    setFailed();
                    return false;
                }

                if (!getInventory().isEmpty()) {
                    getBank().depositAll();
                } else {
                    Optional<Axe> bestAxe = axesInBank.stream().filter(axe -> axe.canUse(getSkills()))
                            .max(Comparator.comparingInt(Axe::getLevelRequired));
                    if (bestAxe.isPresent()) {
                        if (getBank().withdraw(bestAxe.get().name, 1)) {
                            currentAxe = bestAxe.get();
                        }
                    } else {
                        setFailed();
                        return false;
                    }
                }
            } else if (!getInventory().isEmptyExcept(currentAxe.name)) {
                getBank().depositAllExcept(currentAxe.name);
            }

            return true;
        }
    }
}
