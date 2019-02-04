package org.aio.gui.task_panels;

import org.aio.gui.styled_components.StyledJCheckBox;
import org.aio.gui.styled_components.StyledJPanel;
import org.aio.gui.utils.ColourScheme;
import org.aio.gui.utils.DurationPanel;
import org.aio.tasks.Task;
import org.aio.tasks.TaskType;
import org.aio.tasks.break_task.BreakTask;
import org.json.simple.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.TimeUnit;

public class BreakTaskPanel extends TaskPanel {

    private DurationPanel durationPanel;
    private JCheckBox logoutCheckBox;

    BreakTaskPanel() {
        super(TaskType.BREAK);

        JPanel contentPanel = new StyledJPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        contentPanel.setBackground(ColourScheme.PANEL_BACKGROUND_GREY);

        durationPanel = new DurationPanel();
        contentPanel.add(durationPanel);

        logoutCheckBox = new StyledJCheckBox("Logout");
        logoutCheckBox.setBackground(ColourScheme.PANEL_BACKGROUND_GREY);
        contentPanel.add(logoutCheckBox);

        setContentPanel(contentPanel);
    }

    @Override
    public Task toTask() {
        if (durationPanel.getSelectedTimeType() == DurationPanel.TimeType.DATE_TIME) {
            return new BreakTask(durationPanel.getSelectedDateTime(), logoutCheckBox.isSelected());
        }

        return new BreakTask(durationPanel.getDurationMS(), logoutCheckBox.isSelected());
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
