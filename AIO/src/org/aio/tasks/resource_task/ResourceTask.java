package org.aio.tasks.resource_task;

import org.aio.activities.activity.Activity;
import org.aio.tasks.task.Task;
import org.aio.tasks.task.TaskType;

public class ResourceTask extends Task {

    private final String resource;
    private final int targetQuantity;
    private long quantityObtained;
    private long prevInvcount;

    public ResourceTask(final Activity activity, final String resource, final int targetQuantity) {
        super(TaskType.RESOURCE, activity);
        this.resource = resource;
        this.targetQuantity = targetQuantity;
    }

    @Override
    public void onStart() throws InterruptedException {
        super.onStart();
        prevInvcount = getInventory().getAmount(resource);
    }

    @Override
    public boolean isComplete() {
        return quantityObtained >= targetQuantity;
    }

    @Override
    public void run() throws InterruptedException {
        long invCount = getInventory().getAmount(resource);
        if (invCount > prevInvcount) quantityObtained += invCount - prevInvcount;
        prevInvcount = invCount;
        super.run();
    }

    @Override
    public String toString() {
        return String.format("Resource task: %s (%d/%d)", resource, quantityObtained, targetQuantity);
    }
}
