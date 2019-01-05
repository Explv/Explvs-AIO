package org.aio.gui.task_panels;

import org.aio.gui.utils.NumberDocumentFilter;
import org.aio.tasks.break_task.BreakTask;
import org.aio.tasks.Task;
import org.aio.tasks.TaskType;
import org.json.simple.JSONObject;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.util.concurrent.TimeUnit;

public class BreakTaskPanel implements TaskPanel {

    private JPanel mainPanel;
    private JTextField durationField;
    private JCheckBox logoutCheckBox;

    BreakTaskPanel() {
        mainPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        mainPanel.setBorder(new TitledBorder(new EtchedBorder(), "Break Task"));


        mainPanel.add(new JLabel("Duration (minutes):"));

        durationField = new JTextField();
        durationField.setColumns(6);
        ((AbstractDocument) durationField.getDocument()).setDocumentFilter(new NumberDocumentFilter());
        mainPanel.add(durationField);

        logoutCheckBox = new JCheckBox("Logout");
        mainPanel.add(logoutCheckBox);
    }

    public JPanel getPanel() {
        return mainPanel;
    }

    @Override
    public Task toTask() {
        int durationMinutes = Integer.parseInt(durationField.getText());
        long durationMS = TimeUnit.MINUTES.toMillis(durationMinutes);
        return new BreakTask(durationMS, logoutCheckBox.isSelected());
    }

    @Override
    public JSONObject toJSON() {
        JSONObject taskJSONObject = new JSONObject();
        taskJSONObject.put("type", TaskType.BREAK.name());
        taskJSONObject.put("duration", durationField.getText());
        taskJSONObject.put("logout", logoutCheckBox.isSelected());

        return taskJSONObject;
    }

    @Override
    public void fromJSON(final JSONObject jsonObject) {
        durationField.setText((String) jsonObject.get("duration"));
        logoutCheckBox.setSelected((Boolean) jsonObject.get("logout"));
    }
}
