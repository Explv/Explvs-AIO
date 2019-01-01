package org.aio.gui.task_panels;

import org.aio.gui.utils.NumberDocumentFilter;
import org.aio.tasks.task.Task;
import org.aio.tasks.task.TaskType;
import org.aio.tasks.timed_task.TimedTask;
import org.json.simple.JSONObject;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.AbstractDocument;
import java.awt.*;

public class TimedTaskPanel implements TaskPanel {

    private JPanel mainPanel;
    private JTextField durationField;
    private ActivitySelectorPanel activitySelectorPanel;

    public TimedTaskPanel() {
        mainPanel = new JPanel(new BorderLayout());

        JPanel bottomControls = new JPanel();
        bottomControls.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

        bottomControls.add(new JLabel("Duration (minutes):"));

        durationField = new JTextField();
        durationField.setColumns(6);
        ((AbstractDocument) durationField.getDocument()).setDocumentFilter(new NumberDocumentFilter());
        bottomControls.add(durationField);

        mainPanel.setBorder(new TitledBorder(new EtchedBorder(), "Timed Task"));

        mainPanel.add(bottomControls, BorderLayout.SOUTH);

        activitySelectorPanel = new ActivitySelectorPanel();
        mainPanel.add(activitySelectorPanel.getPanel(), BorderLayout.CENTER);
    }

    public JPanel getPanel() {
        return mainPanel;
    }

    @Override
    public Task toTask() {
        return new TimedTask(activitySelectorPanel.getActivityPanel().toActivity(), (Integer.parseInt(durationField.getText())) * 60_000L);
    }

    @Override
    public JSONObject toJSON() {
        JSONObject taskObject = new JSONObject();
        taskObject.put("type", TaskType.TIMED.name());
        taskObject.put("duration", durationField.getText());
        taskObject.put("activity", activitySelectorPanel.toJSON());
        return taskObject;
    }

    @Override
    public void fromJSON(final JSONObject jsonObject) {
        durationField.setText((String) jsonObject.get("duration"));
        activitySelectorPanel.fromJSON((JSONObject) jsonObject.get("activity"));
    }
}
