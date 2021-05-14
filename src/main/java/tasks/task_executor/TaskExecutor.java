package tasks.task_executor;

import tasks.LoopTask;
import tasks.Task;
import tasks.break_task.CustomBreakManager;
import util.executable.Executable;
import util.executable.ExecutionFailedException;

import java.util.*;
import java.util.List;

public final class TaskExecutor extends Executable {

    private final List<Task> allTasks;
    private final Queue<Task> taskQueue = new LinkedList<>();
    private final List<TaskChangeListener> taskChangeListeners = new ArrayList<>();
    private final CustomBreakManager breakManager = new CustomBreakManager();
    private long previousIdleTime;
    private long nextIdleTime;

    private Task currentTask;

    public TaskExecutor(final List<Task> tasks) {
        allTasks = tasks;
        this.taskQueue.addAll(tasks);
    }

    public Task getCurrentTask() {
        return currentTask;
    }

    public void setTaskQueue(final List<Task> taskQueue) {
        this.taskQueue.clear();
        this.taskQueue.addAll(taskQueue);
        currentTask = null;
    }

    public final void addTaskChangeListener(final TaskChangeListener taskChangeListener) {
        this.taskChangeListeners.add(taskChangeListener);
    }

    public final void addTaskChangeListeners(final Collection<TaskChangeListener> taskChangeListeners) {
        this.taskChangeListeners.addAll(taskChangeListeners);
    }

    public boolean isComplete() {
        return taskQueue.isEmpty() && (currentTask == null || currentTask.isComplete());
    }

    @Override
    public void onStart() throws InterruptedException {
        breakManager.exchangeContext(getBot());
        getBot().getRandomExecutor().overrideOSBotRandom(breakManager);

        previousIdleTime = System.currentTimeMillis();
        nextIdleTime = previousIdleTime + calculateNextIdleTime();
    }

    @Override
    public final void run() throws InterruptedException {
        if (currentTask == null || (currentTask.isComplete() && currentTask.canExit())) {
            loadNextTask(taskQueue);
        } else {
            runTask(currentTask);
        }
    }

    private void loadNextTask(Queue<Task> taskQueue) throws InterruptedException {
        if (currentTask != null) {
            getBot().removePainter(currentTask);
        }

        getSkillTracker().stopAll();

        Task prevTask = currentTask;
        currentTask = taskQueue.poll();

        if (currentTask == null) {
            return;
        }

        getBot().addPainter(currentTask);

        currentTask.exchangeContext(getBot(), this);

        if (currentTask instanceof LoopTask) {
            ((LoopTask) currentTask).setup(allTasks, taskChangeListeners);
        }

        currentTask.onStart();

        if (currentTask.getActivity() != null &&
                currentTask.getActivity().getActivityType() != null &&
                currentTask.getActivity().getActivityType().gainedXPSkills != null) {
            getSkillTracker().start(currentTask.getActivity().getActivityType().gainedXPSkills);
        }

        for (final TaskChangeListener taskChangeListener : taskChangeListeners) {
            taskChangeListener.taskChanged(prevTask, currentTask);
        }
    }

    private void runTask(final Task task) throws InterruptedException {
        try {
//            if (System.currentTimeMillis() > nextIdleTime) {
//                previousIdleTime = nextIdleTime;
//                int idleTime = random(1000 * 10, 1000 * 35);
//                log("Idling for: " + (idleTime / 1000) + "s");
//                breakManager.startBreaking(idleTime);
//                nextIdleTime = previousIdleTime + idleTime + calculateNextIdleTime();
//                log("Done idling");
//                log("Next idling in: " + (nextIdleTime - System.currentTimeMillis()) / 1000 + "s");
//            }

            execute(task);
        } catch (NullPointerException nullPointer) {
            log("Found null pointer exception. Task failed, exiting.");

            StackTraceElement[] stack = nullPointer.getStackTrace();
            for (StackTraceElement element : stack) {
                log(element.toString());
            }

            currentTask = null;
            taskQueue.clear();
        } catch (ExecutionFailedException executionFailedException) {
            log("Task execution failed due to error:");
            log(executionFailedException.getMessage());
            log("Proceeding to next task");
            loadNextTask(taskQueue);
        }
    }

    private long calculateNextIdleTime() {
        return (1000L * 60 * random(5, 25)) + random(1000, 15 * 1000);
    }
}
