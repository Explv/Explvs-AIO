package org.aio.tasks.timed_task;

import org.aio.activities.activity.Activity;
import org.aio.tasks.task.Task;
import org.aio.tasks.task.TaskType;

public class TimedTask extends Task {

    private final long durationMs;
    private long startTimeMs;
    private boolean isStarted;

    public TimedTask(final Activity activity, final long durationMs){
        super(TaskType.TIMED, activity);
        this.durationMs = durationMs;
    }

    @Override
    public boolean isComplete() {
        return isStarted && (System.currentTimeMillis() - startTimeMs >= durationMs);
    }

    @Override
    public void run() throws InterruptedException {
        if(!isStarted){
            startTimeMs = System.currentTimeMillis();
            isStarted = true;
        }
        super.run();
    }

    @Override
    public String toString() {
        long runTime = System.currentTimeMillis() - startTimeMs;
        return String.format("Timed task: (%s/%s)", formatTime(runTime), formatTime(durationMs));
    }

    private String formatTime(final long ms){
        long s = ms / 1000, m = s / 60, h = m / 60, d = h / 24;
        s %= 60; m %= 60; h %= 24;

        return d > 0 ? String.format("%02d:%02d:%02d:%02d", d, h, m, s) :
                h > 0 ? String.format("%02d:%02d:%02d", h, m, s) :
                        String.format("%02d:%02d", m, s);
    }
}
