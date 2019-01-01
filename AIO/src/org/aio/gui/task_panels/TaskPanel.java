package org.aio.gui.task_panels;

import org.aio.gui.JSONSerializable;
import org.aio.tasks.task.Task;

import javax.swing.*;

public interface TaskPanel extends JSONSerializable {
    Task toTask();
    JPanel getPanel();
}
