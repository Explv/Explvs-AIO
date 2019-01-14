package org.aio.activities.quests;

import org.aio.util.Executable;
import org.aio.util.Sleep;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.GroundItem;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.model.RS2Object;

import java.util.List;

public class ItemCompleter extends Executable {

    private final String itemName;
    private String objectName;
    private final String interaction;
    private Area area;
    private List<Position> path;
    private ObjectSelector objectSelector;

    /*
        Item from Ground Constructor
     */
    public ItemCompleter(final String itemName){
        this.itemName = itemName;
        this.interaction = "Take";
        this.objectSelector = ObjectSelector.CLOSEST;
    }

    public ItemCompleter(final String itemName, final Area area){
        this(itemName);
        this.area = area;
    }

    public ItemCompleter(final String itemName, final Area area, List<Position> path){
        this(itemName, area);
        this.path = path;
    }

    /*
        Item from Object Constructor
     */
    public ItemCompleter(final String objectName, final String itemName, final String interaction){
        this.itemName = itemName;
        this.objectName = objectName;
        this.interaction = interaction;
        this.objectSelector = ObjectSelector.CLOSEST;
    }

    public ItemCompleter(final String objectName, final String itemName, final String interaction, final ObjectSelector objectSelector){
        this(objectName, itemName, interaction);
        this.objectSelector = objectSelector;
    }

    public ItemCompleter(final String objectName, final String itemName, final String interaction, final Area area){
        this(objectName, itemName, interaction);
        this.area = area;
    }

    public ItemCompleter(final String objectName, final String itemName, final String interaction, final Area area, final ObjectSelector objectSelector){
        this(objectName, itemName, interaction, objectSelector);
        this.area = area;
    }

    public ItemCompleter(final String objectName, final String itemName, final String interaction, final Area area, final List<Position> path){
        this(objectName, itemName, interaction, area);
        this.path = path;
    }

    public ItemCompleter(final String objectName, final String itemName, final String interaction, final Area area, final List<Position> path, final ObjectSelector objectSelector){
        this(objectName, itemName, interaction, area, objectSelector);
        this.path = path;
    }
    /*
        Constructor end, yikes
     */

    @Override
    public void run() throws InterruptedException {
        run(WalkType.PATH);
    }

    public void run(WalkType useForcedWalkType) throws InterruptedException {
        if(objectName != null){
            takeItemFromObject(useForcedWalkType);
        }else{
            takeItemFromGround(useForcedWalkType);
        }
    }

    private void takeItemFromObject(WalkType useForcedWalkType){
        RS2Object object = getObjects().closest(o -> o.getName().equals(objectName) && o.hasAction(interaction));
        List<RS2Object> objects = getObjects().filter(o -> o.getName().equals(objectName) && o.hasAction(interaction));

        if ((object == null && objectSelector == ObjectSelector.CLOSEST)||(objects.isEmpty() && objectSelector == ObjectSelector.RANDOM)) {
            doWalk(useForcedWalkType);
        }

        if(objectSelector == ObjectSelector.RANDOM){
            object = objects.get(random(objects.size()-1));
        }

        if (object.interact(interaction)) {
            Sleep.sleepUntil(() -> getInventory().contains(itemName) && !myPlayer().isAnimating(), 15000);
        }
    }

    private void takeItemFromGround(WalkType useForcedWalkType) {
        GroundItem item = getGroundItems().closest(itemName);
        if (item == null) {
            doWalk(useForcedWalkType);
            return;
        }
        if (item.interact(interaction)) {
            Sleep.sleepUntil(() -> getInventory().contains(itemName), 8000);
        }
    }

    private void doWalk(WalkType useForcedWalkType) {
        if (area != null && !area.contains(myPosition())) {
            if(path != null && useForcedWalkType == WalkType.PATH){
                getWalking().walkPath(path);
            }else{
                getWalking().webWalk(area);
            }
            return;
        } else {
            log(String.format("Could not find any Objects with name '%s'", objectName));
            setFailed();
            return;
        }
    }

    enum WalkType {
        PATH,
        WEBWALK
    }

    enum ObjectSelector{
        CLOSEST,
        RANDOM
    }
}
