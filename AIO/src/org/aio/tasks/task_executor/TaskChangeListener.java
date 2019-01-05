package org.aio.tasks.task_executor;

import org.aio.tasks.Task;

public interface TaskChangeListener {
    void taskChanged(final Task oldTask, final Task newTask);
}
