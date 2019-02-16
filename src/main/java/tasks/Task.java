package tasks;

import activities.activity.Activity;
import util.Copyable;
import util.Executable;

public abstract class Task extends Executable implements Copyable<Task> {
    private final TaskType taskType;
    protected Activity activity;
    private int executionOrder;

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

    public int getExecutionOrder() {
        return executionOrder;
    }

    public void setExecutionOrder(int executionOrder) {
        this.executionOrder = executionOrder;
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
