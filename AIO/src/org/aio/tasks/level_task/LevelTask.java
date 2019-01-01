package org.aio.tasks.level_task;

import org.aio.activities.activity.Activity;
import org.aio.tasks.task.Task;
import org.aio.tasks.task.TaskType;
import org.osbot.rs07.api.ui.Skill;

public class LevelTask extends Task {

    private final Skill skill;
    private final int targetLevel;

    public LevelTask(final Activity activity, final Skill skill, final int targetLevel) {
        super(TaskType.LEVEL, activity);
        this.skill = skill;
        this.targetLevel = targetLevel;
    }

    @Override
    public boolean isComplete() {
        return getSkills().getStatic(skill) >= targetLevel;
    }

    @Override
    public String toString() {
        return String.format("Level task: %s (%d/%d)", skill.toString(), getSkills().getStatic(skill), targetLevel);
    }
}
