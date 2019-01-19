package org.aio.gui.task_panels;

import org.aio.gui.utils.DateTimePanel;
import org.aio.gui.utils.NumberDocumentFilter;
import org.aio.gui.utils.TimeType;
import org.aio.tasks.Task;
import org.aio.tasks.TaskType;
import org.aio.tasks.break_task.BreakTask;
import org.json.simple.JSONObject;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

public class BreakTaskPanel implements TaskPanel {

    private static final DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private JPanel mainPanel;
    private JComboBox<TimeType> timeTypeSelector;
    private DateTimePanel dateTimePanel;
    private JTextField durationField;
    private JCheckBox logoutCheckBox;

    BreakTaskPanel() {
        mainPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        mainPanel.setBorder(new TitledBorder(new EtchedBorder(), "Break Task"));

        timeTypeSelector = new JComboBox<>(TimeType.values());
        mainPanel.add(timeTypeSelector);

        JPanel durationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        durationPanel.add(new JLabel("Duration (minutes):"));
        durationField = new JTextField();
        durationField.setColumns(6);
        ((AbstractDocument) durationField.getDocument()).setDocumentFilter(new NumberDocumentFilter());
        durationPanel.add(durationField);
        mainPanel.add(durationPanel);

        dateTimePanel = new DateTimePanel();
        dateTimePanel.setVisible(false);
        mainPanel.add(dateTimePanel);

        logoutCheckBox = new JCheckBox("Logout");
        mainPanel.add(logoutCheckBox);

        timeTypeSelector.addActionListener(e -> {
            TimeType selectedTimeType = (TimeType) timeTypeSelector.getSelectedItem();
            if (selectedTimeType == TimeType.DURATION) {
                dateTimePanel.setVisible(false);
                durationPanel.setVisible(true);
            } else {
                durationPanel.setVisible(false);
                dateTimePanel.setVisible(true);
            }
        });
    }

    public JPanel getPanel() {
        return mainPanel;
    }

    @Override
    public Task toTask() {
        if (timeTypeSelector.getSelectedItem() == TimeType.DATE_TIME) {
            return new BreakTask(dateTimePanel.getDateTime(), logoutCheckBox.isSelected());
        }

        int durationMinutes = Integer.parseInt(durationField.getText());
        long durationMS = TimeUnit.MINUTES.toMillis(durationMinutes);
        return new BreakTask(durationMS, logoutCheckBox.isSelected());
    }

    @Override
    public JSONObject toJSON() {
        JSONObject taskJSONObject = new JSONObject();
        taskJSONObject.put("type", TaskType.BREAK.name());

        if (timeTypeSelector.getSelectedItem() == TimeType.DURATION) {
            taskJSONObject.put("duration", durationField.getText());
        } else {
            taskJSONObject.put(
                    "datetime",
                    dateTimePanel.getDateTime().format(dtFormatter)
            );
        }

        taskJSONObject.put("logout", logoutCheckBox.isSelected());

        return taskJSONObject;
    }

    @Override
    public void fromJSON(final JSONObject jsonObject) {
        if (jsonObject.containsKey("duration")) {
            durationField.setText((String) jsonObject.get("duration"));
            timeTypeSelector.setSelectedItem(TimeType.DURATION);
        }

        if (jsonObject.containsKey("datetime")) {
            String datetimeStr = (String) jsonObject.get("datetime");
            LocalDateTime datetime = LocalDateTime.parse(
                    datetimeStr,
                    dtFormatter
            );
            dateTimePanel.setDateTime(datetime);
            timeTypeSelector.setSelectedItem(TimeType.DATE_TIME);
        }

        logoutCheckBox.setSelected((Boolean) jsonObject.get("logout"));
    }
}
