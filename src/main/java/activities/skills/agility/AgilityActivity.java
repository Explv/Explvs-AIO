package activities.skills.agility;

import activities.activity.Activity;
import activities.activity.ActivityType;
import activities.banking.DepositAllBanking;
import activities.banking.ItemReqBanking;
import org.osbot.rs07.api.model.GroundItem;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.event.*;
import org.osbot.rs07.utility.Condition;
import util.executable.Executable;
import util.Sleep;
import util.executable.ExecutionFailedException;
import util.item_requirement.ItemReq;

import java.awt.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.function.BooleanSupplier;

public class AgilityActivity extends Activity {

    private static final String MARK = "Mark of grace";

    private final AgilityCourse agilityCourse;
    private final CoursePart firstCoursePart;
    private final Executable depositAllBanking = new DepositAllBanking();
    private LinkedList<CoursePart> course;
    private int enableRunEnergyAmount;
    private RS2Object currentObstacle;
    private GroundItem markOfGrace;

    public AgilityActivity(final AgilityCourse agilityCourse) {
        super(ActivityType.AGILITY);
        this.agilityCourse = agilityCourse;
        this.firstCoursePart = agilityCourse.COURSE_PARTS[0];
    }

    @Override
    public void onStart() {
        enableRunEnergyAmount = random(25, 45);
    }

    @Override
    public boolean canExit() {
        return course == null || course.peek() == agilityCourse.COURSE_PARTS[0];
    }

    @Override
    public void runActivity() throws InterruptedException {
        if (myPosition().getZ() == 0) {
            if (!getInventory().isEmptyExcept(MARK)) {
                execute(depositAllBanking);
                return;
            }
        }

        if (!getSettings().isRunning() && getSettings().getRunEnergy() >= enableRunEnergyAmount) {
            getSettings().setRunning(true);
            enableRunEnergyAmount = random(25, 45);
        }

        markOfGrace = findMarkOfGrace();

        if (markOfGrace != null && getGroundItems().canTakeStackableItem(MARK)) {
            getGroundItems().take(markOfGrace, false);
            return;
        }

        if (course == null || course.isEmpty() || !course.peek().activeArea.contains(myPosition())) {
            course = buildCourse();
        }

        CoursePart currentCoursePart = getCurrentCoursePart();

        currentObstacle = getObstacle();

        // If this is the first course part, and we are not within range
        // we need to walk there using web walking
        if (currentCoursePart == firstCoursePart && !getInteractionHelper().canInteract(currentObstacle)) {
            walkToFirstObstacle();
            return;
        }

        // Make sure the camera pitch is at max, so that we can see as much of the course
        // as possible
        if (!getCamera().isAtTop()) {
            getCamera().toTop();
        }

        // Make sure the camera is zoomed out as much as possible
        if (!getCamera().isZoomedOut()) {
            getCamera().zoomOut();
            return;
        } else if (getTabs().getOpen() != Tab.INVENTORY) {
            getTabs().open(Tab.INVENTORY);
        }

        // When the obstacle is null, we need to walk to it
        if (currentObstacle == null) {
            walkToNextObstacle();
        } else {
            if (!currentObstacle.isVisible()) {
                // If the obstacle is within range, but is not visible, move the camera yaw
                // until it is.
                if (getMap().distance(currentObstacle) < 10) {
                    getCamera().moveYawUntilObjectVisible(this::getObstacle);
                } else {
                    walkToNextObstacle();
                }
            } else if (getInteractionHelper().interactNoMovement(currentObstacle)) {
                long agilityXP = getSkills().getExperience(Skill.AGILITY);
                int zPos = myPosition().getZ();

                // There are occassions where an interaction is successful
                // but the player does not start moving. So we ensure there is a timeout on this action
                Sleep.sleepUntil(() -> myPlayer().isMoving(), 1200);

                // If we're not moving, return so we can try again.
                if (!myPlayer().isMoving()) {
                    return;
                }

                // Sleep until agility xp increases, or we have descended to the ground floor
                Sleep.sleepUntil(() -> getSkills().getExperience(Skill.AGILITY) > agilityXP || (zPos > 0 && myPosition().getZ() == 0), 12_000, 600);

                // Load the next course part
                if (!currentCoursePart.activeArea.contains(myPosition())) {
                    course.add(course.removeFirst());
                }
            }
        }
    }

    private void walkToFirstObstacle() throws InterruptedException {
        BooleanSupplier isTalkingToFemi = () -> getNpcs().isTalkingToNPC("Femi");

        // If this is the Gnome Stronghold course, we may need to
        // complete a dialogue with Femi
        if (agilityCourse == AgilityCourse.GNOME_STRONGHOLD && isTalkingToFemi.getAsBoolean()) {
            getDialogues().completeDialogue("Okay then.");
            return;
        }

        if (myPlayer().isMoving() || myPlayer().isAnimating()) {
            return;
        }

        WebWalkEvent webWalkEvent = new WebWalkEvent(agilityCourse.COURSE_PARTS[0].activeArea);
        webWalkEvent.setBreakCondition(new Condition() {
            @Override
            public boolean evaluate() {
                // End the WebWalkEvent if we are talking to Femi, otherwise
                // we may get stuck.
                if (agilityCourse == AgilityCourse.GNOME_STRONGHOLD && isTalkingToFemi.getAsBoolean()) {
                    return true;
                }

                return agilityCourse.COURSE_PARTS[0].activeArea.contains(myPosition()) ||
                        (getInteractionHelper().canInteract(getObstacle()) && getMap().distance(getObstacle()) < 5);
            }
        });
        execute(webWalkEvent).hasFinished();
    }

    private void walkToNextObstacle() {
        if (getCurrentCoursePart().pathToArea == null) {
            throw new ExecutionFailedException("Cannot walk to next obstacle, there is no path defined");
        }

        WalkingEvent walkingEvent = new WalkingEvent();
        walkingEvent.setPath(new LinkedList<>(Arrays.asList(getCurrentCoursePart().pathToArea)));
        walkingEvent.setMiniMapDistanceThreshold(100);
        walkingEvent.setOperateCamera(false);
        walkingEvent.setBreakCondition(new Condition() {
            @Override
            public boolean evaluate() {
                return getObstacle() != null &&
                        getObstacle().isVisible() &&
                        getMap().distance(getObstacle()) < 10;
            }
        });
        execute(walkingEvent);
    }

    /**
     * Returns the current obstacle to be traversed
     * @return RS2Object for the current obstacle
     */
    private RS2Object getObstacle() {
        CoursePart currentCoursePart = course.peek();

        if (currentCoursePart == null) {
            throw new ExecutionFailedException("Current course part is null");
        }

        return getObjects().closest(obj -> {
            if (!obj.getName().equals(currentCoursePart.obstacle.NAME)) {
                return false;
            }

            if (!obj.hasAction(currentCoursePart.obstacle.ACTION)) {
                return false;
            }

            return currentCoursePart.obstaclePosition == null ||
                   obj.getPosition().equals(currentCoursePart.obstaclePosition);
        });
    }

    /**
     * @return the current part of the course the player is on
     */
    private CoursePart getCurrentCoursePart() {
        CoursePart coursePart = course.peek();
        assert coursePart != null;
        return coursePart;
    }

    /**
     * Finds a mark of grace if it exists
     * @return GroundItem or null
     */
    private GroundItem findMarkOfGrace() {
        return getGroundItems().closest(item ->
                item.getName().equals(MARK) &&
                getMap().canReach(item.getPosition())
        );
    }

    /**
     * Constructs a LinkedList<CoursePart> in the order of which
     * they should be traversed.
     * @return Linked list of course parts.
     */
    private LinkedList<CoursePart> buildCourse() {
        LinkedList<CoursePart> course = new LinkedList<>();

        for (int i = 0; i < agilityCourse.COURSE_PARTS.length; i++) {
            // If the player is standing in this course part, then this is the course part
            // the course will start from.
            if (agilityCourse.COURSE_PARTS[i].activeArea.contains(myPosition())) {
                course.add(agilityCourse.COURSE_PARTS[i]);
                course.addAll(Arrays.asList(agilityCourse.COURSE_PARTS).subList(i + 1, agilityCourse.COURSE_PARTS.length));
                course.addAll(Arrays.asList(agilityCourse.COURSE_PARTS).subList(0, i));
                return course;
            }
        }

        // The player is not standing in any course parts, so we start from the
        // beginning of the course.
        return new LinkedList<>(Arrays.asList(agilityCourse.COURSE_PARTS));
    }

    @Override
    public void onPaint(Graphics2D graphics) {
        Color prevColor = graphics.getColor();

        if (currentObstacle != null) {
            graphics.setColor(Color.GREEN);
            getGraphics().drawEntity(currentObstacle, graphics);
        }

        if (markOfGrace != null) {
            graphics.setColor(Color.RED);
            getGraphics().drawEntity(markOfGrace, graphics);
        }

        graphics.setColor(prevColor);
    }

    @Override
    public AgilityActivity copy() {
        return new AgilityActivity(agilityCourse);
    }
}
