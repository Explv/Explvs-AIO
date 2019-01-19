package org.aio.gui.task_panels;

import org.aio.gui.utils.DurationPanel;
import org.aio.tasks.Task;
import org.aio.tasks.TaskType;
import org.aio.tasks.break_task.BreakTask;
import org.json.simple.JSONObject;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.concurrent.TimeUnit;

public class BreakTaskPanel implements TaskPanel {

    private JPanel mainPanel;
    private DurationPanel durationPanel;
    private JCheckBox logoutCheckBox;

    BreakTaskPanel() {
        mainPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        mainPanel.setBorder(new TitledBorder(new EtchedBorder(), "Break Task"));

        durationPanel = new DurationPanel();
        mainPanel.add(durationPanel);

        logoutCheckBox = new JCheckBox("Logout");
        mainPanel.add(logoutCheckBox);
    }

    public JPanel getPanel() {
        return mainPanel;
    }

    @Override
    public Task toTask() {
        if (durationPanel.getSelectedTimeType() == DurationPanel.TimeType.DATE_TIME) {
            return new BreakTask(durationPanel.getSelectedDateTime(), logoutCheckBox.isSelected());
        }

        int durationMinutes = durationPanel.getDuration();
        long durationMS = TimeUnit.MINUTES.toMillis(durationMinutes);
        return new BreakTask(durationMS, logoutCheckBox.isSelected());
    }

    @Override
    public JSONObject toJSON() {
        JSONObject taskJSONObject = new JSONObject();
        taskJSONObject.put("type", TaskType.BREAK.name());

        JSONObject durationObj = durationPanel.toJSON();
        for (Object key: durationObj.keySet()) {
            taskJSONObject.put(key, durationObj.get(key));
        }

        taskJSONObject.put("logout", logoutCheckBox.isSelected());

        return taskJSONObject;
    }

    @Override
    public void fromJSON(final JSONObject jsonObject) {
        durationPanel.fromJSON(jsonObject);
        logoutCheckBox.setSelected((Boolean) jsonObject.get("logout"));
    }
}
