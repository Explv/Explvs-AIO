package org.aio.activities.activity;

import org.aio.util.Executable;

public abstract class Activity extends Executable {

    /**
     * Current activity status to be displayed in the GUI
     */
    private String status = "";

    /**
     * Update the activity status
     * @param status to set
     */
    protected void setStatus(String status){ this.status = status; }

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

    public boolean isComplete = false;

    public String toString() {
        return activityType != null ? String.format("%s : %s", activityType.toString(), status) : "";
    }
}
