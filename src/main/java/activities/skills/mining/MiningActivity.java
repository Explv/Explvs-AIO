package activities.skills.mining;

import activities.activity.Activity;
import activities.activity.ActivityType;
import activities.banking.DepositAllBanking;
import activities.banking.ToolUpgradeBanking;
import org.osbot.rs07.api.filter.AreaFilter;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.ui.EquipmentSlot;
import util.Executable;
import util.ResourceMode;
import util.Sleep;

import java.util.Arrays;
import java.util.List;

public class MiningActivity extends Activity {

    final ResourceMode resourceMode;
    private final Mine mine;
    private final Rock rock;
    private final List<String> cutGems = Arrays.asList("Opal", "Jade", "Red topaz", "Sapphire", "Emerald", "Ruby", "Diamond", "Dragonstone", "Onyx");
    private final List<String> uncutGems = Arrays.asList("Uncut opal", "Uncut jade", "Uncut red topaz", "Uncut sapphire", "Uncut emerald", "Uncut ruby", "Uncut diamond", "Uncut dragonstone", "Uncut onyx");
    ToolUpgradeBanking<Pickaxe> pickaxeBanking;
    DepositAllBanking depositAllBanking;
    Executable miningNode;

    public MiningActivity(final Mine mine, final Rock rock, final ResourceMode resourceMode) {
        super(ActivityType.MINING);
        this.mine = mine;
        this.rock = rock;
        this.resourceMode = resourceMode;
    }

    @Override
    public void onStart() throws InterruptedException {
        depositAllBanking = new DepositAllBanking();
        depositAllBanking.exchangeContext(getBot());

        pickaxeBanking = new ToolUpgradeBanking<>(Pickaxe.class);
        pickaxeBanking.exchangeContext(getBot());
        pickaxeBanking.onStart();

        if (pickaxeBanking.getCurrentTool() != null) {
            depositAllBanking.setExceptItems(pickaxeBanking.getCurrentTool().getName());
        }

        miningNode = new MiningNode();
        miningNode.exchangeContext(getBot());
    }

    @Override
    public void runActivity() throws InterruptedException {
        if (shouldBank()) {
            depositAllBanking.run();
        } else if (pickaxeBanking.toolUpgradeAvailable()) {
            pickaxeBanking.run();
            if (pickaxeBanking.hasFailed()) {
                setFailed();
            } else if (pickaxeBanking.getCurrentTool() != null) {
                depositAllBanking.setExceptItems(pickaxeBanking.getCurrentTool().getName());
            }
        } else if (getBank() != null && getBank().isOpen()) {
            getBank().close();
        } else if (pickaxeBanking.getCurrentTool().canEquip(getSkills()) && !getEquipment().isWearingItem(EquipmentSlot.WEAPON, pickaxeBanking.getCurrentTool().name)) {
            if (getInventory().getItem(pickaxeBanking.getCurrentTool().name).interact("Wield")) {
                Sleep.sleepUntil(() -> getEquipment().isWearingItem(EquipmentSlot.WEAPON, pickaxeBanking.getCurrentTool().name), 2000);
            }
        } else if (getInventory().isFull() && resourceMode == ResourceMode.DROP) {
            getInventory().dropAll(item -> !item.getName().equals(pickaxeBanking.getCurrentTool().name));
        } else {
            mine();
        }
    }

    protected boolean shouldBank() {
        return inventoryContainsNonMiningItem() || (getInventory().isFull() && resourceMode == ResourceMode.BANK);
    }

    protected boolean inventoryContainsNonMiningItem() {
        return getInventory().contains(item -> {
            if (pickaxeBanking.getCurrentTool() != null) {
                if (item.getName().equals(pickaxeBanking.getCurrentTool().getName())) {
                    return false;
                }
            }
            return !item.getName().contains(rock.ORE) &&
                    !cutGems.contains(item.getName()) &&
                    !uncutGems.contains(item.getName());
        });
    }

    protected void mine() throws InterruptedException {
        miningNode.run();
    }

    @Override
    public MiningActivity copy() {
        return new MiningActivity(mine, rock, resourceMode);
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
}
