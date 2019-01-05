package org.aio.tasks;

import org.aio.tasks.task_executor.TaskChangeListener;
import org.aio.tasks.task_executor.TaskExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class LoopTask extends Task {

    public static final int INFINITE_ITERATIONS = -1;

    private int taskCount;
    private int numIterations;

    private int startTaskIndex;
    private int endTaskIndex;
    private Supplier<List<Task>> taskSupplier;

    private TaskExecutor taskExecutor;
    private int currentIteration;

    public LoopTask(int taskCount, int numIterations) {
        super(TaskType.LOOP);

        this.taskCount = taskCount;
        this.numIterations = numIterations;
    }

    public void setup(final Supplier<List<Task>> taskSupplier,
                      final List<TaskChangeListener> taskChangeListeners) {
        this.taskSupplier = taskSupplier;

        taskExecutor = new TaskExecutor(getLoopTasks(), taskSupplier);
        taskExecutor.addTaskChangeListeners(taskChangeListeners);
    }

    @Override
    public void onStart() {
        taskExecutor.exchangeContext(getBot());
        this.endTaskIndex = getExecutionOrder();
        this.startTaskIndex = this.endTaskIndex - taskCount;
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
            taskExecutor.setTasks(getLoopTasks());
        } else {
            taskExecutor.run();
        }
    }

    @Override
    public String toString() {
        return String.format("Loop Task: %d iterations", numIterations);
    }

    /**
     * Given the start and end indices, pull a new task queue for the current loop
     *
     * @return a queue of tasks
     */
    private List<Task> getLoopTasks() {
        return new ArrayList<>(taskSupplier.get().subList(startTaskIndex, endTaskIndex));
    }

    @Override
    public boolean canExit() {
        return true;
    }
}
