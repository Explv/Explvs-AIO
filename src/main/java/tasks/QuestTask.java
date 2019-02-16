package tasks;

import activities.activity.Activity;
import activities.quests.Quest;

public class QuestTask extends Task {

    private final Quest quest;

    public QuestTask(final Activity activity, final Quest quest) {
        super(TaskType.QUEST, activity);
        this.quest = quest;
    }

    @Override
    public boolean isComplete() {
        return quest.isComplete(getConfigs());
    }

    @Override
    public String toString() {
        return "Quest task";
    }

    @Override
    public Task copy() {
        return new QuestTask(getActivity().copy(), quest);
    }
}
