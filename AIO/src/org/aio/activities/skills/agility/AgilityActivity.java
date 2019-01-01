package org.aio.activities.skills.agility;

import org.aio.activities.activity.Activity;
import org.aio.activities.activity.ActivityType;
import org.aio.activities.banking.Banking;
import org.aio.util.Executable;
import org.aio.util.Sleep;
import org.osbot.rs07.api.model.GroundItem;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.event.WalkingEvent;
import org.osbot.rs07.event.WebWalkEvent;
import org.osbot.rs07.utility.Condition;

import java.util.Arrays;
import java.util.LinkedList;

public class AgilityActivity extends Activity {

    private final AgilityCourse agilityCourse;
    private Executable bankNode;
    private LinkedList<CoursePart> course;
    private int enableRunEnergyAmount;

    public AgilityActivity(final AgilityCourse agilityCourse) {
        super(ActivityType.AGILITY);
        this.agilityCourse = agilityCourse;
    }

    @Override
    public void onStart() {
        bankNode = new AgilityBank();
        bankNode.exchangeContext(getBot());
        enableRunEnergyAmount = random(25, 45);
    }

    @Override
    public boolean canExit() {
        return course.peek() == agilityCourse.COURSE_PARTS[0];
    }

    @Override
    public void runActivity() throws InterruptedException {
        if (getInventory().isEmptyExcept("Mark of grace") || myPosition().getZ() > 0) {
            if (getBank() != null && getBank().isOpen()) {
                getBank().close();
                return;
            }

            if (!getSettings().isRunning() && getSettings().getRunEnergy() >= enableRunEnergyAmount) {
                getSettings().setRunning(true);
                enableRunEnergyAmount = random(25, 45);
            }

            GroundItem markOfGrace = getGrace();

            if (markOfGrace != null && (getInventory().contains("Mark of grace") || !getInventory().isFull())) {
                takeItem(markOfGrace);
                return;
            }

            if (course == null || course.isEmpty() || !course.peek().activeArea.contains(myPosition())) {
                course = getCourse();
            }

            if (course.peek() == agilityCourse.COURSE_PARTS[0] && !agilityCourse.COURSE_PARTS[0].activeArea.contains(myPosition())) {
                if (agilityCourse != AgilityCourse.GNOME_STRONGHOLD) {
                    getWalking().webWalk(agilityCourse.COURSE_PARTS[0].activeArea);
                } else if (talkingToFemi()) {
                    getDialogues().completeDialogue("Okay then.");
                } else if (!myPlayer().isMoving() && !myPlayer().isAnimating()) {
                    // The player may need to complete a dialog before being able to access the Gnome Stronghold
                    WebWalkEvent webWalkEvent = new WebWalkEvent(agilityCourse.COURSE_PARTS[0].activeArea);
                    webWalkEvent.setBreakCondition(new Condition() {
                        @Override
                        public boolean evaluate() {
                            return talkingToFemi();
                        }
                    });
                    execute(webWalkEvent);
                }
                return;
            }

            RS2Object obstacle = getObstacle();

            if ((obstacle == null || getMap().distance(obstacle) > 10) && course.peek().pathToArea != null) {
                WalkingEvent walkingEvent = new WalkingEvent();
                walkingEvent.setPath(new LinkedList<>(Arrays.asList(course.peek().pathToArea)));
                walkingEvent.setMiniMapDistanceThreshold(10);
                walkingEvent.setBreakCondition(new Condition() {
                    @Override
                    public boolean evaluate() {
                        return getObstacle() != null && getMap().distance(getObstacle()) < 10;
                    }
                });
                execute(walkingEvent);
                if (walkingEvent.hasFailed()) {
                    return;
                }
            }

            if (obstacle != null) {
                if (getCamera().toPosition(obstacle.getPosition()) && obstacle.interact(course.peek().obstacle.ACTION)) {
                    long agilityXP = getSkills().getExperience(Skill.AGILITY);
                    int zPos = myPosition().getZ();

                    Sleep.sleepUntil(() -> getSkills().getExperience(Skill.AGILITY) > agilityXP || (zPos > 0 && myPosition().getZ() == 0), 12_000, 600);

                    if (!course.peek().activeArea.contains(myPosition())) {
                        course.add(course.removeFirst());
                    }
                } else {
                    log("Interaction got stuck, moving camera");
                    getCamera().moveYaw(getCamera().getYawAngle() - random(20, 50));
                }
            } else {
                log("Could not find the next obstacle");
            }
        } else {
            bankNode.run();
        }
    }

    private boolean talkingToFemi() {
        return getDialogues().inDialogue() && getNpcs().closest("Femi") != null;
    }

    private RS2Object getObstacle() {
        return getObjects().closest(obj -> {
            if (!obj.getName().equals(course.peek().obstacle.NAME)) {
                return false;
            }

            if (!obj.hasAction(course.peek().obstacle.ACTION)) {
                return false;
            }

            if (course.peek().obstaclePosition != null && !obj.getPosition().equals(course.peek().obstaclePosition)) {
                return false;
            }

            return true;
        });
    }

    private GroundItem getGrace() {
        return getGroundItems().closest(item -> item.getName().equals("Mark of grace") && getMap().canReach(item.getPosition()));
    }

    public boolean takeItem(final GroundItem groundItem) {
        long invAmount = getInventory().getAmount(groundItem.getName());
        if (groundItem.interact("Take")) {
            Sleep.sleepUntil(() -> getInventory().getAmount(groundItem.getName()) > invAmount || !groundItem.exists(), 5000);
        }
        return getInventory().getAmount(groundItem.getName()) > invAmount;
    }

    private LinkedList<CoursePart> getCourse() {
        LinkedList<CoursePart> course = new LinkedList<>();
        for (int i = 0; i < agilityCourse.COURSE_PARTS.length; i++) {
            if (agilityCourse.COURSE_PARTS[i].activeArea.contains(myPosition())) {
                course.add(agilityCourse.COURSE_PARTS[i]);
                course.addAll(Arrays.asList(agilityCourse.COURSE_PARTS).subList(i + 1, agilityCourse.COURSE_PARTS.length));
                course.addAll(Arrays.asList(agilityCourse.COURSE_PARTS).subList(0, i));
                break;
            }
        }
        if (course.isEmpty()) {
            return new LinkedList<>(Arrays.asList(agilityCourse.COURSE_PARTS));
        }
        return course;
    }

    private class AgilityBank extends Banking {
        @Override
        public void bank() {
            getBank().depositAll();
        }
    }
}
