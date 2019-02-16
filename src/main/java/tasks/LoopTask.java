package tasks;

import tasks.task_executor.TaskChangeListener;
import tasks.task_executor.TaskExecutor;
import util.Copyable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class LoopTask extends Task {

    public static final int INFINITE_ITERATIONS = -1;

    private int taskCount;

    private int numIterations;
    private long startTimeMS = -1, durationMS = -1;
    private LocalDateTime endDateTime;

    private int startTaskIndex;
    private int endTaskIndex;

    private TaskExecutor taskExecutor;
    private int currentIteration;

    private List<Task> loopTasks;

    private LoopTask() {
        super(TaskType.LOOP);
    }

    public static LoopTask forIterations(final int taskCount, final int numIterations) {
        LoopTask loopTask = new LoopTask();
        loopTask.taskCount = taskCount;
        loopTask.numIterations = numIterations;
        return loopTask;
    }

    public static LoopTask forDuration(final int taskCount, final long durationMS) {
        LoopTask loopTask = new LoopTask();
        loopTask.taskCount = taskCount;
        loopTask.durationMS = durationMS;
        return loopTask;
    }

    public static LoopTask untilDateTime(final int taskCount, final LocalDateTime endDateTime) {
        LoopTask loopTask = new LoopTask();
        loopTask.taskCount = taskCount;
        loopTask.endDateTime = endDateTime;
        return loopTask;
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

        startTimeMS = System.currentTimeMillis();
    }

    @Override
    public boolean isComplete() {
        if (durationMS != -1) {
            return System.currentTimeMillis() - startTimeMS >= durationMS;
        } else if (endDateTime != null) {
            return LocalDateTime.now().isAfter(endDateTime);
        } else {
            return numIterations != INFINITE_ITERATIONS && currentIteration >= numIterations;
        }
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
        if (endDateTime != null) {
            return LoopTask.untilDateTime(taskCount, endDateTime);
        } else if (durationMS != -1) {
            return LoopTask.forDuration(taskCount, durationMS);
        }
        return LoopTask.forIterations(taskCount, numIterations);
    }

    @Override
    public String toString() {
        return String.format("Loop Task: %d iterations", numIterations);
    }
}
