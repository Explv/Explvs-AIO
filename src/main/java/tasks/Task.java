package tasks;

import activities.activity.Activity;
import org.osbot.rs07.canvas.paint.Painter;
import util.Copyable;
import util.executable.Executable;

import java.awt.*;

public abstract class Task extends Executable implements Copyable<Task>, Painter {
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
            activity.exchangeContext(getBot(), this);
            activity.onStart();
        }
    }

    @Override
    public void run() throws InterruptedException {
        if (activity != null) {
            execute(activity);
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

    @Override
    public void onPaint(Graphics2D graphics) {
        if (activity != null) {
            activity.onPaint(graphics);
        }
    }
}
