package tasks;

import tasks.break_task.BreakTask;
import tasks.task_executor.TaskChangeListener;
import tasks.task_executor.TaskExecutor;
import util.Copyable;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class LoopTask extends Task {

    public static final int INFINITE_ITERATIONS = -1;

    private final int taskCount;
    private final boolean randomize;

    private int numIterations;
    private long startTimeMS = -1, durationMS = -1;
    private LocalDateTime endDateTime;

    private int startTaskIndex;
    private int endTaskIndex;

    private TaskExecutor taskExecutor;
    private int currentIteration;

    private List<Task> loopTasks;

    private LoopTask(final int taskCount, final boolean randomize) {
        super(TaskType.LOOP);
        this.taskCount = taskCount;
        this.randomize = randomize;
    }

    public static LoopTask forIterations(final int taskCount, final boolean randomize, final int numIterations) {
        LoopTask loopTask = new LoopTask(taskCount, randomize);
        loopTask.numIterations = numIterations;
        return loopTask;
    }

    public static LoopTask forDuration(final int taskCount, final boolean randomize, final long durationMS) {
        LoopTask loopTask = new LoopTask(taskCount, randomize);
        loopTask.durationMS = durationMS;
        return loopTask;
    }

    public static LoopTask untilDateTime(final int taskCount, final boolean randomize, final LocalDateTime endDateTime) {
        LoopTask loopTask = new LoopTask(taskCount, randomize);
        loopTask.endDateTime = endDateTime;
        return loopTask;
    }

    public void setup(final List<Task> allTasks,
                      final List<TaskChangeListener> taskChangeListeners) {
        this.endTaskIndex = getExecutionOrder();
        this.startTaskIndex = this.endTaskIndex - taskCount;

        loopTasks = getLoopTasks(allTasks);

        if (randomize) {
            randomizeTasks(loopTasks);
            log("Randomized tasks");
        }

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

    private void randomizeTasks(final List<Task> tasks) {
        // Randomly shuffle any tasks that are not break tasks
        randomizeTasks(tasks, task -> !(task instanceof BreakTask));

        // Then randomly shuffle all break tasks
        randomizeTasks(tasks, task -> task instanceof BreakTask);
    }

    /**
     * Randomizes the order of tasks in a list of tasks
     * Only tasks that match the taskPredicate will be re-ordered
     * @param tasks List if tasks to be randomized
     * @param taskPredicate A task predicate to determine candidates for re-ordering
     */
    private void randomizeTasks(final List<Task> tasks, final Predicate<Task> taskPredicate) {
        List<Task> breakTasks = tasks.stream()
                .filter(taskPredicate)
                .collect(Collectors.toList());

        Collections.shuffle(breakTasks);

        Queue<Task> randomBreakTaskQueue = new LinkedList<>(breakTasks);

        for (int i = 0; i < tasks.size(); i ++) {
            if (taskPredicate.test(tasks.get(i))) {
                tasks.set(i, randomBreakTaskQueue.poll());
            }
        }
    }

    @Override
    public boolean canExit() {
        return true;
    }

    @Override
    public Task copy() {
        if (endDateTime != null) {
            return LoopTask.untilDateTime(taskCount, randomize, endDateTime);
        } else if (durationMS != -1) {
            return LoopTask.forDuration(taskCount, randomize, durationMS);
        }
        return LoopTask.forIterations(taskCount, randomize, numIterations);
    }

    @Override
    public String toString() {
        return String.format("Loop Task: %d iterations", numIterations);
    }
}
