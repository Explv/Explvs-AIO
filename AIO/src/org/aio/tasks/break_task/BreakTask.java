package org.aio.tasks.break_task;

import org.aio.tasks.Task;
import org.aio.tasks.TaskType;
import org.osbot.rs07.script.RandomEvent;

import java.util.concurrent.TimeUnit;

public class BreakTask extends Task {

    private final long duration;
    private final boolean shouldLogout;
    private CustomBreakManager customBreakManager;

    public BreakTask(final long duration, final boolean shouldLogout) {
        super(TaskType.BREAK, null);
        this.duration = duration;
        this.shouldLogout = shouldLogout;
    }

    @Override
    public void onStart() {
        customBreakManager = (CustomBreakManager) getBot().getRandomExecutor().forEvent(RandomEvent.BREAK_MANAGER);
        customBreakManager.startBreaking(duration, shouldLogout);
    }

    @Override
    public boolean isComplete() {
        return customBreakManager.finishedBreaking();
    }

    @Override
    public String toString() {
        long durationMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
        return String.format("Break task (%d minutes) %s", durationMinutes, formatTime(System.currentTimeMillis() - customBreakManager.getBreakStartTime()));
    }

    private String formatTime(final long ms){
        long s = ms / 1000, m = s / 60, h = m / 60, d = h / 24;
        s %= 60; m %= 60; h %= 24;

        return d > 0 ? String.format("%02d:%02d:%02d:%02d", d, h, m, s) :
                h > 0 ? String.format("%02d:%02d:%02d", h, m, s) :
                        String.format("%02d:%02d", m, s);
    }
}
