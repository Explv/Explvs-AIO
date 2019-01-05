package org.aio.script;

import org.aio.gui.Gui;
import org.aio.gui.conf_man.ConfigManager;
import org.aio.gui.dialogs.NewVersionDialog;
import org.aio.gui.utils.EventDispatchThreadRunner;
import org.aio.paint.MouseTrail;
import org.aio.paint.Paint;
import org.aio.tasks.break_task.CustomBreakManager;
import org.aio.tasks.loop_task.LoopTask;
import org.aio.tasks.Task;
import org.aio.tasks.TaskType;
import org.aio.util.SkillTracker;
import org.aio.util.event.ToggleShiftDropEvent;
import org.json.simple.JSONObject;
import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

import java.awt.*;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

@ScriptManifest(author = "Explv", name = "Explv's AIO v2.3", info = "AIO", version = 2.3, logo = "http://i.imgur.com/58Zz0fb.png")
public class AIO extends Script {

    private Gui gui;
    private Paint paint;
    private MouseTrail mouseTrail;
    private Queue<Task> tasks = new LinkedList<>();
    private Task currentTask;
    private SkillTracker skillTracker;

    // Loop tracking
    private int loopIndex; // Our index within the current loop
    private int loopIndexStart;
    private int loopIndexEnd;
    private int numLoopIterations; // Max iterations of the loop (-1 == forever)
    private boolean isLooping; // Whether or not we are currently looping

    // The tasks that are part of the current loop queue
    private Queue<Task> loopTasksCurrentQueue;


    @Override
    public void onStart() throws InterruptedException {
        VersionChecker versionChecker = new VersionChecker(Double.toString(getVersion()));

        if (!versionChecker.updateIsIgnored() && !versionChecker.isUpToDate()) {
            try {
                EventDispatchThreadRunner.runOnDispatchThread(
                        () -> {
                            int selectedOption = NewVersionDialog.showNewVersionDialog(getBot().getBotPanel());

                            if (selectedOption == 0) {
                                versionChecker.ignoreUpdate();
                            }
                        },
                        true
                );
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        if (getParameters() != null && !getParameters().trim().isEmpty()) {
            loadTasksFromCLI();
        } else {
            loadTasksFromGUI();
        }
        if (tasks.isEmpty()) {
            stop(false);
            return;
        }
        skillTracker = new SkillTracker(getSkills());
        paint = new Paint(getBot(), skillTracker);
        getBot().addPainter(paint);
        mouseTrail = new MouseTrail(getMouse(), 20, Color.CYAN);
        getBot().addPainter(mouseTrail);

        if (tasks.stream().anyMatch(task -> task.getTaskType() == TaskType.BREAK)) {
            CustomBreakManager customBreakManager = new CustomBreakManager();
            customBreakManager.exchangeContext(getBot());
            getBot().getRandomExecutor().overrideOSBotRandom(customBreakManager);
        }
    }

    /**
     * Load the task list from the command line
     *
     * Note: Does not currently support looping
     */
    private void loadTasksFromCLI() {
        String parameter = getParameters().trim();

        File configFile = Paths.get(getDirectoryData(), parameter).toFile();

        if (!configFile.exists()) {
            log("Invalid config file: " + parameter);
            return;
        }

        ConfigManager configManager = new ConfigManager();
        Optional<JSONObject> tasksJSON = configManager.readConfig(configFile);

        if (!tasksJSON.isPresent()) {
            log("Failed to load config file: " + parameter);
            return;
        }

        tasks = configManager.getTasksFromJSON(tasksJSON.get());
    }

    /**
     * Load the task list from the GUI
     *
     * @throws InterruptedException
     */
    private void loadTasksFromGUI() throws InterruptedException {
        try {
            EventDispatchThreadRunner.runOnDispatchThread(() -> {
                gui = new Gui();
                gui.open();
            }, true);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            log("Failed to create GUI");
            return;
        }
        if (!gui.isStarted()) {
            return;
        }

        tasks = gui.getTasksAsQueue();
    }

    @Override
    public int onLoop() throws InterruptedException {
        if (isComplete()) {
            stop(true);
        } else if (!Tab.SETTINGS.isDisabled(getBot()) && !getSettings().isShiftDropActive()) {
            execute(new ToggleShiftDropEvent());
        } else {
            executeTasks();
        }
        return random(200, 300);
    }

    private boolean isComplete() {
        return !isLooping && tasks.isEmpty() && (currentTask == null || currentTask.hasFailed() || currentTask.isComplete());
    }

    private void executeTasks() throws InterruptedException {
        if (currentTask == null || (currentTask.isComplete() && currentTask.canExit()) || currentTask.hasFailed()) {
            // Determine task based on looping logic
            if (isLooping) {
                log(String.format("In loop on iteration %d", loopIndex + 2)); // +2, (1 for first step before loop, +1 for 0 based index)

                currentTask = loopTasksCurrentQueue.poll();

                // Ended this loop iteration!
                if (currentTask == null){
                    loopIndex++;

                    // Loop is done, move on
                    if (numLoopIterations != -1 && loopIndex >= numLoopIterations) {
                        isLooping = false;

                        currentTask = tasks.poll();
                    // Restart the loop
                    } else {
                        loopTasksCurrentQueue = getLoopTaskQueue();
                        currentTask = loopTasksCurrentQueue.poll();
                    }
                }

            // Pull non-loop task
            } else {
                currentTask = tasks.poll();
            }

            // Looping can cause this task to be null, short circuit
            if (currentTask == null){
                return;
            }

            // Handle loops
            if (currentTask instanceof LoopTask){
                log("Detected loop, setting up");

                if (gui != null) {
                    // Setup our loop
                    loopIndex = 0;
                    numLoopIterations = ((LoopTask) currentTask).numIterations - 1; // -1 because we already performed the op once

                    // Determine which tasks to execut
                    int taskCount = ((LoopTask) currentTask).taskCount;
                    loopIndexEnd = currentTask.getExecutionOrder();
                    loopIndexStart = loopIndexEnd - taskCount;

                    log(String.format("Will loop for %d iterations starting from task %d and ending at task %d",
                            numLoopIterations, loopIndexStart, loopIndexEnd));

                    loopTasksCurrentQueue = getLoopTaskQueue();

                    // Pull the first task off the queue, start the loop
                    currentTask = loopTasksCurrentQueue.poll();
                    isLooping = true;
                } else {
                    // Skip loops in the CLI mode for now
                    currentTask = tasks.poll();
                }

            }

            paint.setCurrentTask(currentTask);
            currentTask.exchangeContext(getBot());
            currentTask.onStart();
            skillTracker.stopAll();
            if (currentTask.getActivity() != null &&
                    currentTask.getActivity().getActivityType() != null &&
                    currentTask.getActivity().getActivityType().gainedXPSkills != null) {
                skillTracker.start(currentTask.getActivity().getActivityType().gainedXPSkills);
            }
        } else {
            currentTask.run();
        }
    }

    /**
     * Given the start and end indices, pull a new task queue for the current loop
     *
     * @return a queue of tasks
     */
    private Queue<Task> getLoopTaskQueue(){
        ArrayList<Task> allOrderedTasks = gui.getTasksAsList();

        Queue<Task> taskQueue = new LinkedList<>();
        for(int i = loopIndexStart; i < loopIndexEnd; i++){
            taskQueue.add(allOrderedTasks.get(i));
        }

        return taskQueue;
    }

    @Override
    public void pause() {
        if (paint != null) {
            paint.pause();
        }
        if (skillTracker != null) {
            skillTracker.pauseAll();
        }
    }

    @Override
    public void resume() {
        if (paint != null) {
            paint.resume();
        }
        if (skillTracker != null) {
            skillTracker.resumeAll();
        }
    }

    @Override
    public void onExit() {
        if (gui != null && gui.isOpen()) {
            gui.close();
        }
        if (paint != null) {
            getBot().removePainter(paint);
        }
        if (mouseTrail != null) {
            getBot().removePainter(mouseTrail);
        }
    }
}
