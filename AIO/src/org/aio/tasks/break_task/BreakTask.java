package org.aio.tasks.break_task;

import org.aio.tasks.Task;
import org.aio.tasks.TaskType;
import org.osbot.rs07.script.RandomEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

public class BreakTask extends Task {

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    private long duration;
    private LocalDateTime endTime;
    private String endDateStr;
    private String endTimeStr;

    private final boolean shouldLogout;
    private CustomBreakManager customBreakManager;

    public BreakTask(final long duration, final boolean shouldLogout) {
        super(TaskType.BREAK, null);
        this.duration = duration;
        this.shouldLogout = shouldLogout;
    }

    public BreakTask(final LocalDateTime endTime, final boolean shouldLogout) {
        super(TaskType.BREAK, null);
        this.endTime = endTime;
        this.shouldLogout = shouldLogout;

        endDateStr = dateFormatter.format(endTime);
        endTimeStr = timeFormatter.format(endTime);
    }

    @Override
    public void onStart() {
        customBreakManager = (CustomBreakManager) getBot().getRandomExecutor().forEvent(RandomEvent.BREAK_MANAGER);
        customBreakManager.reset();

        if (endTime != null) {
            customBreakManager.startBreaking(endTime, shouldLogout);
        } else {
            customBreakManager.startBreaking(duration, shouldLogout);
        }
    }

    @Override
    public boolean isComplete() {
        return customBreakManager.finishedBreaking();
    }

    @Override
    public String toString() {
        if(endTime != null) {
            return String.format("Break task: Ends on %s at %s", endDateStr, endTimeStr);
        }

        return String.format(
                "Break task (%s) %s",
                formatTime(duration),
                formatTime(System.currentTimeMillis() - customBreakManager.getBreakStartTime())
        );
    }

    private String formatTime(final long ms){
        long s = ms / 1000, m = s / 60, h = m / 60, d = h / 24;
        s %= 60; m %= 60; h %= 24;

        return d > 0 ? String.format("%02d:%02d:%02d:%02d", d, h, m, s) :
               h > 0 ? String.format("%02d:%02d:%02d", h, m, s) :
                       String.format("%02d:%02d", m, s);
    }

    @Override
    public Task copy() {
        return new BreakTask(duration, shouldLogout);
    }
}
