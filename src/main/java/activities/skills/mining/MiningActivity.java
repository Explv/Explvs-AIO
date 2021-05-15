package activities.skills.mining;

import activities.activity.Activity;
import activities.activity.ActivityType;
import activities.banking.DepositAllBanking;
import activities.banking.ToolUpgradeBanking;
import org.osbot.rs07.api.filter.AreaFilter;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.EquipmentSlot;
import util.ResourceMode;
import util.Sleep;
import util.executable.Executable;

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

        pickaxeBanking = new ToolUpgradeBanking<>(Pickaxe.class);

        if (pickaxeBanking.getCurrentTool() != null) {
            depositAllBanking.setExceptItems(pickaxeBanking.getCurrentTool().getName());
        }

        miningNode = new MiningNode();
    }

    @Override
    public void runActivity() throws InterruptedException {
        if (shouldBank()) {
            execute(depositAllBanking);
        } else if (pickaxeBanking.toolUpgradeAvailable()) {
            execute(pickaxeBanking);
            if (pickaxeBanking.getCurrentTool() != null) {
                depositAllBanking.setExceptItems(pickaxeBanking.getCurrentTool().getName());
            }
        } else if (pickaxeBanking.getCurrentTool().canEquip(getSkills()) && !getEquipment().isWearingItem(EquipmentSlot.WEAPON, pickaxeBanking.getCurrentTool().name)) {
            getInventory().equip(pickaxeBanking.getCurrentTool().name);
        } else if (getInventory().isFull() && resourceMode == ResourceMode.DROP) {
            getInventory().dropAllExcept(pickaxeBanking.getCurrentTool().name);
        } else {
            mine();
        }
    }

    protected boolean shouldBank() {
        return inventoryContainsNonMiningItem() ||
               (getInventory().isFull() && resourceMode == ResourceMode.BANK);
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
        execute(miningNode);
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
            } else {
                mineRock();
            }
        }

        private void mineRock() {
            currentRock = getNextRock();
            if (currentRock != null && currentRock.interact("Mine")) {
                Sleep.sleepUntil(() -> !currentRock.exists(), 5000);
            }
        }

        private RS2Object getNextRock() {
            return rock.getClosestWithOre(
                    getBot().getMethods(),
                    new AreaFilter<>(rockArea),
                    obj -> obj != currentRock
            );
        }

        @Override
        public String toString() {
            return "Mining";
        }
    }
}
