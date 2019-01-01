package org.aio.activities.skills.firemaking;

import org.aio.activities.activity.Activity;
import org.aio.activities.activity.ActivityType;
import org.aio.activities.banking.ItemReqBanking;
import org.aio.util.Executable;
import org.aio.util.Location;
import org.aio.util.Sleep;
import org.aio.util.item_requirement.ItemReq;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.GroundDecoration;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.event.Event;
import org.osbot.rs07.event.WalkingEvent;

import java.util.*;

public class FiremakingActivity extends Activity {

    private final Log log;
    private final Location location;
    private final ItemReq[] itemReqs;
    private Executable bankNode;

    public FiremakingActivity(final Log log, final Location location) {
        super(ActivityType.FIREMAKING);
        this.log = log;
        this.location = location;

        itemReqs = new ItemReq[] {
                new ItemReq("Tinderbox"),
                new ItemReq(log.NAME, 1)
        };
    }

    @Override
    public void onStart() {
        bankNode = new ItemReqBanking(itemReqs);
        bankNode.exchangeContext(getBot());
    }

    @Override
    public void runActivity() throws InterruptedException {
        if (ItemReq.hasItemRequirements(itemReqs, getInventory())) {
            if (getBank() != null && getBank().isOpen()) {
                getBank().close();
            } else if(!location.getArea().contains(myPosition())){
                getWalking().webWalk(location.getArea());
            } else{

                Queue<Position> fmPositions = getFMPositions();

                log("Longest chain:");
                for(Position position : fmPositions){
                    log(position.toString());
                }

                execute(new Event() {
                    @Override
                    public int execute() throws InterruptedException {

                        if (fmPositions.isEmpty() || !getInventory().contains(log.toString())) {
                            setFinished();
                            return 0;
                        }

                        Position nextFirePosition = fmPositions.peek();

                        /*if (getObjects().singleFilter(getObjects().getAll(), obj -> !(obj instanceof GroundDecoration), new PositionFilter<>(nextFirePosition)) != null) {
                            fmPositions.poll();
                            return 0;
                        }*/

                        if(!myPosition().equals(nextFirePosition)){
                            if (myPosition().distance(nextFirePosition) > 5) {
                                getWalking().webWalk(nextFirePosition);
                            } else {
                                WalkingEvent walkingEvent = new WalkingEvent(nextFirePosition);
                                walkingEvent.setMinDistanceThreshold(0);
                                walkingEvent.setMiniMapDistanceThreshold(0);
                                execute(walkingEvent);
                            }
                            return 0;
                        }

                        if(!"Tinderbox".equals(getInventory().getSelectedItemName())){
                            if (getInventory().getItem("Tinderbox").interact("Use")) {
                                Sleep.sleepUntil(() -> "Tinderbox".equals(getInventory().getSelectedItemName()), 1000);
                            }
                            return 0;
                        }

                        if (getInventory().getItem(log.toString()).interact()) {
                            Sleep.sleepUntil(() -> (!myPosition().equals(nextFirePosition) && !myPlayer().isMoving()), 30_000);
                            fmPositions.poll();
                        }

                        return random(100, 150);
                    }
                });
            }
        } else {
            bankNode.run();
            if (bankNode.hasFailed()) {
                setFailed();
            }
        }
    }

    private Queue<Position> getFMPositions(){
        List<Position> allPositions = location.getArea().getPositions();

        // Remove any position with an object (except ground decorations, as they can be walked on)
        for(RS2Object object : getObjects().getAll()){
            if (object instanceof GroundDecoration) {
                continue;
            }
            allPositions.removeIf(position -> object.getPosition().equals(position));
        }

        // Sort positions into rows
        HashMap<Integer, List<Position>> rows = new HashMap<>();
        for(Position position : allPositions){
            rows.putIfAbsent(position.getY(), new ArrayList<>());
            rows.get(position.getY()).add(position);
        }

        if(rows.isEmpty()) {
            return new LinkedList<>();
        }

        // Find the longest consecutive row
        Queue<Position> longestConsecutiveRow = new LinkedList<>();
        for(List<Position> row : rows.values()){

            row.sort((p1, p2) -> Integer.compare(p2.getX(), p1.getX()));

            ArrayDeque<Position> current = new ArrayDeque<>();

            for(Position position : row){
                if(current.isEmpty()){
                    current.addLast(position);
                } else if(position.getX() == current.getLast().getX() - 1){
                    current.addLast(position);
                } else if(current.size() > longestConsecutiveRow.size()){
                    longestConsecutiveRow = new LinkedList<>(current);
                    current.clear();
                } else {
                    current.clear();
                }
            }
        }

        return longestConsecutiveRow;
    }
}
