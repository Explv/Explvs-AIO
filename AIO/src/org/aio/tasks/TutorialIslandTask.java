package org.aio.tasks;

import org.aio.activities.tutorial_island.TutorialIsland;
import org.aio.tasks.Task;
import org.aio.tasks.TaskType;

public class TutorialIslandTask extends Task {

    public TutorialIslandTask() {
        super(TaskType.TUTORIAL_ISLAND, new TutorialIsland());
    }

    @Override
    public boolean isComplete() {
        return getConfigs().get(281) == 1000;
    }

    @Override
    public void run() throws InterruptedException {
        if (getSettings().getRunEnergy() > 40) {
            getSettings().setRunning(true);
        }
        super.run();
    }

    @Override
    public String toString() {
        return "Tutorial Island task";
    }
}
