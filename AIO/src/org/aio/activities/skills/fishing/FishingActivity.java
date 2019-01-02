package org.aio.activities.skills.fishing;

import org.aio.activities.activity.Activity;
import org.aio.activities.activity.ActivityType;
import org.aio.activities.banking.Bank;
import org.aio.activities.banking.ItemReqBanking;
import org.aio.util.*;
import org.aio.util.item_requirement.ItemReq;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.event.WebWalkEvent;
import org.osbot.rs07.utility.Condition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class FishingActivity extends Activity {

    private final Fish fish;
    private final FishingLocation location;
    private final ResourceMode resourceMode;
    private final ItemReq[] itemReqs;

    private final CachedWidget musaPointCharterWidget = new CachedWidget(72, new WidgetActionFilter("Musa Point"));

    private NPC currentFishingSpot;
    private Executable bankNode;

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
    }

    @Override
    public void onStart() {
        if (location == FishingLocation.MUSA_POINT) {
            bankNode = new MusaPointBanking(itemReqs);
        } else {
            bankNode = new ItemReqBanking(itemReqs);
        }
        bankNode.exchangeContext(getBot());
    }

    @Override
    public void runActivity() throws InterruptedException {
        if(!ItemReq.hasItemRequirements(fish.fishingMethod.itemReqs, getInventory())
            || inventoryContainsNonFishingItem()
            || (getInventory().isFull() && resourceMode == ResourceMode.BANK)) {
            bankNode.run();
            if (bankNode.hasFailed()) {
                setFailed();
            }
        } else if (getBank() != null && getBank().isOpen()) {
            getBank().close();
        } else if (getInventory().isFull() && resourceMode == ResourceMode.DROP) {
            getInventory().dropAll(item -> item.getName().startsWith("Raw "));
        } else if (!location.location.getArea().contains(myPosition())) {
            if (location == FishingLocation.MUSA_POINT) {
                walkToMusaPoint();
            } else {
                getWalking().webWalk(location.location.getArea());
            }
        } else if (!myPlayer().isInteracting(currentFishingSpot) || getDialogues().isPendingContinuation()){
            fish();
        }
    }

    private void walkToMusaPoint() throws InterruptedException {
        Optional<RS2Widget> charterWidget = musaPointCharterWidget.get(getWidgets()).filter(RS2Widget::isVisible);

        if (getDialogues().isPendingContinuation()) {
            getDialogues().clickContinue();
        } else if (charterWidget.isPresent()) {
            if (charterWidget.get().interact()) {
                sleep(2000);
            }
        } else {
            WebWalkEvent webWalkEvent = new WebWalkEvent(FishingLocation.MUSA_POINT.location.getArea());
            webWalkEvent.setBreakCondition(new Condition() {
                @Override
                public boolean evaluate() {
                    return musaPointCharterWidget.get(getWidgets()).filter(RS2Widget::isVisible).isPresent();
                }
            });
            execute(webWalkEvent);
        }
    }

    private boolean inventoryContainsNonFishingItem(){
        return getInventory().contains(item -> !ItemReq.isRequirementItem(itemReqs, item) && !item.getName().startsWith("Raw "));
    }

    private void fish(){
        currentFishingSpot = getFishingSpot();
        if (currentFishingSpot != null) {
            if(!currentFishingSpot.isOnScreen()) {
                getWalking().walk(currentFishingSpot);
            }
            if (currentFishingSpot.interact(fish.fishingMethod.action)) {
                Sleep.sleepUntil(() -> myPlayer().isInteracting(currentFishingSpot) || !currentFishingSpot.exists(), 5000);
            }
        }
    }

    private NPC getFishingSpot(){
        return getNpcs().closest(npc ->
                npc.getName().equals(fish.fishingMethod.spotName) &&
                        npc.hasAction(fish.fishingMethod.action) &&
                        map.canReach(npc) &&
                        location.location.getArea().contains(npc.getPosition())
        );
    }

    private class MusaPointBanking extends ItemReqBanking {

        private final Area musaPoint = new Area(
                new int[][]{
                        { 2868, 3193 },
                        { 2910, 3198 },
                        { 2952, 3168 },
                        { 2962, 3160 },
                        { 2964, 3142 },
                        { 2918, 3130 },
                        { 2865, 3141 }
                }
        );
        private final Area portSarimDepositBox = new Area(3044, 3237, 3052, 3234);
        private final Area musaPointPier = new Area(2947, 3160, 2961, 3143);

        public MusaPointBanking(final ItemReq[] itemReqs) {
            super(itemReqs);
        }

        @Override
        public void run() throws InterruptedException {
            if (musaPoint.contains(myPosition()) && !musaPointPier.contains(myPosition())) {
                getWalking().webWalk(musaPointPier);
            } else if (ItemReq.hasItemRequirements(itemReqs, getInventory())) {
                if (!portSarimDepositBox.contains(myPosition())) {
                    charterShipToPortSarim(new Area[]{ portSarimDepositBox });
                } else if (!getDepositBox().isOpen()) {
                    getDepositBox().open();
                } else {
                    getDepositBox().depositAllExcept(item -> ItemReq.isRequirementItem(itemReqs, item));
                }
            } else {
                if (!Bank.inAnyBank(myPosition())) {
                    charterShipToPortSarim(Bank.getAreas());
                } else {
                    super.run();
                }
            }
        }

        private void charterShipToPortSarim(Area[] targetAreas) throws InterruptedException {
            if (musaPointPier.contains(myPosition())) {
                if (getDialogues().inDialogue()) {
                    getDialogues().completeDialogue("Can I journey on this ship?", "Search away, I have nothing to hide.", "Ok.");
                } else {
                    NPC customsOfficer = getNpcs().closest("Customs officer");

                    if (customsOfficer != null && customsOfficer.interact("Pay-Fare")) {
                        Sleep.sleepUntil(() -> getDialogues().inDialogue(), 5000);
                    }
                }
            } else {
                WebWalkEvent webWalkEvent = new WebWalkEvent(targetAreas);
                webWalkEvent.setBreakCondition(new Condition() {
                    @Override
                    public boolean evaluate() {
                        return musaPointPier.contains(myPosition());
                    }
                });
                execute(webWalkEvent);
            }
        }
    }
}
