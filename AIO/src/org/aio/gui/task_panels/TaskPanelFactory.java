package org.aio.gui.task_panels;

import org.aio.tasks.TaskType;

public class TaskPanelFactory {
    public static TaskPanel createTaskPanel(final TaskType taskType) {
        switch (taskType) {
            case RESOURCE:
                return new ResourceTaskPanel();
            case LEVEL:
                return new LevelTaskPanel();
            case LOOP:
                return new LoopTaskPanel();
            case TIMED:
                return new TimedTaskPanel();
            case QUEST:
                return new QuestTaskPanel();
            case GRAND_EXCHANGE:
                return new GETaskPanel();
            case BREAK:
                return new BreakTaskPanel();
            default:
                return null;
        }
    }
}
