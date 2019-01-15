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
    private String entityName;
    private final String interaction;
    private Area area;
    private List<Position> path;
    private EntitySelector entitySelector;

    /*
        Item from Ground Constructor
     */
    public ItemCompleter(final String itemName){
        this.itemName = itemName;
        this.interaction = "Take";
        this.entitySelector = EntitySelector.CLOSEST;
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
        Item from Entity Constructor
     */
    public ItemCompleter(final String entityName, final String itemName, final String interaction){
        this.itemName = itemName;
        this.entityName = entityName;
        this.interaction = interaction;
        this.entitySelector = EntitySelector.CLOSEST;
    }

    public ItemCompleter(final String entityName, final String itemName, final String interaction, final EntitySelector entitySelector){
        this(entityName, itemName, interaction);
        this.entitySelector = entitySelector;
    }

    public ItemCompleter(final String entityName, final String itemName, final String interaction, final Area area){
        this(entityName, itemName, interaction);
        this.area = area;
    }

    public ItemCompleter(final String entityName, final String itemName, final String interaction, final Area area, final EntitySelector entitySelector){
        this(entityName, itemName, interaction, entitySelector);
        this.area = area;
    }

    public ItemCompleter(final String entityName, final String itemName, final String interaction, final Area area, final List<Position> path){
        this(entityName, itemName, interaction, area);
        this.path = path;
    }

    public ItemCompleter(final String entityName, final String itemName, final String interaction, final Area area, final List<Position> path, final EntitySelector entitySelector){
        this(entityName, itemName, interaction, area, entitySelector);
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
        if(entityName != null){
            takeItemFromEntity(useForcedWalkType);
        }else{
            takeItemFromGround(useForcedWalkType);
        }
    }

    private void takeItemFromEntity(WalkType useForcedWalkType){
        RS2Object object = getObjects().closest(o -> o.getName().equals(entityName) && o.hasAction(interaction));
        NPC npc = getNpcs().closest(o -> o.getName().equals(entityName) && o.hasAction(interaction));

        if (object == null && npc == null) {
            doWalk(useForcedWalkType);
            return;
        }

        if(object != null){
            takeItemFromObject(object);
        }else{
            takeItemFromNPC(npc);
        }
    }

    private void takeItemFromObject(RS2Object object){
        RS2Object interactObject = object;
        if(entitySelector == EntitySelector.RANDOM){
            List<RS2Object> objects = getObjects().filter(o -> o.getName().equals(entityName) && o.hasAction(interaction));

            if(objects.isEmpty()){
                log(String.format("[Item-%s] Object %s was found but now not in the List Pos[%s,%s,%s]",
                        itemName,
                        object.getName(),
                        object.getX(),
                        object.getY(),
                        object.getZ()
                ));
                return;
            }

            interactObject = objects.get(random(objects.size()-1));
        }

        if (interactObject.interact(interaction)) {
            log(String.format("[Item-%s] Interact with %s Pos[%s,%s,%s]",
                    itemName,
                    interactObject.getName(),
                    interactObject.getX(),
                    interactObject.getY(),
                    interactObject.getZ()
            ));
            Sleep.sleepUntil(() -> !myPlayer().isMoving(), 15000 );
            Sleep.sleepUntil(() -> getInventory().contains(itemName) && !myPlayer().isAnimating(), 15000);
        }
    }

    private void takeItemFromNPC(NPC npc){
        NPC interactNPC = npc;
        if(entitySelector == EntitySelector.RANDOM){
            List<NPC> npcs = getNpcs().filter(o -> o.getName().equals(entityName) && o.hasAction(interaction));

            if(npcs.isEmpty()){
                log(String.format("[Item-%s] NPC %s was found but now not in the List Pos[%s,%s,%s]",
                        itemName,
                        interactNPC.getName(),
                        interactNPC.getX(),
                        interactNPC.getY(),
                        interactNPC.getZ()
                ));
                return;
            }

            interactNPC = npcs.get(random(npcs.size()-1));
        }

        if (interactNPC.interact(interaction)) {
            log(String.format("[Item-%s] Interact with %s Pos[%s,%s,%s]",
                    itemName,
                    interactNPC.getName(),
                    interactNPC.getX(),
                    interactNPC.getY(),
                    interactNPC.getZ()
            ));
            Sleep.sleepUntil(() -> !myPlayer().isMoving(), 15000 );
            Sleep.sleepUntil(() -> getInventory().contains(itemName) && !myPlayer().isAnimating(), 15000);
        }
    }

    private void takeItemFromGround(WalkType useForcedWalkType) {
        GroundItem item = getGroundItems().closest(itemName);
        if (item == null || !area.contains(item)) {
            doWalk(useForcedWalkType);
            return;
        }

        if (item.interact(interaction)) {
            log(String.format("[Item-%s] Interact with Pos[%s,%s,%s]",
                    itemName,
                    item.getX(),
                    item.getY(),
                    item.getZ()
            ));
            Sleep.sleepUntil(() -> getInventory().contains(itemName), 8000);
        }
    }

    private void doWalk(WalkType useForcedWalkType) {
        if (area != null && !area.contains(myPosition())) {
            if(path != null && useForcedWalkType == WalkType.PATH){
                log(String.format("[Item-%s] Using Path moving towards Pos[%s,%s,%s]",
                        itemName,
                        path.get(path.size()-1).getX(),
                        path.get(path.size()-1).getY(),
                        path.get(path.size()-1).getZ()
                ));
                getWalking().walkPath(path);
            }else{
                Position rnd = area.getRandomPosition();
                log(String.format("[Item-%s] Using Webwalk moving towards Pos[%s,%s,%s]",
                        itemName,
                        rnd.getX(),
                        rnd.getY(),
                        rnd.getZ()
                ));
                getWalking().webWalk(area);
            }
        } else {
            log(String.format("[Item-%s] Failed to walk", itemName));
            setFailed();
        }
    }

    enum WalkType {
        PATH,
        WEBWALK
    }

    enum EntitySelector {
        CLOSEST,
        RANDOM
    }
}
