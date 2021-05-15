package script;

import gui.Gui;
import gui.conf_man.ConfigManager;
import gui.dialogs.NewVersionDialog;
import gui.utils.EventDispatchThreadRunner;
import org.json.simple.JSONObject;
import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;
import paint.MouseTrail;
import paint.Paint;
import tasks.Task;
import tasks.TutorialIslandTask;
import tasks.task_executor.TaskExecutor;
import util.custom_method_provider.*;
import util.event.ConfigureClientEvent;

import java.awt.*;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ScriptManifest(author = "Explv", name = "Explv's AIO " + AIO.VERSION, info = "AIO", version = 0, logo = "http://i.imgur.com/58Zz0fb.png")
public class AIO extends Script {

    public static final String VERSION = "v3.4.0";

    private Gui gui;
    private Paint paint;
    private MouseTrail mouseTrail;
    private TaskExecutor taskExecutor;
    CustomMethodProvider customMethodProvider = new CustomMethodProvider();

    private boolean osrsClientIsConfigured;

    @Override
    public void onStart() throws InterruptedException {
        customMethodProvider.init(bot);

        log("Current version: " + AIO.VERSION);
        log("Latest version: " + VersionChecker.getLatestVersion().orElse("not found!"));

        if (!VersionChecker.updateIsIgnored() && !VersionChecker.isUpToDate(AIO.VERSION)) {
            try {
                EventDispatchThreadRunner.runOnDispatchThread(
                        () -> {
                            int selectedOption = NewVersionDialog.showNewVersionDialog(getBot().getBotPanel());

                            if (selectedOption == 0) {
                                VersionChecker.ignoreUpdate();
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
        taskExecutor.exchangeContext(getBot(), customMethodProvider);
        taskExecutor.addTaskChangeListener((oldTask, newTask) -> {
            paint.setCurrentTask(newTask);
        });
        taskExecutor.onStart();

        paint = new Paint(getBot(), taskExecutor.getSkillTracker());
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
     *
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
        if (!getClient().isLoggedIn()) {
            return random(1200, 1800);
        } else if (!osrsClientIsConfigured && osrsClientIsConfigurable()) {
            configureOSRSClient();
            osrsClientIsConfigured = true;
        } else if (taskExecutor.isComplete()) {
            stop(true);
        } else {
            customMethodProvider.execute(taskExecutor);
        }
        return random(200, 300);
    }

    private boolean osrsClientIsConfigurable() {
        return !Tab.SETTINGS.isDisabled(getBot()) &&
                !getDialogues().isPendingContinuation() &&
                !myPlayer().isAnimating() &&
                taskExecutor.getCurrentTask() != null &&
                !(taskExecutor.getCurrentTask() instanceof TutorialIslandTask) &&
                getNpcs().closest("Lumbridge Guide") == null;
    }

    private void configureOSRSClient() throws InterruptedException {
        customMethodProvider.execute(new ConfigureClientEvent());
    }

    @Override
    public void pause() {
        if (paint != null) {
            paint.pause();
        }
        if (customMethodProvider.getSkillTracker() != null) {
            customMethodProvider.getSkillTracker().pauseAll();
        }
    }

    @Override
    public void resume() {
        if (paint != null) {
            paint.resume();
        }
        if (customMethodProvider.getSkillTracker() != null) {
            customMethodProvider.getSkillTracker().resumeAll();
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
