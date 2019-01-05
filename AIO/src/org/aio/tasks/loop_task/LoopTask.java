package org.aio.tasks.loop_task;

import org.aio.tasks.Task;
import org.aio.tasks.TaskType;

public class LoopTask extends Task {

    public int taskCount;
    public int numIterations;

    public LoopTask(int lastXTasks, int numIterations) {
        super(TaskType.LOOP);

        taskCount = lastXTasks;
        this.numIterations = numIterations;
    }

    @Override
    public boolean isComplete() {
        return true;
    } // Not applicable, this task is never actually executed

    @Override
    public String toString() {
        return String.format("Loop Task: %d iterations", numIterations);
    }
}
