package tasks;

import activities.tutorial_island.TutorialIsland;

public class TutorialIslandTask extends Task {

    public TutorialIslandTask() {
        super(TaskType.TUTORIAL_ISLAND, new TutorialIsland());
    }

    @Override
    public boolean isComplete() {
        return getConfigs().get(281) == 1000 && myPlayer().isVisible();
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

    @Override
    public Task copy() {
        return new TutorialIslandTask();
    }
}
