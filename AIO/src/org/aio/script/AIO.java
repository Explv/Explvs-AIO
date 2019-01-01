package org.aio.script;

import org.aio.gui.Gui;
import org.aio.gui.conf_man.ConfigManager;
import org.aio.paint.MouseTrail;
import org.aio.paint.Paint;
import org.aio.tasks.break_task.CustomBreakManager;
import org.aio.tasks.task.Task;
import org.aio.tasks.task.TaskType;
import org.aio.util.SkillTracker;
import org.aio.util.event.ToggleShiftDropEvent;
import org.json.simple.JSONObject;
import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

@ScriptManifest(author = "Explv", name = "Explv's AIO v1.4", info = "AIO", version = 1.4, logo = "http://i.imgur.com/58Zz0fb.png")
public class AIO extends Script {

    private Gui gui;
    private Paint paint;
    private MouseTrail mouseTrail;
    private Queue<Task> tasks = new LinkedList<>();
    private Task currentTask;
    private SkillTracker skillTracker;

    @Override
    public void onStart() throws InterruptedException {
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

    private void loadTasksFromGUI() throws InterruptedException {
        try {
            if (SwingUtilities.isEventDispatchThread()) {
                gui = new Gui();
                gui.open();
            } else {
                SwingUtilities.invokeAndWait(() -> {
                    gui = new Gui();
                    gui.open();
                });
            }
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            log("Failed to create GUI");
            return;
        }
        if (!gui.isStarted()) {
            return;
        }
        tasks = gui.getTasks();
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
        return tasks.isEmpty() && (currentTask == null || currentTask.hasFailed() || currentTask.isComplete());
    }

    private void executeTasks() throws InterruptedException {
        if (currentTask == null || (currentTask.isComplete() && currentTask.canExit()) || currentTask.hasFailed()) {
            currentTask = tasks.poll();
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
    public void onExit() throws InterruptedException {
        if (gui != null) {
            try {
                SwingUtilities.invokeAndWait(() -> gui.close());
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        if (paint != null) {
            getBot().removePainter(paint);
        }
        if (mouseTrail != null) {
            getBot().removePainter(mouseTrail);
        }
    }
}
