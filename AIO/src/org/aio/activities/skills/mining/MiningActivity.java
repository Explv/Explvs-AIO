package org.aio.activities.skills.mining;

import org.aio.activities.activity.Activity;
import org.aio.activities.activity.ActivityType;
import org.aio.activities.banking.Banking;
import org.aio.util.Executable;
import org.aio.util.ResourceMode;
import org.aio.util.Sleep;
import org.osbot.rs07.api.filter.AreaFilter;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.ui.EquipmentSlot;

import java.util.*;

public class MiningActivity extends Activity {

    private final Mine mine;
    private final Rock rock;
    private final ResourceMode resourceMode;
    private final EnumSet<Pickaxe> pickaxesInBank = EnumSet.noneOf(Pickaxe.class);
    private final List<String> cutGems = Arrays.asList("Opal", "Jade", "Red topaz", "Sapphire", "Emerald", "Ruby", "Diamond", "Dragonstone", "Onyx");
    private final List<String> uncutGems = Arrays.asList("Uncut opal", "Uncut jade", "Uncut red topaz", "Uncut sapphire", "Uncut emerald", "Uncut ruby", "Uncut diamond", "Uncut dragonstone", "Uncut onyx");
    protected Pickaxe currentPickaxe;
    private boolean checkedBankForPickaxes;
    protected Executable miningNode, bankNode;

    public MiningActivity(final Mine mine, final Rock rock, final ResourceMode resourceMode) {
        super(ActivityType.MINING);
        this.mine = mine;
        this.rock = rock;
        this.resourceMode = resourceMode;
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

    protected void getCurrentPickaxe() {
        currentPickaxe = Arrays.stream(Pickaxe.values())
                .filter(pickaxe -> pickaxe.canUse(getSkills()))
                .filter(pickaxe -> getInventory().contains(pickaxe.name) || getEquipment().isWearingItem(EquipmentSlot.WEAPON, pickaxe.name))
                .max(Comparator.comparingInt(Pickaxe::getLevelRequired))
                .orElse(null);
    }

    @Override
    public void runActivity() throws InterruptedException {
        if (currentPickaxe == null || !checkedBankForPickaxes || bankContainsBetterPickaxe() || inventoryContainsNonMiningItem() || (getInventory().isFull() && resourceMode == ResourceMode.BANK)) {
            doBanking();
        } else if (getBank() != null && getBank().isOpen()) {
            getBank().close();
        } else if (currentPickaxe.canEquip(getSkills()) && !getEquipment().isWearingItem(EquipmentSlot.WEAPON, currentPickaxe.name)) {
            if (getInventory().getItem(currentPickaxe.name).interact("Wield")) {
                Sleep.sleepUntil(() -> getEquipment().isWearingItem(EquipmentSlot.WEAPON, currentPickaxe.name), 2000);
            }
        } else if (getInventory().isFull() && resourceMode == ResourceMode.DROP) {
            getInventory().dropAll(item -> !item.getName().equals(currentPickaxe.name));
        } else {
            mine();
        }
    }

    private boolean bankContainsBetterPickaxe() {
        return pickaxesInBank.stream().anyMatch(pickaxe -> pickaxe.canUse(getSkills()) && pickaxe.getLevelRequired() > currentPickaxe.getLevelRequired());
    }

    protected boolean inventoryContainsNonMiningItem() {
        return getInventory().contains(item ->
                        !item.getName().equals(currentPickaxe.name) &&
                        !item.getName().contains(rock.ORE) &&
                        !cutGems.contains(item.getName()) &&
                        !uncutGems.contains(item.getName())
        );
    }

    protected void mine() throws InterruptedException {
        miningNode.run();
    }

    protected void doBanking() throws InterruptedException {
        bankNode.run();
        if (bankNode.hasFailed()) {
            setFailed();
        }
    }

    private class MiningNode extends Executable {

        private Area rockArea;
        private Entity currentRock;

        private Area getAreaWithRock(Rock rock) {
            return Arrays.stream(mine.rockAreas).filter(rockArea -> rockArea.rock == rock)
                    .map(rockArea -> rockArea.area).findAny().get();
        }

        @Override
        public void run() throws InterruptedException {
            if (rockArea == null) {
                rockArea = getAreaWithRock(rock);
            } else if (!rockArea.contains(myPosition())) {
                getWalking().webWalk(rockArea);
            } else if (currentRock == null || !rock.hasOre(currentRock) || !myPlayer().isAnimating()) {
                mineRock();
            }
        }

        private void mineRock() {
            currentRock = rock.getClosestWithOre(getBot().getMethods(), new AreaFilter<>(rockArea));
            if (currentRock != null && currentRock.interact("Mine")) {
                Sleep.sleepUntil(() -> myPlayer().isAnimating() || !rock.hasOre(currentRock), 5000);
            }
        }

        @Override
        public String toString() {
            return "Mining";
        }
    }

    protected class ItemReqBankingImpl extends Banking {

        @Override
        public void bank() {
            if (pickaxesInBank.isEmpty()) {
                for (Pickaxe pickaxe : Pickaxe.values()) {
                    if (getBank().contains(pickaxe.name)) {
                        pickaxesInBank.add(pickaxe);
                    }
                }
                checkedBankForPickaxes = true;
            }

            if (currentPickaxe == null || bankContainsBetterPickaxe()) {
                if (pickaxesInBank.isEmpty()) {
                    setFailed();
                    return;
                }

                if (!getInventory().isEmpty()) {
                    getBank().depositAll();
                } else {
                    Optional<Pickaxe> bestPickaxe = pickaxesInBank.stream().filter(pickaxe -> pickaxe.canUse(getSkills())).max(Comparator.comparingInt(Pickaxe::getLevelRequired));
                    if (bestPickaxe.isPresent()) {
                        if (getBank().withdraw(bestPickaxe.get().name, 1)) {
                            currentPickaxe = bestPickaxe.get();
                        }
                    } else {
                        setFailed();
                    }
                }
            } else if (!getInventory().isEmptyExcept(currentPickaxe.name)) {
                getBank().depositAllExcept(currentPickaxe.name);
            }
        }
    }
}
