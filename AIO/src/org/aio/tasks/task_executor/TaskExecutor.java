package org.aio.tasks.task_executor;

import org.aio.tasks.Task;
import org.aio.tasks.TaskType;
import org.aio.tasks.break_task.CustomBreakManager;
import org.aio.tasks.LoopTask;
import org.aio.util.Executable;

import java.util.*;
import java.util.function.Supplier;

public final class TaskExecutor extends Executable {

    private final Supplier<List<Task>> taskSupplier;
    private final Queue<Task> tasks = new LinkedList<>();
    private final List<TaskChangeListener> taskChangeListeners = new ArrayList<>();
    private Task currentTask;

    public TaskExecutor(final List<Task> tasks, final Supplier<List<Task>> taskSupplier) {
        this.taskSupplier = taskSupplier;
        this.tasks.addAll(tasks);
    }

    public void setTasks(final List<Task> tasks) {
        this.tasks.clear();
        this.tasks.addAll(tasks);
        currentTask = null;
    }

    public final void addTaskChangeListener(final TaskChangeListener taskChangeListener) {
        this.taskChangeListeners.add(taskChangeListener);
    }

    public final void addTaskChangeListeners(final Collection<TaskChangeListener> taskChangeListeners) {
        this.taskChangeListeners.addAll(taskChangeListeners);
    }

    public boolean isComplete() {
        return tasks.isEmpty() && (currentTask == null || currentTask.hasFailed() || currentTask.isComplete());
    }

    @Override
    public void onStart() throws InterruptedException {
        if (tasks.stream().anyMatch(task -> task.getTaskType() == TaskType.BREAK)) {
            CustomBreakManager customBreakManager = new CustomBreakManager();
            customBreakManager.exchangeContext(getBot());
            getBot().getRandomExecutor().overrideOSBotRandom(customBreakManager);
        }
    }

    @Override
    public final void run() throws InterruptedException {
        if (currentTask == null || (currentTask.isComplete() && currentTask.canExit()) || currentTask.hasFailed()) {
            loadNextTask(tasks);
        } else {
            runTask(currentTask);
        }
    }

    private void loadNextTask(Queue<Task> taskQueue) throws InterruptedException {
        Task prevTask = currentTask;
        currentTask = taskQueue.poll();

        if (currentTask == null) {
            return;
        }

        currentTask.exchangeContext(getBot());

        if (currentTask instanceof LoopTask) {
            ((LoopTask) currentTask).setup(taskSupplier, taskChangeListeners);
        }

        currentTask.onStart();

        for (final TaskChangeListener taskChangeListener : taskChangeListeners) {
            taskChangeListener.taskChanged(prevTask, currentTask);
        }
    }

    private void runTask(final Task task) throws InterruptedException {
        try {
            task.run();
        } catch (NullPointerException nullPointer) {
            log("Found null pointer exception. Task failed, exiting.");

            StackTraceElement[] stack = nullPointer.getStackTrace();
            for (StackTraceElement element : stack) {
                log(element.toString());
            }

            currentTask = null;
            tasks.clear();
        }
    }
}
