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

public class TimedTaskPanel implements TaskPanel {
    private JPanel mainPanel;
    private ActivitySelectorPanel activitySelectorPanel;
    private DurationPanel durationPanel;

    TimedTaskPanel(){
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new TitledBorder(new EtchedBorder(), "Timed Task"));

        activitySelectorPanel = new ActivitySelectorPanel(this);
        mainPanel.add(activitySelectorPanel.getPanel(), BorderLayout.NORTH);

        durationPanel = new DurationPanel();
        mainPanel.add(durationPanel, BorderLayout.SOUTH);
    }

    public JPanel getPanel() {
        return mainPanel;
    }

    @Override
    public Task toTask() {
        if (durationPanel.getSelectedTimeType() == DurationPanel.TimeType.DURATION) {
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
