package org.aio.script;

import org.aio.gui.Gui;
import org.aio.gui.conf_man.ConfigManager;
import org.aio.gui.dialogs.NewVersionDialog;
import org.aio.gui.utils.EventDispatchThreadRunner;
import org.aio.paint.MouseTrail;
import org.aio.paint.Paint;
import org.aio.tasks.Task;
import org.aio.tasks.task_executor.TaskExecutor;
import org.aio.util.SkillTracker;
import org.aio.util.event.EnableFixedModeEvent;
import org.aio.util.event.ToggleRoofsHiddenEvent;
import org.aio.util.event.ToggleShiftDropEvent;
import org.json.simple.JSONObject;
import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

import java.awt.*;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ScriptManifest(author = "Explv", name = "Explv's AIO v3.8", info = "AIO", version = 3.8, logo = "http://i.imgur.com/58Zz0fb.png")
public class AIO extends Script {

    private Gui gui;
    private Paint paint;
    private MouseTrail mouseTrail;
    private SkillTracker skillTracker;
    private TaskExecutor taskExecutor;

    private boolean osrsClientIsConfigured;

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

        List<Task> tasks;

        if (getParameters() != null && !getParameters().trim().isEmpty()) {
            tasks = loadTasksFromCLI();
        } else {
            tasks = loadTasksFromGUI();
        }

        if (tasks.isEmpty()) {
            log("No tasks loaded");
            stop(false);
            return;
        }

        taskExecutor = new TaskExecutor(tasks);
        taskExecutor.exchangeContext(getBot());
        taskExecutor.addTaskChangeListener((oldTask, newTask) -> {
            paint.setCurrentTask(newTask);
            skillTracker.stopAll();
            if (newTask.getActivity() != null &&
                    newTask.getActivity().getActivityType() != null &&
                    newTask.getActivity().getActivityType().gainedXPSkills != null) {
                skillTracker.start(newTask.getActivity().getActivityType().gainedXPSkills);
            }
        });
        taskExecutor.onStart();

        skillTracker = new SkillTracker(getSkills());
        paint = new Paint(getBot(), skillTracker);
        getBot().addPainter(paint);
        mouseTrail = new MouseTrail(getMouse(), 20, Color.CYAN);
        getBot().addPainter(mouseTrail);
    }

    /**
     * Load the task list from the command line
     */
    private List<Task> loadTasksFromCLI() {
        String parameter = getParameters().trim();

        File configFile = Paths.get(getDirectoryData(), parameter).toFile();

        if (!configFile.exists()) {
            log("Invalid config file: " + parameter);
            return Collections.emptyList();
        }

        ConfigManager configManager = new ConfigManager();
        Optional<JSONObject> tasksJSON = configManager.readConfig(configFile);

        if (!tasksJSON.isPresent()) {
            log("Failed to load config file: " + parameter);
            return Collections.emptyList();
        }

        return configManager.getTasksFromJSON(tasksJSON.get());
    }

    /**
     * Load the task list from the GUI
     * @throws InterruptedException
     */
    private List<Task> loadTasksFromGUI() throws InterruptedException {
        try {
            EventDispatchThreadRunner.runOnDispatchThread(() -> {
                gui = new Gui();
                gui.open();
            }, true);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            log("Failed to create GUI");
            return Collections.emptyList();
        }

        if (!gui.isStarted()) {
            return Collections.emptyList();
        }

        return gui.getTasksAsList();
    }

    @Override
    public int onLoop() throws InterruptedException {
        if (!osrsClientIsConfigured && !Tab.SETTINGS.isDisabled(getBot())) {
            osrsClientIsConfigured = configureOSRSClient();
        } else if (taskExecutor.isComplete()) {
            stop(true);
        } else {
            taskExecutor.run();
        }
        return random(200, 300);
    }

    private boolean configureOSRSClient() {
        if (!EnableFixedModeEvent.isFixedModeEnabled(getBot().getMethods())) {
            execute(new EnableFixedModeEvent());
        } else if (!getSettings().areRoofsEnabled()) {
            execute(new ToggleRoofsHiddenEvent());
        } else if (!getSettings().isShiftDropActive()) {
            execute(new ToggleShiftDropEvent());
        } else {
            return true;
        }
        return false;
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
