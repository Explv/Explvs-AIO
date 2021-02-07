package tasks.break_task;

import org.osbot.rs07.api.Client;
import org.osbot.rs07.script.RandomEvent;
import org.osbot.rs07.script.RandomSolver;

import java.time.LocalDateTime;

public class CustomBreakManager extends RandomSolver {

    private LocalDateTime endDateTime;

    private long breakDuration = -1;
    private long breakStartTime = -1;
    private boolean shouldLogout;

    public CustomBreakManager() {
        super(RandomEvent.BREAK_MANAGER);
    }

    public void reset() {
        endDateTime = null;
        breakDuration = -1;
        breakStartTime = -1;
        shouldLogout = false;
    }

    public void startBreaking(final long breakDuration, final boolean shouldLogout) {
        this.breakDuration = breakDuration;
        this.breakStartTime = System.currentTimeMillis();
        this.shouldLogout = shouldLogout;
    }

    public void startBreaking(final LocalDateTime endDateTime, final boolean shouldLogout) {
        this.endDateTime = endDateTime;
        this.shouldLogout = shouldLogout;
    }

    public long getBreakStartTime() {
        return breakStartTime;
    }

    @Override
    public boolean shouldActivate() {
        return (endDateTime != null || breakDuration > 0) && !finishedBreaking();
    }

    public boolean finishedBreaking() {
        if (endDateTime != null) {
            return LocalDateTime.now().isAfter(endDateTime);
        }

        return System.currentTimeMillis() - breakStartTime >= breakDuration;
    }

    @Override
    public int onLoop() throws InterruptedException {
        if (shouldLogout && getClient().getLoginState() == Client.LoginState.LOGIN_SUCCESSFUL) {
            if (getWidgets().closeOpenInterface() && getLogoutTab().logOut()) {
                return 1000;
            }
        }

        if (getMouse().isOnScreen()) {
            getMouse().moveOutsideScreen();
        }

        return 3000;
    }
}
