package org.aio.tasks.task_executor;

import org.aio.tasks.Task;
import org.aio.tasks.TaskType;
import org.aio.tasks.break_task.CustomBreakManager;
import org.aio.tasks.loop_task.LoopTask;
import org.aio.util.Executable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Supplier;

public final class TaskExecutor extends Executable {

    private final Supplier<List<Task>> taskSupplier;

    private final Queue<Task> tasks = new LinkedList<>();

    private final List<TaskChangeListener> taskChangeListeners = new ArrayList<>();

    private Task currentTask;

    // Loop tracking
    private int loopIndex; // Our index within the current loop
    private int loopIndexStart;
    private int loopIndexEnd;
    private int numLoopIterations; // Max iterations of the loop (-1 == forever)
    private boolean isLooping; // Whether or not we are currently looping

    // The tasks that are part of the current loop queue
    private Queue<Task> loopTasksCurrentQueue;

    public TaskExecutor(final Supplier<List<Task>> taskSupplier) {
        this.taskSupplier = taskSupplier;
        this.tasks.addAll(taskSupplier.get());
    }

    public final void addTaskChangeListener(final TaskChangeListener taskChangeListener) {
        taskChangeListeners.add(taskChangeListener);
    }

    public final Task getCurrentTask() {
        return currentTask;
    }

    public boolean isComplete() {
        return !isLooping && tasks.isEmpty() && (currentTask == null || currentTask.hasFailed() || currentTask.isComplete());
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
            // Determine task based on looping logic
            if (isLooping) {
                log(String.format("In loop on iteration %d", loopIndex + 2)); // +2, (1 for first step before loop, +1 for 0 based index)

                loadNextTask(loopTasksCurrentQueue);

                // Ended this loop iteration!
                if (currentTask == null) {
                    loopIndex++;

                    // Loop is done, move on
                    if (numLoopIterations != -1 && loopIndex >= numLoopIterations) {
                        isLooping = false;

                        loadNextTask(tasks);
                        // Restart the loop
                    } else {
                        loopTasksCurrentQueue = getLoopTaskQueue();
                        loadNextTask(loopTasksCurrentQueue);
                    }
                }

                // Pull non-loop task
            } else {
                loadNextTask(tasks);
            }

            if (currentTask == null) {
                return;
            }

            // Handle loops
            if (currentTask instanceof LoopTask) {
                log("Detected loop, setting up");

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
                loadNextTask(loopTasksCurrentQueue);
                isLooping = true;
            }
        } else {
            try {
                currentTask.run();
            } catch (NullPointerException nullPointer) {
                log("Found null pointer exception. Task failed, exiting.");

                StackTraceElement[] stack = nullPointer.getStackTrace();
                for (StackTraceElement element : stack) {
                    log(element.toString());
                }

                isLooping = false;
                currentTask = null;
                tasks.clear();
            }
        }
    }

    private void loadNextTask(Queue<Task> taskQueue) throws InterruptedException {
        Task prevTask = currentTask;
        currentTask = taskQueue.poll();

        if (currentTask == null) {
            return;
        }

        currentTask.exchangeContext(getBot());
        currentTask.onStart();

        for (final TaskChangeListener taskChangeListener : taskChangeListeners) {
            taskChangeListener.taskChanged(prevTask, currentTask);
        }
    }

    /**
     * Given the start and end indices, pull a new task queue for the current loop
     *
     * @return a queue of tasks
     */
    private Queue<Task> getLoopTaskQueue() {
        List<Task> allOrderedTasks = taskSupplier.get();

        Queue<Task> taskQueue = new LinkedList<>();
        for (int i = loopIndexStart; i < loopIndexEnd; i++) {
            taskQueue.add(allOrderedTasks.get(i));
        }

        return taskQueue;
    }
}
