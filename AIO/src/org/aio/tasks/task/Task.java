package org.aio.tasks.task;

import org.aio.activities.activity.Activity;
import org.aio.util.Executable;

public abstract class Task extends Executable {

    protected final TaskType taskType;
    protected Activity activity;

    public Task(final TaskType taskType) {
        this.taskType = taskType;
    }

    public Task(final TaskType taskType, final Activity activity) {
        this.taskType = taskType;
        this.activity = activity;
    }

    public abstract boolean isComplete();

    public TaskType getTaskType() {
        return taskType;
    }

    public Activity getActivity() {
        return activity;
    }

    @Override
    public void onStart() throws InterruptedException {
        if (activity != null) {
            activity.exchangeContext(getBot());
            activity.onStart();
        }
    }

    @Override
    public void run() throws InterruptedException {
        if (activity != null) {
            activity.run();
            if (activity.hasFailed()) {
                setFailed();
            }
        }
    }

    @Override
    public boolean canExit() {
        return activity == null || activity.canExit();
    }

    @Override
    public void onEnd() throws InterruptedException {
        if (activity != null) {
            activity.onEnd();
        }
    }
}
