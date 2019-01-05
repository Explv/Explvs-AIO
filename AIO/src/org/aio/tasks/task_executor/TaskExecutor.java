package org.aio.tasks.task_executor;

import org.aio.tasks.Task;
import org.aio.util.Executable;

import java.util.*;

public final class TaskExecutor extends Executable {

    private final Queue<Task> tasks = new LinkedList<>();
    private final List<TaskChangeListener> taskChangeListeners = new ArrayList<>();
    private Task currentTask;

    public TaskExecutor(final Collection<Task> tasks) {
        this.tasks.addAll(tasks);
    }

    public final void addTaskChangeListener(final TaskChangeListener taskChangeListener) {
        taskChangeListeners.add(taskChangeListener);
    }

    public final Task getCurrentTask() {
        return currentTask;
    }

    public boolean isComplete() {
        return tasks.isEmpty() && (currentTask == null || currentTask.hasFailed() || currentTask.isComplete());
    }

    @Override
    public final void run() throws InterruptedException {
        if (currentTask == null || (currentTask.isComplete() && currentTask.canExit()) || currentTask.hasFailed()) {
            Task prevTask = currentTask;

            currentTask = tasks.poll();
            currentTask.exchangeContext(getBot());
            currentTask.onStart();

            for (final TaskChangeListener taskChangeListener : taskChangeListeners) {
                taskChangeListener.taskChanged(prevTask, currentTask);
            }
        } else {
            currentTask.run();
        }
    }
}
