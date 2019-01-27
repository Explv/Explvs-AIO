package org.aio.gui.task_panels;

import org.aio.gui.interfaces.JSONSerializable;
import org.aio.tasks.Task;

import javax.swing.*;

/**
 * A TaskPanel used to create tasks of `type`
 */
public interface TaskPanel extends JSONSerializable {

    Task toTask();
    JPanel getPanel();
}
