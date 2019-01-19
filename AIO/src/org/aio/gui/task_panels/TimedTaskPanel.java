package org.aio.gui.task_panels;

import org.aio.gui.utils.DateTimePanel;
import org.aio.gui.utils.NumberDocumentFilter;
import org.aio.tasks.Task;
import org.aio.tasks.TaskType;
import org.aio.tasks.TimedTask;
import org.json.simple.JSONObject;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimedTaskPanel implements TaskPanel {

    private static final DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private JPanel mainPanel;
    private ActivitySelectorPanel activitySelectorPanel;
    private JComboBox<TimeType> timeTypeSelector;
    private DateTimePanel dateTimePanel;
    private JTextField durationField;

    enum TimeType {
        DURATION("Duration"),
        DATE_TIME("Date / Time");

        private String name;

        TimeType(final String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    TimedTaskPanel(){
        mainPanel = new JPanel(new BorderLayout());

        activitySelectorPanel = new ActivitySelectorPanel(this);
        mainPanel.add(activitySelectorPanel.getPanel(), BorderLayout.NORTH);

        JPanel bottomControls = new JPanel();
        bottomControls.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

        timeTypeSelector = new JComboBox<>(TimeType.values());
        bottomControls.add(timeTypeSelector);

        JPanel durationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        durationPanel.add(new JLabel("Duration (minutes):"));
        durationField = new JTextField();
        durationField.setColumns(6);
        ((AbstractDocument) durationField.getDocument()).setDocumentFilter(new NumberDocumentFilter());
        durationPanel.add(durationField);
        bottomControls.add(durationPanel);

        dateTimePanel = new DateTimePanel();
        dateTimePanel.setVisible(false);
        bottomControls.add(dateTimePanel);

        mainPanel.setBorder(new TitledBorder(new EtchedBorder(), "Timed Task"));
        mainPanel.add(bottomControls, BorderLayout.SOUTH);

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
        if (timeTypeSelector.getSelectedItem() == TimeType.DURATION) {
            return new TimedTask(
                    activitySelectorPanel.getActivityPanel().toActivity(),
                    (Integer.parseInt(durationField.getText())) * 60_000L
            );
        }
        return new TimedTask(
            activitySelectorPanel.getActivityPanel().toActivity(),
            dateTimePanel.getDateTime()
        );
    }

    @Override
    public JSONObject toJSON() {
        JSONObject taskObject = new JSONObject();
        taskObject.put("type", TaskType.TIMED.name());

        if (timeTypeSelector.getSelectedItem() == TimeType.DURATION) {
            taskObject.put("duration", durationField.getText());
        } else {
            taskObject.put(
                    "datetime",
                    dateTimePanel.getDateTime().format(dtFormatter)
            );
        }

        taskObject.put("activity", activitySelectorPanel.toJSON());

        return taskObject;
    }

    @Override
    public void fromJSON(final JSONObject jsonObject) {
        activitySelectorPanel.fromJSON((JSONObject) jsonObject.get("activity"));

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
    }
}
