package tasks.task_executor;

import tasks.Task;

public interface TaskChangeListener {
    void taskChanged(final Task oldTask, final Task newTask);
}
