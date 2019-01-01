package org.aio.activities.activity;

import org.aio.util.Executable;

public abstract class Activity extends Executable {

    private final ActivityType activityType;

    public Activity(final ActivityType activityType){
        this.activityType = activityType;
    }

    public final ActivityType getActivityType() {
        return activityType;
    }

    @Override
    public void run() throws InterruptedException {
        runActivity();
    }

    public abstract void runActivity() throws InterruptedException;

    public String toString() {
        return activityType != null ? activityType.toString() : "";
    }
}
