package activities.skills.crafting;

import activities.activity.Activity;
import activities.activity.ActivityType;
import activities.banking.Bank;
import activities.banking.ItemReqBanking;
import org.osbot.rs07.api.ui.RS2Widget;
import util.Executable;
import util.Location;
import util.MakeAllInterface;
import util.Sleep;
import util.item_requirement.ItemReq;
import util.widget.CachedWidget;

public class CraftingActivity extends Activity {

    private CraftingItem craftingItem;
    private final Sleep FINISHED_CRAFTING_SLEEP = new Sleep(() -> !canMake() || getDialogues().isPendingContinuation(), 60_000);
    private Location location;
    private CachedWidget jewelleryWidget;
    private Executable bankNode;
    private MakeAllInterface makeAllInterface;
    private final Sleep MAKE_ALL_INTERFACE_OPEN_SLEEP = new Sleep(() -> makeAllInterface.isMakeAllScreenOpen(), 2000);

    public CraftingActivity(final CraftingItem craftingItem, final Location location) {
        super(ActivityType.CRAFTING);
        this.craftingItem = craftingItem;
        this.location = location;
    }

    @Override
    public void onStart() {
        bankNode = new ItemReqBanking(craftingItem.itemReqs);
        bankNode.exchangeContext(getBot());

        makeAllInterface = new MakeAllInterface(craftingItem.widgetText);
        makeAllInterface.exchangeContext(getBot());
    }

    @Override
    public void runActivity() throws InterruptedException {
        if (!canMake()) {
            try {
                bankNode.run();
                if (bankNode.hasFailed()) {
                    setFailed();
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
                sleep(600);
            }
        } else if (getBank() != null && getBank().isOpen()) {
            getBank().close();
        } else if (location.getArea() == null && !Bank.inAnyBank(myPosition())) {
            getWalking().webWalk(Bank.AREAS);
        } else if (location.getArea() != null && !location.getArea().contains(myPosition())) {
            getWalking().webWalk(location.getArea());
        } else if (makeAllInterface.isMakeAllScreenOpen()) {
            if (makeAllInterface.makeAll()) {
                if (craftingItem.type == CraftingType.POTTERY && getInventory().contains("Soft clay")) {
                    Sleep.sleepUntil(() -> !getInventory().contains("Soft clay") || getDialogues().isPendingContinuation(), 60_000, 600);
                } else {
                    FINISHED_CRAFTING_SLEEP.sleep();
                }
            }
        } else {
            switch (craftingItem.type) {
                case SPINNING:
                    makeXInteract("Spinning wheel", "Spin");
                    break;
                case WEAVING:
                    makeXInteract("Loom", "Weave");
                    break;
                case POTTERY:
                    if (getInventory().contains("Soft clay")) {
                        makeXUse("Potter's Wheel", "Soft clay");
                    } else {
                        makeXInteract("Pottery Oven", "Fire");
                    }
                    break;
                case ARMOUR:
                case SNELM:
                case JEM_CUTTING:
                case GLASS:
                    makeXItem(craftingItem.itemReqs[0].getName(), craftingItem.itemReqs[1].getName());
                    break;
                case MOLTEN_GLASS:
                    makeXUse("Furnace", "Bucket of sand");
                    break;
                case JEWELLERY:
                    makeJewellery();
                    break;
                case WEAPONRY:
                    if (craftingItem == CraftingItem.SILVER_SICKLE || craftingItem == CraftingItem.SILVER_BOLTS) {
                        makeJewellery();
                    } else {
                        makeXItem(craftingItem.itemReqs[0].getName(), craftingItem.itemReqs[1].getName());
                    }
                    break;
            }
        }
    }

    private boolean canMake() {
        if (craftingItem.type == CraftingType.POTTERY) {
            return ItemReq.hasItemRequirements(craftingItem.itemReqs, getInventory())
                    || getInventory().contains("Unfired " + craftingItem.name.toLowerCase());
        }
        return ItemReq.hasItemRequirements(craftingItem.itemReqs, getInventory());
    }

    private void makeJewellery() {
        if (getWidgets().getWidgetContainingText("What would you like to make?") != null) {
            if (jewelleryWidget == null) {
                jewelleryWidget = getJewelleryWidget();
            } else if (jewelleryWidget.interact(getWidgets())) {
                FINISHED_CRAFTING_SLEEP.sleep();
            }
        } else if (getObjects().closest("Furnace").interact("Smelt")) {
            Sleep.sleepUntil(() -> getWidgets().getWidgetContainingText("What would you like to make?") != null, 5000);
        }
    }

    private CachedWidget getJewelleryWidget() {
        if (craftingItem.widgetText != null) {
            RS2Widget textWidget = getWidgets().getWidgetContainingText(craftingItem.widgetText);
            return new CachedWidget(textWidget.getRootId(), textWidget.getSecondLevelId() - 1);
        }
        RS2Widget titleWidget = getWidgets().getWidgetContainingText(craftingItem.widgetTitle);
        return new CachedWidget(titleWidget.getRootId(), titleWidget.getSecondLevelId() + 4 + craftingItem.childIndex);
    }

    private void makeXItem(String useItem, String interactItem) {
        if (!useItem.equals(getInventory().getSelectedItemName())) {
            getInventory().getItem(useItem).interact("Use");
        } else if (getInventory().getItem(interactItem).interact()) {
            MAKE_ALL_INTERFACE_OPEN_SLEEP.sleep();
        }
    }

    private void makeXInteract(String object, String interaction) {
        if (getObjects().closest(object).interact(interaction)) {
            MAKE_ALL_INTERFACE_OPEN_SLEEP.sleep();
        }
    }

    private void makeXUse(String object, String item) {
        if (!item.equals(getInventory().getSelectedItemName())) {
            getInventory().getItem(item).interact("Use");
        } else if (getObjects().closest(object).interact("Use")) {
            MAKE_ALL_INTERFACE_OPEN_SLEEP.sleep();
        }
    }

    @Override
    public CraftingActivity copy() {
        return new CraftingActivity(craftingItem, location);
    }
}
