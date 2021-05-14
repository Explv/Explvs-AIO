package activities.skills.fishing;

import activities.activity.Activity;
import activities.activity.ActivityType;
import activities.banking.DepositAllBanking;
import activities.banking.ItemReqBanking;
import org.osbot.rs07.api.model.NPC;
import util.executable.Executable;
import util.ResourceMode;
import util.Sleep;
import util.item_requirement.ItemReq;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FishingActivity extends Activity {

    private final Fish fish;
    private final FishingLocation location;
    private final ResourceMode resourceMode;
    private final ItemReq[] itemReqs;

    private NPC currentFishingSpot;
    private final Executable itemReqBankNode;
    private final Executable depositAllBankNode;

    public FishingActivity(final Fish fish, final FishingLocation location, final ResourceMode resourceMode) {
        super(ActivityType.FISHING);
        this.fish = fish;
        this.location = location;
        this.resourceMode = resourceMode;

        List<ItemReq> itemReqs = new ArrayList<>();
        Collections.addAll(itemReqs, fish.fishingMethod.itemReqs);

        if (location == FishingLocation.MUSA_POINT) {
            itemReqs.add(new ItemReq("Coins", 60, 5000).setStackable());
        }

        this.itemReqs = itemReqs.toArray(new ItemReq[0]);
        itemReqBankNode = new ItemReqBanking(this.itemReqs);
        depositAllBankNode = new DepositAllBanking(this.itemReqs);
    }

    @Override
    public void runActivity() throws InterruptedException {
        if (!ItemReq.hasItemRequirements(itemReqs, getInventory())
                || ItemReq.hasNonItemRequirement(itemReqs, getInventory(), Fish.RAW_FISH_FILTER)) {
            execute(itemReqBankNode);
        } else if (getInventory().isFull()) {
            if (resourceMode == ResourceMode.BANK) {
                execute(depositAllBankNode);
            } else if (resourceMode == ResourceMode.DROP) {
                getInventory().dropAll(Fish.RAW_FISH_FILTER);
            }
        } else if (!location.location.getArea().contains(myPosition())) {
            getWalking().webWalk(location.location.getArea());
        } else if (!myPlayer().isInteracting(currentFishingSpot) || getDialogues().isPendingContinuation()) {
            fish();
        }
    }

    private void fish() {
        currentFishingSpot = getFishingSpot();
        if (currentFishingSpot != null) {
            if (!currentFishingSpot.isOnScreen()) {
                getWalking().walk(currentFishingSpot);
            }
            if (currentFishingSpot.interact(fish.fishingMethod.action)) {
                Sleep.sleepUntil(() -> myPlayer().isInteracting(currentFishingSpot) || !currentFishingSpot.exists(), 5000);
            }
        }
    }

    private NPC getFishingSpot() {
        return getNpcs().closest(npc ->
                npc.getName().equals(fish.fishingMethod.spotName) &&
                        npc.hasAction(fish.fishingMethod.action) &&
                        map.canReach(npc) &&
                        location.location.getArea().contains(npc.getPosition())
        );
    }

    @Override
    public FishingActivity copy() {
        return new FishingActivity(fish, location, resourceMode);
    }
}
