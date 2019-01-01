package org.aio.tasks.quest_task;

import org.aio.activities.activity.Activity;
import org.aio.activities.quests.Quest;
import org.aio.tasks.task.Task;
import org.aio.tasks.task.TaskType;

public class QuestTask extends Task{

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
}
