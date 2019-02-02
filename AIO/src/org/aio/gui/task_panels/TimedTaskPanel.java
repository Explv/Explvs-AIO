package org.aio.gui.task_panels;

import org.aio.gui.utils.DurationPanel;
import org.aio.tasks.Task;
import org.aio.tasks.TaskType;
import org.aio.tasks.TimedTask;
import org.json.simple.JSONObject;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class TimedTaskPanel extends TaskPanel {
    private ActivitySelectorPanel activitySelectorPanel;
    private DurationPanel durationPanel;

    TimedTaskPanel(){
        super(TaskType.TIMED);

        JPanel contentPanel = new JPanel(new BorderLayout());
        activitySelectorPanel = new ActivitySelectorPanel(this);
        contentPanel.add(activitySelectorPanel.getPanel(), BorderLayout.NORTH);

        durationPanel = new DurationPanel();
        contentPanel.add(durationPanel, BorderLayout.SOUTH);

        setContentPanel(contentPanel);
    }

    @Override
    public Task toTask() {
        if (durationPanel.getSelectedTimeType() == DurationPanel.TimeType.MINUTES) {
            return new TimedTask(
                    activitySelectorPanel.getActivityPanel().toActivity(),
                    durationPanel.getDuration() * 60_000L
            );
        }
        return new TimedTask(
            activitySelectorPanel.getActivityPanel().toActivity(),
            durationPanel.getSelectedDateTime()
        );
    }

    @Override
    public JSONObject toJSON() {
        JSONObject taskObject = new JSONObject();
        taskObject.put("type", TaskType.TIMED.name());

        JSONObject durationObj = durationPanel.toJSON();
        for (Object key: durationObj.keySet()) {
            taskObject.put(key, durationObj.get(key));
        }

        taskObject.put("activity", activitySelectorPanel.toJSON());

        return taskObject;
    }

    @Override
    public void fromJSON(final JSONObject jsonObject) {
        activitySelectorPanel.fromJSON((JSONObject) jsonObject.get("activity"));
        durationPanel.fromJSON(jsonObject);
    }
}
