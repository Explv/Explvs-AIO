package org.aio.tasks;

import org.aio.tasks.task_executor.TaskChangeListener;
import org.aio.tasks.task_executor.TaskExecutor;
import org.aio.util.Copyable;

import java.util.List;
import java.util.stream.Collectors;

public class LoopTask extends Task {

    public static final int INFINITE_ITERATIONS = -1;

    private final int taskCount;
    private final int numIterations;

    private int startTaskIndex;
    private int endTaskIndex;

    private TaskExecutor taskExecutor;
    private int currentIteration;

    private List<Task> loopTasks;

    public LoopTask(final int taskCount, final int numIterations) {
        super(TaskType.LOOP);
        this.taskCount = taskCount;
        this.numIterations = numIterations;
    }

    public void setup(final List<Task> allTasks,
                      final List<TaskChangeListener> taskChangeListeners) {
        this.endTaskIndex = getExecutionOrder();
        this.startTaskIndex = this.endTaskIndex - taskCount;

        loopTasks = getLoopTasks(allTasks);
        taskExecutor = new TaskExecutor(loopTasks);
        taskExecutor.addTaskChangeListeners(taskChangeListeners);
    }

    @Override
    public void onStart() {
        taskExecutor.exchangeContext(getBot());

        log(String.format("Will loop for %d iterations starting from task %d and ending at task %d",
                numIterations, startTaskIndex, endTaskIndex));
    }

    @Override
    public boolean isComplete() {
        return numIterations != -1 && currentIteration >= numIterations;
    }

    @Override
    public void run() throws InterruptedException {
        if (taskExecutor.isComplete()) {
            currentIteration++;
            log(String.format("Loop iteration: (%d/%d)", currentIteration, numIterations));
            taskExecutor.setTaskQueue(copyTasks(loopTasks));
        } else {
            taskExecutor.run();
        }
    }

    /**
     * Given the start and end indices, pull a new task queue for the current loop
     *
     * @return a queue of tasks
     */
    private List<Task> getLoopTasks(final List<Task> allTasks) {
        return copyTasks(allTasks.subList(startTaskIndex, endTaskIndex));
    }

    private List<Task> copyTasks(final List<Task> tasks) {
        return tasks.stream()
                    .map(Copyable::copy)
                    .collect(Collectors.toList());
    }

    @Override
    public boolean canExit() {
        return true;
    }

    @Override
    public Task copy() {
        return new LoopTask(taskCount, numIterations);
    }

    @Override
    public String toString() {
        return String.format("Loop Task: %d iterations", numIterations);
    }
}
